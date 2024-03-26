import org.apache.commons.lang3.RandomStringUtils;
import pojo.UserData;

import static io.restassured.RestAssured.*;

public class UserCreate {
    private static String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    private static String password = RandomStringUtils.randomNumeric(10);
    private static String name = RandomStringUtils.randomAlphabetic(10);
    public static String accessToken;

    public static String createNewUser() {
        UserData userData = new UserData(email, password, name);
        return given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post("/api/auth/register")
                .then().statusCode(200).extract().path("accessToken");
    }

    public static void deleteUser() {
        if (accessToken != null) {
            given()
                    .header("Authorization", "Bearer" + accessToken)
                    .when()
                    .delete(Endpoints.DELETE_USER)
                    .then().log().all().statusCode(200);
        }
    }
}