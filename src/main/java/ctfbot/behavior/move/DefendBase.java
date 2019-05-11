package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;

public class DefendBase extends Behavior {

    private boolean defending = false;

    public DefendBase(CTFBot bot) {
        super(bot, 100, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIDefender() && !defending) return true;
        return false;
    }

    @Override
    public Behavior run() {
        ctx.smartNavigate(ctx.getDefendingPlace());

        defending = true;
        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.amIDefender() && defending) return this;
        defending = false;
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof CollectItem) return true;

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

    @Override
    public String toString() {
        return "DEFENDING BASE";
    }
}
