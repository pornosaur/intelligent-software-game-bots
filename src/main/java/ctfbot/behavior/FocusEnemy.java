package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

public class FocusEnemy extends Behavior {


    public FocusEnemy(CTFBot bot) {
        super(bot, 0, Action.FOCUS);
    }


    @Override
    public boolean isFiring() {
        //TODO check from seen enemies list
        if (ctx.getPlayers().canSeeEnemies()) return true;
        //  if (ctx.getNearestLastSeenEnemy() != null) return true;

        return false;
    }

    @Override
    public Behavior run() {
        Location enemyPlayer = ctx.getPlayers().getNearestVisibleEnemy().getLocation();
        boolean visibleFlag = ctx.getCTF().isEnemyTeamCarryingOurFlag() && ctx.getCTF().getOurFlag().isVisible();

        if (visibleFlag) {
            Player newP = ctx.getPlayers().getPlayer(ctx.getCTF().getOurFlag().getHolder());
            enemyPlayer = newP == null ? enemyPlayer : newP.getLocation();
        }

        if (ctx.getNavigation().isNavigating())
            ctx.getNavigation().setFocus(enemyPlayer);
        else
            ctx.getMove().turnTo(enemyPlayer);

        return this;
    }

    @Override
    public Behavior terminate() {
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if ((toThiBehavior instanceof FocusEnemy)) return true;

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        //((FocusEnemy) transitionTo).setEnemy(enemy);
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

}
