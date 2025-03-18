package photo;

import exception.MissingCompulsoryParameter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.Locale;

/**
 * PhotoPrinter class creates a PhotoFrame with Jlabels based on Photo details.
 * It can display the PhotoFrame in a GUI window.
 * It loads an image, displays its caption, location, and formatted date.
 */
public class PhotoPrinter {

    private static final String locationPinIconPath = "./data/assets/photo_Location_Pin.png";

    /**
     * Returns a PhotoFrame object with Jlabels based on Photo details.
     *
     * @param photo The photo containing image path, caption, location, and datetime.
     * @return PhotoFrame object containing Jlabels and Jframe of the printed photo.
     */
    public static PhotoFrame createFrame(Photo photo) throws FileNotFoundException {
        String filePath = photo.getFilePath();
        if (!(new File(filePath).exists())) {
            throw new FileNotFoundException("File does not exist: " + filePath);
        }

        JFrame frame = new JFrame(photo.getPhotoName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // LocationDateTime label
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
        return new PhotoFrame(frame, captionLabel, locationLabel);
    }

    /**
     * Display the Photo in a JFrame window with its Jlabels.
     *
     * @param photoFrame photoFrame object containing the Jframe to be displayed.
     */
    public static void display(PhotoFrame photoFrame) {
        JFrame frame = photoFrame.getFrame();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws MissingCompulsoryParameter {
        LocalDateTime datetime = LocalDateTime.parse("2022-12-23 8:23PM",
                DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH));
        String filePath = "./data/photos/sample1.jpg";
        String photoName = "First night in Osaka";
        String caption = "This is a photo of my friends and I in Osaka.";
        String location = "Dotonbori River";
        // Creates Photo
        Photo photo = new Photo(filePath, photoName, caption, location, datetime);

        try {
            // Create PhotoFrame
            PhotoFrame photoFrame = PhotoPrinter.createFrame(photo);
            // Display PhotoFrame
            PhotoPrinter.display(photoFrame);
            photoFrame.closeOperation(); // Exit program when Jframe window is closed
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
