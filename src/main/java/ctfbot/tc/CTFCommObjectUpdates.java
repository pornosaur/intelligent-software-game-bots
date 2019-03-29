package ctfbot.tc;

import ctfbot.CTFBot;
import ctfbot.tc.msgs.TCFlagUpdate;
import ctfbot.tc.msgs.TCItemUpdate;
import ctfbot.tc.msgs.TCPlayerUpdate;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.AnnotationListenerRegistrator;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.EventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.ObjectClassEventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.object.IWorldObject;
import cz.cuni.amis.pogamut.base.communication.worldview.object.event.WorldObjectUpdatedEvent;
import cz.cuni.amis.pogamut.base.utils.logging.LogCategory;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.EndMessage;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Item;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.ItemPickedUp;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.teamcomm.bot.UT2004TCClient;
import cz.cuni.amis.pogamut.ut2004.teamcomm.mina.server.messages.TCInfoBotJoined;

/**
 * Communication module for the CTF Bot that maintains team knowledge about states of {@link Player}s, {@link Item}s and {@link FlagInfo}s.
 * 
 * @author Jimmy
 *
 * @param <BOTCTRL> Context class of the action. It's an shared object used by
 * all primitives. it is used as a shared memory and for interaction with the
 * environment.
 */
public class CTFCommObjectUpdates<BOTCTRL extends CTFBot> {
	
	private AnnotationListenerRegistrator listenerRegistrator;
	
	private BOTCTRL ctx;
	
	private UT2004TCClient comm;
	
	private LogCategory log;
	
	private boolean firstUpdate = true;
	
	public CTFCommObjectUpdates(BOTCTRL ctx) {
		this.ctx = ctx;
		this.log = ctx.getBot().getLogger().getCategory("CTFCommObjectUpdates");
		this.comm = ctx.getTCClient();
		
		// INITIALIZE @EventListener SNOTATED LISTENERS
		listenerRegistrator = new AnnotationListenerRegistrator(this, ctx.getWorldView(), ctx.getBot().getLogger().getCategory("Listeners"));
		listenerRegistrator.addListeners();
	}
	
	// ====================
	// COMMUNICATION EVENTS
	// ====================
		
	@EventListener(eventClass=TCFlagUpdate.class)
	public void flagUpdate(TCFlagUpdate msg) {
		IWorldObject obj = ctx.getWorldView().get(msg.id);
		
		// DO WE HAVE THE OBJECT?
		if (obj == null) return;
		
		// IS IT OF CORRECT TYPE?
		if (!(obj instanceof FlagInfo)) return;
		FlagInfo flag = (FlagInfo)obj;
		
		// DOES THE MESSAGE CARRY MORE RECENT INFORMATION?
		if (flag.getSimTime() >= msg.getSimTime()) return;
		
		// IF SO => UPDATE!
		
		// CONSTRUCT UPDATE MESSAGE
		FlagInfo update = msg.getUpdate(flag);
		
		log.info("Updating FLAG: " + update);
		
		// NOTIFY WORLDVIEW, IMMEDIATELY TRIGGERING ALL RELEVANT LISTENERS 
		ctx.getWorldView().notifyImmediately(update);		
	}
	
	@EventListener(eventClass=TCItemUpdate.class)
	public void itemUpdate(TCItemUpdate msg) {
		IWorldObject obj = ctx.getWorldView().get(msg.id);
		
		// DO WE HAVE THE OBJECT?
		if (obj == null) return;
		
		// IS IT OF CORRECT TYPE?
		if (!(obj instanceof Item)) return;		
		Item item = (Item)obj;
		
		// DOES THE ITEM LIES ON THE SPAWNING POINT?
		if (item.getNavPoint() == null) return;
		
		// DOES THE MESSAGE CARRY MORE RECENT INFORMATION?
		if (item.getSimTime() >= msg.getSimTime()) return;
		
		// IF SO => UPDATE!
		
		// CONSTRUCT UPDATE MESSAGE
		NavPoint update = msg.getUpdate(item, item.getNavPoint());
		
		log.info("Updating ITEM NAVPOINT: " + update);
		
		// NOTIFY WORLDVIEW, IMMEDIATELY TRIGGERING ALL RELEVANT LISTENERS 
		ctx.getWorldView().notifyImmediately(update);
	}
	
	@EventListener(eventClass=TCPlayerUpdate.class)
	public void playerUpdate(TCPlayerUpdate msg) {
		IWorldObject obj = ctx.getWorldView().get(msg.id);
		
		// DO WE HAVE THE OBJECT?
		if (obj == null) return;
		
		// IS IT OF CORRECT TYPE?
		if (!(obj instanceof Player)) return;		
		Player player = (Player)obj;
		
		// DOES THE MESSAGE CARRY MORE RECENT INFORMATION?
		if (player.getSimTime() >= msg.getSimTime()) return;
		
		// IF SO => UPDATE!
		
		// CONSTRUCT UPDATE MESSAGE
		Player update = msg.getUpdate(player);
		
		log.info("Updating PLAYER: " + update);
		
		// NOTIFY WORLDVIEW, IMMEDIATELY TRIGGERING ALL RELEVANT LISTENERS 
		ctx.getWorldView().notifyImmediately(update);
	}
	
	@EventListener(eventClass=TCInfoBotJoined.class)
    public void botJoined(TCInfoBotJoined botJoined) {		
		if (botJoined.getTeam() != ctx.getInfo().getTeam()) {
			log.info("Bot joined: " + botJoined.getBotId() + " but to different team.");
			return;
		} else {
			log.info("Bot joined: " + botJoined.getBotId() + " to my team!");
		}
		// NEW BOT JOINED
		// => send actual infos what you know
		
		log.info("Sending object info to: " + botJoined.getBotId());
		
		sendMe(botJoined.getBotId());
		sendItems(botJoined.getBotId());
		sendPlayers(botJoined.getBotId());
		sendFlags(botJoined.getBotId());		
    }

	// ============
	// WORLD EVENTS
	// ============
	
	@EventListener(eventClass=ItemPickedUp.class)
	public void itemPickedUp(ItemPickedUp pickedUp) {
		// DO NOT SEND INFOS BEFORE THE BOT STARTS PLAYING THE GAME
		if (firstUpdate) return;
		
		// ITEM ON SPAWNING POINT?
		if (ctx.getItems().getItem(pickedUp.getId()) == null) return;
		
		comm.sendToTeamOthers(new TCItemUpdate(pickedUp));
	}
	
	@ObjectClassEventListener(eventClass=WorldObjectUpdatedEvent.class, objectClass=NavPoint.class)
	public void navPointUpdated(WorldObjectUpdatedEvent<NavPoint> event) {
		// DO NOT SEND INFOS BEFORE THE BOT STARTS PLAYING THE GAME
		if (firstUpdate) return;
		
		NavPoint navPoint = event.getObject();
		
		if (navPoint.isVisible() && navPoint.getItemInstance() != null) {
			comm.sendToTeamOthers(new TCItemUpdate(navPoint));
		}
	}
	
	@ObjectClassEventListener(eventClass=WorldObjectUpdatedEvent.class, objectClass=Item.class)
	public void itemUpdated(WorldObjectUpdatedEvent<Item> event) {
		// DO NOT SEND INFOS BEFORE THE BOT STARTS PLAYING THE GAME
		if (firstUpdate) return;
		
		Item item = event.getObject();
		
		if (item.isVisible()) {
			comm.sendToTeamOthers(new TCItemUpdate(item));
		}
	}
	
	@ObjectClassEventListener(eventClass=WorldObjectUpdatedEvent.class, objectClass=Player.class)
	public void playerUpdated(WorldObjectUpdatedEvent<Player> event) {
		// DO NOT SEND INFOS BEFORE THE BOT STARTS PLAYING THE GAME
		if (firstUpdate) return;
		
		Player player = event.getObject();
		
		if (!player.isSpectator() && player.isVisible() && player.getTeam() != ctx.getInfo().getTeam()) {
			comm.sendToTeamOthers(new TCPlayerUpdate(player));
		}
	}
	
	@ObjectClassEventListener(eventClass=WorldObjectUpdatedEvent.class, objectClass=FlagInfo.class)
	public void flagUpdated(WorldObjectUpdatedEvent<FlagInfo> event) {
		// DO NOT SEND INFOS BEFORE THE BOT STARTS PLAYING THE GAME
		if (firstUpdate) return;
		
		FlagInfo flag = event.getObject();
		
		if (flag.isVisible() || (flag == ctx.getCTF().getEnemyFlag() && ctx.getCTF().isBotCarryingEnemyFlag())) {
			comm.sendToTeamOthers(new TCFlagUpdate(flag));
		}
	}
	
	@EventListener(eventClass=EndMessage.class)
	public void endMessage(EndMessage evet) {
		sendMe();
	}

	// ===============
	// REGULAR UPDATES
	// ===============
	
	/**
	 * Called regularly from {@link CTFBot#logic()}.
	 */
	public void update() {
		if (!comm.isConnected()) {
			log.warning("Not connected to TC server yet...");
			return;
		}
		
		// SEND UPDATES ABOUT THINGS YOU SEE		
		if (firstUpdate) {
			firstUpdate = false;
			sendMe();
			sendItems();
			sendPlayers();
			sendFlags();
		}
	}
	
	// ========================
	// SENDING INFO TO THE TEAM
	// ========================
	
	private void sendMe() {
		comm.sendToTeamOthers(new TCPlayerUpdate(ctx.getInfo()));
	}

	private void sendItems() {		
		for (NavPoint navPoint : ctx.getWorldView().getAllVisible(NavPoint.class).values()) {
			if (navPoint.getItemInstance() != null) {
				comm.sendToTeamOthers(new TCItemUpdate(navPoint));
			}
		}
	}
	
	private void sendPlayers() {		
		for (Player player : ctx.getWorldView().getAllVisible(Player.class).values()) {
			if (!player.isSpectator() && player.getTeam() != (int)ctx.getInfo().getTeam()) {
				comm.sendToTeamOthers(new TCPlayerUpdate(player));
			}			
		}
	}
	
	private void sendFlags() {
		for (FlagInfo flag : ctx.getWorldView().getAllVisible(FlagInfo.class).values()) {
			comm.sendToTeamOthers(new TCFlagUpdate(flag));
		}
		if (ctx.getCTF().isBotCarryingEnemyFlag() && !ctx.getCTF().getEnemyFlag().isVisible()) {
			comm.sendToTeamOthers(new TCFlagUpdate(ctx.getCTF().getEnemyFlag()));
		}
	}
	
	// ============================
	// SENDING INFO TO CONCRETE BOT
	// ============================
	
	private void sendMe(UnrealId botId) {
		comm.sendToBot(botId, new TCPlayerUpdate(ctx.getInfo()));
	}

	private void sendItems(UnrealId botId) {		
		for (NavPoint navPoint : ctx.getWorldView().getAllVisible(NavPoint.class).values()) {
			if (navPoint.getItemInstance() != null) {
				comm.sendToBot(botId, new TCItemUpdate(navPoint));
			}
		}
	}
	
	private void sendPlayers(UnrealId botId) {		
		for (Player player : ctx.getWorldView().getAllVisible(Player.class).values()) {
			if (player.getTeam() != (int)ctx.getInfo().getTeam()) {
				comm.sendToBot(botId, new TCPlayerUpdate(player));
			}			
		}
	}
	
	private void sendFlags(UnrealId botId) {
		for (FlagInfo flag : ctx.getWorldView().getAllVisible(FlagInfo.class).values()) {
			comm.sendToBot(botId, new TCFlagUpdate(flag));
		}
		if (ctx.getCTF().isBotCarryingEnemyFlag() && !ctx.getCTF().getEnemyFlag().isVisible()) {
			comm.sendToBot(botId, new TCFlagUpdate(ctx.getCTF().getEnemyFlag()));
		}
	}
	
}
