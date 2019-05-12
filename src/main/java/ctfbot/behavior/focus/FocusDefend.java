package ctfbot.behavior.focus;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;

public class FocusDefend extends FocusBehavior {

    private final double DISTANCE_DEFENDING = 100.0;

    public FocusDefend(CTFBot bot) {
        super(bot, 0.0);
    }

    @Override
    public boolean isFiring() {
        if (!ctx.amIDefender()) return false;
        if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING) return true;
        return false;
    }

    @Override
    public Behavior run() {
        if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(ctx.getFocusDefending());
        else ctx.getMove().turnTo(ctx.getFocusDefending());

        return this;
    }

    @Override
    public Behavior terminate() {
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof FocusEnemy) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return transitionTo.run();
    }

}
