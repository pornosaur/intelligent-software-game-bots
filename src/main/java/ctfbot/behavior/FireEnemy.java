package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;

public class FireEnemy extends Behavior {

    public FireEnemy(CTFBot bot) {
        super(bot, 0, Action.FIRE);
    }

    @Override
    public boolean isFiring() {
        double healthRatio = ctx.getInfo().getHealth() / ctx.getInfo().game.getFullHealth();
        if (/*ctx.amIFlagHolder() &&*/ (healthRatio <= 2)) {
            ctx.getWeaponry().changeWeapon(UT2004ItemType.SHIELD_GUN);
            return true;
        }
        return false;
    }

    @Override
    public Behavior run() {
        ctx.getShoot().shootSecondary();
        return this;
    }

    @Override
    public Behavior terminate() {
        //if (ctx.getPlayers().canSeeEnemies()) return this;

        if (ctx.getInfo().isShooting() && ctx.getWeaponry().hasAmmoForWeapon(ctx.getWeaponry().getCurrentWeapon().getType()))
            return this;
        // ctx.getShoot().stopShooting();
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
