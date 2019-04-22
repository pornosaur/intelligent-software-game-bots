package ctfbot.coop;

import ctfbot.CTFBot;
import ctfbot.tc.msgs.TCRoleUpdate;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoleManager {

    private static final int MAX_ROLES = Roles.values().length;


    private CTFBot bot;

    public RoleManager(CTFBot bot) {
      /*  this.CAPTAIN_CHANCE = RAND.nextInt(MAX_RAND_VAL);
        this.hasCaptain = new AtomicBoolean(false);
        this.alreadySetCaptain = new AtomicBoolean(false);
        this.asked = false;*/
    }



   /* public TCRoleUpdate getCaptainAsk(final UnrealId sender) {
        asked = true;
        return new TCRoleUpdate(sender, CAPTAIN_CHANCE);
    }

    public boolean isAsked() {
        return asked;
    }

    public void compareAndSetCaptain(double chance) {
        if (chance < CAPTAIN_CHANCE) {
            hasCaptain.set(true);
        } else if (chance > CAPTAIN_CHANCE) {
            hasCaptain.set(false);
        } else {
            if (!alreadySetCaptain.get()) {
                alreadySetCaptain.set(true);
                hasCaptain.set(true);
            } else {
                hasCaptain.set(false);
            }
        }
    }

    public boolean isCaptain() {
        return hasCaptain.get();
    }

    public void setChance(long c) {
        CAPTAIN_CHANCE = c;
    }*/
}
