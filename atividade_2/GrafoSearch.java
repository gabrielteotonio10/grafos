import java.io.*;
import java.util.*;

public class GrafoSearch {
    private int numVertices;
    private int numArestas;
    private Map<Integer, List<Integer>> adjacencyList;

    // Arrays para rastrear estado dos vértices durante DFS
    private int[] discoveryTime;
    private int[] finishTime;
    private int[] parent;
    private boolean[] visited;
    private int timeCounter;

    // Para armazenar arestas de árvore e classificações
    private List<String> treeEdges;
    private Map<String, String> edgeClassification;
    private Map<Integer, List<String>> edgesFromVertex;

    public GrafoSearch() {
        treeEdges = new ArrayList<>();
        edgeClassification = new HashMap<>();
        edgesFromVertex = new HashMap<>();
    }

    
    //---------- Lê o arquivo de grafo e inicializa as estruturas de dados
    
    public void lerGrafo(String nomeArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha = br.readLine();
            String[] partes = linha.trim().split("\\s+");
            numVertices = Integer.parseInt(partes[0]);
            numArestas = Integer.parseInt(partes[1]);

            // Inicializar lista de adjacência em ordem
            adjacencyList = new TreeMap<>();
            for (int i = 1; i <= numVertices; i++) {
                adjacencyList.put(i, new ArrayList<>());
            }

            // Ler arestas
            while ((linha = br.readLine()) != null) {
                String[] edge = linha.trim().split("\\s+");
                if (edge.length >= 2) {
                    int origem = Integer.parseInt(edge[0]);
                    int destino = Integer.parseInt(edge[1]);
                    adjacencyList.get(origem).add(destino);
                }
            }

            // Ordenar listas de adjacência em ordem lexicográfica
            for (int i = 1; i <= numVertices; i++) {
                Collections.sort(adjacencyList.get(i));
            }

            // Inicializar arrays
            discoveryTime = new int[numVertices + 1];
            finishTime = new int[numVertices + 1];
            parent = new int[numVertices + 1];
            visited = new boolean[numVertices + 1];
            timeCounter = 0;

            // Inicializar parent com -1
            for (int i = 1; i <= numVertices; i++) {
                parent[i] = -1;
            }
        }
    }

    
    //---------- Realiza busca em profundidade no grafo
     
    private void dfs(int v) {
        visited[v] = true;
        discoveryTime[v] = ++timeCounter;

        // Para cada vértice adjacente em ordem lexicográfica
        for (int w : adjacencyList.get(v)) {
            if (!visited[w]) {
                // Aresta de árvore
                parent[w] = v;
                String aresta = v + " -> " + w;
                treeEdges.add(aresta);
                edgeClassification.put(aresta, "ÁRVORE");

                if (!edgesFromVertex.containsKey(v)) {
                    edgesFromVertex.put(v, new ArrayList<>());
                }
                edgesFromVertex.get(v).add(aresta + " (ÁRVORE)");

                dfs(w);
            } else if (finishTime[w] == 0) {
                // Aresta de retorno (w é ancestral, não pai)
                if (parent[v] != w) {
                    String aresta = v + " -> " + w;
                    edgeClassification.put(aresta, "RETORNO");

                    if (!edgesFromVertex.containsKey(v)) {
                        edgesFromVertex.put(v, new ArrayList<>());
                    }
                    edgesFromVertex.get(v).add(aresta + " (RETORNO)");
                }
            } else {
                // Aresta de avanço ou cruzamento
                String aresta = v + " -> " + w;

                // Verificar se w é descendente de v
                if (discoveryTime[v] < discoveryTime[w]) {
                    edgeClassification.put(aresta, "AVANÇO");

                    if (!edgesFromVertex.containsKey(v)) {
                        edgesFromVertex.put(v, new ArrayList<>());
                    }
                    edgesFromVertex.get(v).add(aresta + " (AVANÇO)");
                } else {
                    edgeClassification.put(aresta, "CRUZAMENTO");

                    if (!edgesFromVertex.containsKey(v)) {
                        edgesFromVertex.put(v, new ArrayList<>());
                    }
                    edgesFromVertex.get(v).add(aresta + " (CRUZAMENTO)");
                }
            }
        }

        finishTime[v] = ++timeCounter;
    }

    
    //----------Realiza DFS completo a partir de um vértice específico
    
    public void executarBusca(int verticeInicial) {
        // Inicializar estruturas
        Arrays.fill(visited, false);
        Arrays.fill(discoveryTime, 0);
        Arrays.fill(finishTime, 0);
        treeEdges.clear();
        edgeClassification.clear();
        edgesFromVertex.clear();
        timeCounter = 0;

        // Executar DFS completo a partir do vértice inicial
        System.out.println("\n=== BUSCA EM PROFUNDIDADE ===");
        System.out.println("Vértice inicial: " + verticeInicial);
        System.out.println();

        dfs(verticeInicial);

        // Se houver vértices não visitados, continuar DFS em ordem
        for (int i = 1; i <= numVertices; i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }
    }

    
    // ----------Exibe todas as arestas de árvore encontradas
    
    public void exibirArestasArvore() {
        System.out.println("\n=== ARESTAS DE ÁRVORE ===");
        if (treeEdges.isEmpty()) {
            System.out.println("Nenhuma aresta de árvore encontrada.");
        } else {
            for (String aresta : treeEdges) {
                System.out.println(aresta);
            }
            System.out.println("Total de arestas de árvore: " + treeEdges.size());
        }
    }

    
    // ----------Exibe a classificação de todas as arestas divergentes do vértice especificado
    
    public void exibirArestasDivergentesDo(int vertice) {
        System.out.println("\n=== ARESTAS DIVERGENTES DO VÉRTICE " + vertice + " ===");

        if (!adjacencyList.containsKey(vertice) || adjacencyList.get(vertice).isEmpty()) {
            System.out.println("O vértice " + vertice + " não possui arestas divergentes.");
        } else {
            List<String> arestas = edgesFromVertex.getOrDefault(vertice, new ArrayList<>());
            if (arestas.isEmpty()) {
                System.out.println("O vértice " + vertice + " não foi alcançado durante a busca.");
            } else {
                for (String aresta : arestas) {
                    System.out.println(aresta);
                }
                System.out.println("Total de arestas divergentes classificadas: " + arestas.size());
            }
        }
    }

    
    // ---------- Exibe informações de tempo (descoberta e término) de todos os vértices
    
    public void exibirTempos() {
        System.out.println("\n=== TEMPOS DE DESCOBERTA E TÉRMINO ===");
        System.out.println(String.format("%8s %12s %12s", "Vértice", "Descoberta", "Término"));
        System.out.println("--------------------------------------");
        for (int i = 1; i <= numVertices; i++) {
            System.out.println(String.format("%8d %12d %12d", i, discoveryTime[i], finishTime[i]));
        }
    }

    
    // ---------- Exibe a árvore de DFS (relação pai-filho)
    
    public void exibirArvoreDFS() {
        System.out.println("\n=== ÁRVORE DE DFS (PAI-FILHO) ===");
        for (int i = 1; i <= numVertices; i++) {
            if (parent[i] == -1) {
                System.out.println("Vértice " + i + " é raiz (pai: -1)");
            } else {
                System.out.println("Vértice " + i + " tem pai: " + parent[i]);
            }
        }
    }

    
    // ---------- Programa principal
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java GrafoSearch <arquivo_grafo> <vértice_inicial>");
            System.out.println("Exemplo: java GrafoSearch graph-test-100.txt 1");
            System.exit(1);
        }

        String nomeArquivo = args[0];
        int verticeInicial;

        try {
            verticeInicial = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Erro: O segundo argumento deve ser um número inteiro.");
            System.exit(1);
            return;
        }

        GrafoSearch grafo = new GrafoSearch();

        try {
            System.out.println("Carregando grafo do arquivo: " + nomeArquivo);
            grafo.lerGrafo(nomeArquivo);
            System.out.println("Grafo carregado com sucesso!");
            System.out.println("Número de vértices: " + grafo.numVertices);
            System.out.println("Número de arestas: " + grafo.numArestas);

            // Validar vértice inicial
            if (verticeInicial < 1 || verticeInicial > grafo.numVertices) {
                System.out.println("Erro: Vértice inválido. Deve estar entre 1 e " + grafo.numVertices);
                System.exit(1);
            }

            // Executar busca
            grafo.executarBusca(verticeInicial);

            // Exibir resultados
            grafo.exibirArestasArvore();
            grafo.exibirArestasDivergentesDo(verticeInicial);
            grafo.exibirTempos();
            grafo.exibirArvoreDFS();

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            System.exit(1);
        }
    }
}