import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import pojo.UserData;

public class ChangeUserData {
    private String accessToken;
    private String email = RandomStringUtils.randomAlphabetic(8);
    private String name = RandomStringUtils.randomNumeric(8);

    @Before
    public void setUp() {
        baseURI = Endpoints.BASE_URL;
        accessToken = UserCreate.createNewUser();

    }

    @Test
    @DisplayName("Обновление email пользователя")
    @Description("API - /api/auth/user")
    public void userDataUpdateEmailTest() {
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

}
