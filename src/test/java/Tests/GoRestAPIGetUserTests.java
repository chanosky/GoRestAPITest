package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class GoRestAPIGetUserTests {

    @BeforeMethod (alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://gorest.co.in/public/v2";
    }

    @Test(groups = {"get", "positive", "alltests"})
    public void TC01VerifyUserDataById() {
    	System.out.println("Running test: TC01VerifyUserDataById");

        Response response = RestAssured.given()
            .when()
            .get("/users")
            .then()
            .assertThat().statusCode(200)
            .extract()
            .response();
        
        System.out.println("Response from testGetUsers: " + response.prettyPrint());
        
        int userId = -1; // Default value in case no user ID is fetched
        
        try {
            // Extract the user ID from the response (assuming it's an array of users)
            userId = response.jsonPath().getInt("id[0]");  // Get the ID of the first user

            if (userId == -1) {
                throw new Exception("User ID not found in the response.");
            }
            
            System.out.println("User ID from testGetUsers: " + userId);

            Response response2 = RestAssured.given()
                .when()
                .get("/users/" + userId)
                .then()
                .assertThat().statusCode(200)
                .body("id", equalTo(userId))
                .body("name", notNullValue())
                .body("email", notNullValue())
                .body("gender", anyOf(equalTo("male"), equalTo("female")))
                .body("status", anyOf(equalTo("active"), equalTo("inactive")))
                .extract()
                .response();

            System.out.println("User data for ID " + userId + ": " + response2.prettyPrint());

        } catch (Exception error) {
            System.out.println("Error: " + error.getMessage());
        }
        
        System.out.println("Test completed: TC01VerifyUserDataById");
    }

    
    @Test(groups = {"get", "negative", "alltests"})
    public void TC02VerifyUserDataByInvalidUserId() {
    	System.out.println("Running test: TC02VerifyUserDataByInvalidUserId");
        String invalidUserId = "invalidid"; // Non-existent user ID
        
        RestAssured.given()
            .when()
            .get("/users/" + invalidUserId)
            .then()
            .assertThat().statusCode(404)
            .body("message", containsString("Resource not found"));
        
        System.out.println("Test completed: TC02VerifyUserDataByInvalidUserId");
    }

}
