package furb.compiladores.lexico;

import java.util.Stack;

public class Semantico implements Constants {

    //registros semanticos
    private StringBuilder codigoObjeto = new StringBuilder();
    private Stack<String> pilhaTipos = new Stack<>();

    public void executeAction(int action, Token token) throws SemanticError {
        System.out.println("Acao #" + action + ", Token: " + token);

        switch (action) {
            case 101:
                acao101();
                break;
            case 102:
                acao102();
                break;
            case 109:
                acao109();
                break;
            case 117:
                acao117(token);
                break;
            case 118:
                acao118();
                break;
            case 120:
                acao120();
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
            case 128:
                acao128(token);
                break;
            case 131:
                acao131();
                break;
            case 132:
                acao132();
                break;
            default:
                throw new SemanticError(
                    "Acao semantica nao implementada: " + action
                );
        }
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

    private void acao102() {
        codigoObjeto.append("ret\n");
        codigoObjeto.append("}\n");
        codigoObjeto.append("}");
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

    private void acao120() {
        String tipo = pilhaTipos.pop();
        pilhaTipos.push("bool");
        codigoObjeto.append("ldc.i4.1\n");
        codigoObjeto.append("xor\n");
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

    private void acao128(Token token) {
        pilhaTipos.push("int64");
        codigoObjeto.append("ldc.i8 " + token.getLexeme() + "\n");
        codigoObjeto.append("conv.r8\n");
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

    public String getCodigoObjeto() {
        return codigoObjeto.toString();
    }
}
