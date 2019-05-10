package ctfbot.behavior.move;

import ctfbot.CTFBot;
import ctfbot.behavior.Action;
import ctfbot.behavior.Behavior;
import ctfbot.behavior.PlayerInfo;
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

        /* OUR FLAG IS DROPPED OR HELD AND WAS SEEN */
        /*if (ctx.getSeenEnemyWithOurFlag() != null && !ctx.getCTF().isOurFlagHome() && ctx.amINearestPlayer()) return true;*/
        if (ctx.getCTF().getOurFlag().isVisible() && ctx.getCTF().getOurFlag().getLocation().getDistance(ctx.getInfo().getLocation()) <= 800 && !ctx.getCTF().isOurFlagHome())
            return true;

        if (ctx.getCTF().isEnemyTeamCarryingOurFlag()) return true;

       /* ENEMY FLAG IS DROPPED AND WAS SEEN */
        if (ctx.getSeenEnemyFlag() != null && ctx.getCTF().isEnemyFlagDropped()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        Location target = null;
        if (ctx.getCTF().getOurFlag().isVisible() &&
                ctx.getCTF().getOurFlag().getLocation().getDistance(ctx.getInfo().getLocation()) <= 800 && !ctx.getCTF().isOurFlagHome()) {
            target = ctx.getCTF().getOurFlag().getLocation();
        } else if (ctx.getCTF().isEnemyTeamCarryingOurFlag()) {
            target = ctx.getCTF().getEnemyBase().getLocation();
        } /*else if (ctx.getSeenEnemyWithOurFlag() != null && !ctx.getCTF().isOurFlagHome()
                && ctx.amINearestPlayer()) {
            PlayerInfo ourFlagInfo = ctx.getSeenEnemyWithOurFlag();
            if (ourFlagInfo.getLocation() != null) target = ourFlagInfo.getLocation();
        }*/ else if (ctx.getSeenEnemyFlag() != null && !ctx.getCTF().isEnemyFlagHome()
                && !ctx.getCTF().isOurTeamCarryingEnemyFlag()) {
            PlayerInfo enemyFlagInfo = ctx.getSeenEnemyFlag();
            if (enemyFlagInfo.getLocation() != null) target = enemyFlagInfo.getLocation();
        }

        if (target != null) {
            if (CTFBot.CAN_USE_NAVIGATE) ctx.getNavigation().navigate(target);
            else ctx.navigateAStarPath(ctx.getNavPoints().getNearestNavPoint(target));
            lastTarget = target;
        }

        return this;
    }

    @Override
    public Behavior terminate() {
        if (lastTarget != null && !ctx.cantSeeFlagButClose(lastTarget) && ctx.getNavigation().isNavigating() && (
                !ctx.getCTF().isOurFlagHome() || ctx.getCTF().isEnemyFlagDropped())) return this;

        ctx.getNavigation().stopNavigation();
        lastTarget = null;

        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        //if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof DefendFlager) return true;
        if (toThiBehavior instanceof StealFlag) return true;
        if (toThiBehavior instanceof BackFlag) return true;
        if (toThiBehavior instanceof DefendBase) return true;


        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        lastTarget = null;
        return transitionTo.run();
    }

    @Override
    public void reset() {
        lastTarget = null;
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }


    @Override
    public String toString() {
        return "HAUNTING FLAG";
    }


}
