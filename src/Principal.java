/*
 * Universidade Federal de Santa Catarina - UFSC
 * Departamento de Informática e Estatística - INE
 * Programa de Pós-Graduação em Ciências da Computação - PROPG
 * Disciplinas: Projeto e Análise de Algoritmos
 * Prof Alexandre Gonçalves da Silva 
 *
 * Página 477 Thomas H. Cormen 3a Ed 
 *
 * Caminho mínimos de fonte única em grafos acíclicos dirigidos
 */

/**
 * @author Osmar de Oliveira Braz Junior
 */
import java.util.LinkedList;
import java.util.List;

public class Principal {

    //A medida que o grafo é percorrido, os vértices visitados vão
    //sendo coloridos. Cada vértice tem uma das seguintes cores:
    final static int BRANCO = 0;//Vértce ainda não visitado
    final static int CINZA = 1; //Vértice visitado mas não finalizado
    final static int PRETO = 2; //Vértice visitado e finalizado

    //Vetor da situação vértice, armazena uma das cores
    static int cor[];
    //d[x] armazena o instante de descoberta de x.
    static int d[];
    //f[x] armazena o instante de finalização de x.
    static int f[];
    //Vertor dos pais de um vértice
    static int pi[];
    static int tempo;

    /**
     * Troca um número que representa a posição pela vértice do grafo.
     *
     * @param i Posição da letra
     * @return Uma String com a letra da posição i
     */
    public static String trocar(int i) {
        String letras = "rstxyz";
        if ((i >=0) && (i<=letras.length())) {
            return letras.charAt(i) + "";
        } else {
            return "-";
        }
    }

    /**
     * Troca a letra pela posição na matriz de adjacência
     *
     * @param v Letra a ser troca pela posição
     * @return Um inteiro com a posição da letra no grafo
     */
    public static int destrocar(char v) {
        String letras = "rstxyz";
        int pos = -1;
        for (int i = 0; i < letras.length(); i++) {
            if (letras.charAt(i) == v) {
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Mostra o caminho de s até v no grafo G
     *
     * @param G Matriz de incidência do grafo
     * @param s Origem no grafo
     * @param v Destino no grafo
     */
    public static void mostrarCaminho(int[][] G, int s, int v) {
        if (v != s) {            
            if (pi[v] == -1) {
                System.out.println("Não existe caminho de " + trocar(s) + " a " + trocar(v));
            } else {                
                mostrarCaminho(G, s, pi[v]);
                System.out.println(trocar(pi[v]) + " -> " + trocar(v) + " custo: " + d[v]);                
            }
        }
    }
    
    /**
     * Constrói recursivamente uma Árvore de Busca em profundidade. com raiz u.
     *
     * Consumo de tempo Adj[u] vezes
     *
     * Método DFS-Visit(G,u)
     *
     * @param G Matriz de incidência do grafo
     * @param u Vértice raiz da árvore de busca
     */
    public static void buscaEmProfundidadeVisita(int[][] G, int u, LinkedList lista) {
        //Quantidade vértices do grafo
        int n = G.length;
        cor[u] = CINZA;
        tempo = tempo + 1; //Vértice branco u acabou de ser descoberto
        d[u] = tempo;
        // Exporar as arestas (u,v)
        for (int v = 0; v < n; v++) {
            //Somente com os adjancentes ao vértice u
            if (G[u][v] != 0) {
                //Somente vértices nao visitados
                if (cor[v] == BRANCO) {
                    pi[v] = u;
                    buscaEmProfundidadeVisita(G, v, lista);
                }
            }
        }
        //Vértice u foi visitado e finalizado
        cor[u] = PRETO;
        tempo = tempo + 1;
        f[u] = tempo;
        //Adiciona no início da lista da ordenação topologica
        lista.addFirst(u);
    }

    /**
     * Busca em Profundidade (Breadth-first Search) recursivo.
     *
     * Recebe um grafo G e devolve (i) os instantes d[v] e f[v] para cada v (ii)
     * uma Floresta de Busca em Profundiade
     *
     * Consumo de tempo é O(V)+V chamadas Complexidade de tempo é O(V+E)
     *
     * Método DFS(G)
     *
     * @param G Grafo na forma de uma matriz de adjacência
     */
    public static void buscaEmProfundidadeRecursivo(int[][] G, LinkedList lista) {
        //Quantidade vértices do grafo
        int n = G.length;

        //Inicializa os vetores
        cor = new int[n];
        d = new int[n];
        f = new int[n];
        pi = new int[n];

        //Inicialização dos vetores
        //Consome Theta(V)
        for (int u = 0; u < n; u++) {
            //Vértice i não foi visitado
            cor[u] = BRANCO;
            d[u] = -1;
            pi[u] = -1;
        }
        tempo = 0;

        //Percorre todos os vértices do grafo
        for (int u = 0; u < n; u++) {
            //Somente vértices nao visitados
            if (cor[u] == BRANCO) {
                buscaEmProfundidadeVisita(G, u, lista);
            }
        }
    }

    /**
     * Realiza a ordenação topológica do grafo.
     *
     * Tempo Theta(V+E)
     *
     * @param G Grafo na forma de uma matriz de adjacência a ser ordenado
     */
    public static List ordenacaoTopologica(int[][] G) {
        //Lista do retorno da ordenação topologica
        LinkedList lista = new LinkedList();
        //Vertice completamente visitado é adicionado ao inicio da lista no momento que é atribuido PRETO
        buscaEmProfundidadeRecursivo(G, lista);
        return lista;
    }

    /**
     * Gera um vetor de arestas e pesos.
     *
     * @param G Matriz de adjacência do grafo
     * @return Um vetor de areastas e pesos.
     */
    public static List getMatrizVertices(int[][] G) {
        int n = G.length;
        List vertices = new LinkedList();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (G[i][j] != 0) {
                    vertices.add(new int[]{i, j, G[i][j]});
                }
            }
        }
        return vertices;
    }

    /**
     * Inicializa as estimativas de caminhos mínimos e predecessores.
     *
     * @param G Grafo a ser inicializado
     * @param s Vértice inicial
     */
    public static void inicializaFonteUnica(int[][] G, int s) {
        //Quantidade de vértices do grafo G
        int n = G.length;
        //Instância os vetores        
        d = new int[n];
        pi = new int[n];
        for (int v = 0; v < G.length; v++) {
            d[v] = Integer.MAX_VALUE;
            pi[v] = -1;            
        }
        d[s] = 0;
        pi[s] = 0;
    }

    /**
     * Teste se pode ser melhorado o caminho mínimo de u até v.
     *
     * @param u Vértice de origem.
     * @param v Vértice de destino
     * @param w Peso do caminho u até v.
     */
    private static void relaxamento(int u, int v, int w) {        
        if (d[v] > d[u] + w) {            
            d[v] = d[u] + w;
            pi[v] = u;
        }
    }

    /**
     * Executa o algoritmo de Belmman-Ford para Caminhos Mínimos de fonte única.
     *
     * Encontra a distância mais curta de s para todos os outros vértices.
     * Retorna se existe ciclo negativo no grafo.
     *
     * Complexidade do algoritmo é O(E lg E)
     *
     * @param G Matriz de indicência da árvore
     * @return Vetor com a lista das arestas de menor custo
     */
    public static void caminhosMinimosDAG(int[][] G, int s) {

        //Quantidade de vértices do grafo G
        int n = G.length;
    
        //Converte a matriz em uma lista de arestas
        List arestas = getMatrizVertices(G);

        //Retorna a ordenação topológica de G
        List lista = ordenacaoTopologica(G);
        
        //Inicializa o Grafo
        inicializaFonteUnica(G, s);

        //Percorre cada vértice em ordem topológica
        for (int i = 1; i <= lista.size()-1; i++) {
            //Recupera o primeiro vértice da lista ordenada
            int x = (int) lista.get(i);
            for (int j = 0; j < arestas.size(); j++) {
                int[] vertice = (int[]) arestas.get(j);
                int u = vertice[0];
                int v = vertice[1];
                int w = vertice[2];
                //Para cada vértice de adjacente a u
                if (u == x) {
                    relaxamento(u, v, w);
                }
            }
        }
    }

    public static void main(String args[]) {

        //Grafo da página 465 Thomas H. Cormen 3 ed
        int G[][]
             = //r  s  t  x  y  z       
               {{0, 5, 3, 0, 0, 0}, //r
                {0, 0, 2, 6, 0, 0}, //s
                {0, 0, 0, 7, 4, 2}, //t
                {0, 0, 0, 0,-1, 1}, //x
                {0, 0, 0, 0, 0,-2}, //y
                {0, 0, 0, 0, 0, 0}};//z
//        
//        int G[][]
//                = 
//               //r   s   t   x   y   z       
//                {{0, 0, 2, 6, 0, 0}, //s
//                {0, 0, 0, 7, 4, 2}, //t
//                {0, 0, 0, 0, -1, 1}, //x
//                {0, 0, 0, 0, 0, -2}, //y
//                {0, 0, 0, 0, 0, 0},//z
//                {0, 5, 3, 0, 0, 0}}; //r

        System.out.println("CCaminho mínimos de fonte única em grafos acíclicos dirigidos");

        //Executa o algoritmo
        int s = destrocar('s');
        caminhosMinimosDAG(G, s);

        int v = destrocar('z');
        System.out.println("Caminho de " + trocar(s) + " até " + trocar(v) + ":");
        mostrarCaminho(G, s, v);

        System.out.println("\nMostrando todos dados:");
        for (int i = 0; i < G.length; i++) {
            System.out.println(trocar(pi[i]) + " -> " +  trocar(i) + " custo: " + d[i]);
        }        
    }
}
