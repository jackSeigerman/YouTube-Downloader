public class ytDlpCommand {

    public static String[] getCommand(String videoURL, String format, String outputPath) {
        String codecString = "-f \"bv*[vcodec^=avc]+ba[ext=m4a]/b[ext=mp4]/b\"";

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
                codecString,
                "-o", outputPath,
                videoURL
        };
    }
}
