/* WUGraph.java */


package graph;


import dict.*;
import list.*;
/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */
/*
 ASSUMPTIONS
 The HashTable for vertecies will have the vertex as both the keys and values
    key will be the vertex
    value will be the vertex DListNode in the Vertex list
 The Vertex List will have VertexPairs as objects
    Object1 will be the vertex value
    Object2 will be a DList that will contain edges
 The DLists that contain edges will have a Vertexpair Object as well
    Object 1 will be a vertex Pair
        Object 1-1 will be a vertexPair
            Object 1-1-2 will be the first vertex of the edge (Origin, will always be the vertex we are currently at)
            Object 1-1-2 will be the second vertex of the edge (Destination)
        Object 1-2 will be the weight
    Object 2 will be the "partner of the current node"
 The Hashtable for the edges will have the two edges as the key, the value will be a DListNode
    key: VertexPair with 2 vertecies
    value: DListNode of the Edge
 */

public class WUGraph {
    HashTableChained vertexHash;
    HashTableChained edgeHash;
    DList vertecies;
    int edgeNum;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
    public WUGraph() {
        vertexHash = new HashTableChained(15);
        edgeHash = new HashTableChained(15);
        vertecies= new DList();
        edgeNum = 0;
        
    }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
    public int vertexCount() {
        return vertecies.length(); 
    }

  /**
   * edgeCount() returns the number of edges in the graph.
   *
   * Running time:  O(1).
   */
    public int edgeCount() {
        return edgeNum;
    }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
    /*
     *LOGIC: Create an object array and walk down our vertex DList, then we put each of these items into the list and return the lsit
     */
    public Object[] getVertices() {
        try {
            Object[] result  = new Object[vertexCount()];   //creates an array of vertecies
            DListNode curr = (DListNode) vertecies.front(); //the current is is the front of the vertexlist
            for (int i=0; i<vertexCount(); i++) {
                result[i] = curr.item();                    //put the vertex object in the array
                curr = (DListNode) curr.next();                         //go to the next curr
            }
            return result;
        }
        catch (InvalidNodeException e1) {
            System.out.println("ERROR IN getVertices");
            System.out.println(e1);
        }
    }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.  The
   * vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
    /*
     *LOGIC: Insert a node into the front of vertex list. Then take the front of the vertex list and store it as temp
     * Next, take the key to be the vertex Object and the value to be this temp node, that way, the hash table will be able to access the kth vertex
     */
    public void addVertex(Object vertex) {
        try {
            VertexPair insVert = new VertexPair(vertex,new DList());  //in the DListnode we want to store the vertex object and adjacency List
            vertecies.insertFront(insVert);                           //insert this into the DList
            DListNode temp = (DListNode) vertecies.front();           //take the node we just inserted
            vertexHash.insert(vertex, temp);                          //insert it into the hash table
        }
        catch (InvalidNodeException e1) {
            System.out.println("ERROR IN addVertex");
            System.out.println(e1);
        }
    }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
    /*
     *LOGIC: try to find entry in hash table, if this exists. then take the adjacency list (second object in the node we just found)
     *we will then traverse down the adjacency list, first we will remove the partner and then itself, we will do this until all edges have been removed"
     *We also need to remove the hashtable entry for each of these edges.
     *Finally, we will remove the vertex from the vertex list and the hashtable entry for the given vertex.
     *We need to make sure edgeNum is consistent
     */
    public void removeVertex(Object vertex) {
        try {
            Entry remVert = vertexHash.find(vertex);
            if (remVert!=null) {                                                    //checking if the vertex was found
                DListNode remNode = (DListNode) remVert.value();                    //the node that we will want to remove is this
                DList edgeList = ((DList)((VertexPair) remNode.item()).object2);    //edgeList is the second object in the vertex pair
                DListNode curr = (DListNode) edgeList.front();                      //curr is the front value
                while (edgeList.length()>0) {                                       //we will keep doing this until there are no items in edgeslist
                    edgeHash.remove( ((VertexPair) ((VertexPair) ((VertexPair) curr.item()).object1).object1) ) ;   //we are removing the hashtable entry,stored as first object's first object
                    ((DListNode)((VertexPair)curr.item()).object2).remove();        //we are then removing the "partner" which is the first object
                    curr.remove();                                                  //remove the current node
                    edgeNum--;                                                      //we degrement the total number of edges
                    curr = (DListNode) edgeList.front();                            //the new curr is the front of the edgelist
                }
                remNode.remove();                                                   //we remove the vertex itself
            }
            vertexHash.remove(vertex);                                              //we remove the hashtable entry
            
        }
        catch (InvalidNodeException e1) {
            System.out.println("ERROR IN addVertex");
            System.out.println(e1);
        }
    }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
    public boolean isVertex(Object vertex) {
        Entry remVert = vertexHash.find(vertex);
        return remVert!=null;                   //return if the value was found in the hashTable
    }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
    /*
     *  LOGIC: Should eventually return the length of the vertex adjacency list
     */
    public int degree(Object vertex) {
        Entry remVert = vertexHash.find(vertex);
        if (remVert!=null) {
            return ((DList) ((VertexPair) ((DListNode) remVert.key()).item()).object2).length();
        }
        return 0;
    }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
    /*
     * LOGIC: Access adjacency list through hash table, then walk through all of adjacency list
     * for each item in adjacency list, put the destinaiton node and weight into 2 different arrays and return it
     */
    public Neighbors getNeighbors(Object vertex) {
        Entry currVert = vertexHash.find(vertex);                                                   //making sure the vertex exists
        if (currVert!=null) {
            DList edgeList = ((DList) ((VertexPair) currVert.value()).object2);                     //take the adjacency List
            DListNode curr = (DListNode) edgeList.front();                                 //take the front of the adjacency list
            Neighbors edges = new Neighbors();                                                      //create a neighbors item
            for (int i = 0; i<edgeList.length(); i++) {                                             //for the whole edge list
                edges.neighborList[i] = ((VertexPair) ((VertexPair)curr.item()).object1).object2;                        //the second object will ALWAYS BE DESTINATION
                edges.weightList[i] = ((Integer) ((VertexPair) ((VertexPair) curr.item()).object1).object2).intValue();   //insert the weight into the neighbors (Integer Object)
                curr = (DListNode) curr.next();                                                                 //increment
            }
            return edges;                                                                           //when done return al the neighbors
        }
        return null;
    }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the edge is already
   * contained in the graph, the weight is updated to reflect the new value.
   * Self-edges (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
    /*
     * LOGIC: Use hashtable to access the edge that is in question. if this doesn't exist, create two vertex pair objects. then use hashtable to access the corresponding vertecies and insert them to the front of each list. 
         Make sure they are partners of each other
         Make sure that edgeNum is consistent
     */
    //POSSIBLE PROBLEM: ENSURING BOTH NODES EXIST
    public void addEdge(Object u, Object v, int weight){
        VertexPair tempVert = new VertexPair(u,v);
        Entry tempEnt = edgeHash.find(tempVert);
        if (tempEnt==null) {                                                                                //first check if edge already exists
            tempEnt = vertexHash.find(u);                                                                   //find the vertex node
            DListNode nullNode = new DListNode();
            VertexPair insVert1 = new VertexPair(new VertexPair(new VertexPair(u,v),weight),nullNode);      //create a Vertex node insVert1
            VertexPair insVert2 = new VertexPair(new VertexPair(new VertexPair(v,u),weight),insVert1);      //create a vertex node insVert2
            insVert1.object2 = insVert2;                                                                    //assuring that both of these have "partners"
            ((DList) ((VertexPair) ((DListNode) tempEnt.value()).item()).object2).insertFront(insVert1);    //insert this into the vlist of vertex1
            tempEnt = vertexHash.find(v);                                                                   //finding the other vertex affected by this edge
            ((DList) ((VertexPair) ((DListNode) tempEnt.value()).item()).object2).insertFront(insVert2);    //insert this into the list of vertex v
            //((DList) ((VertexPair) tempEnt.value()).object2).insertFront(u,v);
            //don't know what this is for
            VertexPair insVert = new VertexPair(u,v);                                                   
            edgeHash.insert(insVert,((DList) ((VertexPair)  ((DListNode) tempEnt.value()).item()).object2).front());        //insert the edge into a hashtable
            edgeNum++;
        }
    }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   * We need to go through and remove the edge and its partner, we also need to decrease the edge size
   */
    public void removeEdge(Object u, Object v) {
        VertexPair tempVert = new VertexPair(u,v);
        Entry tempEnt = edgeHash.find(tempVert);                                                        // make sure the edge exists
        if (tempEnt!=null) {
            ((DListNode) ((VertexPair) ((DListNode) tempEnt.value()).item()).object2).remove();         // remove the partner edge
            ((DListNode) tempEnt.value()).remove();                                                     // remove the current edge
            edgeHash.remove(tempVert);                                                                  // remove the hashtable value
            edgeNum--;                                                                                  // decrement the amount of edges
        }
    }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
    public boolean isEdge(Object u, Object v) {
        VertexPair tempVert = new VertexPair(u,v);
        Entry tempEnt = edgeHash.find(tempVert);
        return tempEnt!=null;
    }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but
   * also more annoying.)
   *
   * Running time:  O(1).
   */
    public int weight(Object u, Object v) {
        VertexPair tempVert = new VertexPair(u,v);
        Entry tempEnt = edgeHash.find(tempVert);
        if (tempEnt!=null) {
            return ((Integer) ((VertexPair) ((VertexPair) ((DListNode) tempEnt.value()).item()).object1).object2).intValue();
        }
        return 0;
    }

    
    /*
     TASKS: Make sure size invariants hold
     make sure that I take care of Hashtable resizing
     */
}
