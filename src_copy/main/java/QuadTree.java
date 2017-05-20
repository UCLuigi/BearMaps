import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Quad Tree class
 * Created by LuisAlba on 7/13/16.
 */
public class QuadTree {

    /** Strings to help with imageNaming. */
    private static final String IMG = "img/";
    private static final String PNG = ".png";

    /** The root of current QuadTree. */
    private QNode root;
    /** ArrayList containing name of Tiles that overlap Query. */
    private ArrayList<QNode> overlapTiles;

    /** QuadTree Constructor. */
    public QuadTree(double ullon, double ullat, double lrlon, double lrlat) {

        overlapTiles = new ArrayList<QNode>();
        root = new QNode("img/root.png", null,  ullon, ullat, lrlon, lrlat, 0);

        root.setNW(root.makeChildren("1", root, ullon, ullat, (ullon + lrlon) / 2,
                        (ullat + lrlat) / 2, 1));

        root.setNE(root.makeChildren("2", root, (ullon + lrlon) / 2, ullat, lrlon,
                        (ullat + lrlat) / 2, 1));

        root.setSE(root.makeChildren("4", root, (ullon + lrlon) / 2, (ullat + lrlat) / 2,
                        lrlon, lrlat, 1));

        root.setSW(root.makeChildren("3", root, ullon, (ullat + lrlat) / 2,
                        (ullon + lrlon) / 2, lrlat, 1));
    }

    /** Returns Node Root of QuadTree. */
    public QNode getRoot() {
        return root;
    }

    /** Returns ArrayList of QNodes that overlap. */
    public ArrayList<QNode> getOverlapTiles() {
        orderTiles();
        return overlapTiles;
    }

    /** Adds all Tiles that overlap the query window. */
    public void intersectionQuery(QNode curr, double queryUllon, double queryUllat,
                                  double queryLrlon, double queryLrlat, double dpp) {

        double nodeDPP = (curr.getLrlon() - curr.getUllon()) / 256;

        if (checkOverlap(curr, queryUllon, queryUllat, queryLrlon, queryLrlat)) {

            if (nodeDPP <= dpp || curr.isLeaf()) {
                if (!overlapTiles.contains(curr)) {
                    overlapTiles.add(curr);
                }
            } else {
                intersectionQuery(curr.getNW(), queryUllon, queryUllat,
                        queryLrlon, queryLrlat, dpp);
                intersectionQuery(curr.getNE(), queryUllon, queryUllat,
                        queryLrlon, queryLrlat, dpp);
                intersectionQuery(curr.getSE(), queryUllon, queryUllat,
                        queryLrlon, queryLrlat, dpp);
                intersectionQuery(curr.getSW(), queryUllon, queryUllat,
                        queryLrlon, queryLrlat, dpp);
            }
        }
    }

    /** Returns boolean on whether a Tile overlaps with query. */
    private boolean checkOverlap(QNode curr, double queryUllon, double queryUllat,
                                    double queryLrlon, double queryLrlat) {

        return queryUllon <= curr.getLrlon() && queryLrlon >= curr.getUllon()
                && queryUllat >= curr.getLrlat() && queryLrlat <= curr.getUllat();
    }

    /** Returns an ArrayList that is ordered by tile coordinates starting
     *  from bottom left tile to top right. */
    private void orderTiles() {
        Collections.sort(overlapTiles, Comparator.comparing(QNode::getUllat)
                .thenComparing(QNode::getUllon));
    }

    /** Node class of the QuadTree. */
    public class QNode {

        /** String representation of the image name. */
        private String imgName;

        /** Double representation of the UpperLeftLongitude,
         * UpperLeftLatitude LowerLeftLongitude, and LowerRightLatitude. */
        private double ullon, ullat, lrlon, lrlat;

        /** Children Nodes in NorthEast, NorthWest, SouthWest, and
         *  SouthEast. */
        private QNode NE, NW, SW, SE;

        /** Parent Qnode. */
        private QNode parent;

        /** Integer representing the length of numbers of image. */
        private int depth;

        /** Node constructor. */
        private QNode(String image, QNode parent, double ullon, double ullat, double lrlon,
                                        double lrat, int depth) {
            this.imgName = image;
            this.parent = parent;
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrat;
            this.depth = depth;
        }

        /** Returns a TILE with its four children. */
        private QNode makeChildren(String name, QNode curr, double upX, double upY,
                                   double downX, double downY, int deep) {

            QNode tile = new QNode(IMG + name + PNG, curr, upX, upY, downX, downY, deep);
            if (deep < 7) {
                tile.setNW(makeChildren(name + "1", tile,  upX, upY, (upX + downX) / 2,
                                        (upY + downY) / 2, deep + 1));

                tile.setNE(makeChildren(name + "2", tile, (upX + downX) / 2, upY, downX,
                                        (upY + downY) / 2, deep + 1));

                tile.setSE(makeChildren(name + "4", tile, (upX + downX) / 2,
                                        (upY + downY) / 2, downX, downY, deep + 1));

                tile.setSW(makeChildren(name + "3", tile, upX, (upY + downY) / 2,
                                        (upX + downX) / 2, downY, deep + 1));
            }
            return tile;
        }

        /** Returns UpperLeftLongitude. */
        public double getUllon() {
            return ullon;
        }

        /** Returns whether QNode is a leaf. */
        public boolean isLeaf() {
            return NE == null && NW == null && SW == null && SE == null;
        }

        /** Returns ImageName. */
        public String getImgName() {
            return imgName;
        }

        /** Returns UpperLeftLatitude. */
        public double getUllat() {
            return ullat;
        }

        /** Returns LowerRightLongitude. */
        public double getLrlon() {
            return lrlon;
        }

        /** Returns LowerRightLatitude. */
        public double getLrlat() {
            return lrlat;
        }

        /** Returns Node in NorthEast. */
        public QNode getNE() {
            return NE;
        }

        /** Returns Node in NorthWest. */
        public QNode getNW() {
            return NW;
        }

        /** Returns Node in SouthWest. */
        public QNode getSW() {
            return SW;
        }

        /** Returns Node in SouthEast. */
        public QNode getSE() {
            return SE;
        }

        /** Returns depth of Node. */
        public int getDepth() {
            return depth;
        }

        /** Sets current QNode's NE to NODE. */
        public void setNE(QNode node) {
            this.NE = node;
        }

        /** Sets current QNode's NW to NODE. */
        public void setNW(QNode node) {
            this.NW = node;
        }

        /** Sets current QNode's SW to NODE. */
        public void setSW(QNode node) {
            this.SW = node;
        }

        /** Sets current QNode's SE to NODE. */
        public void setSE(QNode node) {
            this.SE = node;
        }

    }

}
