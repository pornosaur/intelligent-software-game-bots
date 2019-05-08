package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.utils.Cooldown;

public class FireEnemy extends Behavior {

    private final static double ENOUGH_AMMO_DEFENDING = 50.0;

    private Cooldown lightingCool;

    public FireEnemy(CTFBot bot) {
        super(bot, 0, Action.FIRE);
        lightingCool = new Cooldown(1800);
    }

    @Override
    public boolean isFiring() {
        if(ctx.getEnemyTarget() != null) return true;         //TODO Active this!!!
        //if (ctx.getPlayers().canSeeEnemies()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        boolean shootPrimary = true;
        if (ctx.getHealthRatio() <= CTFBot.LOW_HEALTH_RATIO && isEnoughAmmoForDefending()) {
            shootPrimary = false;
            ctx.getWeaponry().changeWeapon(UT2004ItemType.SHIELD_GUN);
        }

        Player target = ctx.getEnemyTarget();

        if (target != null && ctx.isRotatedToEnemy(target)) {
            if (shootPrimary) {
                WeaponPref chosen = ctx.getWeaponPrefs().getWeaponPreference(target);

                if (lightingCool.isCool() && chosen.getWeapon() == UT2004ItemType.LIGHTNING_GUN) {
                    ctx.getShoot().shoot(ctx.getWeaponPrefs(), target);
                    lightingCool.use();
                } else {
                    if (lightingCool.isHot()) {
                        ctx.getShoot().shoot(ctx.getWeaponPrefs(), target, UT2004ItemType.LIGHTNING_GUN);
                    } else {
                        ctx.getShoot().shoot(ctx.getWeaponPrefs(), target);
                    }
                }
            } else {
                ctx.getShoot().shootSecondary(target);
            }
        }  else {
            return null;
        }

    //TODO Intelligent fire at enemy!!

        return this;
}

    @Override
    public Behavior terminate() {
        //if (ctx.getPlayers().canSeeEnemies()) return this;

        /*if (ctx.getInfo().isShooting() && ctx.getWeaponry().hasAmmoForWeapon(ctx.getWeaponry().getCurrentWeapon().getType()))
            return this;*/

        ctx.getShoot().stopShooting();
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    @Override
    public void reset() {
        lightingCool.clear();
        ctx.getShoot().stopShooting();
    }

    private boolean isEnoughAmmoForDefending() {
        return ctx.getWeaponry().getAmmo(UT2004ItemType.SHIELD_GUN) >= ENOUGH_AMMO_DEFENDING;
    }

}
