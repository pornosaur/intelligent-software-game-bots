package ctfbot.behavior;

public interface IBehavior {

    boolean isFiring();

    Behavior run();

    Behavior terminate();

    boolean mayTransition(Behavior toThiBehavior);

    Behavior transition(Behavior transitionTo);

    Action[] getRequiredAction();

    Action getAction();

    double getPriority();
}
