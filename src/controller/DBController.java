package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBController {
	
    
        Connection c = null;
        
	// database connection
	public void connect(){
		try {
			Class.forName("org.postgresql.Driver");
			this.c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ri","postgres","123456");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
				}
		//System.out.println("Opened database successfully");
	}
        
        
	
	// database disconnection
	public void disconnect(){
            try{
		this.c.close();
            }
            catch(Exception e)
            {
                System.err.println("Error closing conn");
            }
	}
	
	// insert a term into database
	public void insert_term (String term, int iddoc, String balise){
                //this.connect();
		Statement stmt = null;
		try {
			this.c.setAutoCommit(false);
                        int term_id = -1;
                        int balise_id = -1;
                        
                        if(check_repeat_term(term) != 0)
                        {
                            term_id = check_repeat_term(term);
                        }else
                        {
                            stmt = this.c.createStatement();
                            String sql = "INSERT INTO term (term) "+ "VALUES ('"+term+"')RETURNING idterm;";
                            ResultSet rs = stmt.executeQuery(sql);
                            rs.next();
                            term_id = rs.getInt(1);
                        }

			
                        if(check_repeat_balise(balise)!=0)
                        {
                            balise_id = check_repeat_balise(balise);
                        }else{
                            stmt = this.c.createStatement();
                            String sql = "INSERT INTO balise (balise) "+ "VALUES ('"+balise+"')RETURNING idbalise;";
                            ResultSet rs = stmt.executeQuery(sql);
                            rs.next();
                            balise_id = rs.getInt(1);
                        }
			
			stmt = this.c.createStatement();
			String sql = String.format("INSERT INTO indexation (idterm,iddoc,idbalise,position) "+ "VALUES (%d, %d, %d, %d);", term_id, iddoc, balise_id, 1) ;
			stmt.executeUpdate(sql);
                        
			stmt.close();
			this.c.commit();
                        //this.c.close();
			} catch (Exception e) {
				System.err.println( e.getClass().getName()+": "+ e.getMessage() );
				System.exit(0);
				}
		System.out.println("Term Records created successfully");
                
		}
	
	// check if input term exists in database
	public int check_repeat_term(String term){
            try{
             Statement stmt = null;
                stmt = this.c.createStatement();
                String sql = "Select * from term where term ='" + term +"'";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    return rs.getInt(1);
                }else
                {
                    return 0;
                }
            }catch(Exception e){
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                
		System.exit(0);
            }
            return 0;
	}
        
        public int check_repeat_balise(String balise)
        {
            try{
             Statement stmt = null;
                stmt = this.c.createStatement();
                String sql = "Select * from balise where balise ='" + balise +"'";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    return rs.getInt(1);
                }else
                {
                    return 0;
                }
            }catch(Exception e){
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
                
		System.exit(0);
            }
            return 0;
        }
	
}
