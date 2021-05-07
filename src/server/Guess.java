package server;

import java.io.Serializable;

public class Guess implements Serializable {
    public int[] digits;
    public int hitCount;
    public int blowCount;

    public static String convert(int[] guess) {
        StringBuffer buf = new StringBuffer();
        for (int i : guess) {
            buf.append(i);
        }
        return buf.toString();
    }

}
