package ui;
import java.io.FileNotFoundException;
import java.util.Scanner;
import parser.Parser;
import java.io.InputStream;
/**
 * Handles all user interface operations in the Travel Diary application.
 * This class is responsible for displaying information to the user and
 * collecting user input through command-line interface.
 */
public class Ui {
    /** Separator line used for UI formatting */
    private static final String SEPARATOR_LINE = "____________________________________________________________";
    
    /** Padding value for UI formatting */
    private static final int PADDING = 25;
    
    /** File path for the ASCII art logo */
    private static final String FILE_PATH = "assets/van.txt";
    
    /** Array of valid command names from the Parser */
    public static final String[] COMMAND_ARRAY = Parser.COMMAND_ARRAY;
    
    /** Scanner object for reading user input */
    private final Scanner scanner;

    /**
     * Constructs a new UI object with a scanner for input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the input string entered by the user
     */
    public String readInput() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message and logo when the application starts.
     */
    public void showWelcome() {
        System.out.println("Welcome to your Travel Diary Management System!");
        System.out.println("Capture and manage your travel memories with ease!\n");
        System.out.println("Please type \"help\" if you need instructions \n");
        this.showLogo();
    }

    /**
     * Displays a separator line for UI formatting.
     */
    public void showLine() {
        System.out.println(SEPARATOR_LINE);
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    public void showToUser(String message) {
        System.out.println(message);
    }

    /**
     * Displays the appropriate UI based on the current FSM state.
     *
     * @param fsmState the current finite state machine state
     *        (0 for Trip Page, 1 for Photo Page)
     */
    public void showFsmState(int fsmState) {
        if ((fsmState == 0)) {
            this.showTripPage();
        } else {
            this.showPhotoPage();
        }
    }

    /**
     * Displays the Trip Page header.
     * This is shown when the application is in the main menu state (FSM state 0).
     */
    public void showTripPage() {
        System.out.println(SEPARATOR_LINE);
        System.out.println(" ".repeat(PADDING) + "|Trip Page|");
    }

    /**
     * Displays the Photo Page header.
     * This is shown when a trip is selected and the application is in 
     * the photo management state (FSM state 1).
     */
    public void showPhotoPage() {
        System.out.println(SEPARATOR_LINE);
        System.out.println(" ".repeat(PADDING) + "|Photo Page|");
    }

    /**
     * Displays the ASCII art logo from a file.
     * If the file cannot be found, displays an error message.
     */
    public void showLogo() {
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(FILE_PATH);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: "  + FILE_PATH);
            }

            // Directly read the InputStream and print its content using Scanner
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Closes the scanner when the application is shutting down.
     */
    public void close() {
        scanner.close();
    }
}
