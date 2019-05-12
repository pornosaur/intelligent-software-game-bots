package ctfbot.behavior;

import ctfbot.CTFBot;
import ctfbot.behavior.focus.FocusBehavior;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

import java.util.*;
import java.util.logging.Level;

public class BehaviorManager<BOTCTRL extends CTFBot> {

    /**
     * Bot instance.
     */
    private BOTCTRL ctx;

    /**
     * List of suggested behaviors.
     */
    private List<Behavior> behaviors;

    /**
     * List of behaviors which can run in actual frame.
     */
    private Queue<Behavior> nextBehaviors;

    /**
     * List of running behaviors.
     */
    private Behavior runningBehaviors[];

    /**
     * Mutex for decision making about behaviors.
     */
    private final Object MUTEX;

    public BehaviorManager(BOTCTRL ctx) {
        this.ctx = ctx;
        this.behaviors = new ArrayList<>();
        this.nextBehaviors = new PriorityQueue<>();
        this.runningBehaviors = new Behavior[]{null, null, null};
        this.MUTEX = new Object();
    }

    /**
     * Suggest new behavior for manger (which does not exist).
     *
     * @param behavior
     */
    public void suggestBehavior(Behavior behavior) {
        if (!behaviors.contains(behavior)) behaviors.add(behavior);
    }

    /**
     * Preparing behaviors for actual frame.
     */
    public void execute() {
        prepareBehaviorNew();
    }

    /**
     * Preparing behaviors for actual frame.
     */
    private void prepareBehaviorNew() {
        Iterator it = behaviors.iterator();
        while (it.hasNext()) {
            Behavior b = (Behavior) it.next();
            if (b.isExpired()) {
                it.remove();
                continue;
            }
            if (b.isFiring()) nextBehaviors.add(b);
        }

        Behavior preparedByAction[] = {null, null, null};
        for (int i = 0; i < Action.values().length; i++) {
            for (Behavior b : nextBehaviors) {
                if (b.getAction() == Action.values()[i]) {
                    preparedByAction[i] = b;
                    break;
                }
            }
        }

        synchronized (MUTEX) {
            for (int i = 0; i < Action.values().length; i++) {
                Behavior nextB = preparedByAction[i], runningB = runningBehaviors[i];

                if (runningB != null) {
                    if (runningB.mayTransition(nextB)) {
                        runningBehaviors[i] = runningB.transition(nextB);
                    } else {
                        runningBehaviors[i] = runningB.terminate();
                        if (runningBehaviors[i] == null && nextB != null) {
                            runningBehaviors[i] = nextB.run();
                        }
                    }
                } else if (nextB != null) {
                    runningBehaviors[i] = nextB.run();
                }
            }
        }

        if (runningBehaviors[Action.MOVE.ordinal()] != null) {
            ctx.getInfo().getBotName().setInfo(runningBehaviors[Action.MOVE.ordinal()].toString());
        }

        nextBehaviors.clear();
    }

    /**
     * Clean all running behaviors.
     */
    public void cleanUp() {
        resetMoveAction();
        runningBehaviors = new Behavior[]{null, null, null};
        nextBehaviors.clear();
    }

    /**
     * Get actual target from focus behaviors.
     *
     * @return player as target. (can be null)
     */
    public Player getPlayerTarget() {
        if (runningBehaviors[Action.FOCUS.ordinal()] != null) {
            FocusBehavior focus = (FocusBehavior) runningBehaviors[Action.FOCUS.ordinal()];
            return focus.getTarget();
        }

        return null;
    }

    /**
     * Reset move action when bot died or was stuck.
     */
    public void resetMoveAction() {
        synchronized (MUTEX) {
            if (runningBehaviors[Action.MOVE.ordinal()] != null) runningBehaviors[Action.MOVE.ordinal()].reset();
            runningBehaviors[Action.MOVE.ordinal()] = null;
            ctx.getNavigation().stopNavigation();
            ctx.getLog().log(Level.INFO, "----------------RESET MOVE ACTION-------------------");
        }
    }
}
