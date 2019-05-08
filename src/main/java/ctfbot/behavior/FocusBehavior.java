package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

public abstract class FocusBehavior extends Behavior {

    protected Player target;

    public FocusBehavior(CTFBot bot, double priority, Action action) {
        super(bot, 0.0, Action.FOCUS);
        target = null;
    }

    @Override
    public void reset() {
        target = null;
    }

    public Player getTarget() {
        return target;
    }
}
