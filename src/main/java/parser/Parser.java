package parser;

import exception.CommandNotRecogniseException;
import exception.TravelDiaryException;


import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static final String[] COMMAND_ARRAY = {"bye", "add_trip", "add_photo", "delete", "view_trip",
                                                  "view_photo", "select", "menu"}; // all possible command
    public boolean isExit = false;
    public int fsmValue = 0;
    protected String commandText;
    protected String detail;
    protected Map<String, String> hashmap = new HashMap<>();

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
        case "delete":
        case "select":
            isEmptyDetail(false);
            this.hashmap.put("command", this.commandText);
            this.hashmap.put("index", this.detail);
            break;
        default:
        }
    }
}
