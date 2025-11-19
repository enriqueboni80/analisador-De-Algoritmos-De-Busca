package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File; // Importado para manipulação de diretórios
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe principal para executar os algoritmos de busca de rota.
 * Todos os parâmetros de entrada (arquivo, origem e destino) agora são fixos no código.
 */
public class Main {

    private static String limparAspas(String s) {
        return s.replace("\"", "")
                .replace("“", "") // Aspa curva inicial
                .replace("”", "") // Aspa curva final
                .replace("'", ""); // Aspa simples, por segurança
    }

    private static List<String> listarConteudo(String caminhoDoDiretorio) {

        // 1. Cria um objeto File para o diretório
        File pasta = new File(caminhoDoDiretorio);

        // 2. Obtém a lista de arquivos e diretórios
        File[] listaDeItens = pasta.listFiles();

        // ❗ CORREÇÃO 1 e 2: Tipo de dado correto (List) e inicialização (new ArrayList<>())
        List<String> arrayDePaths = new ArrayList<>();

        // 3. Verifica se a pasta existe e lista o conteúdo
        if (listaDeItens != null && listaDeItens.length > 0) {
            System.out.println("--- Conteúdo do Diretório: " + caminhoDoDiretorio + " ---");

            for (File item : listaDeItens) {
                // Se o item for um ARQUIVO, adiciona o caminho completo à lista.
                if (item.isFile()) {
                    // ❗ CORREÇÃO 3: Coloque a impressão DENTRO da condição
                    System.out.println("Caminho encontrado: " + item.getAbsolutePath());
                    arrayDePaths.add(item.getAbsolutePath());
                }
            }
        } else {
            System.out.println("O diretório está vazio, não existe ou ocorreu um erro.");
        }

        // ❗ CORREÇÃO 4: O método deve retornar o valor prometido
        return arrayDePaths;
    }

    public static void main(String[] args) {

// --- PARÂMETROS PADRÕES (FIXOS - Fallback) ---
        final String DEFAULT_PATH_MATRIZ = "matrizes/teste_3x3.txt";
        final String DEFAULT_COORD_ORIGEM = "0,0";
        final String DEFAULT_COORD_DESTINO = "2,2";
        // Diretório de saída (SEMPRE FIXO, conforme solicitado)
        final String OUTPUT_DIR = "src/analises-matrizes";

        // --- DEFINIÇÃO DOS PARÂMETROS FINAIS (Prioriza args) ---
        String PATH_MATRIZ = "";
        String coordOrigemStr = "";
        String coordDestinoStr = "";

        // 1. Arquivo de entrada (args[0])
        if (args.length >= 1) {
            PATH_MATRIZ = args[0];
        } else {
            PATH_MATRIZ = DEFAULT_PATH_MATRIZ;
        }

        // 2. Coordenadas de Origem (args[1])
        if (args.length >= 2) {
            coordOrigemStr = limparAspas(args[1]);
        } else {
            coordOrigemStr = DEFAULT_COORD_ORIGEM;
        }

        // 3. Coordenadas de Destino (args[2])
        if (args.length >= 3) {
            coordDestinoStr = limparAspas(args[2]);
        } else {
            coordDestinoStr = DEFAULT_COORD_DESTINO;
        }
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

    /**
     * LÊ O ARQUIVO COMO UM RECURSO (RESOURCE STREAM)
     */
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

    /**
     * Escreve o arquivo de saída formatado.
     * @param diretorio O diretório onde o arquivo deve ser salvo.
     */
    private static void escreverSaida(ResultadoBusca resultado, String diretorio, String nomeBaseArquivo) throws IOException {
        // Constrói o caminho completo: diretorio/nomebase.sufixo
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