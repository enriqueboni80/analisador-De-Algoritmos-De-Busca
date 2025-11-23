package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class ServicoDeBusca {

    private final double[][] matrizAdj;
    private final int numNos;
    private final int tamanhoGrid;
    private final Map<Integer, No> prototiposNos;

    public ServicoDeBusca(double[][] matrizAdj) {
        this.matrizAdj = matrizAdj;
        this.numNos = matrizAdj.length;
        this.tamanhoGrid = (int) Math.sqrt(numNos);
        this.prototiposNos = new HashMap<>();
        for (int i = 0; i < numNos; i++) {
            prototiposNos.put(i, new No(i, i / tamanhoGrid, i % tamanhoGrid));
        }
    }

    public List<ResultadoBusca> rodarTodosAlgoritmos(No origem, No destino) {
        List<ResultadoBusca> resultados = new ArrayList<>();

        resultados.add(rodarAlgoritmo(Algoritmo.BFS, Heuristica.NENHUMA, origem, destino));
        resultados.add(rodarAlgoritmo(Algoritmo.DFS, Heuristica.NENHUMA, origem, destino));
        resultados.add(rodarAlgoritmo(Algoritmo.DIJKSTRA, Heuristica.NENHUMA, origem, destino));

        resultados.add(rodarAlgoritmo(Algoritmo.GBS, Heuristica.MANHATTAN, origem, destino));
        resultados.add(rodarAlgoritmo(Algoritmo.GBS, Heuristica.EUCLIDIANA, origem, destino));

        resultados.add(rodarAlgoritmo(Algoritmo.A_ESTRELA, Heuristica.MANHATTAN, origem, destino));
        resultados.add(rodarAlgoritmo(Algoritmo.A_ESTRELA, Heuristica.EUCLIDIANA, origem, destino));

        return resultados;
    }

    private ResultadoBusca rodarAlgoritmo(Algoritmo algo, Heuristica heuristica, No origem, No destino) {
        long inicioTempo = System.nanoTime();
        List<No> caminho = new ArrayList<>();
        int nosExpandidos;

        No noOrigem = criarNoDoPrototipo(origem.indice);
        No noDestino = criarNoDoPrototipo(destino.indice);

        No noDestinoEncontrado = null;
        ContadorInt contadorNosExpandidos = new ContadorInt(0);
        double custo = 0;

        switch (algo) {
            case BFS:
                noDestinoEncontrado = rodarBFS(noOrigem, noDestino, contadorNosExpandidos);
                break;
            case DFS:
                noDestinoEncontrado = rodarDFS(noOrigem, noDestino, contadorNosExpandidos);
                break;
            case DIJKSTRA:
                noDestinoEncontrado = rodarDijkstra(noOrigem, noDestino, contadorNosExpandidos);
                break;
            case GBS:
                noDestinoEncontrado = rodarGBS(noOrigem, noDestino, heuristica, contadorNosExpandidos);
                break;
            case A_ESTRELA:
                noDestinoEncontrado = rodarAEstrela(noOrigem, noDestino, heuristica, contadorNosExpandidos);
                break;
        }

        long fimTempo = System.nanoTime();
        double tempoMs = (fimTempo - inicioTempo) / 1_000_000.0;
        nosExpandidos = contadorNosExpandidos.valor;

        if (noDestinoEncontrado != null) {
            caminho = reconstruirCaminho(noDestinoEncontrado);

            if (algo == Algoritmo.DIJKSTRA || algo == Algoritmo.A_ESTRELA) {
                // Assume que o No.java tem getCustoG() ou o campo é acessível
                custo = noDestinoEncontrado.custoG;
            } else {
                custo = calcularCusto(caminho);
            }
        }

        return new ResultadoBusca(algo, heuristica, origem, destino, caminho, custo, nosExpandidos, tempoMs);
    }


    private No rodarBFS(No origem, No destino, ContadorInt contadorNosExpandidos) {
        Queue<No> fila = new LinkedList<>();
        Map<Integer, No> visitados = new HashMap<>();

        // O 'origem' aqui é o clone limpo
        origem.pai = null;
        fila.add(origem);
        visitados.put(origem.indice, origem);
        contadorNosExpandidos.valor = 0;

        while (!fila.isEmpty()) {
            No atual = fila.poll();
            contadorNosExpandidos.valor++;

            if (atual.indice == destino.indice) {
                return atual;
            }

            for (int idxVizinho = 0; idxVizinho < numNos; idxVizinho++) {
                if (matrizAdj[atual.indice][idxVizinho] > 0 && !visitados.containsKey(idxVizinho)) {
                    No vizinho = criarNoDoPrototipo(idxVizinho);
                    vizinho.pai = atual;
                    visitados.put(idxVizinho, vizinho);
                    fila.add(vizinho);
                }
            }
        }
        return null;
    }

    private No rodarDFS(No origem, No destino, ContadorInt contadorNosExpandidos) {
        Stack<No> pilha = new Stack<>();
        Map<Integer, No> visitados = new HashMap<>();

        origem.pai = null;
        pilha.push(origem);
        contadorNosExpandidos.valor = 0;

        while(!pilha.isEmpty()) {
            No atual = pilha.pop();

            if (visitados.containsKey(atual.indice)) {
                continue;
            }

            visitados.put(atual.indice, atual);
            contadorNosExpandidos.valor++;

            if (atual.indice == destino.indice) {
                return atual;
            }

            for (int idxVizinho = numNos - 1; idxVizinho >= 0; idxVizinho--) {
                if (matrizAdj[atual.indice][idxVizinho] > 0 && !visitados.containsKey(idxVizinho)) {
                    No vizinho = criarNoDoPrototipo(idxVizinho);
                    vizinho.pai = atual;
                    pilha.push(vizinho);
                }
            }
        }
        return null;
    }

    private No rodarDijkstra(No origem, No destino, ContadorInt contadorNosExpandidos) {
        PriorityQueue<No> filaPrioridade = new PriorityQueue<>(Comparator.comparingDouble(n -> n.custoG));
        Map<Integer, Double> custosG = new HashMap<>();
        Map<Integer, No> nosFechados = new HashMap<>();

        contadorNosExpandidos.valor = 0;
        origem.custoG = 0;
        origem.pai = null;
        filaPrioridade.add(origem);
        custosG.put(origem.indice, 0.0);

        while (!filaPrioridade.isEmpty()) {
            No atual = filaPrioridade.poll();

            if (nosFechados.containsKey(atual.indice)) {
                continue;
            }

            nosFechados.put(atual.indice, atual);
            contadorNosExpandidos.valor++;

            if (atual.indice == destino.indice) {
                return atual;
            }

            for (int idxVizinho = 0; idxVizinho < numNos; idxVizinho++) {
                double pesoAresta = matrizAdj[atual.indice][idxVizinho];
                if (pesoAresta > 0 && !nosFechados.containsKey(idxVizinho)) {
                    double novoCustoG = atual.custoG + pesoAresta;

                    if (novoCustoG < custosG.getOrDefault(idxVizinho, Double.MAX_VALUE)) {
                        custosG.put(idxVizinho, novoCustoG);

                        No vizinho = criarNoDoPrototipo(idxVizinho);
                        vizinho.custoG = novoCustoG;
                        vizinho.pai = atual;
                        filaPrioridade.add(vizinho);
                    }
                }
            }
        }
        return null;
    }

    private No rodarGBS(No origem, No destino, Heuristica heuristica, ContadorInt contadorNosExpandidos) {
        PriorityQueue<No> filaPrioridade = new PriorityQueue<>(Comparator.comparingDouble(n -> n.custoH));
        Map<Integer, No> visitados = new HashMap<>();

        contadorNosExpandidos.valor = 0;
        origem.custoH = calcularHeuristica(origem, destino, heuristica);
        origem.pai = null;
        filaPrioridade.add(origem);
        visitados.put(origem.indice, origem);

        while (!filaPrioridade.isEmpty()) {
            No atual = filaPrioridade.poll();
            contadorNosExpandidos.valor++;

            if (atual.indice == destino.indice) {
                return atual;
            }

            for (int idxVizinho = 0; idxVizinho < numNos; idxVizinho++) {
                if (matrizAdj[atual.indice][idxVizinho] > 0 && !visitados.containsKey(idxVizinho)) {
                    No vizinho = criarNoDoPrototipo(idxVizinho);
                    vizinho.custoH = calcularHeuristica(vizinho, destino, heuristica);
                    vizinho.pai = atual;
                    visitados.put(idxVizinho, vizinho);
                    filaPrioridade.add(vizinho);
                }
            }
        }
        return null;
    }

    private No rodarAEstrela(No origem, No destino, Heuristica heuristica, ContadorInt contadorNosExpandidos) {
        PriorityQueue<No> conjuntoAberto = new PriorityQueue<>(Comparator.comparingDouble(n -> n.custoF));
        Map<Integer, Double> custosG = new HashMap<>();
        Map<Integer, No> conjuntoFechado = new HashMap<>();

        contadorNosExpandidos.valor = 0;
        origem.custoG = 0;
        origem.custoH = calcularHeuristica(origem, destino, heuristica);
        origem.custoF = origem.custoG + origem.custoH;
        origem.pai = null;
        conjuntoAberto.add(origem);
        custosG.put(origem.indice, 0.0);

        while (!conjuntoAberto.isEmpty()) {
            No atual = conjuntoAberto.poll();

            if (conjuntoFechado.containsKey(atual.indice)) {
                continue;
            }

            conjuntoFechado.put(atual.indice, atual);
            contadorNosExpandidos.valor++;

            if (atual.indice == destino.indice) {
                return atual;
            }

            for (int idxVizinho = 0; idxVizinho < numNos; idxVizinho++) {
                double pesoAresta = matrizAdj[atual.indice][idxVizinho];
                if (pesoAresta > 0 && !conjuntoFechado.containsKey(idxVizinho)) {
                    double novoCustoG = atual.custoG + pesoAresta;

                    if (novoCustoG < custosG.getOrDefault(idxVizinho, Double.MAX_VALUE)) {
                        custosG.put(idxVizinho, novoCustoG);

                        No vizinho = criarNoDoPrototipo(idxVizinho);
                        vizinho.custoG = novoCustoG;
                        vizinho.custoH = calcularHeuristica(vizinho, destino, heuristica);
                        vizinho.custoF = vizinho.custoG + vizinho.custoH;
                        vizinho.pai = atual;

                        conjuntoAberto.add(vizinho);
                    }
                }
            }
        }
        return null;
    }


    private No criarNoDoPrototipo(int indice) {
        No prototipo = prototiposNos.get(indice);
        // Retorna uma nova instância do nó com as coordenadas e índice corretos.
        return new No(prototipo.indice, prototipo.linha, prototipo.coluna);
    }

    private double calcularHeuristica(No de, No para, Heuristica heuristica) {
        switch (heuristica) {
            case MANHATTAN:
                return Math.abs(de.linha - para.linha) + Math.abs(de.coluna - para.coluna);
            case EUCLIDIANA:
                return Math.sqrt(Math.pow(de.linha - para.linha, 2) + Math.pow(de.coluna - para.coluna, 2));
            case NENHUMA:
            default:
                return 0;
        }
    }

    private List<No> reconstruirCaminho(No noFinal) {
        List<No> caminho = new ArrayList<>();
        No atual = noFinal;

        // 1. Itera para trás (do destino para a origem)
        while (atual != null) {
            caminho.add(atual);
            if (atual.pai == null) {
                break; // Parou no nó de origem, onde pai deve ser null
            }
            atual = atual.pai;
        }

        if (caminho.isEmpty() || caminho.get(caminho.size() - 1).pai != null) {
            return new ArrayList<>();
        }

        Collections.reverse(caminho);
        return caminho;
    }

    private double calcularCusto(List<No> caminho) {
        double custo = 0;
        if (caminho.size() < 2) return 0;

        for (int i = 0; i < caminho.size() - 1; i++) {
            No u = caminho.get(i);
            No v = caminho.get(i + 1);
            custo += matrizAdj[u.indice][v.indice];
        }
        return custo;
    }

    private static class ContadorInt {
        int valor;
        ContadorInt(int valor) {
            this.valor = valor;
        }
    }
}