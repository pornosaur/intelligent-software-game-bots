package ctfbot.behavior.fire;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
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
        lightingCool = new Cooldown(1700);
    }

    @Override
    public boolean isFiring() {
      /*  if (ctx.getEnemyTarget() != null) return true;
        if (ctx.getPlayers().canSeeEnemies()) return true;*/

        return true;
    }

    @Override
    public Behavior run() {
        boolean shootPrimary = true;
        if (ctx.getHealthRatio() <= CTFBot.LOW_HEALTH_RATIO && isEnoughAmmoForDefending()
                && ctx.amIFlagHolder() && ctx.getPlayers().canSeeEnemies()) {
            shootPrimary = false;
            ctx.getWeaponry().changeWeapon(UT2004ItemType.SHIELD_GUN);
        }

        Player target = ctx.getEnemyTarget();
        if (target == null) target = ctx.getPlayers().getNearestVisibleEnemy();

        if (target != null) {
            if (shootPrimary) {
                WeaponPref chosen = ctx.getWeaponPrefs().getWeaponPreference(target);

                if (ctx.getCombo().canPerformCombo()) {
                    if (ctx.getInfo().getHealth() <= 50) {
                        ctx.getCombo().performDefensive();
                    }
                    ctx.getCombo().performBerserk();
                }

                boolean isGoodRot = ctx.isRotatedToEnemy(target);
                if (chosen.getWeapon() == UT2004ItemType.LIGHTNING_GUN && lightingCool.tryUse()) {
                    ctx.getShoot().shootNow(chosen, target);
                } else {
                    if (lightingCool.isHot()) {
                        ctx.getShoot().shootNow(ctx.getWeaponPrefs(), target, UT2004ItemType.LIGHTNING_GUN);
                    } else {
                        ctx.getShoot().shootNow(ctx.getWeaponPrefs(), target);
                    }
                }
            } else {
                ctx.getShoot().shootSecondary();
            }
        } else {
            if (!ctx.getPlayers().canSeeEnemies()) ctx.getShoot().stopShooting();
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        if (!ctx.getPlayers().canSeeEnemies()) ctx.getShoot().stopShooting();

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
    public void reset() {
        lightingCool.clear();
        ctx.getShoot().stopShooting();
    }

    private boolean isEnoughAmmoForDefending() {
        return ctx.getWeaponry().getAmmo(UT2004ItemType.SHIELD_GUN) >= ENOUGH_AMMO_DEFENDING;
    }

}
