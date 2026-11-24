package furb.compiladores.lexico;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Semantico implements Constants {

    //registros semanticos
    private StringBuilder codigoObjeto = new StringBuilder();
    private Stack<String> pilhaTipos = new Stack<>();
    private Stack<String> pilhaRotulos = new Stack<>();
    private String operadorRelacional = "";
    private String identificador = "";
    private String desvio = "brfalse "; // isTrueDo sai do bloco quando a expressao e falsa
    private Map<String, Integer> tabelaSimbolos = new LinkedHashMap<>(); // identificador, tamanho
    private Set<String> identificadoresUsados = new HashSet<>();
    private int contadorRotulos = 0;
    private int maiorPilha = 0;

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.println("Acao #" + action + ", Token: " + token);

        switch (action) {
            case 101:
                acao101();
                break;
            case 102:
                acao102(token);
                break;
            case 103:
                acao103(token);
                break;
            case 104:
                acao104();
                break;
            case 105:
                acao105(token);
                break;
            case 106:
                acao106();
                break;
            case 107:
                acao107();
                break;
            case 108:
                acao108();
                break;
            case 109:
                acao109();
                break;
            case 110:
                acao110();
                break;
            case 111:
                acao111();
                break;
            case 112:
                acao112();
                break;
            case 113:
                acao113();
                break;
            case 114:
                acao114();
                break;
            case 115:
                acao115();
                break;
            case 116:
                acao116();
                break;
            case 117:
                acao117(token);
                break;
            case 118:
                acao118();
                break;
            case 119:
                acao119();
                break;
            case 120:
                acao120();
                break;
            case 121:
                acao121(token);
                break;
            case 122:
                acao122();
                break;
            case 123:
                acao123();
                break;
            case 124:
                acao124();
                break;
            case 125:
                acao125();
                break;
            case 126:
                acao126();
                break;
            case 127:
                acao127(token);
                break;
            case 128:
                acao128(token);
                break;
            case 129:
                acao129(token);
                break;
            case 130:
                acao130(token);
                break;
            case 131:
                acao131();
                break;
            case 132:
                acao132();
                break;
            case 133:
                acao133();
                break;
            case 134:
                acao134();
                break;
            case 135:
                acao135();
                break;
            default:
                throw new SemanticError(
                    "Acao semantica nao implementada: " + action
                );
        }
        maiorPilha = Math.max(maiorPilha, pilhaTipos.size());
    }

    private void acao101() {
        codigoObjeto.append(".assembly extern mscorlib {}\n");
        codigoObjeto.append(".assembly _codigo_objeto{}\n");
        codigoObjeto.append(".module _codigo_objeto.exe\n");
        codigoObjeto.append("\n");
        codigoObjeto.append(".class public _codigo_objeto{\n");
        codigoObjeto.append(".method static public void _principal(){\n");
        codigoObjeto.append(".entrypoint\n");
    }

    private void acao102(Token token) throws SemanticError {
        for (String id : tabelaSimbolos.keySet()) {
            if (!identificadoresUsados.contains(id)) {
                throw new SemanticError(
                    id + " identificador declarado, não utilizado",
                    token.getPosition()
                );
            }
        }
        // alem da expressao ficam na pilha o array, o indice e uma constante (acoes 107, 120, 132, 134)
        int tamanhoPilha = maiorPilha + 3;
        if (tamanhoPilha > 8) { // 8 e o padrao da IL
            codigoObjeto.append(".maxstack " + tamanhoPilha + "\n");
        }
        codigoObjeto.append("ret\n");
        codigoObjeto.append("}\n");
        codigoObjeto.append("}");
    }

    private void acao103(Token token) {
        identificador = token.getLexeme();
    }

    private void acao104() {
        tabelaSimbolos.put(identificador, 0);
        codigoObjeto.append(
            ".locals(" + tipoDe(identificador) + " " + identificador + ")\n"
        );
    }

    private void acao105(Token token) {
        String tamanho = semZerosAEsquerda(token.getLexeme());
        tabelaSimbolos.put(identificador, Integer.parseInt(tamanho));
        String tipo = tipoDe(identificador);
        codigoObjeto.append(".locals(" + tipo + "[] " + identificador + ")\n");
        codigoObjeto.append("ldc.i4 " + tamanho + "\n");
        codigoObjeto.append("newarr [mscorlib]System." + classeDe(tipo) + "\n");
        codigoObjeto.append("stloc " + identificador + "\n");
    }

    private void acao106() {
        pilhaTipos.pop();
        if ("int64".equals(tipoDe(identificador))) {
            codigoObjeto.append("conv.i8\n");
        }
        armazenar();
    }

    private void acao107() {
        codigoObjeto.append("ldloc " + identificador + "\n");
    }

    private void acao108() {
        String tipo = tipoDe(identificador);
        codigoObjeto.append("call string [mscorlib]System.Console::ReadLine()\n");
        if (!"string".equals(tipo)) {
            codigoObjeto.append(
                "call " + tipo + " [mscorlib]System." + classeDe(tipo) + "::Parse(string)\n"
            );
        }
        armazenar();
    }

    private void acao109() {
        String tipo = pilhaTipos.pop();
        if ("int64".equals(tipo)) {
            codigoObjeto.append("conv.i8\n");
        }
        codigoObjeto.append(
            "call void [mscorlib]System.Console::Write(" + tipo + ")\n"
        );
    }

    private void acao110() {
        desviar();
    }

    private void acao111() {
        codigoObjeto.append(pilhaRotulos.pop() + ":\n");
    }

    private void acao112() {
        String rotulo = novoRotulo();
        codigoObjeto.append("br " + rotulo + "\n");
        codigoObjeto.append(pilhaRotulos.pop() + ":\n");
        pilhaRotulos.push(rotulo);
    }

    private void acao113() {
        String rotulo = novoRotulo();
        codigoObjeto.append(rotulo + ":\n");
        pilhaRotulos.push(rotulo);
    }

    private void acao114() {
        desviar();
    }

    private void acao115() {
        String rotuloFim = pilhaRotulos.pop();
        String rotuloInicio = pilhaRotulos.pop();
        codigoObjeto.append("br " + rotuloInicio + "\n");
        codigoObjeto.append(rotuloFim + ":\n");
    }

    private void acao116() {
        pilhaTipos.pop();
        pilhaTipos.pop();
        pilhaTipos.push("bool");
        codigoObjeto.append("and\n");
    }

    private void acao117(Token token) throws SemanticError {
        String tipo1 = pilhaTipos.pop();
        String tipo2 = pilhaTipos.pop();
        if (!"bool".equals(tipo1) || !"bool".equals(tipo2)) {
            throw new SemanticError(
                "tipos incompatíveis em operador lógico or",
                token.getPosition()
            );
        }
        pilhaTipos.push("bool");
        codigoObjeto.append("or\n");
    }

    private void acao118() {
        pilhaTipos.push("bool");
        codigoObjeto.append("ldc.i4.1\n");
    }

    private void acao119() {
        pilhaTipos.push("bool");
        codigoObjeto.append("ldc.i4.0\n");
    }

    private void acao120() {
        String tipo = pilhaTipos.pop();
        pilhaTipos.push("bool");
        codigoObjeto.append("ldc.i4.1\n");
        codigoObjeto.append("xor\n");
    }

    private void acao121(Token token) {
        operadorRelacional = token.getLexeme();
    }

    private void acao122() {
        String tipo1 = pilhaTipos.pop();
        pilhaTipos.pop();
        pilhaTipos.push("bool");
        if ("string".equals(tipo1)) {
            // compara as strings e depois compara o resultado com zero
            codigoObjeto.append(
                "call int32 [mscorlib]System.String::Compare(string, string)\n"
            );
            codigoObjeto.append("ldc.i4.0\n");
        }
        switch (operadorRelacional) {
            case "=":
                codigoObjeto.append("ceq\n");
                break;
            case "<>":
                codigoObjeto.append("ceq\n");
                codigoObjeto.append("ldc.i4.1\n");
                codigoObjeto.append("xor\n");
                break;
            case "<":
                codigoObjeto.append("clt\n");
                break;
            case ">":
                codigoObjeto.append("cgt\n");
                break;
        }
    }

    private void acao123() {
        String tipo1 = pilhaTipos.pop();
        String tipo2 = pilhaTipos.pop();
        if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
            pilhaTipos.push("int64");
        } else {
            pilhaTipos.push("float64");
        }
        codigoObjeto.append("add\n");
    }

    private void acao124() {
        String tipo1 = pilhaTipos.pop();
        String tipo2 = pilhaTipos.pop();
        if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
            pilhaTipos.push("int64");
        } else {
            pilhaTipos.push("float64");
        }
        codigoObjeto.append("sub\n");
    }

    private void acao125() {
        String tipo1 = pilhaTipos.pop();
        String tipo2 = pilhaTipos.pop();
        if ("int64".equals(tipo1) && "int64".equals(tipo2)) {
            pilhaTipos.push("int64");
        } else {
            pilhaTipos.push("float64");
        }
        codigoObjeto.append("mul\n");
    }

    private void acao126() {
        String tipo1 = pilhaTipos.pop();
        String tipo2 = pilhaTipos.pop();
        pilhaTipos.push("float64");
        codigoObjeto.append("div\n");
    }

    private void acao127(Token token) {
        String id = token.getLexeme();
        String tipo = tipoDe(id);
        pilhaTipos.push(tipo);
        codigoObjeto.append("ldloc " + id + "\n");
        // no array a conversao e feita na acao 133, depois de carregar o valor
        if ("int64".equals(tipo) && !ehArray(id)) {
            codigoObjeto.append("conv.r8\n");
        }
    }

    private void acao128(Token token) {
        pilhaTipos.push("int64");
        codigoObjeto.append("ldc.i8 " + semZerosAEsquerda(token.getLexeme()) + "\n");
        codigoObjeto.append("conv.r8\n");
    }

    private void acao129(Token token) {
        pilhaTipos.push("float64");
        String valor = semZerosAEsquerda(token.getLexeme()).replace(",", ".");
        codigoObjeto.append("ldc.r8 " + valor + "\n");
    }

    private void acao130(Token token) {
        pilhaTipos.push("string");
        codigoObjeto.append("ldstr " + token.getLexeme() + "\n");
    }

    private void acao131() {
        String tipo = pilhaTipos.pop();
        if ("int64".equals(tipo)) {
            pilhaTipos.push("int64");
        } else {
            pilhaTipos.push("float64");
        }
    }

    private void acao132() {
        String tipo = pilhaTipos.pop();
        if ("int64".equals(tipo)) {
            pilhaTipos.push("int64");
        } else {
            pilhaTipos.push("float64");
        }
        codigoObjeto.append("ldc.i8 -1\n");
        codigoObjeto.append("conv.r8\n");
        codigoObjeto.append("mul\n");
    }

    private void acao133() {
        pilhaTipos.pop(); // tipo do indice
        String tipo = pilhaTipos.peek();
        codigoObjeto.append("conv.i4\n");
        codigoObjeto.append("ldelem." + sufixoDe(tipo) + "\n");
        if ("int64".equals(tipo)) {
            codigoObjeto.append("conv.r8\n");
        }
    }

    private void acao134() {
        pilhaTipos.pop(); // tipo do indice
        codigoObjeto.append("conv.i4\n");
    }

    // acao extra: isFalseDo sai do bloco quando a expressao e verdadeira
    private void acao135() {
        desvio = "brtrue ";
    }

    // guarda no identificador o valor que esta no topo da pilha
    private void armazenar() {
        if (ehArray(identificador)) {
            codigoObjeto.append("stelem." + sufixoDe(tipoDe(identificador)) + "\n");
        } else {
            codigoObjeto.append("stloc " + identificador + "\n");
        }
        identificadoresUsados.add(identificador);
    }

    private boolean ehArray(String id) {
        return tabelaSimbolos.getOrDefault(id, 0) > 0;
    }

    private String tipoDe(String id) {
        if (id.startsWith("i_")) {
            return "int64";
        }
        if (id.startsWith("f_")) {
            return "float64";
        }
        if (id.startsWith("s_")) {
            return "string";
        }
        return "bool";
    }

    private String classeDe(String tipo) {
        switch (tipo) {
            case "int64":
                return "Int64";
            case "float64":
                return "Double";
            case "string":
                return "String";
            default:
                return "Boolean";
        }
    }

    private String sufixoDe(String tipo) {
        switch (tipo) {
            case "int64":
                return "i8";
            case "float64":
                return "r8";
            case "string":
                return "ref";
            default:
                return "i1";
        }
    }

    // o ilasm do Windows le numero com zero a esquerda como octal
    private String semZerosAEsquerda(String numero) {
        return numero.replaceFirst("^0+(?=[0-9])", "");
    }

    // desvia para um rotulo novo, que e resolvido nas acoes 111, 112 e 115
    private void desviar() {
        pilhaTipos.pop();
        String rotulo = novoRotulo();
        codigoObjeto.append(desvio + rotulo + "\n");
        pilhaRotulos.push(rotulo);
        desvio = "brfalse ";
    }

    private String novoRotulo() {
        contadorRotulos++;
        return "rotulo" + contadorRotulos;
    }

    public String getCodigoObjeto() {
        return codigoObjeto.toString();
    }
}
