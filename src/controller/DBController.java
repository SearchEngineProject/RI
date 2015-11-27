package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBController {
	
	// database connection
	public Connection connect(){
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ri","yuanbo","");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
				}
		System.out.println("Opened database successfully");
		return c ;
		}
	
	// database disconnection
	public void disconnect(){
		
	}
	
	// insert a term into database
	public void insert_term (Connection c, String term, int iddoc, String balise){
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "INSERT INTO term (idterm,term) "+ "VALUES (1,'"+term+"');";
			stmt.executeUpdate(sql);
			
			stmt = c.createStatement();
			sql = "INSERT INTO balise (idbalise,balise) "+ "VALUES (1,'"+balise+"');";
			stmt.executeUpdate(sql);
			
			stmt = c.createStatement();
			sql = "INSERT INTO indexation (idindex,idterm,iddoc,idbalise,position) "+ "VALUES (1,1,1,1,1);";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
			} catch (Exception e) {
				System.err.println( e.getClass().getName()+": "+ e.getMessage() );
				System.exit(0);
				}
		System.out.println("Records created successfully");
		}
	
	// check if input term exists in database
	public void check_repeat(String term){
		
	}
	
}
