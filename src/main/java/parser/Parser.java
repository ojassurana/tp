package parser;


import exception.CommandNotRecogniseException;
import exception.InvalidIndexException;
import exception.ParserException;
import exception.TravelDiaryException;
import exception.TagException;
import exception.NullIndexException;
import exception.MissingTagsException;
import ui.Ui;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Parser {
    public static final String[] COMMAND_ARRAY = {"bye", "close", "add_trip"
            , "add_photo", "delete", "list", "select", "menu", "help"};
    private static final Ui ui = new Ui();

    public static Map<String, String> getCommandDetails()
            throws TravelDiaryException, InvalidIndexException, CommandNotRecogniseException, ParserException {
        System.out.print("Enter: ");
        String input = ui.readInput().trim();
        if (input.isEmpty()) {
            throw new TravelDiaryException("No command provided. Please enter a command.");
        }
        return processInput(input);
    }

    public static Map<String, String> processInput(String input)
            throws TravelDiaryException, InvalidIndexException, CommandNotRecogniseException, ParserException {
        String[] tokens = splitCommandAndArguments(input);
        String command = tokens[0];
        String rest = tokens[1];
        return convertToHashmap(command, rest);
    }

    private static String[] splitCommandAndArguments(String input) {
        String[] tokens = input.split("\\s+", 2);
        String command = tokens[0].toLowerCase();
        String rest = tokens.length > 1 ? tokens[1].trim() : "";
        return new String[]{command, rest};
    }

    public static Map<String, String> convertToHashmap(String command, String rest)
            throws TravelDiaryException, NullPointerException, InvalidIndexException, CommandNotRecogniseException,
            ParserException {
        switch (command) {
        case "bye":
        case "list":
        case "close":
        case "menu":
            return createSimpleCommandMap(command);
        case "delete":
        case "select":
            return createIndexCommandMap(command, rest);
        case "add_trip":
            return parseAddTrip(rest);
        case "add_photo":
            return parseAddPhoto(rest);
        case "help":
            return parseHelp(rest);
        default:
            throw new CommandNotRecogniseException(command);
        }
    }

    private static Map<String, String> createSimpleCommandMap(String command) {
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        return map;
    }

    private static Map<String, String> createIndexCommandMap(String command, String index) throws NullIndexException {
        if (index.isEmpty()) {
            throw new NullIndexException();
        }
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        map.put("index", index);
        return map;
    }


    private static Map<String, String> parseAddTrip(String rest) throws TravelDiaryException, ParserException {
        Map<String, String> map = new HashMap<>();
        map.put("command", "add_trip");
        
        // Check if rest is empty and throw a more descriptive exception
        if (rest.isEmpty()) {
            throw new MissingTagsException("add_trip", "n# (name) d# (description).");
        }
        
        String[] parts = rest.split(" (?=[nd]#)");
        Set<String> allowedTags = new HashSet<>(Arrays.asList("n#", "d#"));
        Map<String, String> tagsMap = processTags(parts, allowedTags);
        map.put("name", tagsMap.get("n#"));
        map.put("description", tagsMap.get("d#"));
        if (map.get("name") == null || map.get("description") == null) {
            throw new MissingTagsException("add_trip", "n# (name) d# (description).");
        }
        return map;
    }

    private static Map<String, String> parseAddPhoto(String rest) throws TravelDiaryException, ParserException {
        Map<String, String> map = new HashMap<>();
        map.put("command", "add_photo");
        
        // Check if rest is empty and throw a more descriptive exception
        if (rest.isEmpty()) {
            throw new MissingTagsException("add_photo", "f# (filepath) n# (photoname) c# (caption).");
        }
        
        // Split on spaces before any tag that starts with n, d, c, f or l
        String[] parts = rest.split(" (?=[fnc]#)");
        // Allowed tags now only for f#, n#, and c# (l# is optional)
        Set<String> allowedTags = new HashSet<>(Arrays.asList("f#", "n#", "c#"));
        Map<String, String> tagsMap = processTags(parts, allowedTags);

        // Required tags:
        map.put("filepath", tagsMap.get("f#"));
        map.put("photoname", tagsMap.get("n#"));
        map.put("caption", tagsMap.get("c#"));

        // Optional location tag if provided
        if (tagsMap.containsKey("l#")) {
            System.out.println(tagsMap.get("l#"));
            map.put("location", tagsMap.get("l#"));
        }

        // Only check required tags
        if (map.get("filepath") == null || map.get("photoname") == null || map.get("caption") == null) {
            throw new MissingTagsException("add_photo", "f# (filepath) n# (photoname) c# (caption).");
        }
        return map;
    }


    private static Map<String, String> processTags(String[] parts, Set<String> allowedTags)
            throws  ParserException {
        Map<String, String> tagsMap = new HashMap<>();
        Set<String> seenTags = new HashSet<>();
        for (String part : parts) {
            Map.Entry<String, String> entry = processTagPart(part, allowedTags, seenTags);
            tagsMap.put(entry.getKey(), entry.getValue());
        }
        return tagsMap;
    }

    private static Map.Entry<String, String> processTagPart(String part, Set<String> allowedTags,
                                                            Set<String> seenTags) throws ParserException {
        for (String tag : allowedTags) {
            if (part.startsWith(tag)) {
                if (!seenTags.add(tag)) {
                    throw new TagException("\tDuplicate tag: ", tag);
                }
                String value = part.substring(tag.length()).trim();
                if (value.contains("#")) {
                    throw new TagException("\tUnrecognised tag: ", "#");
                }
                if (value.isEmpty()) {
                    throw new TagException("\tEmpty value provided for tag: ", tag);
                }
                return new AbstractMap.SimpleEntry<>(tag, value);
            }
        }
        
        // If we got here, the part doesn't start with any allowed tag
        // Make a more helpful error message showing what tags are expected
        StringBuilder allowedTagsStr = new StringBuilder();
        for (String tag : allowedTags) {
            allowedTagsStr.append(tag).append(" ");
        }
        throw new TagException("\tUnrecognized tag format. Expected tags: ", allowedTagsStr.toString().trim());
    }

    private static Map<String, String> parseHelp(String rest) {
        Map<String, String> map = new HashMap<>();
        map.put("command", "help");

        // If rest is not empty, try to parse it as a fsm value
        if (!rest.isEmpty()) {
            try {
                int fsm = Integer.parseInt(rest.trim());
                // Only accept 0 or 1 as valid FSM states
                if (fsm == 0 || fsm == 1) {
                    map.put("fsm", String.valueOf(fsm));
                }
            } catch (NumberFormatException e) {
                // If parsing fails, ignore and use default fsm
            }
        }

        return map;
    }
}
