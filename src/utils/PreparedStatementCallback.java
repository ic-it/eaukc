package src.utils;

import java.sql.PreparedStatement;

/**
 * When using databases, you can use query templating to avoid SQLInjection
 */
public abstract interface PreparedStatementCallback {

    /**
     * Call
     * 
     * @param ps PreparedStatement
     */
    public abstract void call(PreparedStatement ps);
}