package photo;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.logging.Logger;
/**
 * PhotoPrinter class creates a PhotoFrame with JLabels based on Photo details.
 * It can display the PhotoFrame in a GUI window.
 * It loads an image, displays its caption, location, and formatted date.
 */
public class PhotoPrinter {

    private static final String locationPinIconPath = "assets/photo_Location_Pin.png";
    private static final Logger logger = Logger.getLogger(PhotoPrinter.class.getName());

    /**
     * Returns a PhotoFrame object with JLabels based on Photo details.
     *
     * @param photo The photo containing image path, caption, extracted location, and datetime.
     * @return PhotoFrame object containing JLabels and JFrame of the printed photo.
     */
    public static PhotoFrame createFrame(Photo photo) throws FileNotFoundException {
        assert photo != null : "Photo object cannot be null";
        String filePath = photo.getFilePath();
        assert filePath != null && !filePath.isEmpty() : "Photo file path cannot be null or empty";

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

        // Assert image icon is correctly loaded
        assert originalIcon.getIconWidth() > 0 : "Image icon width must be greater than 0";
        assert originalIcon.getIconHeight() > 0 : "Image icon height must be greater than 0";

        // Resize the location pin icon
        ImageIcon locationIcon = new ImageIcon(locationPinIconPath);
        Image scaledLocationImage = locationIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        locationIcon = new ImageIcon(scaledLocationImage);

        // Format the datetime
        LocalDateTime dateTime = photo.getDatetime();
        assert dateTime != null : "Photo datetime cannot be null";
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        String formattedDate = dateTime.format(outputFormatter);

        // Create a label for location and datetime
        String locationAndDateText = photo.getLocation() + " | " + formattedDate;
        JLabel locationLabel = new JLabel(locationAndDateText, locationIcon, SwingConstants.CENTER);
        locationLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        locationLabel.setForeground(Color.DARK_GRAY);

        // Caption label
        JLabel captionLabel = new JLabel(photo.getCaption(), SwingConstants.CENTER);
        assert captionLabel.getText() != null &&
                !captionLabel.getText().isEmpty() : "Caption label text cannot be null or empty";

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
        assert photoFrame != null : "PhotoFrame object cannot be null";
        JFrame frame = photoFrame.getFrame();
        assert frame != null : "JFrame should not be null";
        frame.setVisible(true);
        logger.info(String.format("Photo displayed: %s", photoFrame.getTitle()));
    }

    /**
     * Closes all open photo windows in the application.
     * tf there are no open windows,
     * it outputs a message indicating that all photos have already been closed.
     * It also logs the details of each closed window.
     *
     * Assertions:
     * - The number of windows (`numOfWindows`) must be non-negative.
     *
     * Outputs:
     * - Logs the closure of each window.
     * - Prints a message to the console indicating the total number of windows that were closed.
     */
    public static void closeAllWindows() {
        Window[] windows = Window.getWindows();
        int numOfWindows = windows.length;
        assert numOfWindows >= 0 : "Number of windows cannot be negative";
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
}
