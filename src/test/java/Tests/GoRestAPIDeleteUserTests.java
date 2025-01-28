package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import org.testng.annotations.Test;
import org.testng.annotations.*;

import static org.hamcrest.Matchers.*;

public class GoRestAPIDeleteUserTests {

    private static final String TOKEN = "3d5cf807e255d14a66f3650027c35e426f94b7ac8a8658f9c2cbc13808aab19d";
    private static final Faker faker = new Faker();
    private int userId;

    @BeforeMethod (alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://gorest.co.in/public/v2";
    }

    static class User {
        public String name;
        public String gender;
        public String email;
        public String status;

        public User() {
            this.name = faker.name().fullName();
            this.gender = faker.options().option("male", "female");
            this.email = faker.internet().emailAddress();
            this.status = faker.options().option("active", "inactive");
        }
    }

    @Test(groups = {"delete", "positive", "alltests"})
    public void TC01DeleteExistingUser() {
    	System.out.println("Running test: TC01CreateAndDeleteUser");       
    	try {
            User user = new User();
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(user);

            Response createResponse = RestAssured.given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .body(requestBody)
                    .when()
                    .post("/users")
                    .then()
                    .assertThat().statusCode(201)
                    .extract().response();

            userId = createResponse.jsonPath().getInt("id");
            System.out.println("Created User ID: " + userId);

            Response verifyResponse = RestAssured.given()
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .assertThat().statusCode(200)
                    .body("id", equalTo(userId))
                    .body("name", equalTo(user.name))
                    .body("email", equalTo(user.email))
                    .body("gender", equalTo(user.gender))
                    .body("status", equalTo(user.status))
                    .extract()
                    .response();

            System.out.println("User Data Verified:");
            System.out.println(verifyResponse.prettyPrint());
        } catch (Exception error) {
            throw new RuntimeException("Failed to create and verify user", error);
        }
        if (userId != 0) {
            RestAssured.given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .delete("/users/" + userId)
                    .then()
                    .assertThat().statusCode(204);
            System.out.println("Deleted User ID: " + userId);
        }
        
        String getResponseBody = RestAssured.given()
        .when()
        .get("/users/" + userId)
        .then()
        .assertThat().statusCode(404)
        .body("message", equalToIgnoringCase("Resource not found"))
        .extract()
        .response()
        .asString();
        
        	System.out.println("User ID not found anymore: " + getResponseBody);
        	System.out.println("Test completed: TC01CreateAndDeleteUser");
        	
        	
    }
    
    @Test(groups = {"delete", "negative", "alltests"})
    public void TC02DeleteNonExistingUser() {
    	
    	System.out.println("Running test: TC02DeleteNonExistingUser");

        String response = RestAssured.given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + TOKEN)
        .when()
        .delete("/users/nonexistent")  // Non-existent user ID
        .then()
        .assertThat().statusCode(404)
        .body("message", equalToIgnoringCase("Resource not found"))
        .extract()
        .response()
        .asString();
        
        System.out.println("User ID not found: " + response);
        System.out.println("Test completed: TC02DeleteNonExistingUser");

    }

    @Test(groups = {"delete", "negative", "alltests"})
    public void TC03DeleteUserWithUnauthorizedUser() {
    	
    	System.out.println("Running test: TC03DeleteUserWithUnauthorizedUser");
   	
    	try {
            User user = new User();
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(user);

            Response createResponse = RestAssured.given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .body(requestBody)
                    .when()
                    .post("/users")
                    .then()
                    .assertThat().statusCode(201)
                    .extract().response();

            userId = createResponse.jsonPath().getInt("id");
            System.out.println("Created User ID: " + userId);

            RestAssured.given()
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .assertThat().statusCode(200)
                    .body("id", equalTo(userId))
                    .body("name", equalTo(user.name))
                    .body("email", equalTo(user.email))
                    .body("gender", equalTo(user.gender))
                    .body("status", equalTo(user.status))
                    .extract()
                    .response();

            System.out.println("User Data Verified:");
       
    	} catch (Exception error) {
            throw new RuntimeException("Failed to create and verify user", error);
      
        }
        	 String response = RestAssured.given()
	        .header("Accept", "application/json")
	        .header("Content-Type", "application/json")
	        .when()
	        .delete("/users/" + userId)  // Valid user ID but no authorization
	        .then()
	        .assertThat().statusCode(404)
	        .body("message", equalToIgnoringCase("Resource not found"))
	        .extract()
	        .response()
	        .asString();
        	 System.out.println("User is unauthorized to perform this action: " + response);
        	 System.out.println("Test completed: TC03DeleteUserWithUnauthorizedUser");
         
    }

    @Test(groups = {"delete", "negative", "alltests"})
    public void TC04DeleteUserWithInvalidToken() {
    	
    	System.out.println("Running test: TC04DeleteUserWithInvalidToken");
    	
    	try {
            User user = new User();
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(user);

            Response createResponse = RestAssured.given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .body(requestBody)
                    .when()
                    .post("/users")
                    .then()
                    .assertThat().statusCode(201)
                    .extract().response();

            userId = createResponse.jsonPath().getInt("id");
            System.out.println("Created User ID: " + userId);

            RestAssured.given()
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .assertThat().statusCode(200)
                    .body("id", equalTo(userId))
                    .body("name", equalTo(user.name))
                    .body("email", equalTo(user.email))
                    .body("gender", equalTo(user.gender))
                    .body("status", equalTo(user.status))
                    .extract()
                    .response();

            System.out.println("User Data Verified:");
       
		    	} catch (Exception error) {
		            throw new RuntimeException("Failed to create and verify user", error);
		      
		        }
		
		   	 String response = RestAssured.given()
		       .header("Accept", "application/json")
		       .header("Content-Type", "application/json")
		       .header("Authorization", "Bearer " + "invalid token")
		       .when()
		       .delete("/users/" + userId)  // Valid user ID but no authorization
		       .then()
		       .assertThat().statusCode(401)
		       .body("message", equalToIgnoringCase("Invalid Token"))
		       .extract()
		       .asString();
		   	 System.out.println("User is unauthorized to perform this action: " + response);
		   	 System.out.println("Test completed: TC04DeleteUserWithInvalidToken");

    }

    
    @AfterMethod (alwaysRun = true)
    public void deleteUser() {
        try {
            Response response = RestAssured.given()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .delete("/users/" + userId)
                    .then()
                    .extract()
                    .response();

            int statusCode = response.getStatusCode();

            if (statusCode == 204) {
                System.out.println("(Clean-up) Deleted User ID: " + userId);
            } else {
            	System.out.println("User already deleted or not existing. Received status: " + statusCode);
            }

        } catch (Exception error) {
            throw new RuntimeException("Error occurred while deleting user", error);
        }
    }
}
