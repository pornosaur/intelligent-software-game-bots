package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Item;

public class CollectItem extends Behavior {

    private static double DEFAULT_MAX_DISTANCE = 8000;

    private Item item;
    private double distance;
    private boolean isWeapon;
    private boolean isAmmo;
    private double maxDistance;

    public CollectItem(CTFBot bot, double priority, Item item) {
        this(bot, priority, item, DEFAULT_MAX_DISTANCE);
    }

    public CollectItem(CTFBot bot, double priority, Item item, double maxDistance) {
        super(bot, priority, Action.MOVE);
        this.item = item;
        this.maxDistance = maxDistance;
        this.distance = ctx.getAStar().getDistance(ctx.getInfo().getNearestNavPoint(), item.getNavPoint());
        isWeapon = item.getType().getCategory() == ItemType.Category.WEAPON;
        isAmmo =  item.getType().getCategory() == ItemType.Category.AMMO;
        //ItemType.Category.
    }

    @Override
    public boolean isFiring() {
        if (distance > maxDistance) return false;
        if (Math.abs(item.getLocation().z - ctx.getInfo().getLocation().z) > 200) return false;
        if (isAmmo && (ctx.getWeaponry().getAmmo(item.getType()) < ctx.getWeaponry().getMaxAmmo(item.getType())))
            return true;
        if (isWeapon && ctx.getWeaponry().hasWeapon(item.getType().getGroup())) return false;
        if (ctx.getItems().isPickable(item)) return true;

        return true;
    }

    @Override
    public Behavior run() {
        ctx.navigateAStarPath(item.getNavPoint());
        return this;
    }

    @Override
    public Behavior terminate() {
        if (!ctx.getNavigation().isNavigating()) return null;
        if (isAmmo && (ctx.getWeaponry().getAmmo(item.getType()) < ctx.getWeaponry().getMaxAmmo(item.getType())))
            return this;
        if (isWeapon && !ctx.getWeaponry().hasWeapon(item.getType())) return this;

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return null;
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }
}
