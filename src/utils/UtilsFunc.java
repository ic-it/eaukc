package src.utils;


/**
 * functions that are used in the code
 */
public class UtilsFunc 
{
    /** 
     * The desired value from the range
     * 
     * @param min
     * @param max
     * @return int
     */
    public static int getRandomNumber(int min, int max) 
    {
        return (int) ((Math.random() * (max - min)) + min);
    }
    
    
    /** 
     * Move cursor to position XY
     * 
     * @param x
     * @param y
     */
    public static void gotoXY(int x, int y)
    {
        char escCode = 0x1B;
        System.out.print(String.format("%c[%d;%df",escCode,x,y));
    }
}
