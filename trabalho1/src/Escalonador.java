import java.io.*;
import java.util.*;

public class Escalonador {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java Escalonador <arquivo de tarefas> <numero de processadores>");
            return;
        }

        String nomeArquivo = args[0];
        int numProcessadores = Integer.parseInt(args[1]);

        List<Tarefa> tarefas = lerTarefas(nomeArquivo);

        // SJF
        tarefas.sort(Comparator.comparingInt(t -> t.tempo));
        escalonarTarefas(tarefas, numProcessadores, "saidaSJF.txt");

        // LJF
        tarefas.sort((t1, t2) -> t2.tempo - t1.tempo);
        escalonarTarefas(tarefas, numProcessadores, "saidaLJF.txt");
    }

    private static List<Tarefa> lerTarefas(String nomeArquivo) {
        List<Tarefa> tarefas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" ");
                String nome = partes[0];
                int tempo = Integer.parseInt(partes[1]);
                tarefas.add(new Tarefa(nome, tempo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tarefas;
    }

    private static void escalonarTarefas(List<Tarefa> tarefas, int numProcessadores, String nomeArquivoSaida) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nomeArquivoSaida))) {
            int[] temposProcessadores = new int[numProcessadores];
            Arrays.fill(temposProcessadores, 0);

            List<List<String>> escalonamento = new ArrayList<>();
            for (int i = 0; i < numProcessadores; i++) {
                escalonamento.add(new ArrayList<>());
            }

            for (Tarefa tarefa : tarefas) {
                int processadorEscolhido = 0;
                for (int i = 1; i < numProcessadores; i++) {
                    if (temposProcessadores[i] < temposProcessadores[processadorEscolhido]) {
                        processadorEscolhido = i;
                    }
                }
                int tempoInicio = temposProcessadores[processadorEscolhido];
                int tempoFim = tempoInicio + tarefa.tempo;
                escalonamento.get(processadorEscolhido).add(tarefa.nome + ";" + tempoInicio + ";" + tempoFim);
                temposProcessadores[processadorEscolhido] = tempoFim;
            }

            for (int i = 0; i < numProcessadores; i++) {
                pw.println("Processador_" + (i + 1));
                for (String linha : escalonamento.get(i)) {
                    pw.println(linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}