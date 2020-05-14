package pl.idzikpro.aliens;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.idzikpro.aliens.conf.AlienTestData;
import pl.idzikpro.aliens.conf.AlienUrl;
import pl.idzikpro.aliens.model.RandomAlien;

import java.util.List;

import static io.restassured.RestAssured.given;
import static pl.idzikpro.aliens.conf.AlienUrl.DELETE_ALL_ALIENS;
import static pl.idzikpro.aliens.conf.AlienUrl.GET_ALL_ALIENS;


public class AliensTestSet {
    @BeforeEach
    public void setUp() {
        deleteAllAliens();
    }

    @AfterEach
    public void tearDown(){
        deleteAllAliens();
    }

    private void deleteAllAliens() {
        given()
                .when()
                .delete(DELETE_ALL_ALIENS)
                .then()
                .statusCode(200);
    }

    private int countAliens() {
        Response response = given()
                .when()
                .get(GET_ALL_ALIENS)
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
                .post("http://localhost:8010/v1/aliens/random?race=" + race + "&rank=" + rank)
                .then()
                .statusCode(200);

    }

    private void addAliens(List<RandomAlien> list){
        for (RandomAlien alien:list
             ) {
            addAlien(alien.getRace(),alien.getRank());
        }
    }

    @Test
    public void addXAliens() {
        addAliens(AlienTestData.list);
        Assertions.assertThat(countAliens()).isEqualTo(AlienTestData.list.size());
    }
}
