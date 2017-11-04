package cz.scores;

public class ScoreRecord {

    static String createScoreCountString(int... scores) {
        int headScore = scores[0];
        int count = 0;
        for (int score : scores) {
            if (score == headScore) {
                count++;
            }
        }
        return headScore + ":" + count;
    }
}
