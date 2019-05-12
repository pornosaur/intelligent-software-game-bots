package ctfbot.behavior.move;

import ctfbot.CTFBot;

import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

public class HuntEnemy extends Behavior {

    public HuntEnemy(CTFBot bot) {
        super(bot, 0, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
       if (ctx.amIDefender() && ctx.getPlayers().canSeeEnemies()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        Player enemy = ctx.getEnemyTarget();
        if (enemy == null) enemy = ctx.getPlayers().getNearestVisibleEnemy();
        if (enemy == null) return null;

        ctx.smartNavigate(enemy);

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.getNavigation().isNavigating()) return this;

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

}
