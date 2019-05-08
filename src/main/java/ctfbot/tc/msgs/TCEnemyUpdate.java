package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCEnemyUpdate extends TCMessageData {

    private static final long serialVersionUID = 2256299073836077548L;

    public final Location location;
    public final UnrealId player;

    public TCEnemyUpdate(Location location, UnrealId player) {
        this.location = location;
        this.player = player;
    }
}
