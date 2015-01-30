import java.io.StringWriter;
import java.io.FileReader;
import java.util.Scanner;

public class StringLoader {
   public static String loadFile(String fileName) {
      FileReader in = null;
      try {
         try {
            in = new FileReader(fileName);
            Scanner scan = new Scanner(in);
            StringWriter buf = new StringWriter();
            while (scan.hasNextLine()) {
               buf.append(scan.nextLine());
               buf.append('\n');
            }
            return buf.toString();
         } finally {
            if (in != null)
               in.close();
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
public static void main(String[] args) {
   String x = loadFile("Player.java");
System.out.println(x);
}
}
