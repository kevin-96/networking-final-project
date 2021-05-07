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

    public static int[] convert(String guess) {
        int[] ints = new int[guess.length()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = guess.charAt(i) - '0';
        }
        return ints;
    }
}
