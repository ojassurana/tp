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
     *
     * @param input String to decode
     * @return Decoded string
     */
    protected static String decodeString(String input) {
        if (input == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (!escaped && c == '\\') {
                escaped = true;
                continue;
            }

            if (escaped) {
                appendEscapedCharacter(result, c);
                escaped = false;
                continue;
            }

            // Normal character case
            result.append(c);
        }

        // Handle trailing backslash if present
        appendTrailingBackslash(result, escaped);

        return result.toString();
    }

    /**
     * Appends the appropriate character for an escaped sequence
     *
     * @param builder StringBuilder to append to
     * @param c The character after the escape character
     */
    private static void appendEscapedCharacter(StringBuilder builder, char c) {
        switch (c) {
        case '|':
            builder.append('|');
            break;
        case 'n':
            builder.append('\n');
            break;
        case '\\':
            builder.append('\\');
            break;
        default:
            // If not a special character, keep the backslash and the character
            builder.append('\\').append(c);
            break;
        }
    }

    /**
     * Handles a trailing backslash at the end of the input string
     *
     * @param builder StringBuilder to append to
     * @param escaped Whether the last character processed was an escape character
     */
    private static void appendTrailingBackslash(StringBuilder builder, boolean escaped) {
        if (escaped) {
            builder.append('\\');
        }
    }
}
