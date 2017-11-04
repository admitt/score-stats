package cz.scores;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreStatsTest {

    @ParameterizedTest(name = "{2}")
    @MethodSource("provideTestData")
    void createScoreCountString(List<Integer> scores, String expectedResult, String message) {
        int[] scoresArray = scores.stream().mapToInt(s -> s).toArray();
        assertEquals(expectedResult, ScoreStats.createScoreCountString(scoresArray), message);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of(emptyList(), "", "No scores"),
            Arguments.of(singletonList(4), "4:1", "Only root element"),
            Arguments.of(asList(4, 4), "4:2", "Root element twice"),
            Arguments.of(asList(4, 2), "4:1, 2:1, ", "Root and left child"),
            Arguments.of(asList(4, 2, 4), "4:2, 2:1, ", "Root twice and left node once"),
            Arguments.of(asList(4, 2, 5), "4:1, 2:1, 5:1", "Root with left and right children"),
            Arguments.of(asList(5, 3, 1, 7, 6), "5:1, 3:1, 7:1, 1:1, , 6:1, ", "3 levels of nodes (left leaf nodes)"),
            Arguments.of(asList(5, 3, 4, 7, 8), "5:1, 3:1, 7:1, , 4:1, , 8:1", "3 levels of nodes (right leaf nodes)"),
            Arguments.of(asList(5, 3, 4, 7, 8, 2, 6), "5:1, 3:1, 7:1, 2:1, 4:1, 6:1, 8:1", "3 levels of nodes (full set of leafs)"),
            Arguments.of(asList(5, 3, 4, 7, 8, 2, 6, 8, 2, 6), "5:1, 3:1, 7:1, 2:2, 4:1, 6:2, 8:2", "3 levels of nodes with repetitions"),
            Arguments.of(asList(4, 2, 5, 5, 6, 1, 4), "4:2, 2:1, 5:2, 1:1, , , 6:1", "Example from the task")
        );
    }
}