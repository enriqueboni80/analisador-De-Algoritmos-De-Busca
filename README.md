# 🔎 Analisador de Algoritmos de Busca em Grafos (Matrizes)

Este projeto em Java visa testar e comparar a performance de diferentes algoritmos de busca (como BFS, DFS, Dijkstra, A\* e GBS) em grafos representados por matrizes de adjacência. O programa automatiza a leitura de múltiplas matrizes de entrada e gera arquivos de saída com métricas detalhadas de tempo, custo e nós expandidos para cada algoritmo.

## 📅 Informações do Projeto

| Item | Detalhes                                               |
| :--- |:-------------------------------------------------------|
| **Programa Principal** | `AnalisadorDeAlgoritmosDeBusca`                        |
| **Autor** | Enrique Santos Bonifácio Leite                         |
| **Unidade Curricular** | Estrutura de Dados e Análise de Algoritmos – 2º / 2025 |
| **Professor** | Lucas Goulart Silva                                    |
| **Prazo de Entrega** | 26/11/2025                                             |
| **Valor** | 25 pontos                                              |

---

---

## ⚙️ Funcionalidades Principais

* **Leitura Automatizada:** Processa todos os arquivos de matrizes `.txt` encontrados no diretório `src/main/resources/matrizes`.
* **Múltiplos Algoritmos:** Executa uma suíte de algoritmos de busca em cada matriz.
* **Entrada Interativa:** Solicita ao usuário as coordenadas de **Origem** e **Destino** do caminho a ser buscado.
* **Geração de Relatórios:** Cria arquivos de saída detalhados para cada combinação de matriz/algoritmo no diretório `src/analises-matrizes`.

---

## 📦 Estrutura de Diretórios Necessária

O projeto assume a seguinte estrutura de diretórios para entrada e saída:
```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/
│   │   │       └── Main.java  <-- Esta classe
│   │   └── resources/
│   │       └── matrizes/       <-- PASTA DE ENTRADA (Coloque suas matrizes .txt aqui)
│   └── analises-matrizes/  <-- PASTA DE SAÍDA (Criada automaticamente)
```
---
### 📋 Configurando as Matrizes de Entrada

1.  Crie ou localize o diretório `src/main/resources/matrizes`.
2.  Coloque seus arquivos de matrizes de adjacência (por exemplo, `matriz_16x16.txt`, `teste_3x3.txt`) neste diretório.
3.  As matrizes devem ser arquivos de texto simples (`.txt`), onde os valores são separados por **espaços** e representam os pesos das arestas. O número de linhas e colunas deve ser igual para representar um grafo-grid.

---

## ▶️ Como Executar

A execução é interativa e requer que o usuário forneça as coordenadas de início e fim da busca.

1.  **Compile o Projeto:** Compile a classe `Main` e as classes auxiliares.
2.  **Execute o Programa:** Inicie a aplicação principal.

### Passo a Passo no Console:

Ao executar (Run Main), o programa solicitará as coordenadas:
```
Insira a coordenada de Origem (Ex: 0,0): 0,0 <--- (Entrada do usuário) 
Insira a coordenada de Destino (Ex: 2,2): 2,2 <--- (Entrada do usuário)
```
O formato das coordenadas é `linha,coluna`.

Após a entrada, o programa irá:

1.  Listar todas as matrizes encontradas.
2.  Executar todos os algoritmos de busca em **cada** matriz.
3.  Gerar um arquivo de saída para cada execução no diretório `src/analises-matrizes`.

---

## 📂 Formato dos Arquivos de Saída

Os arquivos de saída são gerados no diretório `src/analises-matrizes` e seguem o padrão de nomenclatura: `[NOME_DA_MATRIZ].[SUFIXO_ALGORITMO]`

**Exemplo de Conteúdo de Saída:**
```
ALGORITIMO: Busca em Largura (BFS) 
HEURISTICA: ORIGEM: (0,0)[0] 
DESTINO: (2,2)[8] 
CAMINHO: (0,0)[0]->(0,1)[1]->(1,1)[4]->(2,1)[7]->(2,2)
CUSTO: 5.0 
NOS EXPANDIDOS: 9 
TEMPO (ms): 0.15
```