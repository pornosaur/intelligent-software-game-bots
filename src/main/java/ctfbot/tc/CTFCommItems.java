package ctfbot.tc;

import ctfbot.CTFBot;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.AnnotationListenerRegistrator;
import cz.cuni.amis.pogamut.base.utils.logging.LogCategory;
import cz.cuni.amis.pogamut.ut2004.teamcomm.bot.UT2004TCClient;

/**
 * Communication module for the CTF Bot for negotiating what items will team be picking up.
 * 
 * @author Jimmy
 *
 * @param <BOTCTRL> Context class of the action. It's an shared object used by
 * all primitives. it is used as a shared memory and for interaction with the
 * environment.
 */
public class CTFCommItems<BOTCTRL extends CTFBot> {
	
	private AnnotationListenerRegistrator listenerRegistrator;
	
	private BOTCTRL ctx;
	
	private UT2004TCClient comm;
	
	private LogCategory log;
	
	public CTFCommItems(BOTCTRL ctx) {
		this.ctx = ctx;
		this.log = this.ctx.getBot().getLogger().getCategory("CTFCommItems");
		this.comm = this.ctx.getTCClient();
		
		// INITIALIZE @EventListener SNOTATED LISTENERS
		listenerRegistrator = new AnnotationListenerRegistrator(this, ctx.getWorldView(), ctx.getBot().getLogger().getCategory("Listeners"));
		listenerRegistrator.addListeners();
	}
	
//  EXAMPLE HOW TO RECEIVE MESSAGE	
//	@EventListener(eventClass=TCFlagUpdate.class)
//	public void flagUpdate(TCFlagUpdate msg) {				
//	}

	/**
	 * Called regularly from {@link CTFBotContext#logicBeforePlan()}.
	 */
	public void update() {
		if (!comm.isConnected()) {
			log.warning("Not connected to TC server yet...");
			return;
		}
		
		// PERIODIC INFO
	}

//  EXAMPLE HOW TO SEND A MESSAGE TO OTHERS IN THE TEAM	
//	private void sendMe() {
//		comm.sendToTeamOthers(new TCPlayerUpdate(ctx.getInfo()));
//	}

}
