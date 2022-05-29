package src.utils;

import java.sql.ResultSet;

/**
 * Processing a single result from a database query
 */
public abstract interface ResultSetCallback {
    
    /**
     * Call
     * 
     * @param rs ResultSet
     */
    public abstract int call(ResultSet rs);
}