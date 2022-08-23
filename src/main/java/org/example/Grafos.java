package org.example;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Scanner;

public class Grafos {
    public static Graph exampleGraph() {
        Graph g = new SingleGraph("example");
        g.addNode("A").setAttribute("xy", 0, 1);
        g.addNode("B").setAttribute("xy", 1, 2);
        g.addNode("C").setAttribute("xy", 1, 1);
        g.addNode("D").setAttribute("xy", 1, 0);
        g.addNode("E").setAttribute("xy", 2, 2);
        g.addNode("F").setAttribute("xy", 2, 1);
        g.addNode("G").setAttribute("xy", 2, 0);
        g.addEdge("AB", "A", "B").setAttribute("length", 14);
        g.addEdge("AC", "A", "C").setAttribute("length", 9);
        g.addEdge("AD", "A", "D").setAttribute("length", 7);
        g.addEdge("BC", "B", "C").setAttribute("length", 2);
        g.addEdge("CD", "C", "D").setAttribute("length", 10);
        g.addEdge("BE", "B", "E").setAttribute("length", 9);
        g.addEdge("CF", "C", "F").setAttribute("length", 11);
        g.addEdge("DF", "D", "F").setAttribute("length", 15);
        g.addEdge("EF", "E", "F").setAttribute("length", 6);

        g.nodes().forEach(n -> n.setAttribute("label", n.getId()));
        g.edges().forEach(e -> e.setAttribute("label", "" + (int) e.getNumber("length")));

        return g;
    }

    public static void main(String[] args) {
        Graph g = exampleGraph();

        // Edge lengths are stored in an attribute called "length"
        // The length of a path is the sum of the lengths of its edges
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");


        /*
        // Compute the shortest paths in g from A to all nodes
        dijkstra.init(g);
        dijkstra.setSource(g.getNode("A"));
        dijkstra.compute();

        // Print the lengths of all the shortest paths
        for (Node node : g)
            System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,
                    dijkstra.getPathLength(node));


        // Print the shortest path from A to B
        System.out.println(dijkstra.getPath(g.getNode("B")));
        */
        Scanner scan = new Scanner(System.in);

        System.out.println(" Digite o no de inicio: ");
        String a, b;
        a = scan.nextLine();
        System.out.println("Digite o no de fim:");
        b = scan.nextLine();

        dijkstra.init(g);
        dijkstra.setSource(g.getNode(a));
        g.removeEdge(g.getNode(a), g.getNode(b));
        dijkstra.compute();

        for (Node node : g)
            System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,
                    dijkstra.getPathLength(node));


        System.out.println(dijkstra.getPathLength(g.getNode(b)));
        System.out.println(dijkstra.getPath(g.getNode(b)));

        System.out.println("\n\n\n");

        // cleanup to save memory if solutions are no longer needed
        dijkstra.clear();



    }
}