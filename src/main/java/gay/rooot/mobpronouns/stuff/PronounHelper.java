package gay.rooot.mobpronouns.stuff;

import java.util.Random;

public class PronounHelper {
    private static final String[] binaryPronouns = new String[]{"she/her", "she/they", "he/him", "he/they", "they/them", "it/its"};
    private static final String[] neoPronouns = new String[]{"ze/zir", "ze/hir", "xe/xem", "ey/em", "fae/faer", "per/per", "e/eir"};

    public static String getEntityPronouns(long seed) {
        Random rng = new Random(seed);

        // 10% chance of neopronouns (binary pronouns are more common)
        if (rng.nextInt(100) < 10) {
            return neoPronouns[rng.nextInt(neoPronouns.length)];
        } else {
            return binaryPronouns[rng.nextInt(binaryPronouns.length)];
        }
    }
}
