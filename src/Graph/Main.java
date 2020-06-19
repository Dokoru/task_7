package Graph;

import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        Graph graph = new Graph();
        graph.readMatrixFromFile("m1");
        Graph.Vertex v1 = graph.getVertexByIndex(0);
        System.out.println(String.format("For vertex %d:", v1.getName()));
        HashMap<Graph.Vertex, String> hashMap = graph.calcVertexFriendliness(v1);
        System.out.println(graph.toPrintHashMap(hashMap));
    }
}
