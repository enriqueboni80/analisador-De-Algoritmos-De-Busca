# 🔎 Analisador de Algoritmos de Busca em Grafos

Este projeto foi desenvolvido como um **Trabalho Prático** para a disciplina de Estrutura de Dados e Análise de Algoritmos, focado na implementação e comparação de algoritmos de busca de rotas em grafos.

## 🎯 Objetivo do Trabalho

O objetivo central deste trabalho é **implementar e comparar algoritmos clássicos de busca de rota em grafos** [cite: 6][cite_start], aplicados a grades bidimensionais (matrizes) com pesos e obstáculos[cite: 6].

O programa executa e avalia os seguintes algoritmos e suas variações:
Busca em Profundidade (**DFS**)
* **Dijkstra**
* Busca em Largura (**BFS**)
* **Ganancioso (GBS)** - com Heurísticas Euclidiana e Manhattan
* **A\*** - com Heurísticas Euclidiana e Manhattan

---

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

## 🛠️ Como Executar a Aplicação

A aplicação deve ser executada a partir da linha de comando, informando o arquivo de entrada, o vértice de origem e o vértice de destino.

### 1. Formato Padrão (Passando Argumentos)

O formato de comando é: `java [Nome do Programa] [Arquivo da Matriz] [Origem] [Destino]`.

**Exemplo de Comando:**

```bash
java AnalisadorDeAlgoritmosDeBusca matrizes/teste_3x3.txt "0,0" "2,2"
```
| Ordem | Argumento | Exemplo | Descrição |
| :---: | :--- | :--- | :--- |
| `args[0]` | Arquivo da Matriz | `matrizes/teste_3x3.txt` | Caminho do arquivo da matriz de adjacências. |
| `args[1]` | Coordenada de Origem | `"0,0"` | Coordenada (linha,coluna) do ponto inicial. |
| `args[2]` | Coordenada de Destino | `"2,2"` | Coordenada (linha,coluna) do ponto final. |

### 2. Uso de Valores Padrão (Fallback)

Se você **não fornecer** os argumentos na linha de comando, a aplicação usará os **valores padrão (fallback)** que estão setados dentro da classe `Main.java`.
**Exemplo de Comando (usando Fallback):**
```bash
java AnalisadorDeAlgoritmosDeBusca
```
* A aplicação usará o caminho, origem e destino configurados nas variáveis `DEFAULT_PATH_MATRIZ`, `DEFAULT_COORD_ORIGEM`, e `DEFAULT_COORD_DESTINO`.