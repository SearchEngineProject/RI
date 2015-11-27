package test;

import java.sql.Connection;
import controller.DBController;

public class Main {

	public static void main(String[] args) {
		
		DBController db = new DBController();
		Connection c = db.connect();
		db.insert_term(c, "paris", 1, "a");
		
	}
}