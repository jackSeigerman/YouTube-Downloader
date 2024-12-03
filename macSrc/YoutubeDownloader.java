import java.io.*;

public class YoutubeDownloader {
    public interface DownloadListener {
        void onProgress(String progress);
        void onComplete(String message);
        void onError(String errorMessage);
    }

    public static void downloadVideo(String videoURL, String format, String outputPath, DownloadListener listener) {
        String[] command = ytDlpCommand.getCommand(videoURL, format, outputPath);

        new Thread(() -> {
            try {
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("[download]")) {
                        listener.onProgress(line);  // Notify frontend with progress
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    listener.onComplete("Download completed successfully!");
                } else {
                    StringBuilder errorMessage = new StringBuilder();
                    while ((line = errorReader.readLine()) != null) {
                        errorMessage.append(line).append("\n");
                    }
                    listener.onError("Download failed with exit code: " + exitCode + "\n" + errorMessage.toString());
                }

            } catch (IOException | InterruptedException e) {
                listener.onError("An error occurred: " + e.getMessage());
            }
        }).start();
    }


    private static String[] buildCommand(String videoURL, String format, String outputPath) {
        if (format.equals("MP3")) {
            return new String[] {
                    "yt-dlp",
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", "mp3",
                    "-o", outputPath.replace("%(ext)s", "mp3"),
                    videoURL
            };
        }
        return new String[] {
                "yt-dlp",
                "-f", "bestvideo+bestaudio/best",
                "-o", outputPath,
                videoURL
        };
    }
}