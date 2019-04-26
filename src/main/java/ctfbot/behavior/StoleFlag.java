package ctfbot.behavior;

import ctfbot.CTFBot;

import java.util.logging.Level;


public class StoleFlag extends Behavior {

    public StoleFlag(CTFBot bot) {
        super(bot, 0, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (!ctx.amIFlager() || ctx.amIFlagHolder()) return false;
        if (!ctx.getCTF().isEnemyFlagHome()) return false;

        priority = 100;
        return true;
    }

    @Override
    public Behavior run() {
        ctx.getLog().log(Level.INFO, "_____NAVIGATION STOLE FLAG STARTED______");
        ctx.navigateAStarPath(ctx.getCTF().getEnemyBase());
        return this;
    }


    @Override
    public Behavior terminate() {
        //TODO check some other conditionals -> maybe smth more important!!!
        if (ctx.amIFlager() && !ctx.amIFlagHolder()) return this;

        ctx.getNavigation().stopNavigation();
        ctx.getLog().log(Level.INFO, "_____NAVIGATION STOLE FLAG STOPPED_____");
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior == null) return false;

        boolean returnBeh = false;
        if((toThiBehavior instanceof BackFlag) && ctx.amIFlagHolder()) {
            returnBeh = true;
        }

        if ((toThiBehavior instanceof CollectItem)) {
               returnBeh = true;

        }
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
