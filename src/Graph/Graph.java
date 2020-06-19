package Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private int[][] adjacencyMatrix;
    private Vertex[] vertexArray;

    public Graph(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        vertexArray = new Vertex[adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            vertexArray[i] = new Vertex(i);
        }
    }

    public Graph() {
        this.adjacencyMatrix = new int[0][0];
        vertexArray = new Vertex[0];
    }

    public class Vertex {

        private int name;
        private ArrayList<Integer> relationList;

        public Vertex(int name) {
            this.name = name;
            this.relationList = new ArrayList<>();
        }

        public int getName() {
            return name;
        }

        public void addRelation (int relation) {
            relationList.add(relation);
        }

        public ArrayList<Integer> getRelationList() {
            return relationList;
        }
    }

    public void setRelationVertex (Vertex startVertex, Vertex endVertex) {
        class Inner {
            Vertex start = startVertex;
            Vertex end = endVertex;
            ArrayList<Integer> way = new ArrayList<>();

            private void setRelationVertex(Vertex startVertex) {
                if (way.size() == 0) {
                    way.add(startVertex.getName());
                }

                if (startVertex.equals(end)) {
                    start.addRelation(calcRelationByWay(way));
                    way.remove(way.size() - 1);
                    return;
                } else {
                    for (int i = 0; i < adjacencyMatrix.length; i++) {
                        int j = startVertex.getName();
                        if (adjacencyMatrix[j][i] != -1 && !way.contains(i)) {
                            way.add(i);
                            setRelationVertex(getVertexByIndex(i));
                        }
                    }
                    way.remove(way.size() - 1);
                }
            }
        }
        new Inner().setRelationVertex(startVertex);
    }

    public int calcRelationByWay (ArrayList<Integer> list) {
        int count = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            if (adjacencyMatrix[list.get(i)][list.get(i + 1)] == 0) {
                count++;
            }
        }
        return count % 2 == 0 ? 1 : 0;
    }

    public Vertex getVertexByIndex(int index) {
        return vertexArray[index];
    }

    public double calcFriendlinessCoefficient (Vertex vertex) {
        int count = 0;
        for (int i: vertex.getRelationList()) {
            if (i == 1) {
                count++;
            }
        }
        int size = vertex.getRelationList().size();
        if (size == 0) {
            try {
                throw new Exception("EmptyRelationList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (double) count / size;
    }

    public HashMap<Vertex, String> calcVertexFriendliness(Vertex mainVertex) {
        HashMap<Vertex, String> hashMap = new HashMap<>();
        for (Vertex vertex: vertexArray) {
            if (!vertex.equals(mainVertex)) {
                setRelationVertex(vertex, mainVertex);
                if (adjacencyMatrix[mainVertex.getName()][vertex.getName()] == -1) {
                    hashMap.put(vertex, String.format("%.3f", calcFriendlinessCoefficient(vertex)));
                } else {
                    hashMap.put(vertex, String.valueOf(adjacencyMatrix[mainVertex.getName()][vertex.getName()]));
                }
            }
        }
        return hashMap;
    }

    public void readMatrixFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();
        while (br.ready()) {
            lines.add(br.readLine());
        }
        int matrixHeight = lines.size();
        adjacencyMatrix = new int[matrixHeight][matrixHeight];
        vertexArray = new Vertex[matrixHeight];
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixHeight; j++) {
                String[] line = lines.get(i).split(", ");
                adjacencyMatrix[i][j] = Integer.parseInt(line[j]);
            }
            vertexArray[i] = new Vertex(i);
        }
    }

    public String toPrintAdjMatrix() {
        StringBuffer stringBuffer = new StringBuffer();
        int matrixLength = adjacencyMatrix.length;
        for (int i = 0; i < matrixLength; i++) {
            for (int j = 0; j < matrixLength; j++) {
                stringBuffer.append(adjacencyMatrix[i][j]).append(", ");
            }
            int sbLength = stringBuffer.length();
            stringBuffer.delete(sbLength - 2, sbLength);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public String toPrintHashMap(HashMap<Vertex, String> hashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry entry: hashMap.entrySet()) {
            stringBuffer.append(String.format("Friendliness coefficient %d = %s\n",
                    ((Vertex) entry.getKey()).getName(), entry.getValue()));
        }
        return stringBuffer.toString();
    }
}
