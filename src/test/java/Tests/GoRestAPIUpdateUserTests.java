package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import Tests.GoRestAPICreateUserTests.User;

import org.testng.annotations.*;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class GoRestAPIUpdateUserTests {
    private static final String BASE_URL = "https://gorest.co.in/public/v2";
    private static final String TOKEN = "3d5cf807e255d14a66f3650027c35e426f94b7ac8a8658f9c2cbc13808aab19d";
    private static int userId;
    private static final Faker faker = new Faker();

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

    @BeforeMethod (alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        try {
            User createdUser = new User();
            String requestBody = String.format("{\"name\":\"%s\", \"gender\":\"%s\", \"email\":\"%s\", \"status\":\"%s\"}",
                createdUser.name, createdUser.gender, createdUser.email, createdUser.status);

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
        } catch (Exception error) {
            throw new RuntimeException("Failed to create user", error);
        }
    }

    @Test(groups = {"update", "positive", "alltests"})
    public void TC01UpdateExistingUser() {
    	System.out.println("Running test: TC01UpdateExistingUser");
    	String updatedUserData = "{\n"
    	        + "    \"name\": \"Sample Testname\",\n"
    	        + "    \"email\": \"sample@email.com\",\n"
    	        + "    \"status\": \"inactive\",\n"
    	        + "    \"gender\": \"male\"\n"
    	        + "}";

        String responseBody = RestAssured.given()
            .header("Authorization", "Bearer " + TOKEN)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(updatedUserData)
            .when()
            .put("/users/" + userId)
            .then()
            .assertThat().statusCode(200) 
            .body("name", equalTo("Sample Testname"))
            .body("email", equalTo("sample@email.com"))
            .body("status", equalTo("inactive"))
            .body("gender", equalTo("male"))
            .extract().response().asString();
        
        System.out.println("Updated User:" + responseBody);
        System.out.println("Updated User ID: " + userId);
        System.out.println("Test completed: TC01UpdateExistingUser");
    }

    @Test(groups = {"update", "negative", "alltests"})
    public void TC02UpdateUserUsingBlankData() {
    	System.out.println("Running test: TC02UpdateUserUsingBlankData");
        String requestBody = "{"
                + "\"name\": \"\","
                + "\"email\": \"\","
                + "\"gender\": \"\","
                + "\"status\": \"\""
                + "}";

        String responseBody = RestAssured.given()
            .header("Authorization", "Bearer " + TOKEN)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(requestBody) // Using clean JSON body
            .when()
            .put("/users/" + userId)
            .then()
            .assertThat().statusCode(422)  // Expecting validation error
            .body("find { it.field == 'name' }.message", equalTo("can't be blank"))
            .body("find { it.field == 'email' }.message", equalTo("can't be blank"))
            .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"))
            .body("find { it.field == 'status' }.message", equalTo("can't be blank"))
            .extract()
        	.response()
        	.asString();


        System.out.println("Updated User ID: " + userId);
        System.out.println("Response Body: " + responseBody);
        System.out.println("Test completed: TC02UpdateUserUsingBlankData");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC03UpdateUserUsingInvalidData() {
    	System.out.println("Running test: TC03UpdateUserUsingInvalidData");
        String requestBody = "{"
                + "\"name\": null,"
                + "\"email\": \"invalidemail\","
                + "\"gender\": \"invalidgender\","
                + "\"status\": \"invalidstatus\""
                + "}";

        String responseBody = RestAssured.given()
            .header("Authorization", "Bearer " + TOKEN)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(requestBody) // Using clean JSON body
            .when()
            .put("/users/" + userId)
            .then()
            .assertThat().statusCode(422)  // Expecting validation error
            .body("find { it.field == 'name' }.message", equalTo("can't be blank"))
            .body("find { it.field == 'email' }.message", equalTo("is invalid"))
            .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"))
            .body("find { it.field == 'status' }.message", equalTo("can't be blank"))
            .extract()
        	.response()
        	.asString();


        System.out.println("Updated User ID: " + userId);
        System.out.println("Response Body: " + responseBody);
        System.out.println("Test completed: TC03UpdateUserUsingInvalidData");
        
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC04UpdateNonExistingUser() {
    	System.out.println("Running test: TC04UpdateNonExistingUser");
    	String updatedUserData = "{\n"
    	        + "    \"name\": \"Sample Testname\",\n"
    	        + "    \"email\": \"sample@email.com\",\n"
    	        + "    \"status\": \"inactive\",\n"
    	        + "    \"gender\": \"male\"\n"
    	        + "}";

        String responseBody = RestAssured.given()
            .header("Authorization", "Bearer " + TOKEN)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(updatedUserData)
            .when()
            .put("/users/" + "notexisting")
            .then()
            .assertThat().statusCode(404) 
            .body("message", equalToIgnoringCase("Resource not found"))
            .extract().
            response().
            asString();
        
        System.out.println("Updated User:" + responseBody);
        System.out.println("Updated User ID: " + userId);
        System.out.println("Test completed: TC04UpdateNonExistingUser");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC05UpdateExistingUserWithInvalidToken() {
        System.out.println("Running test: TC05UpdateExistingUserWithInvalidToken");

        String requestBody = "{\n"
    	        + "    \"name\": \"Sample Testname\",\n"
    	        + "    \"email\": \"sample@email.com\",\n"
    	        + "    \"status\": \"inactive\",\n"
    	        + "    \"gender\": \"male\"\n"
    	        + "}";
        System.out.println("Request body for missing required fields: " + requestBody);

        String response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "invalidtoken")
                .body(requestBody)
                .when()
                .put("/users/" + userId)
                .then()
                .assertThat().statusCode(401)
                .body("message", equalToIgnoringCase("Invalid Token"))
                .extract()
                .asString();
            	 System.out.println("User is unauthorized to perform this action: " + response);

        System.out.println("Test completed: TC05UpdateExistingUserWithInvalidToken");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC06UpdateExistingUserWithUnauthorizedUser() {
        System.out.println("Running test: TC06UpdateExistingUserWithUnauthorizedUser");

        String requestBody = "{\n"
    	        + "    \"name\": \"Sample Testname\",\n"
    	        + "    \"email\": \"sample@email.com\",\n"
    	        + "    \"status\": \"inactive\",\n"
    	        + "    \"gender\": \"male\"\n"
    	        + "}";
        System.out.println("Request body for missing required fields: " + requestBody);

        String response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put("/users/" + userId)
                .then()
    	        .assertThat().statusCode(404)
    	        .body("message", equalToIgnoringCase("Resource not found"))
    	        .extract()
    	        .response()
    	        .asString();
            	 System.out.println("User is unauthorized to perform this action: " + response);;

        System.out.println("Test completed: TC06UpdateExistingUserWithUnauthorizedUser");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC07UpdateExistingUserWithNameMoreThan200Char() {
        System.out.println("Running test: TC07UpdateExistingUserWithNameMoreThan200Char");

        User user = new User();
        user.name = faker.lorem().characters(201);
        user.gender = faker.options().option("male", "female");
        user.email = faker.internet().emailAddress();
        user.status = faker.options().option("active", "inactive");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for excessive character length: " + requestBody);
        } catch (Exception error) {
            System.err.println("Failed to serialize request body: " + error.getMessage());
            throw new RuntimeException("Failed to serialize request body", error);
        }

        RestAssured.given()
		        .header("Accept", "application/json")
		        .header("Content-Type", "application/json")
		        .header("Authorization", "Bearer " + TOKEN)
		        .body(requestBody)
		        .when()
		        .put("/users/" + userId)
		        .then()
		        .assertThat().statusCode(422)
		        .body("find { it.field == 'name' }.message", equalTo("is too long (maximum is 200 characters)"));
		
		        System.out.println("Test completed: TC07UpdateExistingUserWithNameMoreThan200Char");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC08UpdateExistingUserWithEmailMoreThan200Char() {
        System.out.println("Running test: TC08UpdateExistingUserWithEmailMoreThan200Char");

        User user = new User();
        user.name = faker.name().fullName();
        user.email = faker.lorem().characters(201) + "@example.com";
        user.gender = faker.options().option("male", "female");
        user.status = faker.options().option("active", "inactive");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for excessive character length: " + requestBody);
        } catch (Exception error) {
            System.err.println("Failed to serialize request body: " + error.getMessage());
            throw new RuntimeException("Failed to serialize request body", error);
        }

        RestAssured.given()
		        .header("Accept", "application/json")
		        .header("Content-Type", "application/json")
		        .header("Authorization", "Bearer " + TOKEN)
		        .body(requestBody)
		        .when()
		        .put("/users/" + userId)
		        .then()
		        .assertThat().statusCode(422)
		        .body("find { it.field == 'email' }.message", equalTo("is too long (maximum is 200 characters)"));
		
		        System.out.println("Test completed: TC08UpdateExistingUserWithEmailMoreThan200Char");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC09UpdateExistingUserWithGenderMoreThan200Char() {
        System.out.println("Running test: TC09UpdateExistingUserWithGenderMoreThan200Char");

        User user = new User();
        user.name = faker.name().fullName();
        user.email = faker.internet().emailAddress();
        user.gender = faker.lorem().characters(201);
        user.status = faker.options().option("active", "inactive");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for excessive character length: " + requestBody);
        } catch (Exception error) {
            System.err.println("Failed to serialize request body: " + error.getMessage());
            throw new RuntimeException("Failed to serialize request body", error);
        }

        RestAssured.given()
		        .header("Accept", "application/json")
		        .header("Content-Type", "application/json")
		        .header("Authorization", "Bearer " + TOKEN)
		        .body(requestBody)
		        .when()
		        .put("/users/" + userId)
		        .then()
		        .assertThat().statusCode(422)
		        .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"));
		
		        System.out.println("Test completed: TC09UpdateExistingUserWithGenderMoreThan200Char");
    }
    
    @Test(groups = {"update", "negative", "alltests"})
    public void TC10UpdateExistingUserWithStatusMoreThan200Char() {
        System.out.println("Running test: TC10UpdateExistingUserWithStatusMoreThan200Char");

        User user = new User();
        user.name = faker.name().fullName();
        user.email = faker.internet().emailAddress();
        user.gender = faker.options().option("male", "female");
        user.status = faker.lorem().characters(201);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for excessive character length: " + requestBody);
        } catch (Exception error) {
            System.err.println("Failed to serialize request body: " + error.getMessage());
            throw new RuntimeException("Failed to serialize request body", error);
        }

        RestAssured.given()
		        .header("Accept", "application/json")
		        .header("Content-Type", "application/json")
		        .header("Authorization", "Bearer " + TOKEN)
		        .body(requestBody)
		        .when()
		        .put("/users/" + userId)
		        .then()
		        .assertThat().statusCode(422)
		        .body("find { it.field == 'status' }.message", equalTo("can't be blank"));
		
		        System.out.println("Test completed: TC10UpdateExistingUserWithStatusMoreThan200Char");
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
