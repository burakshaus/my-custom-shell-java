import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.write("$ ".getBytes());;
            System.out.flush();
            input = reader.readLine();
            String[] arr = input.split(" ");
            //System.out.println(Arrays.toString(arr));
            if(arr.length==2 && arr[0] .equals("exit")  && arr[1].equals("0")) {
            	System.exit(0);
            }else if(input.length()>=1) {
            	String output = input + ": Unknown command\n";
            	System.out.write(output.getBytes());
            	System.out.flush();
            }
        }
    }
}