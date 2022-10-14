# Trabalho_LabProg2_Aeroportos

Esse arquivo foi feito como .md no README do github, para uma melhor formatação e conforto na leitura
é sugerido que se veja o README.md em Trabalho_LabProg2_Aeroportos no link do github fornecido no envio
do pdf.

Caso não seja visto, segue o link para acesso:

https://github.com/RafaelCangussuF/Trabalho_LabProg2_Aeroportos/blob/master/README.md

Existem 3 arquivos principais nesse trabalho: Aeroporto, Grafos e SQL

## Aeroporto, Grafos e SQL
- Aeroporto possui a classe aeroporto, seus atributos e métodos. Será utilizada para armazenar os dados obtidos
do banco de dados por meio de uma lista de aeroportos.

- SQL faz a conexão com o banco de dados, recebendo os dados dos aeroportos armazenados nele e fazendo a inserção das respostas das buscas na tabela histórico.

- Grafos é o principal deles, onde o código foco do programa está localizado, utiliza-se a biblioteca graphstream para criar o grafo desejado e a partir dele aplicar
o algoritmo de dijkstra. Podem ser realizadas diversas pesquisas rodando o código uma única vez e elas ficarão salvas no banco de dados, porém no banco de dados
só foi realizado o salvamento do caminho, fazendo-se necessário o entendimento de que o primeiro nó do caminho é a origem e o último o destino.


## Graphstream
Graphstream é uma biblioteca que foi usada para facilitar o desenvolvimento do trabalho, nela já existem a função do algoritmo de dijkstra, para cálculo de caminhos que são objetos do tipo Path e para a criação do grafo com o tipo Graph.

É uma biblioteca com várias funções relacionadas a grafos, permitindo a construção de diferentes tipos de grafos(o usado nesse trabalho foi o SingleGraph, um grafo que não é multigrafo) além da possibilidade de fazer o display gráfico do grafo construído, porém essa última função não será utilizada.

## Explicação dos códigos

### Aeroporto
Classe aeroporto, seus atributos e os métodos para retornar os atributos privados e definir os valores que eles possuem

Mais informações como comentários nos códigos a seguir:


```
//Classe aeroporto onde são armazenadas as informações de cada aeroporto
public class Aeroporto {
    private String sigla;
    private double Latitude, longitude;
    private String Estado, Municipio;

    //Construtor do aeroporto
    public Aeroporto(String sigla1, String Estado1, String Municipio1,
                     double Latitude1, double longitude1){
        setEstado(Estado1);
        setLatitude(Latitude1);
        setSigla(sigla1);
        setMunicipio(Municipio1);
        setLongitude(longitude1);
    }

    //Construtor default
    public Aeroporto() {

    }

    //Metodos de retorno dos atributos privados
    public String getSigla(){
        return this.sigla;
    }

    public String getEstado(){
        return this.Estado;
    }
    public String getMunicipio(){
        return this.Municipio;
    }
    public double getLatitude(){
        return this.Latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }

    //Metodo de definição de valores dos atributos privados
    public void setSigla(String sigla1){
        this.sigla = sigla1;
    }

    public void setLatitude(double l){
        this.Latitude = l;
    }
    public void setLongitude(double l){
        this.longitude = l;
    }
    public void setEstado(String state){
        this.Estado = state;
    }
    public void setMunicipio(String c){
        this.Municipio = c;
    }

```

### SQL
Conexão ao banco de dados, recebimento dos dados dos aeroportos no método ResultadoSQL e inserção das pesquisas no banco de dados no método InserirSQL

Informações mais detalhadas como comentários no código a seguir:

```
import java.util.List;

public class SQL {
    //ResultadoSQL conecta-se ao banco de dados e retorna a lista de aeroportos
    public static List<Aeroporto> ResultadoSQL() {
        List<Aeroporto> air = new ArrayList<>();
        try {
            Connection connection = DriverManager
                                    .getConnection("jdbc:mysql://localhost:3306/jdbc-aeroportos",
                                                    "root", "toor");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from airportData3");
            /*Recebimento do resultSet e colocação dos valores obtidos nos atributos
              de um aeroporto */
            while (resultSet.next()) {
                Aeroporto aero = new Aeroporto();
                aero.setSigla(resultSet.getString("Sigla"));
                aero.setMunicipio(resultSet.getString("county"));
                aero.setEstado(resultSet.getString("state"));
                aero.setLatitude(resultSet.getDouble("latitude"));
                aero.setLongitude(resultSet.getDouble("longitude"));
                //Adição do aeroporto à ArrayList criada no início do método
                air.add(aero);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // retorna a lista de aeroportos
        return air;

    }
    //InserirSQL insere o caminho pesquisado com o primeiro nó sendo a origem e o último o destino
    //Junto com a pesquisa é colocado um ID que identifica a ordem e diferencia caminhos iguais
    public static void InserirSQL(String pesquisa, int i) {

        try {
            Connection connection = DriverManager
                                    .getConnection("jdbc:mysql://localhost:3306/jdbc-aeroportos",
                                                    "root", "toor");

            Statement statement = connection.createStatement();


            String sql = "INSERT INTO `jdbc-aeroportos`.`historico`\n" +
                    "(`idHistórico`,\n" +
                    "`Pesquisa`)\n" +
                    "VALUES\n" +
                    "(?,\n" +
                    "?)";

            PreparedStatement prepstmt = connection.prepareStatement(sql);
            //Salvar a pesquisa no banco de dados e i como idHistórico
            prepstmt.setInt(1, i );
            prepstmt.setString(2, pesquisa);
            prepstmt.execute();
            connection.close();


        } catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {


    }
}
```

### Grafos

#### Informações mais detalhadas sobre cada método/função e sobre a main estarão como comentários nos códigos

- O método d(abreviação de distância) é o método que cálcula distância usando a Haversine Formula

  - Nele, lai e laj são as latitudes de i e j respectivamente, enquanto loi e loj são as longitudes de i e j

```
/*Função de nome d(abreviação de distância) usada para calcular a distância entre
  dois aeroportos a partir de suas coordenadas*/
    public static double distancia(Double lai, Double loi, Double laj, Double loj){
        lai = Math.toRadians(lai);
        laj = Math.toRadians(laj);
        loi = Math.toRadians(loi);
        loj = Math.toRadians(loj);

        double delta1 = Math.abs(lai-laj);
        double delta2 = Math.abs(loi-loj);

        double a = Math.pow(Math.sin(delta1/2),2)+ 
                  + Math.cos(lai)*(Math.cos(laj))*(Math.pow(Math.sin(delta2/2),2));
        return 2*6.371*(Math.asin(Math.pow(a,0.5)));
    }
```

- O método Menu recebe a Sigla do estado desejado e mostra as opções de aeroportos dele, após isso pede que o usuário indique qual o aeroporto desejado pela inserção
de sua sigla, por fim, retorna a sigla do aeroporto como uma String

```
/*Menu é utilizado para receber uma sigla de estado
  e devolver a sigla dos aeroportos internacionais daquele estado*/
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
```

- O método ExampleGraph retorna um Graph, tipo da biblioteca graphstream.

  - Ele usa o resultadoSQL para receber uma lista de aeroportos do banco de dados e montar o grafo a partir dos dados obtidos e das distâncias calculadas pelo método d.
  
```
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
            //O pulo da linha foi realizado para melhor formatação na conversão pdf
                g.addEdge(air.get(i).getSigla()+air.get(j).getSigla(),
                          air.get(i).getSigla(),
                          air.get(j).getSigla())
                          .setAttribute("length", d(air.get(i).getLatitude(),
                                        air.get(i).getLongitude(),
                                        air.get(j).getLatitude(),
                                        air.get(j).getLongitude()));
                j++;
            }
            i++;
        }

        return g;
    }

```


- A main usa os métodos citados anteriormente para realizar a pesquisa

  - Para que não ocorram voos diretos entre origem e destino, visto que foi pedido um caminho com ao menos 1 escala, é realizada a remoção da aresta
entre origem e destino do grafo construído por exampleGraph. Após isso a pesquisa é realizada e após o resultado ser dado a aresta removida é adicionada novamente
para poder serem realizadas novas pesquisas caso o usuário queira. Nos comentários dos códigos possuem explicações sobre o que as funções da Graphstream fazem.

```


public static void main(String[] args) {
        int id = 0;
        boolean game = true;
        // O grafo g é formado com base no grafo construído pelo método exampleGraph
        Graph g = exampleGraph();
        
        // Comprimento das arestas é armazenado no atributo length
        // O comprimento de um caminho é a soma dos comprimentos(length) das arestas do caminho
        
        /* Criação de um objeto do tipo Dijkstra,
           será utilizada no decorrer do código para realizar o algoritmo a partir do grafo g*/
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");


        Scanner scan = new Scanner(System.in);
        /* escolha vai ser utilizado para parar ou não o loop gerado por game,
           utilizado a partir da linha 297*/
        
        int escolha;
        
        /* as strings a, b e c serão respectivamente, a sigla do nó de origem,
           a sigla do nó de destino e a string que armazena o resultado da pesquisa,
           ou seja, o Path entre a e b*/
           
        String a, b, c;
        while(game) {

            // Recebimento dos dados para realisar a pesquisa
            System.out.println("Digite a sigla do estado do no de origem ou DF para o Distrito Federal: ");
            a = scan.nextLine();
            a = Menu(a);
            System.out.println("Digite a sigla do estado do no de destino ou DF para o Distrito Federal: ");
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

            /* inserção da pesquisa feita no banco de dados
               com o primeiro nó sendo origem e o último o destino */
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
```

Para mais informações sobre o código de Grafos, como os imports ir ao arquivo de código em org.examples.

