import java.io.*;

public class FileManager {

    private static final String DOWNLOAD_DIRECTORY_FILE = "downloadDirectory.txt";

    // Load the previously saved download directory
    public static String loadDownloadDirectory() {
        File file = new File(DOWNLOAD_DIRECTORY_FILE);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String path = reader.readLine();
                reader.close();
                if (path != null && !path.isEmpty()) {
                    File dir = new File(path);
                    if (dir.exists() && dir.isDirectory()) {
                        return path; // Valid directory, return the path
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If the file doesn't exist or loading fails, return a default directory
        return System.getProperty("user.home") + File.separator + "Downloads"; // Default to user's Downloads folder
    }

    // Save the current download directory to a file
    public static void saveDownloadDirectory(String directoryPath) {
        File file = new File(DOWNLOAD_DIRECTORY_FILE);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(directoryPath);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
