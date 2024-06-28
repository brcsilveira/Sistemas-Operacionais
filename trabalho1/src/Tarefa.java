public class Tarefa {
    String nome;
    int tempo;

    public Tarefa(String nome, int tempo) {
        this.nome = nome;
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        return nome + ";" + tempo;
    }
 }

