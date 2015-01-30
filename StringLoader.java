import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class StringLoader {
    public static String loadFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.US_ASCII);
        }
        catch(Exception ex) { return ""; }
    }
}
