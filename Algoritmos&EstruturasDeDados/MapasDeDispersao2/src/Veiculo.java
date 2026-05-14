public class Veiculo {

    private String placa;
    private String proprietario;

    public Veiculo(String placa, String proprietario) {

        this.placa = placa;
        this.proprietario = proprietario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }
}