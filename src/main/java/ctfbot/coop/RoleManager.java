package ctfbot.coop;

import ctfbot.tc.msgs.TCRoleUpdate;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class RoleManager {

    private static final int MAX_RAND_VAL = Integer.MAX_VALUE;

    private final Random RAND = new Random();

    private static final int MAX_ROLES = Roles.values().length;

    private final int CAPTAIN_CHANCE;

    private AtomicBoolean hasCaptain;

    private AtomicBoolean alreadySetCaptain;

    private AtomicLong askingTime;

    private boolean asked;


    public RoleManager() {
        this.CAPTAIN_CHANCE = RAND.nextInt(MAX_RAND_VAL);
        this.hasCaptain = new AtomicBoolean(false);
        this.alreadySetCaptain = new AtomicBoolean(false);
        this.askingTime = new AtomicLong(-1);
        this.asked = false;
    }

    public TCRoleUpdate getCaptainAsk(final UnrealId sender) {
        TCRoleUpdate role = new TCRoleUpdate(sender, CAPTAIN_CHANCE);
        askingTime.set(role.getSimTime());
        return role;
    }

    public boolean isAsked() {
        return asked;
    }

    public long getAskingTime() {
        return askingTime.get();
    }

    public void compareAndSetCaptain(int chance) {
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
}
