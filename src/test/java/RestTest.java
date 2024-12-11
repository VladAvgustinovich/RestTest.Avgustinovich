import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class RestTest {
    CookieFilter cookieFilter = new CookieFilter();

    @Test
    @DisplayName("Добавляем уже существующий в БД экзотический овощ")
    public void addOldVegetableExotic() {

        Specification.installSpecification(Specification.requestSpecification("http://localhost:8080/api/"));

        // Добавляем товар в таблицу 1 раз
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Бамия",
                  "type": "VEGETABLE",
                  "exotic": true
                }
                """)
                .when()
                .post("food")
                .then()
                .statusCode(200)
                .log().status();

        // Добавляем товар в таблицу 2 раз
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Бамия",
                  "type": "VEGETABLE",
                  "exotic": true
                }
                """)
                .when()
                .post("food")
                .then()
                // Ожидаем ошибку, так как товар должен быть уникальным
                .statusCode(409)
                .log().all();



    }
    @Test
    @DisplayName("Добавляем уже существующий в БД не экзотический овощ")
    public void addOldVegetableNotExotic() {

        Specification.installSpecification(Specification.requestSpecification("http://localhost:8080/api/"));
        // Добавляем товар в таблицу 1 раз
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Огурец",
                  "type": "VEGETABLE",
                  "exotic": false
                }
                """)
                .when()
                .post("food")
                .then()
                .statusCode(200)
                .log().status();

        // Добавляем товар в таблицу 2 раз
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Огурец",
                  "type": "VEGETABLE",
                  "exotic": false
                }
                """)
                .when()
                .post("food")
                .then()
                // Ожидаем ошибку, так как товар должен быть уникальным
                .statusCode(409)
                .log().all();
    }

    @Test
    @DisplayName("Добавляем в БД новый экзотический фрукт")
    public void addNewFruitExotic() {

        Specification.installSpecification(Specification.requestSpecification("http://localhost:8080/api/"));

        // Добавляем товар в таблицу
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Манго",
                  "type": "FRUIT",
                  "exotic": true
                }
                """)
                .when()
                .post("food")
                .then()
                .statusCode(200)
                .log().all();


        // Проверяем, что товар с name = "Манго" находится в таблице
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .when()
                .get("food")
                .then()
                .statusCode(200)
                .body("name", hasItem("Манго"))
                .assertThat().body("name", hasItem("Манго"))
                .log().all();



        // Приводим таблицу в исходное состояние
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .when()
                .post("data/reset")
                .then()
                .statusCode(200)
                .log().all();
    }
    @Test
    @DisplayName("Добавляем в БД новый не экзотический фрукт")
    public void addNewFruitNotExotic() {

        Specification.installSpecification(Specification.requestSpecification("http://localhost:8080/api/"));


        // Добавляем товар в таблицу
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .body("""
                {
                  "name": "Груша",
                  "type": "FRUIT",
                  "exotic": false
                }
                """)
                .when()
                .post("food")
                .then()
                .statusCode(200)
                .log().status();


        // Проверяем, что товар с name = "Груша" находится в таблице
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .when()
                .get("food")
                .then()
                .statusCode(200)
                .body("name", hasItem("Груша"))
                .assertThat().body("name", hasItem("Груша"));



        // Приводим таблицу в исходное состояние
        given()
                .header("Content-Type", "application/json")
                .filter(cookieFilter)
                .when()
                .post("data/reset")
                .then()
                .statusCode(200);
    }

}
