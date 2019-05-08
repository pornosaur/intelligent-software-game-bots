package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

public class FocusEnemy extends FocusBehavior {

    public FocusEnemy(CTFBot bot) {
        super(bot, 0, Action.FOCUS);
    }

    @Override
    public boolean isFiring() {
        if (target != null) return true;
        if (ctx.getPlayers().canSeeEnemies()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        if (target != null && !target.isVisible()) {
            target = null;
        }

        if (ctx.getSeenEnemyWithFlagID() != null) {
            Player p = ctx.getPlayers().getEnemies().get(ctx.getSeenEnemyWithFlagID());
            if (p != null && p.isVisible()) target = p;
        }

        if (target == null) target = ctx.getPlayers().getNearestVisibleEnemy();

        if (target != null) {
            if (ctx.getNavigation().isNavigating()) ctx.getNavigation().setFocus(target);
            else ctx.getMove().turnTo(target);
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if ((toThiBehavior instanceof FocusEnemy)) return true;
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
