package src.db;

import java.sql.*;

import src.utils.PreparedStatementCallback;
import src.utils.ResultSetCallback;
import src.Constants;
import src.Globals;

public class DataBase 
{
    private static DataBase instance = null;
    private Connection conn;

    private DataBase()
    {
        connect();
    }

    
    /** 
     * Singleton for the database
     * 
     * @return DataBase
     */
    public static DataBase getInstance()
    {
        if (instance == null)
            instance = new DataBase();
        return instance;
    }

    /**
     * Connect to database
     */
    public void connect()
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(Globals.dbFile.get());
        } catch ( Exception e ) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    /**
     * Close database connection
     */
    public void close()
    {
        try 
        {
            conn.close();
        } catch ( Exception e ) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        instance = null;
    }

    
    /** 
     * Execute and work on the query
     * 
     * @param sql
     * @param pscallback
     * @param rscallback
     */
    public void executeQuery(String sql, PreparedStatementCallback pscallback, ResultSetCallback rscallback)
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try 
        {
            ps = conn.prepareStatement(sql);
            
            pscallback.call(ps);
            rs = ps.executeQuery();
            while ( rs.next() ) 
                if ( rscallback.call(rs) == 1 )
                    break;
            rs.close();
            ps.close();
        } catch ( Exception e ) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    
    /** 
     * Execute a query without processing the results
     * 
     * @param sql
     * @param pscallback
     */
    public void executeUpdate(String sql, PreparedStatementCallback pscallback)
    {
        PreparedStatement ps = null;
        try 
        {
            ps = conn.prepareStatement(sql);
            pscallback.call(ps);
            ps.executeUpdate();
            ps.close();
        } catch ( Exception e ) 
        {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}