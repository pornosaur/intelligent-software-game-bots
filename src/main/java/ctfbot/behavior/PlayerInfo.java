package ctfbot.behavior;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.io.Serializable;

public class PlayerInfo implements Comparable, Serializable{

    /**
     * Player ID.
     */
    private UnrealId id;
    /**
     * Player location.
     */
    private Location location;
    /**
     * Player distance.
     */
    private double distance;

    public PlayerInfo(UnrealId id) {
        this(id, null, Double.MAX_VALUE);
    }

    public PlayerInfo(UnrealId id, Location location, double distance) {
        this.id = id;
        this.location = location;
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UnrealId getId() {
        return id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }


    @Override
    public int compareTo(Object o) {
        PlayerInfo i = (PlayerInfo) o;

        if (i.getDistance() <= distance) {
            return -1;
        } else if (i.getDistance() > distance) {
            return 1;
        }

        return 0;
    }
}
