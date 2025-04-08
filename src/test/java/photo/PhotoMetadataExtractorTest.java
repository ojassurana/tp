package photo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PhotoMetadataExtractorTest {

    @Test
    void noGpsData(){
        assertThrows(exception.NoGPSMetaDataException.class, () ->
                new PhotoMetadataExtractor("./data/photos/no gps.jpg"));
    }
    @Test
    void extractMetadata(){
        PhotoMetadataExtractor photoMetadataExtractor = assertDoesNotThrow(() -> new PhotoMetadataExtractor("./data/photos/group_photo.jpg"));
        assertEquals(35.693691666666666, photoMetadataExtractor.getLatitude());
        assertEquals(139.701325, photoMetadataExtractor.getLongitude());
        LocalDateTime dateTime = LocalDateTime.parse("2024-05-19T02:14:53");
        assertEquals(dateTime, photoMetadataExtractor.getDatetime());
    }
    @Test
    void noMetadata(){
        assertThrows(exception.NoMetaDataException.class, () ->
                new PhotoMetadataExtractor("./data/photos/clem.jpg"));
    }

    @Test
    void getLocation(){
        PhotoMetadataExtractor photoMetadataExtractor = assertDoesNotThrow(() -> new
                PhotoMetadataExtractor("./data/photos/group_photo.jpg"));
        assertEquals(35.693691666666666, photoMetadataExtractor.getLatitude());
        assertEquals(139.701325, photoMetadataExtractor.getLongitude());
        LocalDateTime dateTime = LocalDateTime.parse("2024-05-19T02:14:53");
        assertEquals(dateTime, photoMetadataExtractor.getDatetime());
        String location = PhotoMetadataExtractor.getLocationFromCoordinates(photoMetadataExtractor.getLatitude(),
                photoMetadataExtractor.getLongitude());
        assertEquals("Tokyo, Japan", location);
    }

}