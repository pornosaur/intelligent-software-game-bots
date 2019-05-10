package ctfbot.map;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.NavPoints;

public class MapPlaces {

    public Location defendingPlace, defendingPlace2;
    public Location hidingPlace;
    public Location defendingFocus, walkFocusEnemy;

    private NavPoints navPoints;

    private static boolean isMaul = false;
    private static boolean isConc = false;

    public MapPlaces(NavPoints navPoints, String map, Integer team) {
        this.defendingPlace = null;
        this.hidingPlace = null;
        this.defendingFocus = null;
        this.navPoints = navPoints;

        if (map.toLowerCase().compareTo("ctf-maul") == 0) isMaul = true;
        else if (map.toLowerCase().compareTo("ctf-bp2-concentrate") == 0) isConc = true;
        if (team != null) setPlaces(map, team);
    }

    public static boolean isMapConc() {
        return isConc;
    }

    public static boolean isMapMaul() {
        return isMaul;
    }

    private void setPlaces(String map, Integer team) {
        String mapTmp = map.toLowerCase();
        if (mapTmp.toLowerCase().compareTo("ctf-maul") == 0) {
            if (team == AgentInfo.TEAM_BLUE) {
                defendingFocus = navPoints.getNavPoint("CTF-Maul.xBlueFlagBase0").getLocation();
                defendingPlace = new Location(101, 5105, -1685);
                hidingPlace = new Location(-1890, 2987, -1992);
            } else if (team == AgentInfo.TEAM_RED) {
                defendingFocus = navPoints.getNavPoint("CTF-Maul.xRedFlagBase0").getLocation();
                defendingPlace = new Location(303, -4496, -4684);
                hidingPlace = new Location(1099, -2348, -2080);
            }
        } else if (mapTmp.compareTo("ctf-citadel") == 0) {
            if (team == AgentInfo.TEAM_BLUE) {
                defendingFocus = navPoints.getNavPoint("CTF-Citadel.xBlueFlagBase0").getLocation();
                defendingPlace = new Location(-39, -3512, -1822);
                defendingPlace2 = new Location(-626, -3502, -1822);
                hidingPlace = navPoints.getNavPoint("CTF-Citadel.InventorySpot160").getLocation();
                walkFocusEnemy = navPoints.getNavPoint("CTF-Citadel.xRedFlagBase0").getLocation();
            } else if (team == AgentInfo.TEAM_RED) {
                defendingFocus = navPoints.getNavPoint("CTF-Citadel.xRedFlagBase0").getLocation();
                defendingPlace = new Location(-565, 2198, -1822);
                defendingPlace2 = new Location(66, 221, -1822);
                hidingPlace = navPoints.getNavPoint("CTF-Citadel.InventorySpot156").getLocation();
                walkFocusEnemy = navPoints.getNavPoint("CTF-Citadel.xBlueFlagBase0").getLocation();
            }
        } else if (mapTmp.compareTo("ctf-bp2-concentrate") == 0) {
            if (team == AgentInfo.TEAM_BLUE) {
                defendingFocus = navPoints.getNavPoint("CTF-BP2-Concentrate.xBlueFlagBase0").getLocation();
                defendingPlace = new Location(-2055, -2991, -202);
                defendingPlace2 = new Location(-1486, -3019, -74);
                hidingPlace = new Location(-1834, -4253, 369);
                walkFocusEnemy = new Location(2195, -222, 369);
            } else if (team == AgentInfo.TEAM_RED) {
                defendingPlace = new Location(944, 4, -202);
                defendingPlace2 = new Location(947, 531, -75);
                hidingPlace = new Location(2195, -222, 369);
                walkFocusEnemy = new Location(-1834, -4253, 369);
            }
        }
    }


}
