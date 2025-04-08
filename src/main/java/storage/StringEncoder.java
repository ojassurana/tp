package storage;

/**
 * A utility class for encoding and decoding strings to safely store them in delimited text files.
 * This class provides methods to escape and unescape special characters that might conflict
 * with the storage format used in the Travel Diary application, particularly the delimiter
 * character and other control characters.
 */
public class StringEncoder {
    /**
     * Encodes a string to handle special characters.
     * This method escapes backslashes, pipe characters, and newlines to ensure
     * they don't interfere with the storage format's delimiter system.
     * The escaping sequence is as follows:
     * - Backslash (\) becomes double backslash (\\)
     * - Pipe character (|) becomes backslash+pipe (\|)
     * - Newline (\n) becomes backslash+n (\n)
     *
     * @param input String to encode
     * @return Encoded string with special characters escaped, or empty string if input is null
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
     * This method reverses the encoding process, converting escaped sequences
     * back to their original characters:
     * - Double backslash (\\) becomes backslash (\)
     * - Backslash+pipe (\|) becomes pipe character (|)
     * - Backslash+n (\n) becomes newline (\n)
     * The method handles the characters one by one to properly process escape sequences.
     *
     * @param input String to decode
     * @return Decoded string with original characters restored, or empty string if input is null
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
     * Appends the appropriate character for an escaped sequence.
     * This helper method determines how to interpret a character that follows
     * an escape character (backslash) and appends the correct character to the result.
     * For example, if the escaped character is 'n', it appends a newline character.
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
     * Handles a trailing backslash at the end of the input string.
     * This helper method ensures that if the input string ends with an escape character,
     * it is properly handled by appending a literal backslash to the result.
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
