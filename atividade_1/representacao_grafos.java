import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

class informacoes {
    public String nomeArquivo;
    public int n;
    public int m;
    public int[] pointer;
    public int[] arc_dest;
    public int verticeDesejado;
    public int grauSaida;
    public int[] conjSucessores;
    public int grauEntrada;
    public int[] conjPredecessores;
}

public class representacao_grafos {
    // Criando o grafo com as informações essenciais
    static informacoes grafo = new informacoes();

    // Função para capturar as informações do vértice desejado
    public static void capturandoInformacoesVertice() {
        int v = grafo.verticeDesejado;

        // Grau de saída e Conjunto de sucessores
        grafo.grauSaida = grafo.pointer[v + 1] - grafo.pointer[v];
        grafo.conjSucessores = new int[grafo.grauSaida]; // Instanciando o vetor

        int indexSuc = 0;
        for (int i = grafo.pointer[v]; i < grafo.pointer[v + 1]; i++) {
            grafo.conjSucessores[indexSuc] = grafo.arc_dest[i];
            indexSuc++;
        }

        // Grau de entrada e Conjunto de predecessores
        grafo.grauEntrada = 0;

        // Primeiro passa para contar o grau de entrada
        for (int u = 1; u <= grafo.n; u++) {
            for (int i = grafo.pointer[u]; i < grafo.pointer[u + 1]; i++) {
                if (grafo.arc_dest[i] == v) {
                    grafo.grauEntrada++;
                }
            }
        }

        grafo.conjPredecessores = new int[grafo.grauEntrada]; // Instanciando o vetor

        // Segundo passaa para preencher os predecessores
        int indexPred = 0;
        for (int u = 1; u <= grafo.n; u++) {
            for (int i = grafo.pointer[u]; i < grafo.pointer[u + 1]; i++) {
                if (grafo.arc_dest[i] == v) {
                    grafo.conjPredecessores[indexPred] = u;
                    indexPred++;
                }
            }
        }

        // Imprimindo os resultados
        System.out.println("\n--- RESULTADOS PARA O VÉRTICE " + v + " ---");
        System.out.println("Grau de Saída: " + grafo.grauSaida);
        System.out.println("Conjunto de Sucessores: " + Arrays.toString(grafo.conjSucessores));
        System.out.println("Grau de Entrada: " + grafo.grauEntrada);
        System.out.println("Conjunto de Predecessores: " + Arrays.toString(grafo.conjPredecessores));
    }

    // Função para leitura do arquivo
    public static void lerArquivo(int arq) {
        // Mensagem
        if (arq == 1) {
            System.out.println("\nLendo o arquivo: grap-teste-100-1.txt");
        } else {
            System.out.println("\nLendo o arquivo: grap-teste-50000-1.txt");
        }
        // Capturando nome do arquivo
        String nomeArquivo = (arq == 1) ? "graph-test-100.txt" : "graph-test-50000.txt";
        grafo.nomeArquivo = nomeArquivo;

        // Lendo o arquivo
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {

            String primeiraLinha = br.readLine();
            if (primeiraLinha != null) {
                String[] partes = primeiraLinha.trim().split("\\s+");
                int n = Integer.parseInt(partes[0]); // Total de vértices
                int m = Integer.parseInt(partes[1]); // Total de arestas

                grafo.n = n;
                grafo.m = m;

                // Criando vetores para armazenar as informações do grafo
                grafo.pointer = new int[n + 2];
                Arrays.fill(grafo.pointer, -1);
                // No fim pointer[0] = -1 e pointer[n+1] = m
                grafo.arc_dest = new int[m];

                // Loop de m vezes para ler as arestas
                String linhaAresta;
                for (int i = 0; i < m; i++) {
                    linhaAresta = br.readLine();
                    if (linhaAresta != null) {
                        String[] dadosAresta = linhaAresta.trim().split("\\s+");
                        int origem = Integer.parseInt(dadosAresta[0]);
                        int destino = Integer.parseInt(dadosAresta[1]);

                        grafo.arc_dest[i] = destino; // Salva o destino na posição i

                        // Se é a primeira vez da origem marca onde começa
                        if (grafo.pointer[origem] == -1) {
                            grafo.pointer[origem] = i;
                        }
                    }
                }

                grafo.pointer[n + 1] = m;
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Capturando nome do arquivo
        System.out.println("Escolha qual arquivo deseja ler:");
        System.out.println("1- grap-teste-100-1.txt");
        System.out.println("2- grap-teste-50000-1.txt");
        System.out.print("Digite o que deseja: ");
        int arq = sc.nextInt();

        while (arq != 1 && arq != 2) {
            System.out.println("Apenas números de 1 a 2");
            System.out.print("Digite o que deseja: ");
            arq = sc.nextInt();
        }

        // Chamando a função para ler o arquivo
        lerArquivo(arq);

        // Capturando qual vértice desja
        System.out.print("Qual vértice do grafo você deseja[" + 1 + " a " + grafo.n + "]: ");
        int verticeDesejado = sc.nextInt();
        while (verticeDesejado < 1 || verticeDesejado > grafo.n) {
            System.out.println("Apenas números de 1 a " + grafo.n);
            System.out.print("Qual vértice do grafo você deseja[" + 1 + " a " + grafo.n + "]: ");
            verticeDesejado = sc.nextInt();
        }
        grafo.verticeDesejado = verticeDesejado;

        // Captueando informaçoes do vértice desejado
        capturandoInformacoesVertice();

        sc.close();
    }
}