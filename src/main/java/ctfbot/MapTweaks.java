package ctfbot;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
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

    public static void tweakBP(NavigationGraphBuilder navBuilder) {
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode43", "CTF-BP2-Concentrate.JumpSpot4");
        navBuilder.removeEdgesTo("CTF-BP2-Concentrate.InventorySpot19");

        navBuilder.removeEdge("CTF-BP2-Concentrate.InventorySpot9", "CTF-BP2-Concentrate.PathNode43");

        navBuilder.removeEdge("CTF-BP2-Concentrate.InventorySpot1", "CTF-BP2-Concentrate.AIMarker6");
        navBuilder.removeEdge("CTF-BP2-Concentrate.InventorySpot2", "CTF-BP2-Concentrate.AIMarker6");
        navBuilder.removeEdge("CTF-BP2-Concentrate.InventorySpot55", "CTF-BP2-Concentrate.PathNode44");

        navBuilder.removeEdge("CTF-BP2-Concentrate.JumpSpot3", "CTF-BP2-Concentrate.xBlueFlagBase0");

        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.JumpSpot0");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.PathNode39");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.xBlueFlagBase0");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode2", "CTF-BP2-Concentrate.PathNode76");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode39", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.JumpSpot0");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.JumpSpot11");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.PathNode39");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.xBlueFlagBase0");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode68", "CTF-BP2-Concentrate.JumpSpot12");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode69", "CTF-BP2-Concentrate.JumpSpot10");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.JumpSpot1");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.JumpSpot2");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.PathNode75");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.xRedFlagBase1");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode75", "CTF-BP2-Concentrate.JumpSpot2");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode76", "CTF-BP2-Concentrate.JumpSpot11");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.JumpSpot1");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.JumpSpot2");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.PathNode75");
        navBuilder.removeEdge("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.xRedFlagBase1");


        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.AssaultPath12", "CTF-BP2-Concentrate.PathNode74");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.AssaultPath12", "CTF-BP2-Concentrate.PathNode81");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.AssaultPath5", "CTF-BP2-Concentrate.PathNode0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.AssaultPath5", "CTF-BP2-Concentrate.PathNode44");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot55", "CTF-BP2-Concentrate.PathNode44");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot59", "CTF-BP2-Concentrate.PathNode81");

        /* OKEY */
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.PathNode75");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.JumpSpot14");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.PathNode31");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.JumpSpot13");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.PathNode44");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot11", "CTF-BP2-Concentrate.PathNode40");
        /////////////

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot14", "CTF-BP2-Concentrate.PathNode75");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot13", "CTF-BP2-Concentrate.PathNode75");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot2", "CTF-BP2-Concentrate.PathNode74");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot2", "CTF-BP2-Concentrate.PathNode81");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot3", "CTF-BP2-Concentrate.PathNode0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot3", "CTF-BP2-Concentrate.PathNode44");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot4", "CTF-BP2-Concentrate.JumpSpot5");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot4", "CTF-BP2-Concentrate.JumpSpot6");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot4", "CTF-BP2-Concentrate.PathNode18");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot4", "CTF-BP2-Concentrate.PathNode30");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.JumpSpot0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode0", "CTF-BP2-Concentrate.PathNode39");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode23", "CTF-BP2-Concentrate.JumpSpot5");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode35", "CTF-BP2-Concentrate.JumpSpot6");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode39", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode39", "CTF-BP2-Concentrate.JumpSpot5");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode39", "CTF-BP2-Concentrate.JumpSpot6");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.JumpSpot0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.PathNode39");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.xBlueFlagBase0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode67", "CTF-BP2-Concentrate.PathNode64");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.JumpSpot1");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.PathNode75");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode74", "CTF-BP2-Concentrate.xRedFlagBase1");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.JumpSpot1");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.PathNode75");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode81", "CTF-BP2-Concentrate.xRedFlagBase1");
        // navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode9", "CTF-BP2-Concentrate.PathNode39");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.xBlueFlagBase0", "CTF-BP2-Concentrate.PathNode0");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.xBlueFlagBase0", "CTF-BP2-Concentrate.PathNode44");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.xRedFlagBase1", "CTF-BP2-Concentrate.PathNode74");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.xRedFlagBase1", "CTF-BP2-Concentrate.PathNode81");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode44", "CTF-BP2-Concentrate.JumpSpot3");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode17", "CTF-BP2-Concentrate.InventorySpot16");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode23", "CTF-BP2-Concentrate.InventorySpot16");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot52", "CTF-BP2-Concentrate.PathNode31");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot52", "CTF-BP2-Concentrate.PathNode40");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot53", "CTF-BP2-Concentrate.PathNode31");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot53", "CTF-BP2-Concentrate.PathNode40");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot38", "CTF-BP2-Concentrate.PathNode30");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot38", "CTF-BP2-Concentrate.PathNode18");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot39", "CTF-BP2-Concentrate.PathNode30");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.InventorySpot39", "CTF-BP2-Concentrate.PathNode18");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.JumpSpot14", "CTF-BP2-Concentrate.PathNode77");
        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode5", "CTF-BP2-Concentrate.AIMarker7");

        navBuilder.removeEdgesBetween("CTF-BP2-Concentrate.PathNode39", "CTF-BP2-Concentrate.PathNode91");
    }

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
