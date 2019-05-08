package ctfbot.behavior;

import ctfbot.CTFBot;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.utils.Cooldown;

import javax.xml.stream.Location;
import java.util.Locale;

public class HuntEnemy extends Behavior {

    private Cooldown checkPosition;


    public HuntEnemy(CTFBot bot) {
        super(bot, 120, Action.MOVE);
        checkPosition = new Cooldown(4000);
    }

    @Override
    public boolean isFiring() {
        //TODO maybe defenders also hunt enemy!!! But in different way!!!
        if (ctx.amIFlagHolder()) return false;
        if (ctx.amIAttacker() && ctx.getPlayers().canSeeEnemies()) return true;

        return false;
    }

    @Override
    public Behavior run() {
        Player enemy = ctx.getEnemyTarget();
        if (enemy == null) return null;

        if (CTFBot.CAN_USE_NAVIGATE) ctx.getNavigation().navigate(enemy);
        else ctx.navigateAStarPath(ctx.getNavPoints().getNearestNavPoint(enemy.getLocation()));

        return this;
    }

    @Override
    public Behavior terminate() {
        if (ctx.getEnemyTarget() != null) return this;
        if (ctx.getNavigation().isNavigating()) return this;

        ctx.getNavigation().stopNavigation();
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior instanceof BackFlag) return true;
        if (toThiBehavior instanceof GetFlag) return true;
        if (toThiBehavior instanceof CollectItem) {
            return !ctx.hasReadyAnyWeapon();
        }
        if ((toThiBehavior instanceof StoleFlag) && !ctx.amIAttacker()) {
            return true;
        }
        //if (toThiBehavior instanceof StoleFlag) return true;      //TODO Consider this

        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        if (checkPosition != null) checkPosition.clear();
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }

    @Override
    public void reset() {
        checkPosition.clear();
    }
}
