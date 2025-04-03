import java.util.Comparator;
import photo.Photo;

public class PhotoDateTimeComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo p1, Photo p2) {
        if (p1.getDatetime() == null && p2.getDatetime() == null) {
            return 0;
        } else if (p1.getDatetime() == null) {
            return -1;
        } else if (p2.getDatetime() == null) {
            return 1;
        }
        return p1.getDatetime().compareTo(p2.getDatetime());
    }
}