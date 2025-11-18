import java.io.*;
import java.nio.file.Paths;

public class Lab11_Ex1 {
    public static void main() {
        System.out.println(Paths.get("").toAbsolutePath());
        try( FileInputStream in = new FileInputStream("input.txt");
             FileOutputStream out = new FileOutputStream("output.txt"))
        {
            byte[] arr = new byte[in.available()];
            in.read(arr, 0, arr.length);
            out.write(arr);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
