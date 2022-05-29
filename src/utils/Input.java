package src.utils;

import java.util.Scanner;

public class Input {
    
    /** 
     * Input String
     * 
     * @param text Text before input
     * @return String
     */
    public static String S(String text)
    {
        // Read STR
        System.out.print(text);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    
    
    /** 
     * Input int
     * 
     * @param text Text before input
     * @return int
     */
    public static int I(String text)
    {
        // Read INT
        System.out.print(text);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    
    /** 
     * Enter string and move the cursor to the xy position in the console
     * 
     * @param text
     * @param x
     * @param y
     * @return String
     */
    public static String S(String text, int x, int y)
    {
        UtilsFunc.gotoXY(x, y);
        return S(text);
    }

    
    /** 
     * Enter int and move the cursor to the xy position in the console
     * 
     * @param text
     * @param x
     * @param y
     * @return int
     */
    public static int I(String text, int x, int y)
    {
        UtilsFunc.gotoXY(x, y);
        return I(text);
    }
}
