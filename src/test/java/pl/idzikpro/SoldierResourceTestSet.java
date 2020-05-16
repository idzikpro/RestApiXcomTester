package pl.idzikpro;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.idzikpro.conf.SoldierUrl;
import pl.idzikpro.model.RandomSoldier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class SoldierResourceTestSet {
    @BeforeEach
    public void setUp() {
        deleteAllSoldiers();
    }

    @AfterEach
    public void tearDown() {
        deleteAllSoldiers();
    }

    private void deleteAllSoldiers() {
        given()
                .when()
                .delete(SoldierUrl.DELETE_ALL_SOLDIERS)
                .then()
                .statusCode(200);
    }

    private int countSoldiers() {
        Response response = given()
                .when()
                .get(SoldierUrl.GET_ALL_SOLDIERS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        List<String> list = jsonPath.get("");
        return list.size();
    }

    private void addSoldier(String rank) {
        given()
                .when()
                .post(SoldierUrl.POST_RANDOM_SOLDIER.replace("x",rank))
                .then()
                .statusCode(200);

    }

    private void addSoldiers(List<RandomSoldier> list) {
        for (RandomSoldier soldier : list
        ) {
            addSoldier(soldier.getRank());
        }
    }

    private static Stream<Arguments> feedcountAddedRandomSoldiers() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new RandomSoldier("rookie"),
                        new RandomSoldier("captain"),
                        new RandomSoldier("captain")
                        )
                ),
                Arguments.of(Arrays.asList(
                        new RandomSoldier("colonel"),
                        new RandomSoldier("squaddie"),
                        new RandomSoldier("sergeant")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "feedcountAddedRandomSoldiers")
    public void add_and_remove_RandomSoldiers_byRank_resultTrue(List<RandomSoldier> list) {
        addSoldiers(list);
        Assertions.assertThat(countSoldiers()).isEqualTo(list.size());
    }
}
