package ctfbot.tc.msgs;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Item;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.ItemPickedUp;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPointMessage;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.messages.TCMessageData;

public class TCItemUpdate extends TCMessageData {

	/**
	 * Auto-generated.
	 */
	private static final long serialVersionUID = 3573776037176945900L;
	
	public UnrealId id;
	public Boolean	spawned;
	public long     time;
	
	public TCItemUpdate(ItemPickedUp pickedUp) {
		this.id = pickedUp.getId();
		this.time = pickedUp.getSimTime();
		this.spawned = false;			
	}
	
	public TCItemUpdate(Item item) {
		this.id = item.getId();
		this.time = item.getSimTime();
	
		if (item.isVisible()) {
			spawned = true;			
		} else {
			if (item.getNavPoint() != null && item.getNavPoint().isVisible()) {
				spawned = false;
			} else {
				spawned = null;
			}
		}
	}
	
	public TCItemUpdate(NavPoint navPoint) {
		this.id = navPoint.getItem();
		this.time = navPoint.getSimTime();
	
		if (navPoint.isVisible()) {
			spawned = navPoint.isItemSpawned();			
		} else {
			if (navPoint.getItemInstance() != null && navPoint.getItemInstance().isVisible()) {
				spawned = true;
			} else {
				spawned = null;
			}
		}
	}

	public NavPoint getUpdate(Item item, NavPoint navPoint) {
		if (this.spawned == null) return navPoint;
		
		boolean spawned = this.spawned == null ? navPoint.isItemSpawned() : this.spawned;
		
		NavPoint update = new NavPointMessage(
				navPoint.getId(),
				navPoint.getLocation(),
				navPoint.getVelocity(),
				navPoint.isVisible(),
				navPoint.getItem(),
				navPoint.getItemClass(),
				spawned,
				navPoint.isDoorOpened(),
				navPoint.getMover(),
				navPoint.getLiftOffset(),
				navPoint.isLiftJumpExit(),
				navPoint.isNoDoubleJump(),
				navPoint.isInvSpot(),
				navPoint.isPlayerStart(),
				navPoint.getTeamNumber(),
				navPoint.isDomPoint(),
				navPoint.getDomPointController(),
				navPoint.isDoor(),
				navPoint.isLiftCenter(),
				navPoint.isLiftExit(),
				navPoint.isAIMarker(),
				navPoint.isJumpSpot(),
				navPoint.isJumpPad(),
				navPoint.isJumpDest(),
				navPoint.isTeleporter(),
				navPoint.getRotation(),
				navPoint.isRoamingSpot(),
				navPoint.isSnipingSpot(),
				navPoint.getItemInstance(),
				navPoint.getOutgoingEdges(),
				navPoint.getIncomingEdges(),
				navPoint.getPreferedWeapon()
			);
		
		return update;
	}
	
}
