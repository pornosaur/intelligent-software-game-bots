package ctfbot.behavior;

import ctfbot.CTFBot;


public class BackFlag extends Behavior {

    public BackFlag(CTFBot bot) {
        super(bot, 0, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        return (ctx.amIFlagHolder());
    }

    @Override
    public Behavior run() {
        if (ctx.navigateAStarPath(ctx.getCTF().getOurBase())) {
            //TODO maybe cant go, why??
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.amIFlagHolder() && ctx.getNavigation().isNavigating()) return this;
        ctx.getNavigation().stopNavigation();

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        boolean returnBeh = false;
        if ((toThiBehavior instanceof StoleFlag) && !ctx.getCTF().isOurTeamCarryingEnemyFlag()) {
            returnBeh = true;
        }

        //TODO make getBehaviorType!!!
        if ((toThiBehavior instanceof CollectItem)) returnBeh = true;

        return returnBeh;
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
