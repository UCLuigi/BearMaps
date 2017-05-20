import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *  @author Alan Yao
 */
public class MapDBHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));

    /** Parsing mode. */
    private String activeState = "";
    /** Current ID of GraphNode. */
    private Long currId;
    /** Nodes of a road. */
    private ArrayList<Long> connectedNodes;
    /** Type of Road. */
    private String roadType;
    /** List of all Nodes created. */
    private HashMap<Long, GraphNode> allNodes = new HashMap<>();
    /** GraphDatabase. */
    private final GraphDB g;

    public MapDBHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equals("node")) {

            activeState = "node";
            currId = Long.parseLong(attributes.getValue("id"));
            Double lat = Double.parseDouble(attributes.getValue("lat"));
            Double lon = Double.parseDouble(attributes.getValue("lon"));
            allNodes.put(currId, new GraphNode(lon, lat, currId));

        } else if (qName.equals("way")) {

            activeState = "way";
            currId = Long.parseLong(attributes.getValue("id"));
            roadType = "";
            connectedNodes = new ArrayList<>();
            connectedNodes.add(currId);

        } else if (activeState.equals("way") && qName.equals("nd")) {

            Long k = Long.parseLong(attributes.getValue("ref"));
            currId = k;
            connectedNodes.add(k);

        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {

            String v = attributes.getValue("v");
            g.addWord(v, allNodes.get(currId));

        } else if (activeState.equals("way") && qName.equals("tag") && attributes.getValue("k")
                .equals("highway")) {

            roadType = attributes.getValue("v");
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals("way")) {
            if (roadType.length() > 0 && ALLOWED_HIGHWAY_TYPES.contains(roadType)) {
                if (connectedNodes.size() != 0) {
                    GraphNode prev = null;
                    for (Long node : connectedNodes) {
                        if (prev == null) {
                            prev = allNodes.get(node);
                        } else {
                            GraphNode node1 = allNodes.get(node);
                            prev.addNeighbor(node1);
                            node1.addNeighbor(prev);
                            g.addNode(prev.getId(), prev);
                            g.addNode(node, node1);
                            prev = node1;
                        }

                    }
                }
            }
        }
    }

}
