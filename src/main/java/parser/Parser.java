package parser;

import exception.InvalidCommandException;
import exception.InvalidPhotoFormatException;
import exception.InvalidSelectFormatException;
import exception.InvalidTripFormatException;
import exception.InvalidDeleteFormatException;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static final String[] COMMAND_ARRAY = {"bye", "add_trip", "add_photo", "delete_trip",
                                                  "delete_photo", "view_trip", "view_photo", "select_trip",
                                                  "select_photo"}; // all possible command
    protected String commandText;
    protected String detail;
    protected Map<String, String> hashmap = new HashMap<>();

    public Parser(String userString) throws InvalidCommandException, InvalidTripFormatException,
            InvalidDeleteFormatException, InvalidSelectFormatException, InvalidPhotoFormatException {
        commandChecker(userString);
        String[] parsedCommand = userString.split(" ", 2);
        this.commandText = parsedCommand[0];
        if (parsedCommand.length > 1) {
            this.detail = parsedCommand[1];
        }
        this.checkDetail();
        this.convertToHashmap();
    }

    public Map<String, String> getHashmap() {
        return hashmap;
    }

    public void setHashmap(Map<String, String> hashmap) {
        this.hashmap = hashmap;
    }

    public static void commandChecker(String userString) throws InvalidCommandException {
        boolean isValid = false;
        for (String s : COMMAND_ARRAY) {
            if (userString.startsWith(s + " ") || s.equals("bye") || s.equals("view_trip") || s.equals("view_photo")) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new InvalidCommandException();
        }

    }

    public void checkDetail() throws InvalidTripFormatException, InvalidDeleteFormatException,
            InvalidSelectFormatException, InvalidPhotoFormatException {
        if (this.detail == null) {
            switch (this.commandText) {
            case "add_trip":
                throw new InvalidTripFormatException();
            case "add_photo":
                throw new InvalidPhotoFormatException();
            case "delete_trip":
            case "delete_photo":
                throw new InvalidDeleteFormatException();
            case "select_photo":
            case "select_trip":
                throw new InvalidSelectFormatException();
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
                } else if (part.startsWith("d#")) {
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
}
