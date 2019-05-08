package ctfbot.behavior;

import ctfbot.CTFBot;

public class DefendBase extends Behavior {

    public boolean defending;

    public DefendBase(CTFBot bot) {
        super(bot, 100, Action.MOVE);
        this.defending = false;
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIDefender() && !defending) return true;
        return false;
    }

    @Override
    public Behavior run() {
        defending = true;

        if (CTFBot.CAN_USE_NAVIGATE) ctx.getNavigation().navigate(ctx.getDefendingPlace());
        else ctx.navigateAStarPath(ctx.getNavPoints().getNearestNavPoint(ctx.getDefendingPlace()));

        return this;
    }

    @Override
    public Behavior terminate() {
        if (defending /*&& ctx.getNavigation().isNavigating()*/) return this;

        defending = false;
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof CollectItem) return true;
        if (toThiBehavior instanceof HuntEnemy) return true;
        if (toThiBehavior instanceof StoleFlag) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        defending = false;
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    @Override
    public void reset() {
        defending = false;
    }
}
