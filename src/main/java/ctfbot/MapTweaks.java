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
        if (navBuilder.isMapName("CTF-BP2-Concentrate")) tweakBP(navBuilder);
        if (navBuilder.isMapName("CTF-Citadel")) tweakCTFCitadel(navBuilder);
    }

    /**
     * Updating navs for CTF-BP2-Concentrate
     *
     * @param navBuilder navigation builder.
     */
    public static void tweakBP(NavigationGraphBuilder navBuilder) {
        navBuilder.removeEdgesTo("InventorySpot19");
        navBuilder.removeEdge("PathNode43", "JumpSpot4");
        navBuilder.removeEdge("InventorySpot9", "PathNode43");
        navBuilder.removeEdge("PathNode2", "PathNode76");
        navBuilder.removeEdge("InventorySpot1", "AIMarker6");
        navBuilder.removeEdge("InventorySpot2", "AIMarker6");
        navBuilder.removeEdge("InventorySpot55", "PathNode44");
        navBuilder.removeEdge("JumpSpot3", "xBlueFlagBase0");
        navBuilder.removeEdge("PathNode39", "JumpSpot3");
        navBuilder.removeEdge("PathNode75", "JumpSpot2");
        navBuilder.removeEdge("PathNode68", "JumpSpot12");
        navBuilder.removeEdge("PathNode69", "JumpSpot10");
        navBuilder.removeEdge("PathNode74", "JumpSpot1");
        navBuilder.removeEdge("PathNode74", "JumpSpot2");
        navBuilder.removeEdge("PathNode74", "PathNode75");
        navBuilder.removeEdge("PathNode74", "xRedFlagBase1");
        navBuilder.removeEdge("PathNode81", "JumpSpot2");
        navBuilder.removeEdge("PathNode81", "PathNode75");
        navBuilder.removeEdge("PathNode81", "xRedFlagBase1");
        navBuilder.removeEdge("PathNode81", "JumpSpot1");
        navBuilder.removeEdge("PathNode76", "JumpSpot11");
        navBuilder.removeEdge("PathNode44", "JumpSpot0");
        navBuilder.removeEdge("PathNode44", "JumpSpot11");
        navBuilder.removeEdge("PathNode44", "PathNode39");
        navBuilder.removeEdge("PathNode44", "xBlueFlagBase0");
        navBuilder.removeEdgesBetween("AssaultPath12", "PathNode74");
        navBuilder.removeEdgesBetween("AssaultPath12", "PathNode81");
        navBuilder.removeEdgesBetween("AssaultPath5", "PathNode0");
        navBuilder.removeEdgesBetween("AssaultPath5", "PathNode44");
        navBuilder.removeEdgesBetween("InventorySpot55", "PathNode44");
        navBuilder.removeEdgesBetween("InventorySpot59", "PathNode81");
        navBuilder.removeEdge("PathNode0", "xBlueFlagBase0");
        navBuilder.removeEdge("PathNode0", "JumpSpot3");
        navBuilder.removeEdge("PathNode0", "PathNode39");
        navBuilder.removeEdge("PathNode0", "JumpSpot0");
        navBuilder.removeEdgesBetween("PathNode74", "JumpSpot1");
        navBuilder.removeEdgesBetween("PathNode74", "PathNode75");
        navBuilder.removeEdgesBetween("PathNode74", "xRedFlagBase1");
        navBuilder.removeEdgesBetween("JumpSpot4", "JumpSpot5");
        navBuilder.removeEdgesBetween("JumpSpot4", "JumpSpot6");
        navBuilder.removeEdgesBetween("JumpSpot4", "PathNode18");
        navBuilder.removeEdgesBetween("JumpSpot4", "PathNode30");
        navBuilder.removeEdge("PathNode0", "xBlueFlagBase0");
        navBuilder.removeEdge("PathNode0", "JumpSpot3");
        navBuilder.removeEdge("PathNode0", "PathNode39");
        navBuilder.removeEdge("PathNode0", "JumpSpot0");
        navBuilder.removeEdgesBetween("JumpSpot11", "PathNode75");
        navBuilder.removeEdgesBetween("JumpSpot11", "JumpSpot14");
        navBuilder.removeEdgesBetween("JumpSpot11", "PathNode31");
        navBuilder.removeEdgesBetween("JumpSpot11", "JumpSpot13");
        navBuilder.removeEdgesBetween("JumpSpot11", "PathNode44");
        navBuilder.removeEdgesBetween("JumpSpot11", "PathNode40");
        navBuilder.removeEdgesBetween("JumpSpot13", "PathNode75");
        navBuilder.removeEdgesBetween("JumpSpot14", "PathNode75");
        navBuilder.removeEdgesBetween("JumpSpot2", "PathNode74");
        navBuilder.removeEdgesBetween("JumpSpot2", "PathNode81");
        navBuilder.removeEdgesBetween("JumpSpot3", "PathNode0");
        navBuilder.removeEdgesBetween("JumpSpot3", "PathNode44");
        navBuilder.removeEdgesBetween("PathNode23", "JumpSpot5");
        navBuilder.removeEdgesBetween("PathNode35", "JumpSpot6");
        navBuilder.removeEdgesBetween("PathNode39", "JumpSpot3");
        navBuilder.removeEdgesBetween("PathNode39", "JumpSpot5");
        navBuilder.removeEdgesBetween("PathNode39", "JumpSpot6");
        navBuilder.removeEdgesBetween("PathNode44", "JumpSpot0");
        navBuilder.removeEdgesBetween("PathNode44", "JumpSpot3");
        navBuilder.removeEdgesBetween("PathNode44", "PathNode39");
        navBuilder.removeEdgesBetween("PathNode44", "xBlueFlagBase0");
        navBuilder.removeEdgesBetween("PathNode67", "PathNode64");
        navBuilder.removeEdgesBetween("PathNode81", "JumpSpot1");
        navBuilder.removeEdgesBetween("PathNode81", "PathNode75");
        navBuilder.removeEdgesBetween("PathNode81", "xRedFlagBase1");
        navBuilder.removeEdgesBetween("xBlueFlagBase0", "PathNode0");
        navBuilder.removeEdgesBetween("xBlueFlagBase0", "PathNode44");
        navBuilder.removeEdgesBetween("xRedFlagBase1", "PathNode74");
        navBuilder.removeEdgesBetween("xRedFlagBase1", "PathNode81");
        navBuilder.removeEdgesBetween("PathNode44", "JumpSpot3");
        navBuilder.removeEdgesBetween("PathNode17", "InventorySpot16");
        navBuilder.removeEdgesBetween("PathNode23", "InventorySpot16");
        navBuilder.removeEdgesBetween("InventorySpot53", "PathNode31");
        navBuilder.removeEdgesBetween("InventorySpot52", "PathNode31");
        navBuilder.removeEdgesBetween("InventorySpot52", "PathNode40");
        navBuilder.removeEdgesBetween("InventorySpot53", "PathNode40");
        navBuilder.removeEdgesBetween("InventorySpot38", "PathNode30");
        navBuilder.removeEdgesBetween("InventorySpot39", "PathNode18");
        navBuilder.removeEdgesBetween("InventorySpot38", "PathNode18");
        navBuilder.removeEdgesBetween("InventorySpot39", "PathNode30");
        navBuilder.removeEdgesBetween("JumpSpot14", "PathNode77");
        navBuilder.removeEdgesBetween("PathNode5", "AIMarker7");
        navBuilder.removeEdgesBetween("PathNode39", "PathNode91");
        navBuilder.removeEdgesBetween("PathNode0", "JumpSpot0");
        navBuilder.removeEdgesBetween("PathNode0", "JumpSpot3");
        navBuilder.removeEdgesBetween("PathNode0", "PathNode39");
    }

    /**
     * Updating navs for CTF-Citadel
     *
     * @param navBuilder navigation builder.
     */
    public static void tweakCTFCitadel(NavigationGraphBuilder navBuilder) {
        navBuilder.removeEdge("PathNode75", "JumpSpot26");
        navBuilder.removeEdge("PathNode14", "JumpSpot10");
        navBuilder.removeEdge("JumpSpot18", "JumpSpot4");
        navBuilder.removeEdge("PathNode36", "JumpSpot11");
        navBuilder.removeEdge("PathNode23", "PathNode26");
        navBuilder.removeEdge("PathNode47", "PathNode49");
        navBuilder.removeEdge("PathNode103", "JumpSpot28");
        navBuilder.removeEdge("PathNode99", "JumpSpot27");
    }

    /**
     * Updating navs for CTF-Maul
     *
     * @param navBuilder navigation builder.
     */
    public static void tweakCTFMaul(NavigationGraphBuilder navBuilder) {
        navBuilder.removeEdge("PlayerStart8", "JumpSpot2");
        navBuilder.removeEdge("InventorySpot807", "JumpSpot2");
        navBuilder.removeEdge("PathNode92", "JumpSpot2");
        navBuilder.removeEdge("PathNode66", "JumpSpot18");
        navBuilder.removeEdge("PathNode93", "JumpSpot18");
        navBuilder.removeEdge("PathNode78", "JumpSpot3");
        navBuilder.removeEdge("PathNode96", "JumpSpot3");
        navBuilder.removeEdge("PathNode10", "JumpSpot20");
        navBuilder.removeEdge("PathNode63", "JumpSpot20");
        navBuilder.removeEdge("PathNode6", "JumpSpot20");
        navBuilder.removeEdge("PathNode67", "JumpSpot18");
        navBuilder.removeEdge("PathNode76", "JumpSpot18");
        navBuilder.removeEdge("PathNode95", "JumpSpot18");
        navBuilder.removeEdge("PathNode67", "JumpSpot3");
        navBuilder.removeEdge("PathNode77", "JumpSpot3");
        navBuilder.removeEdge("PathNode66", "JumpSpot2");
        navBuilder.removeEdge("InventorySpot801", "JumpSpot7");
        navBuilder.removeEdge("PathNode48", "JumpSpot7");
        navBuilder.removeEdge("PathNode64", "JumpSpot7");
        navBuilder.removeEdge("PathNode95", "JumpSpot3");
        navBuilder.removeEdge("PathNode67", "JumpSpot2");
        navBuilder.removeEdge("PathNode10", "JumpSpot6");
        navBuilder.removeEdge("PathNode12", "JumpSpot6");
        navBuilder.removeEdge("PlayerStart20", "JumpSpot6");
        navBuilder.removeEdge("PathNode64", "JumpSpot20");
        navBuilder.removeEdge("PathNode143", "JumpSpot6");
        navBuilder.removeEdge("PathNode63", "JumpSpot6");
        navBuilder.removeEdge("PathNode142", "JumpSpot6");
        navBuilder.removeEdge("InventorySpot801", "JumpSpot20");
        navBuilder.removeEdge("PlayerStart1", "JumpSpot7");
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
