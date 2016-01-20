package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;

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
        
        public int get_total_term(int article_id)
        {
            try{
                Statement stmt = null;
                stmt = this.c.createStatement();
                String sql = "Select count(idindex) from indexation where iddoc ='" + article_id +"'";
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
        
        

        
        public HashMap single_term_query(String term)
        {
            HashMap<Integer, Double> results = new HashMap<Integer, Double> ();
            
            int id_term = this.getTermId(term);
                                                
            try{
                Statement stmt = null;
                stmt = this.c.createStatement();
                String sql = "Select iddoc, idbalise from indexation where idterm ='" + id_term +"'";
                ResultSet rs = stmt.executeQuery(sql);
                
                while(rs.next())
                {
                    int doc_id = rs.getInt(1);
                    int balise_id = rs.getInt(2);
                    
                    HTMLController htmlhelper = new HTMLController();
                    //System.out.println(getBaliseText(balise_id));
                    
                    double balise_importance = htmlhelper.getDegreeImportance(getBaliseText(balise_id));
                    
                    //System.out.println("Debug");
                    if(results.containsKey(doc_id))
                    {
                        double o_value =  results.get(doc_id);
                        double n_value = o_value+balise_importance;
                        results.put(doc_id, n_value);
                    }
                    else
                    {
                        results.put(doc_id, balise_importance);
                    }
                        //results.put results.get(doc_id) + balise_importance;
                }
            }catch(Exception e){
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		System.exit(0);
            }
            //System.out.println("ok");
            return results ;
        }
	
        public String getBaliseText(int balise_id)
        {
            try{
                Statement stmt = null;
                stmt = this.c.createStatement();
                
                String sql = "Select balise from balise where idbalise ='" + balise_id +"'";
                ResultSet rs = stmt.executeQuery(sql);
                
                if(rs.next())
                {
                    return rs.getString(1);
                }else
                {
                    return "";
                }
            }catch(Exception e){
                System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		System.exit(0);
            }
            return "";
            
        }
        
        public int getTermId(String term)
        {
            try{
                Statement stmt = this.c.createStatement();
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
                System.err.println(e.getClass().getName()+": "+ e.getMessage());
		System.exit(0);
            }
            return 0;
        }
}