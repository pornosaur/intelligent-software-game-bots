package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.BombInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCRoleUpdate extends TCMessageData {

    private static final long serialVersionUID = 7807538139733391545L;

    private final UnrealId sender;

    private final double value;

    public TCRoleUpdate(UnrealId player, double value) {
        this.value = value;
        this.sender = player;
    }

    public UnrealId getSender() {
        return sender;
    }

    public double getValue() {
        return value;
    }
}
