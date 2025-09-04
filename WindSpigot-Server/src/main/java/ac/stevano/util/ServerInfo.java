package ac.stevano.util;

public class ServerInfo {
    public static final String SERVER_NAME = "BughaSpigot";
    public static final String SERVER_VERSION = "1.8.8";

    private ServerInfo() {} // prevent instantiation

    public static String getServerName() {
        return SERVER_NAME;
    }

    public static String getServerVersion() {
        return SERVER_VERSION;
    }

    public static String getFullVersion() {
        return SERVER_NAME + " " + SERVER_VERSION;
    }
}
