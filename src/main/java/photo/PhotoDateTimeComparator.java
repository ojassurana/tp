package photo;
import java.util.Comparator;

/**
 * The PhotoDateTimeComparator class implements the Comparator interface to provide a comparison logic
 * for Photo objects based on their datetime values. It ensures that Photo objects are sorted
 * in ascending order of their datetime values.
 */
public class PhotoDateTimeComparator implements Comparator<Photo> {

    /**
     * Compares two Photo objects based on their datetime values.
     * Handles cases where either or both datetime values may be null.
     *
     * @param p1 The first Photo object to be compared.
     * @param p2 The second Photo object to be compared.
     * @return A negative integer, zero, or a positive integer as the datetime of the
     *         first Photo is less than, equal to, or greater than the datetime of the second Photo.
     */
    @Override
    public int compare(Photo p1, Photo p2) {
        // Case: Both photos have null datetime values, they are considered equal.
        if (p1.getDatetime() == null && p2.getDatetime() == null) {
            return 0;
        }
        // Case: The first photo has a null datetime, it should come before the second photo.
        else if (p1.getDatetime() == null) {
            return -1;
        }
        // Case: The second photo has a null datetime, it should come after the first photo.
        else if (p2.getDatetime() == null) {
            return 1;
        }
        // Case: Both photos have non-null datetime values, compare them normally.
        return p1.getDatetime().compareTo(p2.getDatetime());
    }
}
