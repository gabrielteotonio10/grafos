#  Busca em Profundidade com Classificação de Arestas

##  Visão Geral

Programa em Java que implementa o algoritmo de Busca em Profundidade (DFS) em grafos direcionados. Principais características:

* Visita os vizinhos em **ordem lexicográfica** (usando `TreeMap`).
* Classifica arestas em 4 tipos (Árvore, Retorno, Avanço e Cruzamento).
* Calcula tempos de descoberta (TD) e término (TT) para cada vértice.
* Constrói e exibe a árvore de DFS (relação pai-filho).

##  Como Usar

**1. Compilar:**

```bash
javac GrafoSearch.java

```

**2. Executar:**

```bash
java GrafoSearch  

```

*Exemplo:* `java GrafoSearch graph-test.txt 1`

**3. Formato do Arquivo de Grafo:**
A primeira linha contém o número de vértices e arestas. As seguintes definem as conexões.

```text
5 7
1 2
1 3
...

```

##  Classificação das Arestas

Durante a execução, o algoritmo classifica cada aresta encontrada:

* **ÁRVORE:** É a primeira vez que o vértice destino é alcançado (descobre um novo vértice).
* **RETORNO:** O destino é um ancestral na árvore DFS (indica um ciclo no grafo).
* **AVANÇO:** O destino é um descendente direto (salta para um vértice mais profundo já processado).
* **CRUZAMENTO:** Conecta vértices de ramos diferentes da árvore (sem relação de ancestralidade).

##  Complexidade

* **Tempo:** O(V + E + V log V) — *Na prática aproxima-se de O(V + E), o `log V` é devido à ordenação lexicográfica no TreeMap.*
* **Espaço:** O(V + E) — *Para armazenar a lista de adjacência e as estruturas de controle (arrays e pilha de recursão).*