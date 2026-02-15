import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.write("$ ".getBytes());;
            System.out.flush();
            input = reader.readLine();
            String[] array = input.split(" ");
            if (array[0].equals("echo")){
                String retecho = "";
                if (array.length == 2){
                    retecho = array[1];
                    if (retecho.startsWith("\"") && retecho.endsWith("\"")){
                        retecho = retecho.substring(1,retecho.length()-1);
                    }
                    retecho+= "\n";
                    System.out.write(retecho.getBytes());
                    System.out.flush();
                } else if (array.length>2){
                    retecho = input.substring(4,input.length());
                    retecho += "\n";
                    System.out.write(retecho.getBytes());
                    System.out.flush();
                }
            }

            else if(input.length()>=1) {
            	String output = input + ": Unknown command\n";
            	System.out.write(output.getBytes());
            	System.out.flush();
            } else continue;
        }
    }
}