package ri.constant;

public class Constant {
	
	private static String DB_URI = "jdbc:postgresql://localhost:5432/ri";
	// private static String DB_User = "postgres" ;
	// private static String DB_PWD = "123456" ;
	private static String DB_User = "yuanbo" ;
	private static String DB_PWD = "" ;

    public static String getDB_URI() {
		return DB_URI;
	}

	public static String getDB_User() {
		return DB_User;
	}

	public static String getDB_PWD() {
		return DB_PWD;
	}

	public static String getCurrentPath() {
        return System.getProperty("user.dir");
    }

    public static String getStoplistPath() {
        return System.getProperty("user.dir") + "/dist/stopliste.txt";
    }
    
}