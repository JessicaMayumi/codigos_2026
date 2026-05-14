public class Main {

    public static void main(String[] args) {

        MapaDispersao<String, Veiculo> mapa =
                new MapaDispersao<>(53);

        mapa.inserir("AIQ-3041",
                new Veiculo("AIQ-3041", "Ana"));

        mapa.inserir("MSE-7521",
                new Veiculo("MSE-7521", "Pedro"));

        mapa.inserir("ZAI-5931",
                new Veiculo("ZAI-5931", "Marta"));

        mapa.inserir("MQO-2241",
                new Veiculo("MQO-2241", "Lucas"));

        String[] placas = {
                "AIQ-3041",
                "MSE-7521",
                "ZAI-5931",
                "MQO-2241"
        };

        for (String placa : placas) {

            Veiculo veiculo = mapa.buscar(placa);

            if (veiculo != null) {

                System.out.println("Placa: "
                        + veiculo.getPlaca());

                System.out.println("Proprietário: "
                        + veiculo.getProprietario());

                System.out.println();
            }
        }

        System.out.println("Fator de carga: "
                + mapa.calcularFatorCarga());
    }
}