package ctfbot.behavior;

import ctfbot.CTFBot;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;

public class HuntEnemy extends Behavior {

    private NavPoint lastTarget;

    public HuntEnemy(CTFBot bot) {
        super(bot, 120, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (ctx.getPlayers().canSeeEnemies() && !ctx.amIFlagHolder()) return true;
        return false;
    }

    @Override
    public Behavior run() {
        NavPoint newNav = ctx.getNavPoints().getNearestNavPoint(ctx.getPlayers().getNearestVisibleEnemy().getLocation());
        if (newNav.equals(lastTarget)) newNav = ctx.getNavPoints().getNearestVisibleNavPoint();
        /*if (ctx.getInfo().getHealth() / ctx.getInfo().game.getFullHealth() < 0.35)
            newNav = ctx.getNearestCoverPoint(ctx.getPlayers().getNearestVisibleEnemy());*/
        ctx.navigateAStarPath(newNav);
        lastTarget = newNav;

        return this;
    }

    @Override
    public Behavior terminate() {
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
