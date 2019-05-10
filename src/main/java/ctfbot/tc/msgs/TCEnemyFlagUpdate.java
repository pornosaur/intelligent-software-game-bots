package ctfbot.tc.msgs;

import ctfbot.behavior.PlayerInfo;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCEnemyFlagUpdate extends TCMessageData {

    private static final long serialVersionUID = 5098843294214852510L;

    public final PlayerInfo player;

    public TCEnemyFlagUpdate(PlayerInfo player) {
        this.player = player;
    }
}
