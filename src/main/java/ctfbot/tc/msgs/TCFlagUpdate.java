package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfoMessage;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCFlagUpdate extends TCMessageData {

	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = 3573776037176945900L;
	
	public UnrealId id;
	public UnrealId holder;
	public Location location;
	public String   state;
	public long     time;
	
	public TCFlagUpdate(FlagInfo flag) {
		this.id = flag.getId();
		this.time = flag.getSimTime();
		
		if (flag.isVisible()) {
			holder = flag.getHolder();
			location = flag.getLocation();
			state = flag.getState();
		}
	}
	
	public FlagInfo getUpdate(FlagInfo flag) {
		Location location = this.location == null ? flag.getLocation() : this.location;
		String   state    = this.state == null    ? flag.getState()    : this.state;
		UnrealId holder   = this.location == null ? flag.getHolder()   : this.holder;
		
		return new FlagInfoMessage(id, location, holder, flag.getTeam(), flag.isVisible(), state);
	}
	
}
