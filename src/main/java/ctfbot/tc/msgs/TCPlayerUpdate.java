package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.base3d.worldview.object.Velocity;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.PlayerMessage;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCPlayerUpdate extends TCMessageData {

	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = 3573776037176945900L;
	
	public UnrealId id;
	public Location location;
	public Rotation rotation;
	public Velocity velocity;
	public String   weapon;
	public long     time;
	
	public TCPlayerUpdate(Player player) {
		this.id = player.getId();
		this.time = player.getSimTime();
		
		this.location = player.getLocation();
		this.rotation = player.getRotation();
		this.velocity = player.getVelocity();
		this.weapon   = player.getWeapon();
	}

	public TCPlayerUpdate(AgentInfo info) {
		this.id = info.getId();
		this.time = info.getSelf().getSimTime();
		
		this.location = info.getLocation();
		this.rotation = info.getRotation();
		this.velocity = info.getVelocity();
		this.weapon = info.getCurrentWeaponName();
	}

	public Player getUpdate(Player player) {
		Location location = this.location == null ? player.getLocation() : this.location;
		Rotation rotation = this.rotation == null ? player.getRotation() : this.rotation;
		Velocity velocity = this.velocity == null ? player.getVelocity() : this.velocity;
		String   weapon   = this.weapon == null   ? player.getWeapon()   : this.weapon;
		
		Player update = new PlayerMessage(
							player.getId(),
							player.getJmx(),
							player.getName(),
							player.isSpectator(),
							player.getAction(),
							player.isVisible(),
							rotation,
							location,
							velocity,
							player.getTeam(),
							weapon,
							player.isCrouched(),
							player.getFiring(),
							player.getEmotLeft(),
							player.getEmotCenter(),
							player.getEmotRight(),
							player.getBubble(),
							player.getAnim()
						);
		 
		return update;
	}
	
}
