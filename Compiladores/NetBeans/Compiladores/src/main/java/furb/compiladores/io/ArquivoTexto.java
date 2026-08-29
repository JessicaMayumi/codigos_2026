package furb.compiladores.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class ArquivoTexto {

    private static final Charset ANSI = Charset.forName("windows-1252");
    private static final String QUEBRA_DO_WINDOWS = "\r\n";
    private static final int MARCA_DE_ORDEM = 0xFEFF;

    private ArquivoTexto() {
    }

    public static String ler(File arquivo) throws IOException {
        String conteudo = decodificar(Files.readAllBytes(arquivo.toPath()));
        if (!conteudo.isEmpty() && conteudo.charAt(0) == MARCA_DE_ORDEM) {
            conteudo = conteudo.substring(1);
        }
        return conteudo.replace(QUEBRA_DO_WINDOWS, "\n").replace("\r", "\n");
    }

    // grava com quebra de linha do Windows para o arquivo abrir certo no Notepad
    public static void gravar(File arquivo, String texto) throws IOException {
        String comQuebrasDoWindows = texto.replace(QUEBRA_DO_WINDOWS, "\n").replace("\n", QUEBRA_DO_WINDOWS);
        Files.write(arquivo.toPath(), comQuebrasDoWindows.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodificar(byte[] bytes) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException e) {
            return new String(bytes, ANSI);   // arquivo salvo em ANSI pelo Notepad
        }
    }
}
