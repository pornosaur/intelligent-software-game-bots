package ctfbot.behavior;

import ctfbot.CTFBot;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class BehaviorManager<BOTCTRL extends CTFBot> {

    private BOTCTRL ctx;

    private List<Behavior> behaviors;

    private Queue<Behavior> nextBehaviors;

    private Behavior runningBehaviors[];

    private final Object MUTEX;

    public BehaviorManager(BOTCTRL ctx) {
        this.ctx = ctx;
        this.behaviors = new ArrayList<>();
        this.nextBehaviors = new PriorityQueue<>();
        this.runningBehaviors = new Behavior[]{null, null, null};
        this.MUTEX = new Object();
    }

    public void suggestBehavior(Behavior behavior) {
        if (!behaviors.contains(behavior)) behaviors.add(behavior);
    }

    public void execute() {
        prepareBehaviorNew();
    }

    private void prepareBehaviorNew() {
        Iterator it = behaviors.iterator();
        while (it.hasNext()) {
            Behavior b = (Behavior) it.next();
            if (b.isExpirated()) {
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
                    break; //TODO: think about this
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

        nextBehaviors.clear();
    }

    public void cleanUp() {
        //TODO FORCED TERMINATE!!
        runningBehaviors = new Behavior[]{null, null, null};
        nextBehaviors.clear();
    }

    public void resetMoveAction() {
        synchronized (MUTEX) {
            runningBehaviors[Action.MOVE.ordinal()] = null;
            ctx.getNavigation().stopNavigation();
            ctx.getLog().log(Level.INFO, "----------------RESET MOVE ACTION-------------------");
        }
    }
}
