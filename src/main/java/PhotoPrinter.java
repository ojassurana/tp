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
import java.awt.GraphicsEnvironment;

/**
 * PhotoPrinter class displays photo details in a GUI window.
 * It loads an image, displays its caption, location, and formatted date.
 */
public class PhotoPrinter {
    private static final String locationPinIconPath = "./data/assets/photo_Location_Pin.png";

    /**
     * Display the photo in a JFrame window.
     *
     * @param photo The photo containing image path, caption, location, and datetime to be displayed.
     * @return PhotoFrame object containing Jlabels and Jframe of the printed photo.
     */
    public static PhotoFrame print(Photo photo) throws FileNotFoundException {
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
        if (!GraphicsEnvironment.isHeadless()) {
            // Frame is not displayed if running in headless testing environment
            frame.setVisible(true);
        }

        return new PhotoFrame(frame, captionLabel, locationLabel);
    }
}
