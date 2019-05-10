package ctfbot.behavior.move;

import ctfbot.CTFBot;

import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

public class HuntEnemy extends Behavior {

    public HuntEnemy(CTFBot bot) {
        super(bot, 120, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        //TODO maybe defenders also hunt enemy!!! But in different way!!!
        if (ctx.amIFlagHolder()) return false;
        if (ctx.getEnemyTarget() == null) return false;
        if (ctx.amIAttacker()) return true;
        if (ctx.amIDefender() && ctx.getEnemyTarget().getLocation().getDistance(ctx.getInfo().getLocation()) <= 1500)
            return true;

        return false;
    }

    @Override
    public Behavior run() {
        Player enemy = ctx.getEnemyTarget();
        if (enemy == null) enemy = ctx.getPlayers().getNearestVisibleEnemy();
        if (enemy == null) return null;

        if (CTFBot.CAN_USE_NAVIGATE) ctx.getNavigation().navigate(enemy);
        else ctx.navigateAStarPath(ctx.getNavPoints().getNearestNavPoint(enemy.getLocation()));

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.getEnemyTarget() != null && ctx.getEnemyTarget().isVisible()) return this;
        if (ctx.getNavigation().isNavigating()) return this;

        ctx.getNavigation().stopNavigation();
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof BackFlag) return true;
        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof DefendBase) return true;
        if (toThiBehavior instanceof CollectItem) return true;
        if ((toThiBehavior instanceof StealFlag)) {
            return true;
        }

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

}
