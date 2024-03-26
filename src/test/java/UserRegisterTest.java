import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import pojo.UserData;

public class UserRegisterTest {
    private String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    private String password = RandomStringUtils.randomNumeric(10);
    private String name = RandomStringUtils.randomAlphabetic(10);

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("API - /api/auth/register")
    public void createNewUserTest() {
        UserData userData = new UserData(email, password, name);
        Response response =
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.ADD_NEW_USER);
                response.then().log().all().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("API - /api/auth/register")
    public void createUserAlreadyRegisteredTest() {
        UserData userData = new UserData("test-data@yandex.ru", "password", "Username");
        Response response =
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.ADD_NEW_USER);
                response.then().log().all().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("API - /api/auth/register")
    public void createUserWithEmptyEmailField() {
        UserData userData = new UserData(null, password, name);
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.ADD_NEW_USER)
                .then().log().all().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("API - /api/auth/register")
    public void createUserWithEmptyPasswordField() {
        UserData userData = new UserData(email, null, name);
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.ADD_NEW_USER)
                .then().log().all().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("API - /api/auth/register")
    public void createUserWithEmptyNameField() {
        UserData userData = new UserData(email, password, null);
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(userData)
                .when()
                .post(Endpoints.ADD_NEW_USER)
                .then().log().all().statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    public void deleteTestData() {
        UserCreate.deleteUser();
    }
}
