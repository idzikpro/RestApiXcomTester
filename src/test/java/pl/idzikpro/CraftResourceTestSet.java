package pl.idzikpro;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.idzikpro.conf.CraftUrl;
import pl.idzikpro.conf.ShipUrl;
import pl.idzikpro.model.Craft;
import pl.idzikpro.model.Ship;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class CraftResourceTestSet {
    @BeforeEach
    public void setUp() {
        deleteAllCraft();
    }

    @AfterEach
    public void tearDown() {
        deleteAllCraft();
    }

    private void deleteAllCraft() {
        given()
                .when()
                .delete(CraftUrl.DELETE_ALL_CRAFT)
                .then()
                .statusCode(200);
    }

    private int countCraft() {
        Response response = given()
                .when()
                .get(CraftUrl.GET_ALL_CRAFT)
                .then()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        List<String> list = jsonPath.get("");
        return list.size();
    }

    private void addOneCraft(String name) {
        given()
                .when()
                .post(CraftUrl.POST_CRAFT.replace("x", name))
                .then()
                .statusCode(200);

    }

    private void addCraft(List<Craft> list) {
        for (Craft craft : list
        ) {
            addOneCraft(craft.getName());
        }
    }

    private static Stream<Arguments> feedcountAddedCraft() {
        return Stream.of(
                Arguments.of(Arrays.asList(
                        new Craft("skyranger"),
                        new Craft("interceptor")
                        )
                ),
                Arguments.of(Arrays.asList(
                        new Craft("firestorm"),
                        new Craft("avenger")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "feedcountAddedCraft")
    public void add_and_remove_Craft_resultTrue(List<Craft> list) {
        addCraft(list);
        Assertions.assertThat(countCraft()).isEqualTo(list.size());
    }
}