package common.datatypes;

import java.io.Serializable;
import java.util.HashSet;

public class Guess implements Serializable {
    private int[] digits;
    private int hitCount;
    private int blowCount;

    public Guess(int hitCount, int blowCount, int[] digits) {
        this.hitCount = hitCount;
        this.blowCount = blowCount;
        this.digits = digits;
    }

    public static String convertToString(int[] guess) {
        StringBuilder str = new StringBuilder();
        for (int i : guess) {
            str.append(i);
        }
        return str.toString();
    }

    public int[] getDigits() {
        return digits;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getBlowCount() {
        return blowCount;
    }

    public static boolean digitsAreDistinct(int[] digits) {
        HashSet<Integer> digitSet = new HashSet<>();
        for (int i : digits) {
            digitSet.add(i);
        }
        return digitSet.size() == digits.length;
    }
}
