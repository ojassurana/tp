package ui;

import java.util.Scanner;
import parser.Parser;
import java.io.File;
import java.io.FileNotFoundException;

public class Ui {
    private static final String SEPARATOR_LINE = "____________________________________________________________";
    private static final int PADDING = 25;
    private static final String FILE_PATH = "./data/assets/van.txt";
    public static final String[] COMMAND_ARRAY = Parser.COMMAND_ARRAY;
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readInput() {

        return scanner.nextLine();
    }

    public void showWelcome() {
        System.out.println("Welcome to your Travel Diary Management System!");
        System.out.println("Capture and manage your travel memories with ease!\n");
        System.out.println("Please type \"help\" if you need instructions \n");
        this.showLogo();
    }

    public void showLine() {
        System.out.println(SEPARATOR_LINE);
    }

    public void showToUser(String message) {
        System.out.println(message);
    }

    public void showFsmState(int fsmState) {
        if ((fsmState == 0)) {
            this.showTripPage();
        } else {
            this.showPhotoPage();
        }
    }

    public void showTripPage() {
        System.out.println(SEPARATOR_LINE);
        System.out.println(" ".repeat(PADDING) + "|Trip Page|");
    }

    public void showPhotoPage() {
        System.out.println(SEPARATOR_LINE);
        System.out.println(" ".repeat(PADDING) + "|Photo Page|");
    }

    public void showLogo() {
        try {
            File file = new File(FILE_PATH);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            // Handle the exception if the file is not found
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public void close() {
        scanner.close();
    }
}
