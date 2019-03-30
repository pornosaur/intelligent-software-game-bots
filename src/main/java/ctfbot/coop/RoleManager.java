package ctfbot.coop;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoleManager {

    private static int MAX_ROLES = Roles.values().length;

    private AtomicInteger actualPos;

    private Map<UnrealId, Roles> botRolesMap;

    private UnrealId botCaptain;

    public RoleManager() {
        this.botCaptain = UnrealId.NONE;
        this.botRolesMap = new HashMap<>();
        this.actualPos = new AtomicInteger(0);
    }

    /**
     * Assign (register) role for each bot.
     *
     * @param botID bot id.
     * @return flag if the bot is also captain;
     */
    synchronized public boolean registerBot(final UnrealId botID) {
        actualPos.compareAndSet(MAX_ROLES, 0);
        final int pos = actualPos.getAndIncrement();

        botRolesMap.put(botID, Roles.values()[pos]);

        if (botCaptain == UnrealId.NONE) {
            botCaptain = botID;
            return true;
        }

        return false;
    }

    /**
     * Check if the bot with ID is captain of team.
     *
     * @param botID bot id.
     * @return true - the bot is captain, otherwise false.
     */
    boolean isCaptain(final UnrealId botID) {
        return botID == botCaptain;
    }

    /**
     * Get role of bot by his ID.
     *
     * @param botID bot ID.
     * @return return role if the bot is registered, otherwise null.
     */
    Roles getRoleByID(final UnrealId botID) {
        return botRolesMap.getOrDefault(botID, null);
    }
}
