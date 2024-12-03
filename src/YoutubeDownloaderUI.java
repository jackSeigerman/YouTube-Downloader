import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YoutubeDownloaderUI extends JFrame {

    private JTextField urlField;
    private JComboBox<String> formatComboBox;
    private JButton downloadButton;
    private JLabel statusLabel;
    private JButton selectDirectoryButton;
    private JLabel directoryLabel;
    private String downloadDirectory;

    public YoutubeDownloaderUI() {
        // Load the last used directory from file
        downloadDirectory = FileManager.loadDownloadDirectory();

        setTitle("YouTube Downloader");
        setSize(700, 215); // Adjusted size to fit additional components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // Set the window icon
        setIconImage(new ImageIcon(getClass().getResource("/resources/logo.png")).getImage());

        // Create components
        urlField = new JTextField(); // Default width for the text field
        urlField.setToolTipText("Enter YouTube video URL");
        urlField.setPreferredSize(new Dimension(300, 30)); // Make it larger by default

        String[] formats = { "MP4", "MP3" };
        formatComboBox = new JComboBox<>(formats);

        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startDownload();
            }
        });

        statusLabel = new JLabel("Status: Waiting for input...");

        selectDirectoryButton = new JButton("Select Download Folder");
        selectDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectDownloadDirectory();
            }
        });

        selectDirectoryButton.setBackground(Color.white); // Sets the background color of the button to blue
        // Added JLabel to display the current directory
        directoryLabel = new JLabel("Current Download Folder: " + downloadDirectory);

        // Layout components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // First row: URL label and URL field (expanding as much as possible)
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("YouTube Video URL:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2; // Make URL field take up more space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow it to stretch horizontally
        add(urlField, gbc);

        // Second row: Format label (right-aligned) and format combo box
        gbc.gridx = 0;
        gbc.gridy = 1;

        add(new JLabel("Select Format:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Left-align the combo box
        add(formatComboBox, gbc);

        // Third row: Select directory button and download button
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(selectDirectoryButton, gbc);

        gbc.gridx = 2;
        add(downloadButton, gbc);

        // Fourth row: Status label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(statusLabel, gbc);

        // Fifth row: Directory label
        gbc.gridy = 4;
        add(directoryLabel, gbc);
    }

    private void startDownload() {
        String videoURL = urlField.getText().trim();
        String format = (String) formatComboBox.getSelectedItem();

        if (videoURL.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a YouTube video URL.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String outputPath = downloadDirectory + "\\" + "%(title)s.%(ext)s";

        // Disable button and start download
        downloadButton.setEnabled(false);
        statusLabel.setText("Downloading...");

        YoutubeDownloader.downloadVideo(videoURL, format, outputPath, new YoutubeDownloader.DownloadListener() {
            @Override
            public void onProgress(String progress) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(progress);
                });
            }

            @Override
            public void onComplete(String message) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(message);
                    downloadButton.setEnabled(true);
                });
            }

            @Override
            public void onError(String errorMessage) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error: " + errorMessage);
                    downloadButton.setEnabled(true);
                });
            }
        });
    }

    private void selectDownloadDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Download Folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            downloadDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            // Save the selected directory path to the text file
            FileManager.saveDownloadDirectory(downloadDirectory);
            // Update the directory label to show the new directory
            directoryLabel.setText("Current Download Folder: " + downloadDirectory);
            JOptionPane.showMessageDialog(this, "Download folder set to: " + downloadDirectory, "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            YoutubeDownloaderUI app = new YoutubeDownloaderUI();
            app.setVisible(true);
        });
    }
}
