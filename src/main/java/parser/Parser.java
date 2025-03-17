package parser;

import exception.TravelDiaryException;

import trip.TripManager;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static final String[] COMMAND_ARRAY = {"bye", "add_trip", "add_photo", "delete_trip",
                                                  "delete_photo", "view_trip", "view_photo", "select_trip",
                                                  "select_photo", "menu"}; // all possible command
    public boolean isExit = false;
    public int fsmValue = 0;
    protected String commandText;
    protected String detail;
    protected Map<String, String> hashmap = new HashMap<>();


    public Parser() {
    }

    public Parser(String userString) throws TravelDiaryException {
        commandChecker(userString);
        String[] parsedCommand = userString.split(" ", 2);
        this.commandText = parsedCommand[0];
        if (parsedCommand.length > 1) {
            this.detail = parsedCommand[1];
        }
        this.checkEmpty();
        this.convertToHashmap();
    }

    public Map<String, String> getHashmap() {
        return hashmap;
    }

    public void setHashmap(Map<String, String> hashmap) {
        this.hashmap = hashmap;
    }

    public static void commandChecker(String userString) throws TravelDiaryException {
        boolean isValid = false;
        for (String s : COMMAND_ARRAY) {
            if (userString.startsWith(s + " ") || s.equals("bye") || s.equals("view_trip")
                    || s.equals("view_photo") || s.equals("menu")) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new TravelDiaryException();
        }

    }

    public void checkEmpty() throws TravelDiaryException {
        if (this.detail == null) {
            switch (this.commandText) {
            case "add_trip":
                throw new TravelDiaryException();
            case "add_photo":
                throw new TravelDiaryException();
            case "delete_trip":
            case "delete_photo":
                throw new TravelDiaryException();
            case "select_photo":
            case "select_trip":
                throw new TravelDiaryException();
            default:
            }
        }
    }

    public void convertToHashmap() {
        String[] parts;
        switch (this.commandText) {
        case "view_trip":
        case "view_photo":
        case "bye":
        case "menu":
            this.hashmap.put("command", this.commandText);
            break;
        case "add_trip":
            this.hashmap.put("command", this.commandText);
            parts = this.detail.split(" (?=[ndl]#)");
            for (String part : parts) {
                if (part.startsWith("n#")) {
                    this.hashmap.put("name", part.substring(2).trim());
                } else if (part.startsWith("d#")) {
                    this.hashmap.put("description", part.substring(2).trim());
                } else if (part.startsWith("l#")) {
                    this.hashmap.put("location", part.substring(2).trim());
                }
            }

            break;
        case "add_photo":
            this.hashmap.put("command", this.commandText);
            parts = this.detail.split(" (?=[ndlcf]#)");
            for (String part : parts) {
                if (part.startsWith("n#")) {
                    this.hashmap.put("photoname", part.substring(2).trim());
                } else if (part.startsWith("f#")) {
                    this.hashmap.put("filepath", part.substring(2).trim());
                } else if (part.startsWith("l#")) {
                    this.hashmap.put("location", part.substring(2).trim());
                } else if (part.startsWith("c#")) {
                    this.hashmap.put("caption", part.substring(2).trim());
                }
            }
            break;
        case "delete_trip":
        case "delete_photo":
        case "select_trip":
        case "select_photo":
            this.hashmap.put("command", this.commandText);
            this.hashmap.put("index", this.detail);
            break;
        default:

        }
    }

    public void execute(TripManager tripManager, int fsmValue) throws TravelDiaryException {
        this.fsmValue = fsmValue;
        System.out.println(fsmValue);
        if (this.fsmValue == 0) {
            switch (this.getHashmap().get("command")) {
            case "bye":
                this.isExit = true;
                break;
            case "view_trip":
                tripManager.viewTrips();
                break;
            case "select_trip":
                tripManager.selectTrip(Integer.parseInt(this.getHashmap().get("index")));
                this.fsmValue = 1;
                break;
            case "add_trip":
                tripManager.addTrip(this.getHashmap().get("name"),
                        this.getHashmap().get("description"), this.getHashmap().get("location"));
                break;
            case "delete_trip":
                tripManager.deleteTrip(Integer.parseInt(this.getHashmap().get("index")));
                break;
            default:
                throw new TravelDiaryException();
            }
        } else if (this.fsmValue == 1) {
            String command = this.getHashmap().get("command") + "";
            switch (command) {
            case "bye":
                this.isExit = true;
                break;
            case "add_photo":
                tripManager.getSelectedTrip().album.addPhoto(this.getHashmap().get("filepath"),
                        this.getHashmap().get("photoname"), this.getHashmap().get("photoname"),
                        this.getHashmap().get("location"));
                break;
            case "select_photo":
                // waiting for album
                tripManager.getSelectedTrip().album.selectPhoto(Integer.parseInt(this.getHashmap().get("index")));
                break;
            case "view_photo":
                tripManager.getSelectedTrip().album.viewPhotos();
                // waiting for album
                break;
            case "delete_photo":
                tripManager.getSelectedTrip().album.deletePhoto(Integer.parseInt(this.getHashmap().get("index")));

                break;
            case "menu":
                this.fsmValue = 0;
                break;
            default:
                throw new TravelDiaryException();
            }
        }
    }
}
