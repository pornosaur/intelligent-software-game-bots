package ctfbot.behavior;

import ctfbot.CTFBot;

import java.util.PriorityQueue;
import java.util.Queue;

public class BehaviorManager<BOTCTRL extends CTFBot> {

    private BOTCTRL ctx;

    private Queue<Behavior> behaviorList;

    private Behavior runningList[];

    public BehaviorManager(BOTCTRL ctx) {
        this.ctx = ctx;
        this.behaviorList = new PriorityQueue<>();
        this.runningList = new Behavior[]{null, null, null};
    }

    public void suggestBehavior(Behavior behavior) {
        if (behavior.isFiring()) behaviorList.add(behavior);
    }

    public void execute() {
        prepareBehaviorNew();
    }

    private void prepareBehaviorNew() {
        for (Behavior b : runningList) {
            if (b != null) behaviorList.add(b);
        }

        Behavior preparedByAction[] = {null, null, null};
        for (int i = 0; i < Action.values().length; i++) {
            for (Behavior b : behaviorList) {
                if (b.getAction() == Action.values()[i]) {
                    preparedByAction[i] = b;
                    break;
                }
            }
        }

        for (int i = 0; i < preparedByAction.length; i++) {
            Behavior b = preparedByAction[i], bRunning = runningList[i];

            if (bRunning != null) {
                boolean sameInstance = bRunning == b;
                if (bRunning.mayTransition(b)) {
                    runningList[i] = bRunning.transition(b);
                } else {
                    bRunning = bRunning.terminate();
                    if (bRunning == null) {
                        if (sameInstance) {
                            runningList[i] = null;
                        } else {
                            runningList[i] = b;
                            if (b != null) b.run();
                        }
                    }
                }
            } else {
                runningList[i] = b;
                if (b != null) b.run();
            }
        }

        behaviorList.clear();
    }

    public void cleanUp() {
        //TODO FORCED TERMINATE!!
        runningList = new Behavior[]{null, null, null};
        behaviorList.clear();
    }
}
