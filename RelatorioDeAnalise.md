# 📈 Relatório de Análise de Algoritmos de Busca em Grafos

Este relatório analisa os resultados de testes de performance de sete variações de algoritmos de busca (BFS, DFS, Dijkstra, GBS com duas heurísticas e A\* com duas heurísticas) em quatro tamanhos de matrizes (4x4, 16x16, 32x32 e 64x64). A análise se baseia em três métricas principais: **Custo do Caminho**, **Nós Expandidos** e **Tempo de Execução (ms)**.

---

## 1. Tabela de Consolidação de Resultados

| Matriz | Algoritmo | Heurística | Custo (Ideal: ↓) | Nós Exp. (Ideal: ↓) | Tempo (ms) (Ideal: ↓) |
| :---: | :---: | :---: | :---: | :---: | :---: |
| 4x4 | BFS | N/A | 14,0 | 12 | 0,02 |
| 4x4 | DFS | N/A | 47,0 | 11 | 0,02 |
| 4x4 | Dijkstra | N/A | **11,0** | 8 | 0,05 |
| 4x4 | GBS | Manhattan | 14,0 | 5 | 0,06 |
| 4x4 | GBS | Euclidiana | 23,0 | **5** | 0,02 |
| 4x4 | A\* | Manhattan | **11,0** | 8 | 0,06 |
| 4x4 | A\* | Euclidiana | **11,0** | 8 | 0,04 |
| --- | --- | --- | --- | --- | --- |
| 16x16 | BFS | N/A | 18,0 | 13 | 2,01 |
| 16x16 | DFS | N/A | 133,0 | 35 | 1,14 |
| 16x16 | Dijkstra | N/A | **10,0** | 8 | 2,29 |
| 16x16 | GBS | Manhattan | 18,0 | **5** | 0,28 |
| 16x16 | GBS | Euclidiana | 20,0 | **5** | 0,04 |
| 16x16 | A\* | Manhattan | **10,0** | 6 | 0,26 |
| 16x16 | A\* | Euclidiana | **10,0** | 6 | **0,05** |
| --- | --- | --- | --- | --- | --- |
| 32x32 | BFS | N/A | 23,0 | 13 | 0,29 |
| 32x32 | DFS | N/A | 369,0 | 67 | 2,01 |
| 32x32 | Dijkstra | N/A | **18,0** | 10 | 0,40 |
| 32x32 | GBS | Manhattan | 23,0 | **5** | 0,11 |
| 32x32 | GBS | Euclidiana | 21,0 | **5** | 0,10 |
| 32x32 | A\* | Manhattan | **18,0** | 8 | 0,19 |
| 32x32 | A\* | Euclidiana | **18,0** | 8 | **0,19** |
| --- | --- | --- | --- | --- | --- |
| 64x64 | BFS | N/A | 22,0 | 13 | 0,94 |
| 64x64 | DFS | N/A | 612,0 | 131 | 2,86 |
| 64x64 | Dijkstra | N/A | **10,0** | 10 | 1,00 |
| 64x64 | GBS | Manhattan | 22,0 | **5** | 0,59 |
| 64x64 | GBS | Euclidiana | 24,0 | 5 | 0,31 |
| 64x64 | A\* | Manhattan | **10,0** | 5 | **0,37** |
| 64x64 | A\* | Euclidiana | **10,0** | 6 | 0,47 |

---

## 2. Comentários e Conclusões

### a. A Heurística foi Determinante para os Resultados?

**Sim, a heurística foi absolutamente determinante**, tanto para a otimização quanto para a eficiência da busca.

* **Otimização de Caminho (Custo):** Para o algoritmo **A\***, a heurística **não alterou o Custo do Caminho**, pois ele manteve a propriedade de ser ótimo (encontrando o menor custo), igualando o resultado de Dijkstra em todos os testes.
* **Eficiência (Nós Expandidos e Tempo):** Para o **GBS (Busca Gulosa)**, a heurística levou a um desempenho extremamente eficiente em termos de nós: consistentemente **5 nós expandidos** em todos os tamanhos de matriz. Contudo, a **Heurística de Manhattan** demonstrou ser mais precisa, resultando em caminhos de **custo menor** para o GBS do que a Euclidiana na maioria dos casos.

| Algoritmo | Impacto da Heurística |
| :---: | :---: |
| **A\*** | **Minimiza o custo** (mantém o caminho ótimo) e **reduz dramaticamente** os nós expandidos, garantindo ótima escalabilidade. |
| **GBS** | **Reduz drasticamente os Nós Expandidos** (sempre 5), mas **influencia o Custo** (o caminho não é ótimo, mas é muito rápido). |

---

### b. Algum dos Algoritmos Apresentou Melhor Performance?

A melhor performance é distribuída entre algoritmos, dependendo da métrica prioritária:

* **Menor Custo do Caminho (Otimização):** Os algoritmos **Dijkstra** e **A\*** apresentaram o **menor custo** consistentemente em todas as matrizes, confirmando que ambos são algoritmos ótimos para encontrar o caminho mais barato.
* **Menor Número de Nós Expandidos (Eficiência de Busca):** O algoritmo **GBS (Busca Gulosa)** foi o vencedor absoluto, expandindo consistentemente o **mínimo de nós (5)**, pois segue a heurística agressivamente, ignorando a maior parte do grafo.
* **Menor Tempo de Execução (Velocidade Geral):** Os algoritmos **A\*** e **GBS**, especialmente com a heurística Euclidiana, apresentaram os **tempos mais rápidos** na maioria dos testes, mostrando que a orientação da heurística compensa o custo de cálculo.

O algoritmo **DFS (Busca em Profundidade)** apresentou a pior performance em Custo do Caminho (valores extremamente altos, como 612,0 em 64x64), pois ele tende a explorar caminhos longos e ineficientes antes de encontrar a solução de menor custo.

---

### c. O Tamanho do Grafo Impacta a Performance?

**Sim, o tamanho do grafo impacta a performance, mas o efeito é mais severo em algoritmos não-heurísticos.**

| Algoritmo | Impacto do Tamanho (4x4 $\to$ 64x64) |
| :---: | :---: |
| **DFS** | **Impacto Extremo:** O Custo e Nós Expandidos aumentam drasticamente (Custo: 47,0 $\to$ 612,0; Nós: 11 $\to$ 131), mostrando baixa escalabilidade. |
| **Dijkstra** | **Impacto Moderado no Tempo:** Mantém a otimização (Custo e Nós baixos), mas o tempo aumenta perceptivelmente (0,05 ms $\to$ 1,00 ms) devido à necessidade de gerenciar uma fila de prioridade maior. |
| **A\* e GBS** | **Impacto Mínimo na Busca:** O número de **Nós Expandidos (5-8) e o Custo Ótimo são estáveis** em todos os tamanhos de matriz. O Tempo de Execução aumenta (A\* Manhattan: 0,06 ms $\to$ 0,37 ms), mas de forma muito mais controlada, provando que a **Heurística garante escalabilidade** ao limitar a porção do grafo que precisa ser explorada.

---

### Conclusão: 
Os algoritmos Guiados por Heurística (A* e GBS) demonstram muito mais escalabilidade em termos de Nós Expandidos, pois são capazes de ignorar grande parte do grafo, independentemente do seu tamanho total.