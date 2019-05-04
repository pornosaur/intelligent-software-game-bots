package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.utils.Cooldown;

public class FocusPath extends Behavior {

    static private final double HEALTH_RATIO = 0.35;

    private Location forcedFocus;

    public FocusPath(CTFBot bot) {
        this(bot, 0, null);
    }

    public FocusPath(CTFBot bot, double priority, Location location) {
        super(bot, priority, Action.FOCUS);
        this.forcedFocus = location;

        this.expiration = new Cooldown(2000);
        this.expiration.use();
    }

    @Override
    public boolean isFiring() {
        double healthRatio = ctx.getInfo().getHealth() / ctx.getInfo().game.getFullHealth();
        if (ctx.amIFlagHolder() && (healthRatio <= HEALTH_RATIO)) return false;
        if (!ctx.getPlayers().canSeeEnemies()) return true;

        return true;
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
