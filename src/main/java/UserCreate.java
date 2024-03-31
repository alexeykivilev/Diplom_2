import org.apache.commons.lang3.RandomStringUtils;
import pojo.UserData;

import static io.restassured.RestAssured.*;

public class UserCreate {
    public static String accessToken;

    public static String createNewRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomNumeric(10);
        String name = RandomStringUtils.randomAlphabetic(10);

        UserData userData = new UserData(email, password, name);
        accessToken = given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post("/api/auth/register")
                .then().statusCode(200).extract().path("accessToken");
        return accessToken;
    }

    public static String createBaseUser() {
        String email = "alexey@yandex.ru";
        String password = "123456";
        String name = "alexeytestbaseuser";

        UserData userData = new UserData(email, password, name);
        accessToken = given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post("/api/auth/register")
                .then().extract().path("accessToken");
        return accessToken;
    }


    public static void deleteUser(String accessToken) {
        if (accessToken != null) {
            given()
                    .header("Authorization", "Bearer" + accessToken)
                    .when()
                    .delete(Endpoints.DELETE_USER)
                    .then().log().all().statusCode(202);
        }
    }
}