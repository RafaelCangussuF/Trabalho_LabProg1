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
    //Menu é utilizado para receber uma sigla de estado e devolver a sigla dos aeroportos internacionais daquele estado
    public static String Menu(String Sigla_Estado){
        //Criando a lista com os aeroportos
        List<Aeroporto> air = new ArrayList<>();
        air.addAll(ResultadoSQL());
        Scanner scan = new Scanner(System.in);
        String Sigla_Aeroporto_retorno;
        int i =0;
        //Printando as siglas dos aeroportos de dado estado
        while(i<40){
            if(air.get(i).getEstado().equals(Sigla_Estado)){
                System.out.println(air.get(i).getSigla());
            }
            i++;
        }
        System.out.println("Digite a sigla do aeroporto desejado:");
        Sigla_Aeroporto_retorno = scan.nextLine();
        //retornando a sigla do aeroporto selecionado
        return Sigla_Aeroporto_retorno;
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

        // Comprimento das arestas é armazenado no atributo length
        // O comprimento de um caminho é a soma dos comprimentos(length) das arestas do caminho
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");


        Scanner scan = new Scanner(System.in);
        int escolha;
        String a, b, c;
        while(game) {

            // Recebimento dos dados para realisar a pesquisa
            System.out.println("Digite o estado do no de origem: ");
            a = scan.nextLine();
            a = Menu(a);
            System.out.println("Digite o estado do no de destino: ");
            b = scan.nextLine();
            b = Menu(b);

            //Inicialização do algoritmo de dijkstra a partir do nó de origem dado
            dijkstra.init(g);
            //Definição de a como nó de origem
            dijkstra.setSource(g.getNode(a));
            //Remoção da aresta entre origem e destino para garantir um caminho com no mínimo 1 escala
            g.removeEdge(g.getNode(a), g.getNode(b));
            dijkstra.compute();

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