package parser;

import exception.InvalidCommandException;
import exception.InvalidPhotoFormatException;
import exception.InvalidSelectFormatException;
import exception.InvalidTripFormatException;
import exception.InvalidDeleteFormatException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void testAddTrip() {
        try {
            Parser parser = new Parser("add_trip n# Japan Trip d# Skiing in Hokkaido l# Japan");
            assertEquals("add_trip", parser.getHashmap().get("command"));
            assertEquals("Skiing in Hokkaido", parser.getHashmap().get("description"));
            assertEquals("Japan", parser.getHashmap().get("location"));
            assertEquals("Japan Trip", parser.getHashmap().get("name"));
        } catch (InvalidTripFormatException | InvalidSelectFormatException | InvalidCommandException |
                 InvalidPhotoFormatException | InvalidDeleteFormatException e) {
            System.out.println("error encountered");
        }
    }

    @Test
    void testAddPhoto() {
        try {
            String datetime = "2022-12-23 8:23PM";
            String filePath = "./data/photos/sample1.jpg";
            String photoName = "First night in Osaka";
            String caption = "This is a photo of my friends and I in Osaka.";
            String location = "Dotonbori River";

            Parser parser = new Parser("add_photo d# 2022-12-23 8:23PM f# ./data/photos/sample1.jpg " +
                    "n# First night in Osaka c# Skiing in Hokkaido c# This is a " +
                    "photo of my friends and I in Osaka. l# Dotonbori River");
            assertEquals("./data/photos/sample1.jpg", parser.getHashmap().get("filepath"));
            assertEquals("This is a photo of my friends and I in Osaka.", parser.getHashmap().get("caption"));
            assertEquals("Dotonbori River", parser.getHashmap().get("location"));
            assertEquals("First night in Osaka", parser.getHashmap().get("photoname"));
            assertEquals("add_photo", parser.getHashmap().get("command"));
        } catch (InvalidTripFormatException | InvalidSelectFormatException | InvalidCommandException |
                 InvalidPhotoFormatException | InvalidDeleteFormatException e) {
            System.out.println("error encountered");
        }
    }
}
