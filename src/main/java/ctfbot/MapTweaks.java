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
     *
     * @param navBuilder
     */
    public static void tweak(NavigationGraphBuilder navBuilder) {
        if (navBuilder.isMapName("DM-1on1-Roughinery-FPS")) tweakDM1on1RoughineryFPS(navBuilder);
        if (navBuilder.isMapName("CTF-1on1-Joust")) tweakCTF1on1Joust(navBuilder);
        if (navBuilder.isMapName("CTF-DoubleDammage")) tweakCTFDoubleDammage(navBuilder);
        if (navBuilder.isMapName("CTF-January")) tweakCTFJanuary(navBuilder);
        if (navBuilder.isMapName("CTF-Lostfaith")) tweakCTFLostfaith(navBuilder);
        if (navBuilder.isMapName("CTF-Maul")) tweakCTFMaul(navBuilder);

    }

    public static void tweakCTFMaul(NavigationGraphBuilder navBuilder) {
        navBuilder.removeEdge("PlayerStart8", "JumpSpot2");
        navBuilder.removeEdge("InventorySpot807", "JumpSpot2");
        navBuilder.removeEdge("PathNode92", "JumpSpot2");
        navBuilder.removeEdge("PathNode66", "JumpSpot2");
        navBuilder.removeEdge("PathNode67", "JumpSpot2");
        navBuilder.removeEdge("PathNode66", "JumpSpot18");
        navBuilder.removeEdge("PathNode93", "JumpSpot18");
        navBuilder.removeEdge("PathNode67", "JumpSpot18");
        navBuilder.removeEdge("PathNode76", "JumpSpot18");
        navBuilder.removeEdge("PathNode95", "JumpSpot18");
        navBuilder.removeEdge("PathNode67", "JumpSpot3");
        navBuilder.removeEdge("PathNode77", "JumpSpot3");
        navBuilder.removeEdge("PathNode95", "JumpSpot3");
        navBuilder.removeEdge("PathNode78", "JumpSpot3");
        navBuilder.removeEdge("PathNode96", "JumpSpot3");

        navBuilder.removeEdge("PathNode10", "JumpSpot6");
        navBuilder.removeEdge("PathNode12", "JumpSpot6");
        navBuilder.removeEdge("PlayerStart20", "JumpSpot6");
        navBuilder.removeEdge("PathNode143", "JumpSpot6");
        navBuilder.removeEdge("PathNode63", "JumpSpot6");
        navBuilder.removeEdge("PathNode142", "JumpSpot6");

        navBuilder.removeEdge("PathNode10", "JumpSpot20");
        navBuilder.removeEdge("PathNode63", "JumpSpot20");
        navBuilder.removeEdge("PathNode6", "JumpSpot20");
        navBuilder.removeEdge("PathNode64", "JumpSpot20");
        navBuilder.removeEdge("InventorySpot801", "JumpSpot20");

        navBuilder.removeEdge("PlayerStart1", "JumpSpot7");
        navBuilder.removeEdge("InventorySpot801", "JumpSpot7");
        navBuilder.removeEdge("PathNode48", "JumpSpot7");
        navBuilder.removeEdge("PathNode64", "JumpSpot7");

        /*navBuilder.removeEdge("PlayerStart20", "AIMarker151");
        navBuilder.removeEdge("PathNode143", "AIMarker151");
        navBuilder.removeEdge("PathNode12", "AIMarker151");
        navBuilder.removeEdge("PathNode63", "AIMarker151");*/
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
