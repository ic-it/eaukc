package src.utils;


/**
 * Custom implementation of the get-set mechanism
 * If the parameter is empty, the value becomes null
 */
public class GetSet<T> 
{
    private T t;

    /**
     * Constructor. 
     */
    public GetSet() {}
    /**
     * Constructor.
     * Set data
     * 
     * @param t data to add
     */
    public GetSet(T t) { this.t = t; }
    /**
     * Set data
     * 
     * @param t data to add
     */
    public void set(T t) { this.t = t; }
    /**
     * Get data
     * 
     * @return data
     */
    public T get() { return t; }
}