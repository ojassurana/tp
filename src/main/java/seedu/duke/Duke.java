package seedu.duke;


import Exceptions.*;
import trip.Trip;
import parser.Parser;

import java.util.Scanner;


public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("What is your name?");

        Scanner in = new Scanner(System.in);
        String userInput;
        // create scanner loop
        while (true) {
            userInput = in.nextLine();
            System.out.println(1);
            if (userInput.equals("bye")){
                break;
            }
            try {
                Parser parser = new Parser(userInput);
                for (String i : parser.getHashmap().keySet()) {
                    System.out.println("key: " + i + " value: " + parser.getHashmap().get(i));
                }
            } catch (InvalidCommandException | InvalidTripFormatException | InvalidDeleteFormatException
                     | InvalidSelectFormatException | InvalidPhotoFormatException e) {
                System.out.println(2);
            }

        }
        System.out.println("Hello ");
    }
}
