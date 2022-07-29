package db.mysql;

public class MySQLDBUtil {
	private static final String HOSTNAME = "localhost";
	private static final String PORT_NUM = "3306"; // change it to your mysql port number
	// change from 3307 to 3306 inorder to deploy to AWS on 11/16/19
	public static final String DB_NAME = "laiproject";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	public static final String URL = "jdbc:mysql://"
			+ HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";
}

