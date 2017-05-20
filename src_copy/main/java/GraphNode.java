import java.util.ArrayList;

/**
 * Created by LuisAlba on 7/31/16.
 */
public class GraphNode {

    /** ID of the current GraphNode. */
    private long id;
    /** Longitude and Latitude of current GraphNode. */
    private double longitude, latitude;
    /** A List of current GraphNode's neighbors. */
    private ArrayList<GraphNode> neighbors;
    /** Name of node. */
    private String name;

    /** GraphNode Constructor. */
    public GraphNode(double lon, double lat, long id) {
        this.longitude = lon;
        this.latitude = lat;
        this.id = id;
        neighbors = new ArrayList<>();
    }

    /** Returns Longitude of GraphNode. */
    public double getLongitude() {
        return longitude;
    }

    /** Returns Latitude of GraphNode. */
    public double getLatitude() {
        return latitude;
    }

    /** Returns ID of GraphNode. */
    public long getId() {
        return id;
    }

    /** Add a neighbor to current GraphNode. */
    public void addNeighbor(GraphNode neigh) {
        neighbors.add(neigh);
    }

    /** Returns List of all neighbors. */
    public ArrayList<GraphNode> getNeighbors() {
        return neighbors;
    }

    /** Returns distance to neighbor. */
    public double distanceTo(GraphNode neighbor) {
        return Math.sqrt(Math.pow(neighbor.getLongitude() - this.longitude, 2)
                + Math.pow(neighbor.getLatitude() - this.latitude, 2));
    }

    /** Returns distance from this Node to certain (lon,lat). */
    public double distanceTo(double lon, double lat) {
        return Math.sqrt(Math.pow(lon - this.longitude, 2)
                + Math.pow(lat - this.latitude, 2));
    }

    /** Sets name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Iterates through instance variables. */
    public Object value(int i) {
        switch (i) {
            case 0:
                return name;
            case 1:
                return longitude;
            case 2:
                return id;
            case 3:
                return latitude;
            default:
                return null;
        }
    }

    /** Iterates through instance variables. */
    public String key(int i) {
        switch (i) {
            case 0:
                return "name";
            case 1:
                return "lon";
            case 2:
                return "id";
            case 3:
                return "lat";
            default:
                return null;
        }
    }

}
