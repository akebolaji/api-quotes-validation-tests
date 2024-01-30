package api.endpoints;

import api.payload.User;
import api.payload.UserDetails;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

//Created to perform Create, Read, Update and Delete requests the quotes API
public class UserEndPoints {
    public static Response createUser(String key, String value, User user) {
        Response response;
        response = given().header(key, value).contentType(ContentType.JSON)
                .body(user).when().post(Routes.create_user);
        return response;
    }

    public static Response readQuote(String key, String value) {
        Response response;
        response = given().header(key, value).when().get(Routes.get_url);
        return response;
    }

    public static Response updateQuote(int id, String key, String value, String token) {
        Response response;
        response = given().pathParam("quote_id", id).header(key, value).header("User-Token", token)
                .contentType(ContentType.JSON)
                .when()
                .put(Routes.update_url);
        return response;
    }

    public static Response updateQuote2(int id, String key, String value, String token) {
        Response response;
        response = given().pathParam("quote_id", id).header(key, value).header("User-Token", token)
                .contentType(ContentType.JSON)
                .when()
                .put(Routes.update_unFav_url);
        return response;
    }
}
