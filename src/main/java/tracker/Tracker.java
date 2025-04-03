package tracker;
import photo.Location;
import photo.Photo;
import photo.PhotoDateTimeComparator;
import album.Album;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

public class Tracker {
    public static final int EARTH_RADIUS = 6371;
    private static final Logger logger = Logger.getLogger(Tracker.class.getName());
    public static void sortPhotosByDate(List<Photo> photoList) {
        photoList.sort(new PhotoDateTimeComparator());
    }

    public static double getDist(Photo p1, Photo p2) {
        Location l1 = p1.getLocation();
        Location l2 = p2.getLocation();
        double lat1 = l1.getLatitude();
        double lon1 = l1.getLongitude();
        double lat2 = l2.getLatitude();
        double lon2 = l2.getLongitude();

        double dLatitude = Math.toRadians(lat2 - lat1);
        double dLongitude = Math.toRadians(lon2 - lon1);
        // Formula to calculate distance in km
        double a = Math.sin(dLatitude / 2) * Math.sin(dLatitude/ 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLongitude/ 2) * Math.sin(dLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = Math.round(EARTH_RADIUS * c);
        logger.info(String.format("Distance between %s and %s: %skm", p1.getPhotoName(), p2.getPhotoName(), dist));
        return dist;
    }

    public static List<String> getPeriod(Album album){
        List<LocalDateTime> dateTimes = new ArrayList<>(List.of());
        for (Photo photo : album.getPhotos()){
            dateTimes.add(photo.getDatetime());
        }
        // Find the minimum date-time
        LocalDateTime minDateTime = dateTimes.stream().min(LocalDateTime::compareTo).orElse(null);
        LocalDateTime maxDateTime = dateTimes.stream().max(LocalDateTime::compareTo).orElse(null);
        List<String> minMaxDates = new ArrayList<>(List.of());
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        minMaxDates.add((minDateTime != null) ? minDateTime.format(outputFormatter) : "No Date Available");
        minMaxDates.add((maxDateTime != null) ? maxDateTime.format(outputFormatter) : "No Date Available");
        return minMaxDates;
    }
}
