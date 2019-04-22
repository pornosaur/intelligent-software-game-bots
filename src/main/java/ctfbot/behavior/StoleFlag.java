package ctfbot.behavior;

import ctfbot.CTFBot;


public class StoleFlag extends Behavior {

    public StoleFlag(CTFBot bot, double priority) {
        super(bot, priority, Action.MOVE);
    }

    @Override
    public boolean isFiring() {
        if (!ctx.amIFlager()) return false;
        if (ctx.amIFlagHolder() && ctx.getCTF().isOurTeamCarryingEnemyFlag()) return false;
       // if (ctx.getCTF().isOurFlagHome()) return true;
        if (ctx.getCTF().getEnemyFlag().isVisible()) return false;


        return true;
    }

    @Override
    public Behavior run() {
        ctx.navigateAStarPath(ctx.getCTF().getEnemyBase());
        return this;
    }

    private void stop() {
        ctx.getNavigation().stopNavigation();
    }

    @Override
    public Behavior terminate() {
        //TODO check some other conditionals -> maybe smth more important!!!

        if (!ctx.amIFlager()) {
            stop();
            return null;
        }
        if (!ctx.getCTF().isOurTeamCarryingEnemyFlag()) return this;
        if (ctx.getCTF().isOurTeamCarryingEnemyFlag() && !ctx.amIFlagHolder()) return this;

       stop();
        return null;
    }

    @Override
    public boolean mayTransition(Behavior toThiBehavior) {
        if (toThiBehavior == null) return false;
        if ((toThiBehavior instanceof CollectItem)) {
            if (toThiBehavior.getPriority() > this.priority) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Behavior transition(Behavior transitionTo) {
        return transitionTo.run();
    }

    @Override
    public Action[] getRequiredAction() {
        return new Action[0];
    }
}
