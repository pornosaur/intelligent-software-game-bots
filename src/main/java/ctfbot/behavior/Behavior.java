package ctfbot.behavior;

import ctfbot.CTFBot;
import cz.cuni.amis.utils.Cooldown;

public abstract class Behavior implements IBehavior, Comparable {

    /**
     * Instance of bot.
     */
    protected CTFBot ctx;

    /**
     * Behavior priority.
     */
    protected double priority;

    /**
     * Action.
     */
    private Action action;

    /**
     * Expiration cooldown.
     */
    protected Cooldown expiration;

    public Behavior(CTFBot bot, double priority, Action action) {
        this.ctx = bot;
        this.priority = priority;
        this.action = action;
        this.expiration = null;
    }

    public boolean isExpired() {
        return expiration != null && expiration.isCool();
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public double getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Object o) {
        Behavior b = (Behavior) o;

        if (priority < b.getPriority()) {
            return 1;
        } else if (priority > b.getPriority()) {
            return -1;
        }

        return 0;
    }

    public void reset() {
    }
}
