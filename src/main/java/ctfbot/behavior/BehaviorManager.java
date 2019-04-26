package ctfbot.behavior;

import ctfbot.CTFBot;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;

public class BehaviorManager<BOTCTRL extends CTFBot> {

    private BOTCTRL ctx;

    private List<Behavior> behaviors;

    private Queue<Behavior> nextBehaviors;

    private Behavior runningBehaviors[];

    public BehaviorManager(BOTCTRL ctx) {
        this.ctx = ctx;
        this.behaviors = new ArrayList<>();
        this.nextBehaviors = new PriorityQueue<>();
        this.runningBehaviors = new Behavior[]{null, null, null};
    }

    public void suggestBehavior(Behavior behavior) {
        if (!behaviors.contains(behavior)) behaviors.add(behavior);
    }

    public void execute() {
        prepareBehaviorNew();
    }

    private void prepareBehaviorNew() {
        for (Behavior b : behaviors) {
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

        for (int i = 0; i < Action.values().length; i++) {
            Behavior nextB = preparedByAction[i], runningB = runningBehaviors[i];

            if (runningB != null) {
                if (runningB.mayTransition(nextB)) {
                    runningBehaviors[i] = runningB.transition(nextB);
                } else {
                    runningBehaviors[i] = runningB.terminate();
                    if (runningBehaviors[i] == null && nextB != null)
                        runningBehaviors[i] = nextB.run();

                }
            } else if (nextB != null) {
                runningBehaviors[i] = nextB.run();
            }
        }

        nextBehaviors.clear();
    }

    public void cleanUp() {
        //TODO FORCED TERMINATE!!
        runningBehaviors = new Behavior[]{null, null, null};
        nextBehaviors.clear();
    }
}
