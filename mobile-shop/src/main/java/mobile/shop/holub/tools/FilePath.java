package mobile.shop.holub.tools;

public class FilePath {
    public static String resourceFilePath = "c:\\dp2023";
    public static String dbPath;

    static {
//        System.out.println(resourceFilePath);
//        System.out.println(getDBPath(resourceFilePath));
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            resourceFilePath = System.getProperty("user.dir") + "/resources/";
//            System.out.println(resourceFilePath);
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
