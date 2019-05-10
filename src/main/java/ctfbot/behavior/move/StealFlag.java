package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;

import java.util.logging.Level;


public class StealFlag extends Behavior {

    private boolean stealing = false;

    public StealFlag(CTFBot bot) {
        super(bot, 100, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIDefender() || ctx.amIFlagHolder()) return false;
        if (!ctx.getCTF().isOurFlagHome() && ctx.getSeenEnemyWithOurFlag() != null) return false;
        if (stealing) return false;

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
        if (ctx.getWhoIsFlagHolder() == null && ctx.amIFlager()) return this;

        stealing = false;
        ctx.getNavigation().stopNavigation();
        ctx.getLog().log(Level.INFO, "_____NAVIGATION STOLE FLAG STOPPED____ _");

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior == null) return false;

        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof DefendFlager) return true;
        if (toThiBehavior instanceof DefendBase) return true;
        if (toThiBehavior instanceof BackFlag) return true;
        if (toThiBehavior instanceof CollectItem) return true;

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

    @Override
    public String toString() {
        return "STEALING FLAG";
    }
}
