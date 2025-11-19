package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;


public class Main {

    private static String limparAspas(String s) {
        return s.replace("\"", "")
                .replace("“", "") // Aspa curva inicial
                .replace("”", "") // Aspa curva final
                .replace("'", ""); // Aspa simples, por segurança
    }

    private static List<String> listarConteudo(String caminhoDoDiretorio) {
        File pasta = new File(caminhoDoDiretorio);
        File[] listaDeItens = pasta.listFiles();
        List<String> arrayDePaths = new ArrayList<>();
        if (listaDeItens != null && listaDeItens.length > 0) {
            System.out.println("--- Conteúdo do Diretório: " + caminhoDoDiretorio + " ---");
            for (File item : listaDeItens) {
                if (item.isFile()) {
                    System.out.println("Caminho encontrado: " + item.getAbsolutePath());
                    arrayDePaths.add(item.getAbsolutePath());
                }
            }
        } else {
            System.out.println("O diretório está vazio, não existe ou ocorreu um erro.");
        }
        return arrayDePaths;
    }

    public static String extrairCaminhoClasspath(String caminhoCompleto) {
        String caminhoPadronizado = caminhoCompleto.replace(File.separatorChar, '/');
        String PONTO_DE_CORTE = "src/main/resources/";
        int indiceInicial = caminhoPadronizado.indexOf(PONTO_DE_CORTE);
        return caminhoPadronizado.substring(indiceInicial + PONTO_DE_CORTE.length());
    }

    public static void main(String[] args) {
        final String OUTPUT_DIR = "src/analises-matrizes";
        String coordOrigemStr = "";
        String coordDestinoStr = "";

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Insira a coordenada de Origem (Ex: 0,0):");
            coordOrigemStr = scanner.nextLine();
            System.out.println("Insira a coordenada de Destino (Ex: 2,2):");
            coordDestinoStr = scanner.nextLine();

            List<String> caminhos = listarConteudo("src/main/resources/matrizes");
            for (String PATH_MATRIZ : caminhos) {
                PATH_MATRIZ = extrairCaminhoClasspath(PATH_MATRIZ);

                // ----------------------------------------
                System.out.println("--- Parâmetros de Execução ---");
                System.out.println("Arquivo de Entrada: " + PATH_MATRIZ);
                System.out.println("Origem: " + coordOrigemStr);
                System.out.println("Destino: " + coordDestinoStr);
                System.out.println("Arquivos de Saída: " + OUTPUT_DIR);
                System.out.println("------------------------------");

                try {
                    // 1. Ler o grafo (matriz de adjacência)
                    double[][] matrizAdj = lerGrafo(PATH_MATRIZ);

                    int tamanhoGrid = (int) Math.sqrt(matrizAdj.length);
                    if (tamanhoGrid * tamanhoGrid != matrizAdj.length) {
                        System.err.println("Erro: A matriz de adjacência não é quadrada para formar um grid (ex: 9x9, 16x16).");
                        return;
                    }

                    // 2. Parsear coordenadas de origem e destino
                    No noOrigem = parsearNo(coordOrigemStr, tamanhoGrid);
                    No noDestino = parsearNo(coordDestinoStr, tamanhoGrid);

                    // 3. Inicializar o serviço de busca
                    ServicoDeBusca servicoBusca = new ServicoDeBusca(matrizAdj);

                    // 4. Executar todos os algoritmos e salvar os resultados
                    List<ResultadoBusca> resultados = servicoBusca.rodarTodosAlgoritmos(noOrigem, noDestino);

                    // 5. Preparar e Gerar arquivos de saída
                    String nomeBaseSaida = PATH_MATRIZ.replace("matrizes/", "").replace(".txt", "");

                    // Cria o diretório de saída se ele não existir
                    File dir = new File(OUTPUT_DIR);
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            throw new IOException("Não foi possível criar o diretório de saída: " + OUTPUT_DIR);
                        }
                    }

                    for (ResultadoBusca resultado : resultados) {
                        // Chama o método com o diretório
                        escreverSaida(resultado, OUTPUT_DIR, nomeBaseSaida);
                    }

                    System.out.println("Buscas concluídas. " + resultados.size() + " arquivos de saída gerados em: " + OUTPUT_DIR);

                } catch (IOException e) {
                    System.err.println("Erro de I/O: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao processar as coordenadas ou o arquivo de matriz.");
                } catch (Exception e) {
                    System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro durante a execução");
        }
    }

    private static double[][] lerGrafo(String nomeArquivo) throws IOException {
        List<double[]> linhas = new ArrayList<>();

        InputStream is = Main.class.getClassLoader().getResourceAsStream(nomeArquivo);

        if (is == null) {
            throw new IOException("Arquivo não encontrado no classpath: " + nomeArquivo);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                String[] valores = linha.split("\\s+");
                double[] linhaVetor = new double[valores.length];
                for (int i = 0; i < valores.length; i++) {
                    linhaVetor[i] = Double.parseDouble(valores[i]);
                }
                linhas.add(linhaVetor);
            }
        }

        if (linhas.isEmpty() || linhas.get(0).length != linhas.size()) {
            throw new IOException("A matriz no arquivo não parece ser quadrada ou está vazia.");
        }

        return linhas.toArray(new double[0][]);
    }

    private static No parsearNo(String coordStr, int tamanhoGrid) {
        String[] partes = coordStr.replace("\"", "").split(",");
        int linha = Integer.parseInt(partes[0]);
        int coluna = Integer.parseInt(partes[1]);
        if (linha >= tamanhoGrid || coluna >= tamanhoGrid || linha < 0 || coluna < 0) {
            throw new IllegalArgumentException("Coordenadas fora dos limites do grid: " + coordStr);
        }
        int indice = linha * tamanhoGrid + coluna;
        return new No(indice, linha, coluna);
    }

    private static void escreverSaida(ResultadoBusca resultado, String diretorio, String nomeBaseArquivo) throws IOException {
        String nomeArquivoSaida = diretorio + File.separator + nomeBaseArquivo + "." + resultado.getSufixoArquivo();

        try (PrintWriter escritor = new PrintWriter(new FileWriter(nomeArquivoSaida))) {

            escritor.println("ALGORITIMO: " + resultado.getNomeAlgoritmo());
            escritor.println("HEURISTICA: " + (resultado.getNomeHeuristica() != null ? resultado.getNomeHeuristica() : ""));
            escritor.println("ORIGEM: " + resultado.getNoOrigem());
            escritor.println("DESTINO: " + resultado.getNoDestino());

            if (resultado.getCaminho() != null && !resultado.getCaminho().isEmpty()) {
                String caminhoStr = resultado.getCaminho().stream()
                        .map(No::toString)
                        .collect(Collectors.joining("->"));
                escritor.println("CAMINHO: " + caminhoStr);
                escritor.println("CUSTO: " + String.format("%.1f", resultado.getCusto()));
            } else {
                escritor.println("CAMINHO: ");
                escritor.println("CUSTO: ");
            }

            escritor.println("NOS EXPANDIDOS: " + resultado.getNosExpandidos());
            escritor.println("TEMPO (ms): " + String.format("%.2f", resultado.getTempoMs()).replace(",", "."));
        }
    }
}