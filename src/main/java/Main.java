import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.write("$ ".getBytes());
            ;
            System.out.flush();
            input = reader.readLine();
            String[] array = input.split(" ");
            if (array[0].equals("echo")) {
                String retecho = "";
                if (array.length == 2) {
                    retecho = array[1];
                    if (retecho.startsWith("\"") && retecho.endsWith("\"")) {
                        retecho = retecho.substring(1, retecho.length() - 1);
                    }
                    retecho += "\n";
                    System.out.write(retecho.getBytes());
                    System.out.flush();
                } else if (array.length > 2) {
                    retecho = input.substring(4, input.length());
                    retecho += "\n";
                    System.out.write(retecho.getBytes());
                    System.out.flush();
                }
            } else if (array[0].equals("type") && array.length == 2) {
                String retString = "";
                array[1] = array[1].trim();
                if (array[1].equals("echo")) {
                    System.out.write("echo is a shell builtin\n".getBytes());
                    System.out.flush();
                } else if (array[1].equals("exit")) {
                    System.out.write("exit is a shell builtin\n".getBytes());
                    System.out.flush();
                } else if (array[1].equals("type")) {
                    System.out.write("type is a shell builtin\n".getBytes());
                    System.out.flush();
                } else {
                    retString += array[1];
                    retString += ": not found\n";
                    System.out.write(retString.getBytes());
                    System.out.flush();
                }

            }else if (input.length() >= 1) {
                String output = input + ": Unknown command\n";
                System.out.write(output.getBytes());
                System.out.flush();
            } else continue;
        }
    }
}

