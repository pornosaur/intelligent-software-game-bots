package ctfbot.behavior;

public interface IBehavior {

    /**
     * If behavior is ready to use.
     *
     * @return true - can use.
     */
    boolean isFiring();

    /**
     * Run actual behavior.
     *
     * @return instance of behavior.
     */
    Behavior run();

    /**
     * Terminate running behavior.
     *
     * @return null - it was terminated; instance - can not terminate.
     */
    Behavior terminate();

    /**
     * Can transition to another behavior.
     *
     * @param toThiBehavior another behavior.
     * @return true - can transition.
     */
    boolean mayTransition(Behavior toThiBehavior);

    /**
     * Transition to another behavior.
     *
     * @param transitionTo another behavior.
     * @return instance of behavior.
     */
    Behavior transition(Behavior transitionTo);

    /**
     * Get action of behavior.
     *
     * @return behavior action.
     */
    Action getAction();

    /**
     * Get priority.
     *
     * @return priority.
     */
    double getPriority();
}
