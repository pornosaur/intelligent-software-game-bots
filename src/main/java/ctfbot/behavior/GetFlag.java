package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;


public class GetFlag extends Behavior {

    private Location lastTarget;

    public GetFlag(CTFBot bot) {
        super(bot, 999, Action.MOVE);
        lastTarget = null;
    }

    @Override
    public boolean isFiring() {
        if (ctx.amIFlagHolder()) return false;
        if (ctx.getCTF().getOurFlag().isVisible() && !ctx.getCTF().isOurFlagHome()) return true;
        if (ctx.getCTF().isOurFlagDropped() && ctx.getOurFlagLoc() != null) return true;
        if (ctx.getCTF().getEnemyFlag().isVisible() && !ctx.getCTF().isEnemyFlagHome()) return true;
        if (ctx.getCTF().isEnemyFlagDropped() && ctx.getEnemyFlagLoc() != null) return true;

        return false;
    }

    @Override
    public Behavior run() {
        if (ctx.getCTF().getOurFlag().isVisible() && !ctx.getCTF().isOurFlagHome()) {
            lastTarget = ctx.getCTF().getOurFlag().getLocation();
        } else if (ctx.getCTF().isOurFlagDropped() && ctx.getOurFlagLoc() != null) {
            lastTarget = ctx.getOurFlagLoc();
        } else if (ctx.getCTF().getEnemyFlag().isVisible() && !ctx.getCTF().isEnemyFlagHome()) {
            lastTarget = ctx.getCTF().getEnemyFlag().getLocation();
        } else if (ctx.getCTF().isEnemyFlagDropped() && ctx.getEnemyFlagLoc() != null) {
            lastTarget = ctx.getEnemyFlagLoc();
        }

        ctx.getNavigation().navigate(lastTarget);
        return this;
    }

    @Override
    public Behavior terminate() {
        boolean ourFlagIsVisible = ctx.getCTF().getOurFlag().isVisible() && !ctx.getCTF().isOurFlagHome();
        if (ourFlagIsVisible && ctx.getCTF().getOurFlag().getLocation().equals(lastTarget)) return this;
        if (ctx.getCTF().isOurFlagDropped() && lastTarget.equals(ctx.getOurFlagLoc())) return this;

        if (ctx.getCTF().getEnemyFlag() != null) {
            boolean isFriend = ctx.getPlayers().getFriends().containsKey(ctx.getCTF().getEnemyFlag().getHolder());
            boolean enemyFlagIsVisible = ctx.getCTF().getEnemyFlag().isVisible() && !ctx.getCTF().isEnemyFlagHome()
                    && !isFriend;
            if (enemyFlagIsVisible && ctx.getCTF().getEnemyFlag().getLocation().equals(lastTarget)) return this;
        }
        if (ctx.getCTF().isEnemyFlagDropped() && lastTarget.equals(ctx.getEnemyFlagLoc())) return this;

        ctx.getNavigation().stopNavigation();

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
