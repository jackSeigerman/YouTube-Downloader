import java.io.*;

public class YoutubeDownloader {
    public interface DownloadListener {
        void onProgress(String progress);
        void onComplete(String message);
        void onError(String errorMessage);
    }

    public static void downloadVideo(String videoURL, String format, String outputPath, DownloadListener listener) {
        String[] command = buildCommand(videoURL, format, outputPath);

        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
                    listener.onError("Download failed with exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                listener.onError("An error occurred: " + e.getMessage());
            }
        }).start();
    }

    private static String[] buildCommand(String videoURL, String format, String outputPath) {
        String codecString = "-f \"bv*[vcodec^=avc]+ba[ext=m4a]/b[ext=mp4]/b\"";
        if (format.equals("MP3")) {
            return new String[] {
                    "yt-dlp",  // or use full path if not in PATH
                    "-f", "bestaudio",
                    "--extract-audio",
                    "--audio-format", "mp3",
                    "-o", outputPath.replace("%(ext)s", "mp3"),
                    videoURL
            };
        }
        return new String[] {
                "yt-dlp",  // or full path if necessary
                codecString,
                "-o", outputPath,
                videoURL
        };
    }
}
