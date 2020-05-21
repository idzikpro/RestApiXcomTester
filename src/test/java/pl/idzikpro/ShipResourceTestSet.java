package pl.idzikpro;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.idzikpro.conf.ShipUrl;
import pl.idzikpro.model.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class ShipResourceTestSet {
    @BeforeEach
    public void setUp() {
        deleteAllShips();
    }

    @AfterEach
    public void tearDown() {
        deleteAllShips();
    }

    private void deleteAllShips() {
        given()
                .when()
                .delete(ShipUrl.DELETE_ALL_SHIP)
                .then()
                .statusCode(200);
    }

    private int countShips() {
        Response response = given()
                .when()
                .get(ShipUrl.GET_ALL_SHIPS)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        List<String> list = jsonPath.get("");
        return list.size();
    }

    private void addShip(String name) {
        given()
                .when()
                .post(ShipUrl.POST_SHIP.replace("x",name))
                .then()
                .statusCode(200);

    }

    private void addShips(List<Ship> list) {
        for (Ship ship : list
        ) {
            addShip(ship.getName());
        }
    }

    private static Stream<Arguments> feedcountAddedShips() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new Ship("battleship"),
                        new Ship("terrorship")
                        )
                ),
                Arguments.of(Arrays.asList(
                        new Ship("smallscout"),
                        new Ship("harvester")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "feedcountAddedShips")
    public void add_and_remove_Ships_resultTrue(List<Ship> list) {
        addShips(list);
        Assertions.assertThat(countShips()).isEqualTo(list.size());
    }
}
