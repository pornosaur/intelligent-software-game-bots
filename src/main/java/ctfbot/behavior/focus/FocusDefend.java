package ctfbot.behavior.focus;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;

public class FocusDefend extends FocusBehavior {

    private final double DISTANCE_DEFENDING = 100.0;

    public FocusDefend(CTFBot bot) {
        super(bot, 0.0, Action.FOCUS);
    }

    @Override
    public boolean isFiring() {
        if (!ctx.amIDefender()) return false;
        if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING) return true;
        return false;
    }

    @Override
    public Behavior run() {
        if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(ctx.getFocucDefending());
        else ctx.getMove().turnTo(ctx.getFocucDefending());

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING) return this;
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof FocusEnemy) return true;
        if (toThiBehavior instanceof FocusPath) return true;

        return false;
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
