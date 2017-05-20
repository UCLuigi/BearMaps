/**
 * Created by LuisAlba on 8/4/16.
 */
public class RouteNodeWrapper {

    /** Current GraphNode. */
    private GraphNode currentNode;

    /** GraphNode I visited before. */
    private RouteNodeWrapper previous;

    /** Total distance I have covered since start. */
    private double distanceCovered;

    /** Heuristic to guess distance. */
    private double heuristic;

    /** Priority of RouteNode based on distance from beginning and its heuristic. */
    private double priority;

    /** Begin Routing constructor. */
    RouteNodeWrapper(GraphNode start) {
        this.currentNode = start;
        this.previous = null;
        this.distanceCovered = 0;
        this.heuristic = 0;
        priority = distanceCovered + heuristic;
    }

    /** Adding another Node to current route. */
    RouteNodeWrapper(RouteNodeWrapper current, GraphNode next, GraphNode end) {
        this.previous = current;
        this.currentNode = next;
        this.heuristic = currentNode.distanceTo(end);
        this.distanceCovered = previous.getDistanceCovered()
                + previous.getCurrentNode().distanceTo(next);
        this.priority = distanceCovered + heuristic;
    }

    /** Return CurrentNode. */
    public GraphNode getCurrentNode() {
        return currentNode;
    }

    /** Return previous RouteNodeWrapper. */
    public RouteNodeWrapper getPrevious() {
        return previous;
    }

    /** Returns total distance covered. */
    public double getDistanceCovered() {
        return distanceCovered;
    }

    /** Returns priority. */
    public double getPriority() {
        return priority;
    }
}
