package cz.scores;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreRecordTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    void createScoreCountString(List<Integer> scores, String expectedResult) {
        int[] scoresArray = scores.stream().mapToInt(s -> s).toArray();
        assertEquals(expectedResult, ScoreRecord.createScoreCountString(scoresArray));
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(singletonList(4), "4:1"),
            Arguments.of(asList(4, 4), "4:2"),
            Arguments.of(asList(4, 2), "4:1, 2:1, ")
        );
    }
}