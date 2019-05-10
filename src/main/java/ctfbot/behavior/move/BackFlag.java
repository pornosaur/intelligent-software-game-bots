package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;


public class BackFlag extends Behavior {

    private boolean hiding = false;

    public BackFlag(CTFBot bot) {
        super(bot, 150, Action.MOVE);

    }

    @Override
    public boolean isFiring() {
        return (ctx.amIFlagHolder());
    }

    @Override
    public Behavior run() {
        if (!ctx.getCTF().isOurFlagHome()) {
            ctx.navigateAStarPath(ctx.getNavPoints().getNearestNavPoint(ctx.getHidingPlace()));
            hiding = true;
        } else {
            ctx.navigateAStarPath(ctx.getCTF().getOurBase());
            hiding = false;
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.amIFlagHolder() && !ctx.getCTF().isOurFlagHome()) {
            if (!hiding){
                ctx.getNavigation().stopNavigation();
                run();
            }
            return this;
        } else if (ctx.amIFlagHolder() && ctx.getCTF().isOurFlagHome() && hiding) {
            run();
            return this;
        }

        if (ctx.amIFlagHolder() && hiding) return this;
        if (ctx.amIFlagHolder() && ctx.getNavigation().isNavigating()) return this;

        ctx.getNavigation().stopNavigation();
        hiding = false;

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        boolean returnBeh = false;
        if ((toThiBehavior instanceof StealFlag) && !ctx.getCTF().isOurTeamCarryingEnemyFlag()) {
            returnBeh = true;
        }


        //TODO make getBehaviorType!!!
        if ((toThiBehavior instanceof CollectItem)) returnBeh = true;

        return returnBeh;
    }

    @Override
    public Behavior transition(Behavior transitionTo)
    {
        hiding = false;
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    @Override
    public void reset() {
        hiding = false;
    }

    @Override
    public String toString() {
        return "BACK WITH FLAG";
    }
}
