
package constant;

public class Constant {


    public static String getCurrentPath() {
        return System.getProperty("user.dir");
    }

    public static String getStoplistPath() {
        return System.getProperty("user.dir") + "/dist/stopliste.txt";
    }

}