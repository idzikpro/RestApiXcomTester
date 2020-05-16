package pl.idzikpro;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.idzikpro.conf.AlienUrl;
import pl.idzikpro.model.RandomAlien;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class AlienResourceTestSet {
    @BeforeEach
    public void setUp() {
        deleteAllAliens();
    }

    @AfterEach
    public void tearDown() {
        deleteAllAliens();
    }

    private void deleteAllAliens() {
        given()
                .when()
                .delete(AlienUrl.DELETE_ALL_ALIENS)
                .then()
                .statusCode(200);
    }

    private int countAliens() {
        Response response = given()
                .when()
                .get(AlienUrl.GET_ALL_ALIENS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        List<String> list = jsonPath.get("");
        return list.size();
    }

    private void addAlien(String race, String rank) {
        given()
                .when()
                .post(AlienUrl.POST_RANDOM_ALIEN.replace("x",race).replace("y",rank))
                .then()
                .statusCode(200);

    }

    private void addAliens(List<RandomAlien> list) {
        for (RandomAlien alien : list
        ) {
            addAlien(alien.getRace(), alien.getRank());
        }
    }

    private static Stream<Arguments> feedcountAddedRandomAliens() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new RandomAlien("sectoid", "leader"),
                        new RandomAlien("floater", "navigator"),
                        new RandomAlien("ethereal", "commander")
                        )
                ),
                Arguments.of(Arrays.asList(
                        new RandomAlien("sectoid", "leader"),
                        new RandomAlien("zombie", "terrorist"),
                        new RandomAlien("sectoid", "soldier")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "feedcountAddedRandomAliens")
    public void add_and_remove_RandomAliens_byRaceandRank_resultTrue(List<RandomAlien> list) {
        addAliens(list);
        Assertions.assertThat(countAliens()).isEqualTo(list.size());
    }


}
