package org.example;

import java.util.Objects;

public class No {
    final int indice; // Índice na matriz de adjacência (0 a N*N-1)
    final int linha;
    final int coluna;

    double custoG; // Custo do início até este nó
    double custoH; // Custo heurístico deste nó até o fim
    double custoF; // custoG + custoH
    No pai;  // Nó do qual viemos para chegar aqui

    public No(int indice, int linha, int coluna) {
        this.indice = indice;
        this.linha = linha;
        this.coluna = coluna;
        this.custoG = Double.MAX_VALUE;
        this.custoH = 0;
        this.custoF = Double.MAX_VALUE;
        this.pai = null;
    }

    public double getCustoG() {
        return custoG;
    }

    public void setCustoG(double custoG) {
        this.custoG = custoG;
    }

    public No getPai() {
        return pai;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    @Override
    public String toString() {
        return "(" + linha + "," + coluna + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        No no = (No) o;
        return indice == no.indice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(indice);
    }
}
