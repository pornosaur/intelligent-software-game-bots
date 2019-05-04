package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base.utils.math.DistanceUtils;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Item;

import java.util.logging.Level;
import java.util.stream.Collectors;

public class CollectItem extends Behavior {

    private static double DEFAULT_MAX_DISTANCE = 8000;
    private static double MAX_Z_AXIS_VARIANCE = 50;
    private static double LOW_AMMO_RATIO = 0.35;

    private static UT2004ItemType defenderWeapons[] = {UT2004ItemType.MINIGUN, UT2004ItemType.LINK_GUN,
            UT2004ItemType.LIGHTNING_GUN};

    private Item item, nextItem;
    private double itemDistance;

    public CollectItem(CTFBot bot) {
        super(bot, 0, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        nextItem = null;
        itemDistance = Double.MAX_VALUE;

        double maxDistanceWeapon = getMaxDistanceWeapon();
        double maxDistanceAmmo = getMaxDistanceAmmo();
        double maxDistanceHealth = getMaxDistanceHealth();
        double maxDistanceArmor = getMaxDistanceArmor();

        Item tmpItem = DistanceUtils.getNearest(ctx.getItems().getSpawnedItems().values().stream()
                        .filter((item) -> ctx.getItems().isPickable(item)).collect(Collectors.toList()),
                ctx.getInfo().getLocation(), (DistanceUtils.IGetDistance<Item>) (object, target) -> {
                    double m = 1;

                    if (object.getType().getCategory() == ItemType.Category.WEAPON) {
                        if (isPreferedWeapon(object)) return Double.MAX_VALUE;
                        if (ctx.getInfo().getLocation().getDistance(object.getLocation()) > maxDistanceWeapon)
                            return Double.MAX_VALUE;
                        if (ctx.amIDefender() && ctx.isFarFromBase(object)) return Double.MAX_VALUE;

                        double aStarDistance = ctx.getAStar().getDistance(object.getNavPoint(),
                                ctx.getInfo().getNearestNavPoint());

                        if (aStarDistance > maxDistanceWeapon) return Double.MAX_VALUE;

                        //TODO IMPROVE THIS
                        if (object.getType() == UT2004ItemType.LIGHTNING_GUN) {
                            if (Math.abs(object.getLocation().z - ctx.getInfo().getLocation().z) > MAX_Z_AXIS_VARIANCE)
                                return Double.MAX_VALUE; //m = 0.1;

                            m = 0.1;
                        }

                        if (m * aStarDistance < itemDistance) {
                            itemDistance = m * aStarDistance;
                        } else {
                            return Double.MAX_VALUE;
                        }

                        return m * aStarDistance;
                    } else if (object.getType().getCategory() == ItemType.Category.AMMO) {
                        ItemType weaponAmmo = ctx.getWeaponry().getWeaponForAmmo(object.getType());

                        if (weaponAmmo == UT2004ItemType.LIGHTNING_GUN_AMMO) return Double.MAX_VALUE;

                        if (!ctx.getWeaponry().hasWeapon(weaponAmmo)
                                || !ctx.getWeaponry().hasLowAmmoForWeapon(weaponAmmo, LOW_AMMO_RATIO)
                                || (ctx.getInfo().getLocation().getDistance(object.getLocation()) > maxDistanceAmmo))
                            return Double.MAX_VALUE;

                        double aStarDistance = ctx.getAStar().getDistance(object.getNavPoint(),
                                ctx.getInfo().getNearestNavPoint());

                        if (aStarDistance > maxDistanceAmmo) return Double.MAX_VALUE;
                        if (ctx.isFarFromBase(object) && ctx.amIDefender()) return Double.MAX_VALUE;

                        if (m * aStarDistance < itemDistance) {
                            itemDistance = m * aStarDistance;
                        } else {
                            return Double.MAX_VALUE;
                        }

                        return m * aStarDistance;

                    } else if (object.getType().getCategory() == ItemType.Category.HEALTH) {
                        if (maxDistanceHealth < 0) return Double.MAX_VALUE;
                        if (ctx.getInfo().getLocation().getDistance(object.getLocation()) > maxDistanceHealth)
                            return Double.MAX_VALUE;

                        double aStarDistance = ctx.getAStar().getDistance(object.getNavPoint(),
                                ctx.getInfo().getNearestNavPoint());

                        if (aStarDistance > maxDistanceHealth) return Double.MAX_VALUE;
                        if (ctx.isFarFromBase(object) && ctx.amIDefender()) return Double.MAX_VALUE;
                        if (ctx.amIFlagHolder() && !ctx.getInfo().isHealthy()) m = 0.1;


                        if (m * aStarDistance < itemDistance) {
                            itemDistance = m * aStarDistance;
                        } else {
                            return Double.MAX_VALUE;
                        }

                        return m * aStarDistance;
                    } else if (object.getType().getCategory() == ItemType.Category.ARMOR) {
                        if (maxDistanceArmor < 0) return Double.MAX_VALUE;

                        double aStarDistance = ctx.getAStar().getDistance(object.getNavPoint(),
                                ctx.getInfo().getNearestNavPoint());

                        if (aStarDistance > maxDistanceArmor) return Double.MAX_VALUE;
                        if (ctx.isFarFromBase(object) && ctx.amIDefender()) return Double.MAX_VALUE;
                        if (ctx.getInfo().hasLowArmor()) {
                            m = 0.3;
                        }
                        if (object.getType() == UT2004ItemType.SHIELD_PACK || object.getType() == UT2004ItemType.SUPER_SHIELD_PACK) {
                            m = 0.1;
                        }

                        if (m * aStarDistance < itemDistance) {
                            itemDistance = m * aStarDistance;
                        } else {
                            return Double.MAX_VALUE;
                        }

                        return m * aStarDistance;
                    }

                    return Double.MAX_VALUE;
                });

        if (tmpItem == null) return false;
        if (itemDistance > getMaxDistance(tmpItem)) return false;

        this.nextItem = tmpItem;
        return true;
    }

    @Override
    public Behavior run() {
        item = nextItem;
        ctx.getLog().log(Level.INFO, "__________COLLECT: " + item);
        ctx.navigateAStarPath(item.getNavPoint());

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.getNavigation().isNavigating())
            return this;

        ctx.getNavigation().stopNavigation();
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        boolean beh = false;
        if (toThiBehavior instanceof GetFlag) {
            beh = true;
        }

        return beh;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    private double getMaxDistance(Item item) {
        ItemType.Category itemCat = item.getType().getCategory();

        double distance;
        switch (itemCat) {
            case WEAPON:
                distance = getMaxDistanceWeapon();
                break;
            case AMMO:
                distance = getMaxDistanceAmmo();
                break;
            case ARMOR:
                distance = getMaxDistanceArmor();
                break;
            case HEALTH:
                distance = getMaxDistanceHealth();
                break;
            default:
                distance = -1;
        }

        return distance;
    }

    private double getMaxDistanceArmor() {
        if (ctx.amIFlager()) {
            return 800;
        } else if (ctx.amIFlagHolder()) {
            return 300;
        }
        return 5000;
    }

    private double getMaxDistanceHealth() {
        if (ctx.amIFlager()) {
            if (ctx.amIFlagHolder() && !ctx.getInfo().isHealthy()) {
                return 800;
            } else if (!ctx.getInfo().isHealthy()) {
                return 1800;
            }

            return -1;
        }

        return (ctx.getInfo().isHealthy() ? -1 : 5000);
    }

    private double getMaxDistanceAmmo() {
        if (ctx.amIFlager()) {
            boolean flagHolder = ctx.amIFlagHolder();
            if (flagHolder) {
                return 300;
            }
            return 650;
        }

        return 1500;
    }

    private double getMaxDistanceWeapon() {
        if (ctx.amIFlager()) {
            if (ctx.amIFlagHolder()) {
                return 200;
            }
            return 500;
        }

        return 6000;
    }

    private boolean isPreferedWeapon(Item item) {
        // if (ctx.amIDefender()) {
        for (UT2004ItemType i : defenderWeapons) {
            if (i == item.getType().getGroup() && !ctx.getWeaponry().hasWeapon(item.getType())) {
                return true;
            }
        }
      /*  } else if (ctx.amIFlager()) {
            if (ctx.amIFlagHolder()) {
                //TODO weapons for flager flag-holder
            } else {
                //TODO weapons for flager non-flag-holder
            }
        }*/

        return false;
    }
}
