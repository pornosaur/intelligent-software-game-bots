package ctfbot.tc.msgs;

import ctfbot.behavior.PlayerInfo;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCFlager extends TCMessageData {

    private static final long serialVersionUID = -7560646859360894084L;

    public final PlayerInfo player;

    public TCFlager(PlayerInfo player) {
        this.player = player;
    }
}
