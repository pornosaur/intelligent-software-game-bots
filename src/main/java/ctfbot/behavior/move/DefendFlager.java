package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import ctfbot.behavior.PlayerInfo;
import cz.cuni.amis.utils.Cooldown;

public class DefendFlager extends Behavior {

    private final static double DISTANCE_TO_BASE = 800;

    private Cooldown checkLoc;

    public DefendFlager(CTFBot bot) {
        super(bot, 900, Action.MOVE);
        checkLoc = new Cooldown(1300);
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIDefender() || ctx.amIFlagHolder()) return false;
        if (ctx.getCTF().isEnemyFlagHeld() && ctx.getWhoIsFlagHolder() != null) return true;

        return false;
    }

    @Override
    public Behavior run() {
        PlayerInfo p = ctx.getWhoIsFlagHolder();
        if (p == null) return null;

        if (checkLoc.tryUse()) {
            ctx.smartNavigate(p.getLocation());
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        /* This also check if flag holder does not exist */
        if (!isFlagHolderCloseToBase()) return this;

        ctx.getNavigation().stopNavigation();
        checkLoc.clear();

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        /* THIS IS FOR UPDATING LOCATION*/
        if ((toThiBehavior instanceof DefendFlager) && !isFlagHolderCloseToBase()) return true;
        if (toThiBehavior instanceof GetFlag) return true;
        if((toThiBehavior instanceof  CollectItem) && !ctx.hasReadyAnyWeapon()) return true;

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

    @Override
    public void reset() {
        checkLoc.clear();
    }

    @Override
    public String toString() {
        return "DEFENDING FLAGER";
    }

    private boolean isFlagHolderCloseToBase() {
        return calculateDistance() <= DISTANCE_TO_BASE;
    }

    private double calculateDistance() {
        if (ctx.getWhoIsFlagHolder() == null) return -1;
        return ctx.getCTF().getOurBase().getLocation().getDistance(ctx.getWhoIsFlagHolder().getLocation());
    }
}
