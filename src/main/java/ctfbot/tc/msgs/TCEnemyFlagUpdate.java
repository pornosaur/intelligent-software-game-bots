package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCEnemyFlagUpdate extends TCMessageData {

    private static final long serialVersionUID = 5098843294214852510L;

    public final Location location;

    public TCEnemyFlagUpdate(Location location) {
        this.location = location;
    }
}
