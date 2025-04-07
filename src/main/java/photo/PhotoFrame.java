package photo;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Represents a photo frame with labels for caption and location/date,
 * and an associated JFrame to display the photo information.
 * This class encapsulates the UI components needed to represent a photo frame,
 * including labels for the photo's caption and location/date, and provides
 * methods to access and manipulate these components.
 */
public class PhotoFrame {

    private JFrame frame;
    private JLabel captionLabel;
    private JLabel locationDateLabel;

    /**
     * Constructs a PhotoFrame with the specified JFrame and JLabels.
     * @param frame The JFrame that represents the photo frame window.
     * @param captionLabel The JLabel for the photo's caption.
     * @param locationDateLabel The JLabel for the photo's location and date.
     */
    public PhotoFrame(JFrame frame, JLabel captionLabel, JLabel locationDateLabel) {
        this.frame = frame;
        this.captionLabel = captionLabel;
        this.locationDateLabel = locationDateLabel;
    }

    /**
     * Returns the JFrame associated with this photo frame.
     * @return The JFrame instance.
     */
    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Returns the title of the JFrame associated with this photo frame.
     * @return The title of the JFrame.
     */
    public String getTitle() {
        return frame.getTitle();
    }

    /**
     * Returns the JLabel for the photo's caption.
     * @return The JLabel for the caption.
     */
    public JLabel getCaptionLabel() {
        return captionLabel;
    }

    /**
     * Returns the JLabel for the photo's location and date.
     * @return The JLabel for the location and date.
     */
    public JLabel getLocationDateLabel() {
        return locationDateLabel;
    }

    /**
     * Sets the default close operation for the JFrame associated with this photo frame.
     * Typically, this sets the JFrame to close when the user exits the application.
     */
    public void closeOperation() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
