package furb.compiladores.lexico;

public interface ParserConstants
{
    int START_SYMBOL = 38;

    int FIRST_NON_TERMINAL    = 38;
    int FIRST_SEMANTIC_ACTION = 77;

    int[][] PARSER_TABLE =
    {
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1,  5,  5,  5,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1,  7,  7,  7,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1,  8,  8,  8,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 11, -1, -1, -1, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 18, 18, 18, 18, -1, -1, -1, -1, -1, 18, 18, -1, -1, -1, -1, -1, 18, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 20, 20, 20, 20, -1, -1, -1, -1, -1, 20, 20, -1, -1, -1, -1, -1, 20, -1, 20, -1, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 13, 13, 13, 13, -1, -1, -1, -1, -1, 16, 14, -1, -1, -1, -1, -1, 15, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, 21, -1, -1, 22, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1 },
        { -1, -1, 23, 23, 23, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 31, 31, 31, 31, -1, -1, -1, -1, -1, 31, 31, 32, -1, -1, -1, -1, 31, -1, 31, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 33, 33, 33, 33, -1, -1, -1, -1, -1, 33, 33, -1, 34, -1, -1, -1, 33, -1, 33, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1,  1,  2,  3,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 44, 44, 44, 44, 44, 44, 44, -1, 44, -1, -1, -1, -1, -1, 44, -1, -1, 44, -1, -1, -1, -1, 44, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 38, 38, 38, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 41, 41, 41, 41, 41, 41, 41, -1, 41, -1, -1, -1, -1, -1, 41, -1, -1, 41, -1, -1, -1, -1, 41, -1, -1, -1, 41, 41, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, -1, -1, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 48, 48, 48, 48, 48, 48, 48, -1, 50, -1, -1, -1, -1, -1, 51, -1, -1, 49, -1, -1, -1, -1, 48, -1, -1, -1, 48, 48, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 52, 52, 52, 52, 52, 52, 52, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 52, -1, -1, -1, 52, 52, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 53, 53, 53, 53, -1, -1, -1, 53, -1, 53, 53, -1, -1, -1, -1, 53, 53, -1, 53, 53, -1, 53, -1, 53, -1, 53, -1, -1, -1, -1, -1, 54, 54, 54, 54 },
        { -1, -1, 45, 45, 45, 45, -1, -1, -1, 46, -1, 45, 45, -1, -1, -1, -1, 47, 45, -1, 45, 45, -1, 45, -1, 45, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 55, 57, 58, 56 },
        { -1, -1, 59, 59, 59, 59, 59, 59, 59, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 59, -1, -1, -1, 59, 59, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 60, 60, 60, 60, -1, -1, -1, 60, -1, 60, 60, -1, -1, -1, -1, 60, 60, -1, 60, 60, -1, 60, -1, 60, -1, 60, 61, 62, -1, -1, -1, 60, 60, 60, 60 },
        { -1, -1, 63, 63, 63, 63, 63, 63, 63, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 63, -1, -1, -1, 63, 63, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 64, 64, 64, 64, -1, -1, -1, 64, -1, 64, 64, -1, -1, -1, -1, 64, 64, -1, 64, 64, -1, 64, -1, 64, -1, 64, 64, 64, 65, 66, -1, 64, 64, 64, 64 },
        { -1, -1, 67, 67, 67, 67, 68, 69, 70, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 71, -1, -1, -1, 72, 73, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 74, 75, 76, 77, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 78, 78, 78, 78, -1, -1, -1, 78, -1, 78, 78, -1, -1, -1, -1, 78, 78, -1, 78, 78, 79, 78, -1, 78, -1, 78, 78, 78, 78, 78, -1, 78, 78, 78, 78 }
    };

    int[][] PRODUCTIONS = 
    {
        {  16, 178,  39,  27,  44,  28, 179 },
        {   3 },
        {   4 },
        {   5 },
        {   6 },
        {  40 },
        {   0 },
        {  41 },
        {  59, 180,  43,  42 },
        {   0 },
        {  22,  41 },
        {  23,   7, 182,  24 },
        { 181 },
        {  48 },
        {  49 },
        {  50 },
        {  51 },
        {  57 },
        {  46,  45 },
        {   0 },
        {  44 },
        { 184,  23,  60,  24, 211 },
        {   0 },
        {  59, 180,  47,  33,  60, 183 },
        {  13,  25,  61,  26 },
        {  19,  25,  63,  26 },
        {  12,  25,  60,  26,  52 },
        { 187,  55,  53, 188 },
        { 212, 187,  56,  54, 188 },
        {  15,  27,  44,  28 },
        {  14,  27,  44,  28 },
        {   0 },
        { 189,  56 },
        {   0 },
        { 189,  55 },
        { 190,  21,  25,  60,  26,  58 },
        { 191,  55, 192 },
        { 212, 191,  56, 192 },
        {  59, 180,  47, 185,  62 },
        {  22,  61 },
        {   0 },
        {  60, 186,  64 },
        {   0 },
        {  22,  63 },
        {  65,  68 },
        {   0 },
        {  10,  65, 193,  68 },
        {  18,  65, 194,  68 },
        {  66 },
        {  20, 195 },
        {  11, 196 },
        {  17,  65, 197 },
        {  70,  67 },
        {   0 },
        {  69, 198,  70, 199 },
        {  34 },
        {  37 },
        {  35 },
        {  36 },
        {  72,  71 },
        {   0 },
        {  29,  72, 200,  71 },
        {  30,  72, 201,  71 },
        {  74,  73 },
        {   0 },
        {  31,  74, 202,  73 },
        {  32,  74, 203,  73 },
        {  75, 204,  76 },
        {   7, 205 },
        {   8, 206 },
        {   9, 207 },
        {  25,  60,  26 },
        {  29,  74, 208 },
        {  30,  74, 209 },
        {   3 },
        {   4 },
        {   5 },
        {   6 },
        {   0 },
        {  23,  60,  24, 210 }
    };

    String[] PARSER_ERROR =
    {
        "",
        "esperado EOF", // Era esperado fim de programa
        "esperado palavra reservada", // Era esperado p_res
        "esperado identificador", // Era esperado id_int
        "esperado identificador", // Era esperado id_float
        "esperado identificador", // Era esperado id_string
        "esperado identificador", // Era esperado id_bool
        "esperado constante_int", // Era esperado c_int
        "esperado constante_float", // Era esperado c_float
        "esperado constante_string", // Era esperado c_string
        "esperado and", // Era esperado and
        "esperado false", // Era esperado false
        "esperado if", // Era esperado if
        "esperado in", // Era esperado in
        "esperado isFalseDo", // Era esperado isfalsedo
        "esperado isTrueDo", // Era esperado istruedo
        "esperado module", // Era esperado module
        "esperado not", // Era esperado not
        "esperado or", // Era esperado or
        "esperado out", // Era esperado out
        "esperado true", // Era esperado true
        "esperado while", // Era esperado while
        "esperado ,", // Era esperado \",\"
        "esperado [", // Era esperado \"[\"
        "esperado ]", // Era esperado \"]\"
        "esperado (", // Era esperado \"(\"
        "esperado )", // Era esperado \")\"
        "esperado {", // Era esperado \"{\"
        "esperado }", // Era esperado \"}\"
        "esperado +", // Era esperado \"+\"
        "esperado -", // Era esperado \"-\"
        "esperado *", // Era esperado \"*\"
        "esperado /", // Era esperado \"/\"
        "esperado <-", // Era esperado \"<-\"
        "esperado =", // Era esperado \"=\"
        "esperado <", // Era esperado \"<\"
        "esperado >", // Era esperado \">\"
        "esperado <>", // Era esperado \"<>\"
        "esperado module", // <def_module> inválido
        "esperado identificador {", // <dec_vars> inválido
        "esperado identificador {", // <lista_vars> inválido
        "esperado identificador", // <lista_vars_aux> inválido
        "esperado , {", // <lista_vars_aux_2> inválido
        "esperado , [ {", // <vetor_aux> inválido
        "esperado identificador if in out while", // <lista_cmd> inválido
        "esperado identificador if in out while }", // <lista_cmd_2> inválido
        "esperado identificador if in out while", // <cmd> inválido
        "esperado expressao", // <exp_opcional> inválido
        "esperado identificador", // <cmd_atribuicao> inválido
        "esperado in", // <cmd_entrada> inválido
        "esperado out", // <cmd_saida> inválido
        "esperado if", // <cmd_selecao> inválido
        "esperado isFalseDo isTrueDo", // <cmd_selecao_2> inválido
        "esperado identificador if in isFalseDo out while }", // <false_opcional> inválido
        "esperado identificador if in isTrueDo out while }", // <true_opcional> inválido
        "esperado isTrueDo", // <is_true> inválido
        "esperado isFalseDo", // <is_false> inválido
        "esperado while", // <cmd_repeticao> inválido
        "esperado isFalseDo isTrueDo", // <cmd_repeticao_2> inválido
        "esperado identificador", // <identificador> inválido
        "esperado expressao", // <expressao> inválido
        "esperado identificador", // <lista_identificadores> inválido
        "esperado , )", // <lista_identificadores_2> inválido
        "esperado expressao", // <lista_expressoes> inválido
        "esperado , )", // <lista_expressoes_2> inválido
        "esperado expressao", // <termo_logico> inválido
        "esperado expressao", // <relacional> inválido
        "esperado expressao", // <relacional_> inválido
        "esperado expressao", // <expressao_> inválido
        "esperado = < > <>", // <operador_relacional> inválido
        "esperado expressao", // <aritmetica> inválido
        "esperado expressao", // <aritmetica_> inválido
        "esperado expressao", // <termo_aritmetico> inválido
        "esperado expressao", // <termo_aritmetico_> inválido
        "esperado expressao", // <fator> inválido
        "esperado identificador", // <identificador_expressao> inválido
        "esperado expressao" // <vetor> inválido
    };
}
