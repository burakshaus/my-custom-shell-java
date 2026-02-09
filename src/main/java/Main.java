import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("$ ");
        System.out.flush(); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while ((input = reader.readLine()) != null) {
            System.out.print("$ ");
            System.out.flush();
        }
    }
}