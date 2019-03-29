package ctfbot;

import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.NavigationGraphBuilder;

/**
 * Class containing adjustments for navigation graph of PogamutCup competition maps.
 * 
 * @author Jimmy
 */
public class MapTweaks {

	/**
	 * Called from {@link CTFBot#botInitialized(cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.GameInfo, cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.ConfigChange, cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.InitedMessage)}.
	 * @param navBuilder
	 */
	public static void tweak(NavigationGraphBuilder navBuilder) {
		if (navBuilder.isMapName("DM-1on1-Roughinery-FPS")) tweakDM1on1RoughineryFPS(navBuilder);
		if (navBuilder.isMapName("CTF-1on1-Joust")) tweakCTF1on1Joust(navBuilder);
		if (navBuilder.isMapName("CTF-DoubleDammage")) tweakCTFDoubleDammage(navBuilder);
		if (navBuilder.isMapName("CTF-January")) tweakCTFJanuary(navBuilder);
		if (navBuilder.isMapName("CTF-Lostfaith")) tweakCTFLostfaith(navBuilder);
		
	}
	
	// ======================
	// DM-1on1-Roughinery-FPS
	// ======================
	
	private static void tweakDM1on1RoughineryFPS(NavigationGraphBuilder navBuilder) {
	}
	
	// ======================
	// CTF-1on1-Joust
	// ======================
	
	private static void tweakCTF1on1Joust(NavigationGraphBuilder navBuilder) {
		navBuilder.modifyNavPoint("xRedFlagBase0").addZ(-45).apply();
		navBuilder.modifyNavPoint("xBlueFlagBase0").addZ(-45).apply();
		navBuilder.modifyNavPoint("PathNode1").addX(600).apply();
		navBuilder.modifyNavPoint("PathNode3").addX(-200).apply();
		navBuilder.removeEdgesBetween("PathNode0", "PathNode2");
		navBuilder.removeEdgesBetween("PathNode1", "PathNode2");		
		navBuilder.modifyNavPoint("xBlueFlagBase0").modifyEdgeTo("PlayerStart1").clearFlags();
		navBuilder.modifyNavPoint("xBlueFlagBase0").modifyEdgeTo("InventorySpot7").clearFlags();
		navBuilder.modifyNavPoint("xBlueFlagBase0").modifyEdgeTo("PlayerStart8").clearFlags();
		navBuilder.modifyNavPoint("xBlueFlagBase0").modifyEdgeTo("PathNode1").clearFlags();
		navBuilder.modifyNavPoint("xRedFlagBase0").modifyEdgeTo("PathNode3").clearFlags();
		navBuilder.modifyNavPoint("xRedFlagBase0").modifyEdgeTo("InventorySpot5").clearFlags();
		navBuilder.modifyNavPoint("xRedFlagBase0").modifyEdgeTo("InventorySpot4").clearFlags();
		navBuilder.modifyNavPoint("xRedFlagBase0").modifyEdgeTo("PlayerStart0").clearFlags();		
		navBuilder.modifyNavPoint("PlayerStart1").modifyEdgeTo("xBlueFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("InventorySpot7").modifyEdgeTo("xBlueFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("PlayerStart8").modifyEdgeTo("xBlueFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("PathNode1").modifyEdgeTo("xBlueFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("PathNode3").modifyEdgeTo("xRedFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("InventorySpot5").modifyEdgeTo("xRedFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("InventorySpot4").modifyEdgeTo("xRedFlagBase0").clearFlags();
		navBuilder.modifyNavPoint("PlayerStart0").modifyEdgeTo("xRedFlagBase0").clearFlags();
	}

	
	// ======================
	// CTF-Loftfaith
	// ======================
	
	private static void tweakCTFLostfaith(NavigationGraphBuilder navBuilder) {
	}
	
	// ======================
	// CTF-January
	// ======================

	private static void tweakCTFJanuary(NavigationGraphBuilder navBuilder) {
	}

	// ======================
	// CTF-DoubleDammage
	// ======================
	
	private static void tweakCTFDoubleDammage(NavigationGraphBuilder navBuilder) {		
	}
	
}
