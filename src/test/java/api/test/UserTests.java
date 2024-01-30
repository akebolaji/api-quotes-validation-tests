package api.test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.payload.UserDetails;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class UserTests {
    String key = "Authorization";
    String value = "Token token=e8123e7e5626da733298562fe7d81872";
    Faker faker;
    User user;
    String token;
    int id;
    String errorMessage = "Not Found";

    @BeforeClass
    public void setupData() {
        faker = new Faker();
        user = new User();
        UserDetails userPayload = new UserDetails();
        userPayload.setLogin(faker.name().firstName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(5, 20));
        user.setUser(userPayload);
    }

    // Step 1 ----> Create a user and check user already exist when user details is submitted again
    @Test()
    public void testPostUserCreatedAndUserExist() {
        Response response = UserEndPoints.createUser(key, value, user);
        assertEquals(response.getStatusCode(), 200);
        token = response.then().extract().path("User-Token");
        Response resp = UserEndPoints.createUser(key, value, user);
        resp.then().body("message", equalTo("Username has already been taken; Email has already been taken"));
    }

    // Step 2 ----> Test access is denied when user is not authorized
    @Test
    public void testPostUserNotCreated() {
        Response response = UserEndPoints.createUser(key, value.replace("token", "value"), user);
        assertEquals(response.getStatusCode(), 401);
        Response res = UserEndPoints.createUser("", "", user);
        assertEquals(res.getStatusCode(), 401);
    }

    // Step 3 ----> Retrieve list of quotes through Get method and validate it returns 25 quotes as well extract id needed for subsequent validation
    @Test
    public void testGetQuotes() {
        Response response = UserEndPoints.readQuote(key, value);
        assertEquals(response.getStatusCode(), 200);
        response.then().body("quotes", hasSize(25));
        id = response.then().extract().path("quotes[0].id");
    }

    // Step 4 ----> Use Put method to retrieve a particular favor quote using id generated from Get Quote request and check for error message if a random id is provided
    @Test(dependsOnMethods = {"testPostUserCreatedAndUserExist", "testGetQuotes"})
    public void testFavQuoteAndErrorMessage() {
        Response response = UserEndPoints.updateQuote(id, key, value, token);
        assertEquals(response.getStatusCode(), 200);
        response.then().body("user_details.favorite", equalTo(true));
        Response res = UserEndPoints.updateQuote(1, key, value, token);
        assertEquals(res.getStatusCode(), 404);
        res.then().body("error", equalTo(errorMessage));
    }

    // Step 5 ----> Use Put method to retrieve a particular un-favor quote using id generated from Get Quote request and check for error message if a random id is provided
    @Test(dependsOnMethods = {"testPostUserCreatedAndUserExist", "testGetQuotes"})
    public void testUnFavQuoteAndErrorMessage() {
        Response response = UserEndPoints.updateQuote2(id, key, value, token);
        assertEquals(response.getStatusCode(), 200);
        response.then().body("user_details.favorite", equalTo(false));
        Response res = UserEndPoints.updateQuote2(1, key, value, token);
        assertEquals(res.getStatusCode(), 404);
        res.then().body("error", equalTo(errorMessage));
    }
}





