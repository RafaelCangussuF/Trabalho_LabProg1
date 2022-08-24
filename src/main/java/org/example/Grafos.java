package org.example;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.SQL.ResultadoSQL;

public class Grafos {
    //private static final String CSV_Path = "C:\\Users\\rafae\\OneDrive\\Área de Trabalho\\3º Ano\\2 semestre\\LabProg2\\airportData.csv";


    public static double d(Double lai, Double loi, Double laj, Double loj){
        lai = Math.toRadians(lai);
        laj = Math.toRadians(laj);
        loi = Math.toRadians(loi);
        loj = Math.toRadians(loj);

        double delta1 = Math.abs(lai-laj);
        double delta2 = Math.abs(loi-loj);

        double a = Math.pow(Math.sin(delta1/2),2)+ Math.cos(lai)*(Math.cos(laj))*(Math.pow(Math.sin(delta2/2),2));
        double resultado = 2*6.371*(Math.asin(Math.pow(a,0.5)));
        return resultado;
    }
    public static Graph exampleGraph() {
        List<Aeroporto> air = new ArrayList<>();
     /*   List<String[]> Data = readData(CSV_Path);

        for (String[] row : Data) {
            Aeroporto aero = new Aeroporto();

            for (String cell : row) {
                String[] sep = cell.split(",");
                aero.setSigla(sep[1]);
                // System.out.print(cell+"\t");
                aero.setMunicipio(sep[4]);
                aero.setEstado(sep[5]);
                aero.setLatitude(sep[16]);
                aero.setLongitude(sep[17]);


            }
            air.add(aero);
            System.out.println();
        }*/

        air.addAll(ResultadoSQL());
        Graph g = new SingleGraph("Aeroportos");
        int i=0, j;

        while(i<40){
            g.addNode(air.get(i).getSigla());
            i++;
        }
        i=0;
        while(i<40){
            j=i;
            while(j<40) {
                //int lai, loi, laj, loj;
                //g.addEdge(air.get(i).getSigla()+air.get(j).getSigla(), air.get(i).getSigla(), air.get(j).getSigla()).setAttribute("length", d(Double.parseDouble(air.get(i).getLatitude()), Double.parseDouble(air.get(i).getLongitude()), Double.parseDouble(air.get(j).getLatitude()), Double.parseDouble(air.get(j).getLongitude())));
                g.addEdge(air.get(i).getSigla()+air.get(j).getSigla(),air.get(i).getSigla(),air.get(j).getSigla()  ).setAttribute("length", d(air.get(i).getLatitude(), air.get(i).getLongitude(), air.get(j).getLatitude(), air.get(j).getLongitude()));
                j++;
            }
            i++;
        }

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