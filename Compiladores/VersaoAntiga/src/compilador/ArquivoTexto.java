package compilador;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Leitura e gravação dos arquivos texto (.txt) editados pelo compilador.
 *
 * <p>Os arquivos gravados usam quebra de linha do Windows (CRLF), para que
 * fiquem compatíveis com o Notepad. Na leitura, arquivos em UTF-8 e em ANSI
 * (windows-1252) são aceitos.</p>
 */
final class ArquivoTexto {

    private static final Charset ANSI = Charset.forName("windows-1252");
    private static final String QUEBRA_DO_WINDOWS = "\r\n";
    private static final int MARCA_DE_ORDEM = 0xFEFF;

    private ArquivoTexto() {
    }

    static String ler(File arquivo) throws IOException {
        String conteudo = decodificar(Files.readAllBytes(arquivo.toPath()));
        if (!conteudo.isEmpty() && conteudo.charAt(0) == MARCA_DE_ORDEM) {
            conteudo = conteudo.substring(1);
        }
        return conteudo.replace(QUEBRA_DO_WINDOWS, "\n").replace("\r", "\n");
    }

    static void gravar(File arquivo, String texto) throws IOException {
        String comQuebrasDoWindows = texto.replace(QUEBRA_DO_WINDOWS, "\n").replace("\n", QUEBRA_DO_WINDOWS);
        Files.writeString(arquivo.toPath(), comQuebrasDoWindows, StandardCharsets.UTF_8);
    }

    private static String decodificar(byte[] bytes) {
        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException naoEstaEmUtf8) {
            return new String(bytes, ANSI);
        }
    }
}
