package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;

public class StealFlag extends Behavior {

    private boolean stealing = false;

    public StealFlag(CTFBot bot) {
        super(bot, 100, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIDefender() || ctx.amIFlagHolder()) return false;
        if (!ctx.getCTF().isEnemyFlagHome() && ctx.amINearest()) return true;
        if (!ctx.getCTF().isOurFlagHome() && ctx.getSeenEnemyWithOurFlag() != null) return false;
        if (stealing) return false;

        return true;
    }

    @Override
    public Behavior run() {
        stealing = true;

        ctx.smartNavigate(ctx.getCTF().getEnemyBase());

        return this;
    }


    @Override
    public Behavior terminate() {
        if (!ctx.getCTF().isEnemyFlagHome() && ctx.amINearest()) return this;
        if (ctx.getWhoIsFlagHolder() == null && ctx.amIFlager()) return this;

        stealing = false;
        ctx.getNavigation().stopNavigation();

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior == null) return false;

        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof DefendFlager) return true;
        if (toThiBehavior instanceof DefendBase) return true;
        if (toThiBehavior instanceof CaptureFlag) return true;
        if (toThiBehavior instanceof CollectItem) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        stealing = false;
        return transitionTo.run();
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
