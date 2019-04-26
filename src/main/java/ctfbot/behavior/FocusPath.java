package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;

public class FocusPath extends Behavior {

    private Location forcedFocus;

    public FocusPath(CTFBot bot) {
        this(bot, 0, null);
    }

    public FocusPath(CTFBot bot, double priority, Location location) {
        super(bot, priority, Action.FOCUS);
        this.forcedFocus = location;
    }

    @Override
    public boolean isFiring() {
        double healthRatio = ctx.getInfo().getHealth() / ctx.getInfo().game.getFullHealth();
        if (ctx.amIFlagHolder() && (healthRatio <= 0.35)) return false;
        if (!ctx.getPlayers().canSeeEnemies()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(forcedFocus);
        else if (forcedFocus != null) ctx.getMove().turnTo(forcedFocus);

        return this;
    }

    @Override
    public Behavior terminate() {
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return null;
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }
}