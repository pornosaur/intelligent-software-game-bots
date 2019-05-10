package ctfbot.behavior.focus;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.utils.Cooldown;

public class FocusPath extends FocusBehavior {

    private final double DISTANCE_DEFENDING = 100.0;
    private final double DISTANCE_STEALING_FLAG = 300.0;
    static private final double HEALTH_RATIO = 0.35;

    private Location forcedFocus;

    public FocusPath(CTFBot bot) {
        super(bot, 0, Action.FOCUS);
        this.forcedFocus = null;
        this.expiration = null;
    }

    public FocusPath(CTFBot bot, double priority, Location location) {
        super(bot, priority, Action.FOCUS);
        this.forcedFocus = location;

        this.expiration = new Cooldown(500);
        this.expiration.use();
    }

    @Override
    public boolean isFiring() {
        double healthRatio = ctx.getInfo().getHealth() / ctx.getInfo().game.getFullHealth();
        if (forcedFocus != null) return true;
        double disEnemyBase = ctx.getCTF().getEnemyBase().getLocation().getDistance(ctx.getInfo().getLocation());
        if (disEnemyBase <= 800) return true;
        if (ctx.getCTF().getEnemyBase().getLocation().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_STEALING_FLAG)
            return true;
        if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING) return true;
       // if (ctx.amIFlagHolder() && (healthRatio <= HEALTH_RATIO)) return false;
        if (!ctx.getPlayers().canSeeEnemies()) return true;

        return true;
    }

    @Override
    public Behavior run() {
        double disEnemyBase = ctx.getCTF().getEnemyBase().getLocation().getDistance(ctx.getInfo().getLocation());
        if (forcedFocus != null) {
            if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(forcedFocus);
            else if (forcedFocus != null) ctx.getMove().turnTo(forcedFocus);
        } else if (disEnemyBase <= 800 && disEnemyBase > DISTANCE_STEALING_FLAG) {
            if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(ctx.getCTF().getEnemyBase());
            else ctx.getMove().turnTo(ctx.getCTF().getEnemyBase());
        } else if (ctx.getHidingPlace().getLocation().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_STEALING_FLAG) {
            ctx.getMove().turnHorizontal(90);
        } else if (ctx.getCTF().getEnemyBase().getLocation().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_STEALING_FLAG) {
            ctx.getMove().turnHorizontal(90);
        } else if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING) {
            if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(ctx.getFocucDefending());
            else ctx.getMove().turnTo(ctx.getFocucDefending());
        } else {
            if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(null);
            //else ctx.getMove().turnTo((Player) null);
        }
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
