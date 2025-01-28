package Tests;

import com.github.javafaker.Faker;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;

import static org.hamcrest.Matchers.*;

public class GoRestAPICreateUserTests {

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

    @Test(groups = {"create", "positive", "alltests"})
    public void TC01CreateAndVerifyUser() {
    	
    	System.out.println("Running test: TC01CreateAndVerifyUser");
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
            System.out.println("Test completed: TC01CreateAndVerifyUser");
        } catch (Exception error) {
            throw new RuntimeException("Failed to create and verify user", error);
        }
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC02CreateUserWithInvalidEmail() {
        System.out.println("Running test: TC02CreateUserWithInvalidEmail");
        
        User user = new User();
        user.email = "invalid-email";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for invalid email: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'email' }.message", equalTo("is invalid"));

        System.out.println("Test completed: TC02CreateUserWithInvalidEmail");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC03CreateUserWithInvalidStatus() {
        System.out.println("Running test: TC03CreateUserWithInvalidStatus");

        User user = new User();
        user.status = "invalid-status";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for invalid status: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'status' }.message", equalTo("can't be blank"));

        System.out.println("Test completed: TC03CreateUserWithInvalidStatus");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC04CreateUserWithInvalidGender() {
        System.out.println("Running test: TC04CreateUserWithInvalidGender");

        User user = new User();
        user.gender = "unknown";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for invalid gender: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"));

        System.out.println("Test completed: TC04CreateUserWithInvalidGender");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC05CreateUserWithBlankEmail() {
        System.out.println("Running test: TC05CreateUserWithBlankEmail");

        User user = new User();
        user.email = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for blank email: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'email' }.message", equalTo("can't be blank"));

        System.out.println("Test completed: TC05CreateUserWithBlankEmail");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC06CreateUserWithBlankStatus() {
        System.out.println("Running test: TC06CreateUserWithBlankStatus");

        User user = new User();
        user.status = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for blank status: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'status' }.message", equalTo("can't be blank"));

        System.out.println("Test completed: TC06CreateUserWithBlankStatus");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC07CreateUserWithBlankGender() {
        System.out.println("Running test: TC07CreateUserWithBlankGender");

        User user = new User();
        user.gender = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for blank gender: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"));

        System.out.println("Test completed: TC07CreateUserWithBlankGender");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC08CreateUserWithBlankName() {
        System.out.println("Running test: TC08CreateUserWithBlankName");

        User user = new User();
        user.name = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(user);
            System.out.println("Request body for blank name: " + requestBody);
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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'name' }.message", equalTo("can't be blank"));

        System.out.println("Test completed: TC08CreateUserWithBlankName");
    }

    @Test(groups = {"create", "negative", "alltests"})
    public void TC09CreateUserWithMissingRequiredFields() {
        System.out.println("Running test: TC09CreateUserWithMissingRequiredFields");

        String requestBody = "{}";
        System.out.println("Request body for missing required fields: " + requestBody);

        RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + TOKEN)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .assertThat().statusCode(422);

        System.out.println("Test completed: TC09CreateUserWithMissingRequiredFields");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC10CreateUserWithInvalidToken() {
        System.out.println("Running test: TC10CreateUserWithInvalidToken");

        String requestBody = "{}";
        System.out.println("Request body for missing required fields: " + requestBody);

        String response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "invalidtoken")
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .assertThat().statusCode(401)
                .body("message", equalToIgnoringCase("Invalid Token"))
                .extract()
                .asString();
            	 System.out.println("User is unauthorized to perform this action: " + response);

        System.out.println("Test completed: TC10CreateUserWithInvalidToken");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC11CreateUserWithUnauthorizedUser() {
        System.out.println("Running test: TC11CreateUserWithUnauthorizedUser");

        String requestBody = "{}";
        System.out.println("Request body for missing required fields: " + requestBody);

        String response = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/users")
                .then()
    	        .assertThat().statusCode(401)
    	        .body("message", equalToIgnoringCase("Authentication failed"))
    	        .extract()
    	        .response()
    	        .asString();
            	 System.out.println("User is unauthorized to perform this action: " + response);;

        System.out.println("Test completed: TC11CreateUserWithUnauthorizedUser");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC12CreateUserWithNameMoreThan200Char() {
        System.out.println("Running test: TC12CreateUserWithNameMoreThan200Char");

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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'name' }.message", equalTo("is too long (maximum is 200 characters)"));

        System.out.println("Test completed: TC12CreateUserWithNameMoreThan200Char");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC13CreateUserWithEmailMoreThan200Char() {
        System.out.println("Running test: TC13CreateUserWithEmailMoreThan200Char");

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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'email' }.message", equalTo("is too long (maximum is 200 characters)"));

        System.out.println("Test completed: TC13CreateUserWithEmailMoreThan200Char");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC14CreateUserWithGenderMoreThan200Char() {
        System.out.println("Running test: TC14CreateUserWithGenderMoreThan200Char");

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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'gender' }.message", equalTo("can't be blank, can be male of female"));

        System.out.println("Test completed: TC14CreateUserWithGenderMoreThan200Char");
    }
    
    @Test(groups = {"create", "negative", "alltests"})
    public void TC15CreateUserWithStatusMoreThan200Char() {
        System.out.println("Running test: TC15CreateUserWithStatusMoreThan200Char");

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
                .post("/users")
                .then()
                .assertThat().statusCode(422)
                .body("find { it.field == 'status' }.message", equalTo("can't be blank"));

        System.out.println("Test completed: TC15CreateUserWithStatusMoreThan200Char");
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
            	System.out.println("(Clean-up) User already deleted or not existing. Received status: " + statusCode);
            }

        } catch (Exception error) {
            throw new RuntimeException("Error occurred while deleting user", error);
        }
    }
}



