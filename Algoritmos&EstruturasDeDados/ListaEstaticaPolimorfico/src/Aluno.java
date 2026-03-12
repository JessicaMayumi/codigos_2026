import java.util.Objects;

public class Aluno {
    String nome;
    int matricula;

    public Aluno(String nome, int matricula){
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() { return nome; }
    public int getMatricula() { return matricula; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aluno)) return false;
        Aluno aluno = (Aluno) o;
        return matricula == aluno.matricula && Objects.equals(nome, aluno.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, matricula);
    }
}