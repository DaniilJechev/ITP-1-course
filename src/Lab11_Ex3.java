import java.io.*;
import java.util.Scanner;


public class Lab11_Ex3 {
    public static void main() {
        try (Scanner in = new Scanner(new File("input.txt"))) {
            int num1 = in.nextInt();
            int num2 = in.nextInt();
            float res = (float) num1 / num2;
            System.out.printf("%.2f%n", res);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}