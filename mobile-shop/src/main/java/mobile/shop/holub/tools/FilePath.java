package mobile.shop.holub.tools;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilePath {
    public static String resourceFilePath = "c:\\dp2023\\";
    public static String dbPath;

    static {

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            resourceFilePath = System.getProperty("user.dir") + "/resources/";
        } else if (osName.contains("windows")) {
            resourceFilePath = "c:\\dp2023";
            Path sourceDir = Paths.get(System.getProperty("user.dir") + "/resources/");

            Path targetDir = Paths.get(resourceFilePath);

            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir);

                for (Path file : stream) {
                    Files.move(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }

                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        dbPath = getDBPath(resourceFilePath);

    }

    public static String getDBPath(String databaseName) {
        String path = "file:/" + databaseName;
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            path = "file://" + databaseName;
        }
        return path;
    }

}
