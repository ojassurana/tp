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

/**
 * Handles parsing of user input to convert it into structured command details.
 * This class provides static methods to process input strings into hashmap-based
 * command representations that can be used by the CommandFactory.
 */
public class Parser {
    /** Array of valid command names recognized by the parser */
    public static final String[] COMMAND_ARRAY = {"bye", "close", "add_trip"
            , "add_photo", "delete", "list", "select", "menu", "help"};
    private static final Ui ui = new Ui();

    /**
     * Gets command details from user input via the UI.
     * Reads input from the user, validates it's not empty, and processes it.
     *
     * @return a map containing the processed command details
     * @throws TravelDiaryException if no command is provided
     * @throws InvalidIndexException if an invalid index is provided
     * @throws CommandNotRecogniseException if the command is not recognized
     * @throws ParserException if there is an error during parsing
     */
    public static Map<String, String> getCommandDetails()
            throws TravelDiaryException, InvalidIndexException, CommandNotRecogniseException, ParserException {
        System.out.print("Enter: ");
        String input = ui.readInput().trim();
        if (input.isEmpty()) {
            throw new TravelDiaryException("No command provided. Please enter a command.");
        }
        return processInput(input);
    }

    /**
     * Processes the input string to extract command and arguments.
     * 
     * @param input the raw user input string
     * @return a map containing the processed command details
     * @throws TravelDiaryException if there is a general error
     * @throws InvalidIndexException if an invalid index is provided
     * @throws CommandNotRecogniseException if the command is not recognized
     * @throws ParserException if there is an error during parsing
     */
    public static Map<String, String> processInput(String input)
            throws TravelDiaryException, InvalidIndexException, CommandNotRecogniseException, ParserException {
        String[] tokens = splitCommandAndArguments(input);
        String command = tokens[0];
        String rest = tokens[1];
        return convertToHashmap(command, rest);
    }

    /**
     * Splits the input string into command and arguments.
     * 
     * @param input the raw user input string
     * @return an array containing the command (at index 0) and arguments (at index 1)
     */
    private static String[] splitCommandAndArguments(String input) {
        String[] tokens = input.split("\\s+", 2);
        String command = tokens[0].toLowerCase();
        String rest = tokens.length > 1 ? tokens[1].trim() : "";
        return new String[]{command, rest};
    }

    /**
     * Converts command and arguments into a hashmap with structured details.
     * Different commands are processed according to their specific format requirements.
     * 
     * @param command the command name
     * @param rest the arguments for the command
     * @return a map containing the processed command details
     * @throws TravelDiaryException if there is a general error
     * @throws InvalidIndexException if an invalid index is provided
     * @throws CommandNotRecogniseException if the command is not recognized
     * @throws ParserException if there is an error during parsing
     */
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

    /**
     * Creates a hashmap for simple commands that don't require arguments.
     * 
     * @param command the command name
     * @return a map containing just the command name
     */
    private static Map<String, String> createSimpleCommandMap(String command) {
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        return map;
    }

    /**
     * Creates a hashmap for commands that require an index argument.
     * 
     * @param command the command name
     * @param index the index value as a string
     * @return a map containing the command name and index
     * @throws NullIndexException if the index is missing
     */
    private static Map<String, String> createIndexCommandMap(String command, String index) throws NullIndexException {
        if (index.isEmpty()) {
            throw new NullIndexException();
        }
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        map.put("index", index);
        return map;
    }

    /**
     * Parses the add_trip command arguments to extract name and description.
     * 
     * @param rest the arguments for the add_trip command
     * @return a map containing the command details including name and description
     * @throws TravelDiaryException if there is a general error
     * @throws ParserException if there is an error during parsing
     */
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

    /**
     * Parses the add_photo command arguments to extract filepath, name, and caption.
     * 
     * @param rest the arguments for the add_photo command
     * @return a map containing the command details including filepath, name, and caption
     * @throws TravelDiaryException if there is a general error
     * @throws ParserException if there is an error during parsing
     */
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

    /**
     * Processes the parsed tags into a map of tag names to their values.
     * 
     * @param parts the array of tag parts
     * @param allowedTags the set of allowed tags
     * @return a map of tag names to their values
     * @throws ParserException if there is an error during parsing
     */
    private static Map<String, String> processTags(String[] parts, Set<String> allowedTags)
            throws ParserException {
        Map<String, String> tagsMap = new HashMap<>();
        Set<String> seenTags = new HashSet<>();
        for (String part : parts) {
            Map.Entry<String, String> entry = processTagPart(part, allowedTags, seenTags);
            tagsMap.put(entry.getKey(), entry.getValue());
        }
        return tagsMap;
    }

    /**
     * Processes a single tag part to extract the tag name and value.
     * 
     * @param part the tag part to process
     * @param allowedTags the set of allowed tags
     * @param seenTags the set of tags already seen (to detect duplicates)
     * @return an entry containing the tag name and value
     * @throws ParserException if there is an error during parsing
     */
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

    /**
     * Parses the help command arguments to optionally extract the FSM state.
     * 
     * @param rest the arguments for the help command
     * @return a map containing the command details and optionally the FSM state
     */
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
