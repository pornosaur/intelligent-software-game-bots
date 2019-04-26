package ctfbot.behavior;

import ctfbot.CTFBot;

public abstract class Behavior implements IBehavior, Comparable {

    protected CTFBot ctx;

    protected double priority;

    protected Behavior transition;

    private Action action;

    public Behavior(CTFBot bot, double priority, Action action) {
        this.ctx = bot;
        this.priority = priority;
        this.action = action;
        this.transition = null;
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

        if(priority < b.getPriority()){
            return 1;
        } else if (priority > b.getPriority()) {
            return -1;
        }

        return 0;
    }
}
