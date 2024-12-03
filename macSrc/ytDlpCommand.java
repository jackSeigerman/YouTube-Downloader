public class ytDlpCommand {

    public static String[] getCommand(String videoURL, String format, String outputPath) {
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
                "-f", "best",
                "-o", outputPath,
                videoURL
        };
    }
}