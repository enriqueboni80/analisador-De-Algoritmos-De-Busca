package org.example;

import java.util.List;


public class ResultadoBusca {
    private final Algoritmo algoritmo;
    private final Heuristica heuristica;
    private final No noOrigem;
    private final No noDestino;
    private final List<No> caminho;
    private final double custo;
    private final int nosExpandidos;
    private final double tempoMs;

    public ResultadoBusca(Algoritmo algoritmo, Heuristica heuristica, No noOrigem, No noDestino,
                          List<No> caminho, double custo, int nosExpandidos, double tempoMs) {
        this.algoritmo = algoritmo;
        this.heuristica = heuristica;
        this.noOrigem = noOrigem;
        this.noDestino = noDestino;
        this.caminho = caminho;
        this.custo = custo;
        this.nosExpandidos = nosExpandidos;
        this.tempoMs = tempoMs;
    }

    // Getters
    public List<No> getCaminho() { return caminho; }
    public double getCusto() { return custo; }
    public int getNosExpandidos() { return nosExpandidos; } // [cite: 16]
    public double getTempoMs() { return tempoMs; } // [cite: 17]
    public No getNoOrigem() { return noOrigem; }
    public No getNoDestino() { return noDestino; }

    public String getNomeAlgoritmo() {
        if (algoritmo == Algoritmo.A_ESTRELA) return "A*"; // [cite: 60]
        return algoritmo.name();
    }

    public String getNomeHeuristica() {
        if (heuristica == Heuristica.NENHUMA) return null; // [cite: 69]
        if (heuristica == Heuristica.MANHATTAN) return "Manhattan"; // [cite: 61]
        if (heuristica == Heuristica.EUCLIDIANA) return "Euclidiana";
        return null;
    }


    public String getSufixoArquivo() {
        switch (algoritmo) {
            case BFS: return "bfs";
            case DFS: return "dfs";
            case DIJKSTRA: return "dijkstra"; // [cite: 54] (implícito)
            case GBS:
                return "gbs." + (heuristica == Heuristica.MANHATTAN ? "manhattan" : "euclidiana"); // [cite: 57, 58]
            case A_ESTRELA:
                return "a." + (heuristica == Heuristica.MANHATTAN ? "manhattan" : "euclidiana"); // [cite: 55, 56]
            default:
                return "unknown";
        }
    }
}
