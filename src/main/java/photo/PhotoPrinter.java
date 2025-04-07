package photo;

import com.drew.imaging.ImageProcessingException;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * PhotoPrinter class creates a PhotoFrame with JLabels based on Photo details.
 * It can display the PhotoFrame in a GUI window.
 * It loads an image, displays its caption, location, and formatted date.
 */
public class PhotoPrinter {

    private static final String locationPinIconPath = "./data/assets/photo_Location_Pin.png";
    private static final Logger logger = Logger.getLogger(PhotoPrinter.class.getName());

    /**
     * Returns a PhotoFrame object with JLabels based on Photo details.
     *
     * @param photo The photo containing image path, caption, extracted location, and datetime.
     * @return PhotoFrame object containing JLabels and JFrame of the printed photo.
     */
    public static PhotoFrame createFrame(Photo photo) throws FileNotFoundException {
        String filePath = photo.getFilePath();
        if (!(new File(filePath).exists())) {
            throw new FileNotFoundException("File does not exist: " + filePath);
        }

        JFrame frame = new JFrame(photo.getPhotoName());
        logger.info("Created JFrame for: " + photo.getPhotoName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(255, 254, 224));

        // Load and resize image
        ImageIcon originalIcon = new ImageIcon(filePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(51, 36, 33), 5));

        // Resize the location pin icon
        ImageIcon locationIcon = new ImageIcon(locationPinIconPath);
        Image scaledLocationImage = locationIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        locationIcon = new ImageIcon(scaledLocationImage);

        // Format the datetime
        LocalDateTime dateTime = photo.getDatetime();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        String formattedDate = dateTime.format(outputFormatter);

        // Create a label for location and datetime
        String locationAndDateText = photo.getLocation() + " | " + formattedDate;
        JLabel locationLabel = new JLabel(locationAndDateText, locationIcon, SwingConstants.CENTER);
        locationLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        locationLabel.setForeground(Color.DARK_GRAY);

        // Caption label
        JLabel captionLabel = new JLabel(photo.getCaption(), SwingConstants.CENTER);
        captionLabel.setFont(new Font("Helvetica", Font.BOLD, 16));

        // Adding labels to Frame
        frame.add(locationLabel, BorderLayout.NORTH);
        frame.add(imageLabel, BorderLayout.CENTER);
        frame.add(captionLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        logger.info(String.format("PhotoFrame instantiated: %s, %s", photo.getCaption(), photo.getLocation()));
        return new PhotoFrame(frame, captionLabel, locationLabel);
    }

    /**
     * Display the Photo in a JFrame window with its JLabels.
     *
     * @param photoFrame photoFrame object containing the JFrame to be displayed.
     */
    public static void display(PhotoFrame photoFrame) {
        JFrame frame = photoFrame.getFrame();
        assert frame != null : "JFrame should not be null";
        frame.setVisible(true);
        logger.info(String.format("Photo displayed: %s", photoFrame.getTitle()));
    }

    public static void closeAllWindows() {
        Window[] windows = Window.getWindows();
        int numOfWindows = windows.length;
        if (numOfWindows == 0) {
            System.out.println("\tAll photos have been closed.");
        } else {
            for (int i = 0; i < numOfWindows; i++) {
                windows[i].dispose(); // Close each open window
                logger.info(String.format("%s photo has been closed.", i + 1));
            }
            System.out.println(String.format("\t%s photo has been closed.", numOfWindows));
        }
    }

    // Updated main method for testing purposes.
    // Note: In production, you would invoke PhotoPrinter from another class rather than using main here.
    public static void main(String[] args) throws TravelDiaryException, ImageProcessingException,
            IOException, NoMetaDataException {
        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH));
        String filePath = "./data/photos/sample1.jpg";
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        // Create Photo using the updated constructor (location is extracted automatically)
        Photo photo = new Photo(filePath, photoName, caption, datetime);

        try {
            // Create PhotoFrame and display it
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);
            PhotoPrinter.display(photoFrame);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
