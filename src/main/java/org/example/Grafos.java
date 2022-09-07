package org.example;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.SQL.InserirSQL;
import static org.example.SQL.ResultadoSQL;

public class Grafos {
    //private static final String CSV_Path = "C:\\Users\\rafae\\OneDrive\\Área de Trabalho\\3º Ano\\2 semestre\\LabProg2\\airportData.csv";


    //Função de nome d(abreviação de distância) usada para calcular a distância entre dois aeroportos a partir de suas coordenadas
    public static double d(Double lai, Double loi, Double laj, Double loj){
        lai = Math.toRadians(lai);
        laj = Math.toRadians(laj);
        loi = Math.toRadians(loi);
        loj = Math.toRadians(loj);

        double delta1 = Math.abs(lai-laj);
        double delta2 = Math.abs(loi-loj);

        double a = Math.pow(Math.sin(delta1/2),2)+ Math.cos(lai)*(Math.cos(laj))*(Math.pow(Math.sin(delta2/2),2));
        return 2*6.371*(Math.asin(Math.pow(a,0.5)));
    }
    public static Graph exampleGraph() {
        // Inicialização do grafo a partir dos dados existentes no Banco de dados
        List<Aeroporto> air = new ArrayList<>();

        air.addAll(ResultadoSQL());
        Graph g = new SingleGraph("Aeroportos");
        int i=0, j;
        //Criação dos nós
        while(i<40){
            g.addNode(air.get(i).getSigla());
            i++;
        }
        i=0;
        //Criação das arestas
        while(i<40){
            j=i;
            while(j<40) {
                g.addEdge(air.get(i).getSigla()+air.get(j).getSigla(),air.get(i).getSigla(),air.get(j).getSigla()  ).setAttribute("length", d(air.get(i).getLatitude(), air.get(i).getLongitude(), air.get(j).getLatitude(), air.get(j).getLongitude()));
                j++;
            }
            i++;
        }

        return g;
    }

    public static void main(String[] args) {
        int id = 0;
        boolean game = true;
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
        int escolha;
        String a, b, c;
        while(game) {

            // Recebimento dos dados para realisar a pesquisa
            System.out.println("Digite o no de inicio: ");
            a = scan.nextLine();
            System.out.println("Digite o no de fim:");
            b = scan.nextLine();

            //Inicialização do algoritmo de dijkstra a partir do nó de origem dado
            dijkstra.init(g);
            //Definição de a como nó de origem
            dijkstra.setSource(g.getNode(a));
            //Remoção da aresta entre origem e destino para garantir um caminho com no mínimo 1 escala
            g.removeEdge(g.getNode(a), g.getNode(b));
            dijkstra.compute();

            //Parte comentada a seguir é a impressão do comprimento de todos os caminhos a partir do nó de início
            /*for (Node node : g)
                System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,
                        dijkstra.getPathLength(node));*/


            //Impressão do menor caminho entre origem e destino e de qual caminho ele é
            System.out.println(dijkstra.getPath(g.getNode(b)));
            System.out.println(dijkstra.getPathLength(g.getNode(b)));

            // inserção da pesquisa feita no banco de dados com o primeiro nó sendo origem e o último o destino
            c = dijkstra.getPath(g.getNode(b)).toString();
            InserirSQL(c, id);

            //Adicionar a aresta retirada para garantir o estado original do grafo g
            g.addEdge(a+b,g.getNode(a),g.getNode(b));

            //Escolha de continuar ou não a fazer pesquisas
            System.out.println("Digite 1 se deseja continuar ou 0 para terminar a pesquisa: ");
            escolha = scan.nextInt();
            if(escolha == 0){
                game = false;
            }
            else
            if(escolha == 1){
                //String criada para resolver um problema de descarte
               String descarte = scan.nextLine();
               id++;
            }
        }
        // cleanup to save memory if solutions are no longer needed
        dijkstra.clear();



    }
}