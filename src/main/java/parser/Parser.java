package parser;

import exception.CommandNotRecogniseException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;

import exception.WrongMachineState;
import trip.TripManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Parser(String userString) throws TravelDiaryException, CommandNotRecogniseException {
        String[] parsedCommand = userString.split(" ", 2);
        this.commandText = parsedCommand[0];
        commandChecker(this.commandText);
        if (parsedCommand.length > 1) {
            this.detail = parsedCommand[1];
        }
        this.convertToHashmap();
    }

    public Map<String, String> getHashmap() {
        return hashmap;
    }

    public void setHashmap(Map<String, String> hashmap) {
        this.hashmap = hashmap;
    }

    public static void commandChecker(String userString) throws CommandNotRecogniseException {
        boolean isValid = false;
        for (String s : COMMAND_ARRAY) {
            if (userString.equals(s)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new CommandNotRecogniseException(userString);
        }
    }

    public void isEmptyDetail(boolean expected) throws TravelDiaryException {
        if ((null == this.detail) != expected) {
            throw new TravelDiaryException();
        }
    }

    public void convertToHashmap() throws TravelDiaryException {
        String[] parts;
        switch (this.commandText) {
        case "view_trip":
        case "view_photo":
        case "bye":
        case "menu":
            isEmptyDetail(true);
            this.hashmap.put("command", this.commandText);
            break;
        case "add_trip":
            isEmptyDetail(false);
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
            isEmptyDetail(false);
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
            isEmptyDetail(false);
            this.hashmap.put("command", this.commandText);
            this.hashmap.put("index", this.detail);
            break;
        default:

        }
    }

    public void execute(TripManager tripManager, int fsmValue) throws TravelDiaryException, NumberFormatException,
            MissingCompulsoryParameter, WrongMachineState {
        this.fsmValue = fsmValue;
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
            case "menu":
                this.fsmValue = 0;
                System.out.println("back to main menu");
                break;
            default:
                throw new WrongMachineState(fsmValue);

            }
        } else if (this.fsmValue == 1) {
            String command = this.getHashmap().get("command");
            if (!Objects.equals(command, "bye") && !Objects.equals(command, "menu")) {
                System.out.println("current trip: " + tripManager.getSelectedTrip() + "\n");
            }
            switch (command) {
            case "bye":
                this.isExit = true;
                break;
            case "add_photo":
                tripManager.getSelectedTrip().album.addPhoto(this.getHashmap().get("filepath"),
                        this.getHashmap().get("photoname"), this.getHashmap().get("caption"),
                        this.getHashmap().get("location"));
                break;
            case "select_photo":
                tripManager.getSelectedTrip().album.selectPhoto(Integer.parseInt(this.getHashmap().get("index")));
                break;
            case "view_photo":
                tripManager.getSelectedTrip().album.viewPhotos();
                break;
            case "delete_photo":
                tripManager.getSelectedTrip().album.deletePhoto(Integer.parseInt(this.getHashmap().get("index")));
                break;
            case "menu":
                this.fsmValue = 0;
                tripManager.getSelectedTrip().album.setSelectedPhoto(null);
                tripManager.setSelectedTrip(null);
                System.out.println("back to main menu");
                break;
            default:
                throw new WrongMachineState(fsmValue);
            }
        }
    }
}
