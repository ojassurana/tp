import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * PhotoFrame class stores the Jlabels and Jframe of a photo.
 */
public class PhotoFrame {

    private JFrame frame;
    private JLabel captionLabel;
    private JLabel locationDateLabel;

    public PhotoFrame(JFrame frame, JLabel captionLabel, JLabel locationDateLabel) {
        this.frame = frame;
        this.captionLabel = captionLabel;
        this.locationDateLabel = locationDateLabel;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public String getTitle() {
        return frame.getTitle();
    }

    public JLabel getCaptionLabel() {
        return captionLabel;
    }

    public JLabel getLocationDateLabel() {
        return locationDateLabel;
    }

    public void closeOperation() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
