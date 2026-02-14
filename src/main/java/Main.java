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
            String[] array = input.split(" ");
            boolean wrote = true;
            //System.out.println(Arrays.toString(array));
            if(array.length==2 && array[0] .equals("exit")  && array[1].equals("0")) {
            	System.exit(0);
            }else if(array.length >=2 && array[0].equals("echo") && wrote){
                if (array.length == 2) {
                    String echoString = getEchoString(array[1]);
                    //= (String)array[1] + "\n";
                    System.out.write(echoString.getBytes());
               
                    System.out.flush();
                    wrote = false;
                } else {
                    continue;
                }
            }
            
            else if(input.length()>=1 && wrote) {
            	String output = input + ": Unknown command\n";
            	System.out.write(output.getBytes());
            	System.out.flush();
            }
        }
    }
    static String getEchoString(String string){
    	String returnStr = "";
    	if(!string.startsWith("\"")){
    		return "invalid";
    	}else if(string.startsWith("\"")){
    	for (int i = 1; i < string.length(); i++){
    			returnStr += string.charAt(i);
    		}
    	
    	}else {
    		return "error here";
    	}
    	
    	return returnStr;
    		
    }
}