import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// //
//
public class Main {
    private static final String[] BUILTIN_COMMANDS = { "echo", "exit", "type", "pwd", "cd" };

    public static void main(String[] args) {
        runShell();
    }

    /**
     * Main REPL loop for the shell
     */
    private static void runShell() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String pathEnv = System.getenv("PATH");
            String[] pathDirs = pathEnv != null ? pathEnv.split(":") : new String[0];

            while (true) {
                printPrompt();
                String input = reader.readLine();

                if (input == null) {
                    break; // EOF
                }

                handleCommand(input.trim(), pathDirs);
            }
        } catch (IOException e) {
            print("Error reading input: " + e.getMessage() + "\n");
        }
    }

    /**
     * Parse and execute a command
     */
    private static void handleCommand(String input, String[] pathDirs) {
        if (input.isEmpty()) {
            return;
        }

        String[] tokens = parseInput(input);
        String command = tokens[0];
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, args.length);

        // Check if it's a builtin command
        if (isBuiltinCommand(command)) {
            executeBuiltin(command, args, input, pathDirs);
        } else {
            // Try to execute as external command
            executeExternalCommand(command, args, pathDirs);
        }
    }

    /**
     * Parse input string into tokens (simplified - splits on spaces)
     */
    private static String[] parseInput(String input) {
        return input.split(" ");
    }

    /**
     * Check if a command is a shell builtin
     */
    private static boolean isBuiltinCommand(String command) {
        for (String builtin : BUILTIN_COMMANDS) {
            if (builtin.equals(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Execute a builtin command
     */
    private static void executeBuiltin(String command, String[] args, String fullInput, String[] pathDirs) {
        switch (command) {
            case "echo":
                handleEcho(args, fullInput);
                break;
            case "type":
                handleType(args, pathDirs);
                break;
            case "exit":
                handleExit(args);
                break;
            case "pwd":
                handlePwd();
                break;
            case "cd":
                handleCd(args);
                break;
            default:
                print(command + ": builtin not implemented\n");
        }
    }

    /**
     * Handle the 'echo' command
     */
    private static void handleEcho(String[] args, String fullInput) {
        if (args.length == 0) {
            print("\n");
            return;
        }

        String output;
        if (args.length == 1) {
            output = removeQuotes(args[0]);
        } else {
            // Multiple arguments - use everything after "echo "
            output = fullInput.substring(5); // "echo ".length() == 5
        }

        print(output + "\n");
    }

    /**
     * Handle the 'type' command
     */
    private static void handleType(String[] args, String[] pathDirs) {
        if (args.length != 1) {
            print("type: wrong number of arguments\n");
            return;
        }

        String command = args[0].trim();

        // Check if it's a builtin
        if (isBuiltinCommand(command)) {
            print(command + " is a shell builtin\n");
            return;
        }

        // Check if it exists in PATH
        String executablePath = findExecutableInPath(command, pathDirs);
        if (executablePath != null) {
            print(command + " is " + executablePath + "\n");
        } else {
            print(command + ": not found\n");
        }
    }

    /**
     * Handle the 'exit' command
     */
    private static void handleExit(String[] args) {
        int exitCode = 0;
        if (args.length > 0) {
            try {
                exitCode = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                exitCode = 1;
            }
        }
        System.exit(exitCode);
    }

    /**
     * Handle the 'pwd' command (placeholder for future implementation)
     */
    private static void handlePwd() {
        String currentDir = System.getProperty("user.dir");
        print(currentDir + "\n");
    }

    /**
     * Handle the 'cd' command
     */
    private static void handleCd(String[] args) {
        String targetPath;

        // If no arguments, go to HOME directory
        if (args.length == 0) {
            targetPath = System.getenv("HOME");
            if (targetPath == null) {
                targetPath = System.getProperty("user.home");
            }
        } else {
            targetPath = args[0];

            // Handle ~ expansion
            if (targetPath.equals("~") || targetPath.startsWith("~/")) {
                String home = System.getenv("HOME");
                if (home == null) {
                    home = System.getProperty("user.home");
                }
                if (targetPath.equals("~")) {
                    targetPath = home;
                } else {
                    targetPath = home + targetPath.substring(1);
                }
            }
        }

        // Resolve the path
        Path path = Paths.get(targetPath);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir")).resolve(targetPath).normalize();
        }

        // Check if the directory exists
        if (!Files.exists(path)) {
            print("cd: " + args[0] + ": No such file or directory\n");
            return;
        }

        if (!Files.isDirectory(path)) {
            print("cd: " + args[0] + ": Not a directory\n");
            return;
        }

        // Change the current directory
        System.setProperty("user.dir", path.toAbsolutePath().toString());
    }

    /**
     * Execute an external command from PATH
     */
    private static void executeExternalCommand(String command, String[] args, String[] pathDirs) {
        if (command.contains("/")) {
            Path path = Paths.get(command);
            if (Files.exists(path)) {
                runProcess(command, args);
                return;
            }
        }

        String executablePath = findExecutableInPath(command, pathDirs);

        if (executablePath != null) {
            runProcess(executablePath, args);
        } else {
            print(command + ": command not found\n");
        }
    }

    /**
     * Search for an executable in PATH directories
     */
    private static String findExecutableInPath(String command, String[] pathDirs) {
        for (String dir : pathDirs) {
            String fullPath = dir + "/" + command;
            Path path = Paths.get(fullPath);

            if (Files.exists(path) && Files.isExecutable(path)) {
                return fullPath;
            }
        }
        return null;
    }

    /**
     * Run an external process
     */
    private static void runProcess(String executablePath, String[] args) {
        try {
            // Build command array: executable + arguments
            String[] commandArray = new String[args.length + 1];
            commandArray[0] = executablePath;
            System.arraycopy(args, 0, commandArray, 1, args.length);

            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
            processBuilder.inheritIO(); // Inherit stdin, stdout, stderr
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            print("Error executing command: " + e.getMessage() + "\n");
        }
    }

    /**
     * Remove surrounding quotes from a string if present
     */
    private static String removeQuotes(String str) {
        if (str.startsWith("\"") && str.endsWith("\"") && str.length() >= 2) {
            return str.substring(1, str.length() - 1);
        }
        if (str.startsWith("'") && str.endsWith("'") && str.length() >= 2) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /**
     * Print the shell prompt
     */
    private static void printPrompt() {
        print("$ ");
    }

    /**
     * Print a message to stdout
     */
    private static void print(String message) {
        try {
            System.out.write(message.getBytes());
            System.out.flush();
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }
}
