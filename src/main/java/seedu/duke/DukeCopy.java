package seedu.duke;

import exception.*;
import parser.Parser;
import trip.TripManager;

import java.util.Scanner;

public class DukeCopy {
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
        String userString;
        Parser parser;
        TripManager tripManager = new TripManager();
        loop:while (true) {
            try {
                userString = in.nextLine();
                parser = new Parser(userString);
                System.out.println(parser.getHashmap().getOrDefault("command","none"));
                switch (parser.getHashmap().get("command")) {
                case "bye":
                    break loop;
                case "view_trip":
                    tripManager.viewTrips();
                    break;
                case "view_photo":
                    // waiting for album
                    break;
                case "select_trip":
                    tripManager.selectTrip(Integer.parseInt(parser.getHashmap().get("index")));
                    break;
                case "add_trip":
                    tripManager.addTrip(parser.getHashmap().get("name"), parser.getHashmap().get("description"), parser.getHashmap().get("location") );
                    break;
                case "select_photo":
                    // waiting for album
                    break;
                case "delete_trip":
                    tripManager.deleteTrip(Integer.parseInt(parser.getHashmap().get("index")));
                    break;
                case "delete_photo":
                    // waiting for album
                    break;
                default:
                    System.out.println(1);
                    break;
                }



            } catch (InvalidTripFormatException | InvalidSelectFormatException | InvalidCommandException |
                     InvalidPhotoFormatException | InvalidDeleteFormatException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Hello " + in.nextLine());
    }
}
