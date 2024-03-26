import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import pojo.UserData;

public class ChangeUserData {
    public String accessToken;
    public String email = RandomStringUtils.randomAlphabetic(8);
    public String name = RandomStringUtils.randomNumeric(8);

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
    }

    @Test
    @DisplayName("Обновление email, name пользователя")
    @Description("API - /api/auth/user")
    public void userDataUpdateTest() {
        accessToken = UserCreate.createNewUser();
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setName(name);
        given()
                .header("Authorization", accessToken)
                .and()
                .body(userData)
                .patch(Endpoints.UPDATE_USER_DATA)
                .then().log().all().statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    @DisplayName("Обновление email, name пользователя без авторизации")
    @Description("API - /api/auth/user")
    public void userDataUpdateWithNoAuthTest() {
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setName(name);
        given()
                .body(userData)
                .patch(Endpoints.UPDATE_USER_DATA)
                .then().log().all().statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    @DisplayName("Удаление тестового пользователя")
    public void deleteTestData() {
        UserCreate.deleteUser();
    }

}
