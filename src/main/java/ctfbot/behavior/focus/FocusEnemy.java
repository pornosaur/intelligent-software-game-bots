package ctfbot.behavior.focus;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import ctfbot.map.MapPlaces;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.utils.Cooldown;

public class FocusEnemy extends FocusBehavior {

    private final double DISTANCE_DEFENDING = 100.0;

    private Cooldown cd = new Cooldown(600);
    private boolean backward;

    public FocusEnemy(CTFBot bot) {
        super(bot, 120, Action.FOCUS);
        backward = false;
    }

    @Override
    public boolean isFiring() {
        if (ctx.getDefendingPlace().getDistance(ctx.getInfo().getLocation()) <= DISTANCE_DEFENDING
                && !ctx.getPlayers().canSeeEnemies()) return false;

        return true;
    }

    @Override
    public Behavior run() {
        if (target != null && target.isVisible()) {
            ctx.getNavigation().setFocus(target);
            return this;
        } else if (target != null) {
            target = null;
        }

        if (ctx.getSeenEnemyWithOurFlag() != null && ctx.getSeenEnemyWithOurFlag().getId() != null
                && ctx.getCTF().isEnemyTeamCarryingOurFlag()) {
            Player tmpTarget = ctx.getPlayers().getEnemies().get(ctx.getSeenEnemyWithOurFlag().getId());
            if (tmpTarget != null && tmpTarget.isVisible())  target = tmpTarget;
        }

        if (target == null) {
            target = ctx.getPlayers().getNearestVisibleEnemy();
        }

        if (target == null) {
            if (ctx.getSeenEnemyWithOurFlag() != null && ctx.getSeenEnemyWithOurFlag().getLocation() != null) {
                ctx.getNavigation().setFocus(ctx.getSeenEnemyWithOurFlag().getLocation());
            } else {
                if (cd.tryUse()) {
                    if (backward) ctx.getNavigation().setFocus(ctx.getHidingPlace());
                    else ctx.getNavigation().setFocus(ctx.focusWalkEnemy());

                    backward = !backward;
                }
            }
        }

        if (target != null) {
            ctx.getNavigation().setFocus(target);
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        target = null;
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if ((toThiBehavior instanceof FocusEnemy)) return true;
        //if (toThiBehavior instanceof FocusPath) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        target = null;
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

}
