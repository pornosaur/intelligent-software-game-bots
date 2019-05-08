package ctfbot.behavior;

import ctfbot.CTFBot;

import java.util.logging.Level;


public class StoleFlag extends Behavior {

    private boolean stealing = false;

    public StoleFlag(CTFBot bot) {
        super(bot, 0, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (!ctx.amIFlager() || ctx.amIFlagHolder()) return false;
        if (stealing) return false;
//        if (!ctx.getCTF().isEnemyFlagHome()) return false;        //TODO: Think about this behavior!

        priority = 100;
        return true;
    }

    @Override
    public Behavior run() {
        stealing = true;
        ctx.getLog().log(Level.INFO, "_____NAVIGATION STOLE FLAG STARTED______");
        ctx.navigateAStarPath(ctx.getCTF().getEnemyBase());

        return this;
    }


    @Override
    public Behavior terminate() {
        //TODO check some other conditionals -> maybe smth more important!!!
        if (ctx.amIFlager() && !ctx.amIFlagHolder()) return this;

        stealing = false;
        ctx.getNavigation().stopNavigation();
        ctx.getLog().log(Level.INFO, "_____NAVIGATION STOLE FLAG STOPPED_____");
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior == null) return false;

        if ((toThiBehavior instanceof CollectItem) && ctx.getCTF().isOurFlagHome()) {
            return  true;
        }
        if ((toThiBehavior instanceof BackFlag) && ctx.amIFlagHolder()) {
            return true;
        }
        if ((toThiBehavior instanceof GetFlag)) {
            return true;
        }
        if ((toThiBehavior instanceof HuntEnemy) && ctx.amIAttacker()) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        stealing = false;
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    @Override
    public void reset() {
        stealing = false;
    }
}
