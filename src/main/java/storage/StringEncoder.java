package storage;

public class StringEncoder {
    /**
     * Encodes a string to handle special characters.
     *
     * @param input String to encode
     * @return Encoded string
     */
    protected static String encodeString(String input) {
        if (input == null) {
            return "";
        }
        String result = input;
        // First escape backslashes to avoid double escaping
        result = result.replace("\\", "\\\\");
        // Then escape the delimiter characters
        result = result.replace("|", "\\|");
        result = result.replace("\n", "\\n");
        return result;
    }

    /**
     * Decodes a string to restore original characters.
     */
    protected static String decodeString(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaped) {
                if (c == '|') {
                    result.append('|');
                } else if (c == 'n') {
                    result.append('\n');
                } else if (c == '\\') {
                    result.append('\\');
                } else {
                    // If not a special character, keep the backslash and the character
                    result.append('\\').append(c);
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                result.append(c);
            }
        }

        // If there's a trailing backslash, add it
        if (escaped) {
            result.append('\\');
        }

        return result.toString();
    }
}
