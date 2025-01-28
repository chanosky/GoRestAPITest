
# API Automation Test for Go Rest API

github flow sample

*This is a robust and maintainable API testing framework built using **Rest Assured**. It is designed to ensure the reliability and functionality of RESTful services while incorporating best practices and advanced features to streamline testing and improve reporting.*

---

## Key Features

### 1. BDD Approach
The framework leverages the **Behavior-Driven Development (BDD)** style built into Rest Assured. Tests are written using the `given-when-then` syntax, making them clear and readable.

### 2. Setup and Teardown Management
To ensure clean test execution, the framework includes **BeforeMethod and AfterMethod** classes. These handle:
- **Setup**: Initializing test data before each test method.
- **Teardown**: Cleaning up garbage data after each test method to prevent residual data from affecting subsequent tests.

### 3. Dynamic Test Data Generation
For test cases requiring dynamic data, the **Faker library** is integrated. This allows the framework to generate realistic and randomized test data on the fly, enhancing test coverage and reducing dependencies on static data.

### 4. Step-by-Step Execution Logs
The framework includes **print logs** at key steps during test execution. These logs provide visibility into the test flow, making it easier for users to understand what is happening at each stage and to debug issues if they arise.

### 5. Test Case Grouping by Functionality
Test cases are logically grouped based on their functionality, such as:
- **READ**
- **CREATE**
- **UPDATE**
- **DELETE**

This modular approach improves organization and makes it easier to execute specific subsets of tests based on the functionality being validated.

### 6. Comprehensive Reporting with Extent Reports
The framework integrates **Extent Reports** to generate detailed and visually appealing test reports. These reports include:
- Pass/fail status
- Step-by-step logs
- Graph of test results

These reports are invaluable for analyzing test results and sharing them with stakeholders.

---

## Key Benefits
- **Readability**: BDD-style tests and clear logs make the framework easy to understand and use.
- **Maintainability**: Modular design and dynamic data generation reduce the effort required to update tests.
- **Reliability**: Setup and teardown mechanisms ensure consistent and clean test execution.
- **Actionable Insights**: Extent Reports provide detailed and actionable test results for better decision-making.

---

## Test Cases
The script tests the Go Rest API **users/** endpoints using a variety of test cases, including:

### READ Operations
- `TC01VerifyUserDataById`
- `TC02VerifyUserDataByInvalidUserId`

### CREATE Operations
- `TC01CreateAndVerifyUser`
- `TC02CreateUserWithInvalidEmail`
- `TC03CreateUserWithInvalidStatus`
- `TC04CreateUserWithInvalidGender`
- `TC05CreateUserWithBlankEmail`
- `TC06CreateUserWithBlankStatus`
- `TC07CreateUserWithBlankGender`
- `TC08CreateUserWithBlankName`
- `TC09CreateUserWithMissingRequiredFields`
- `TC10CreateUserWithInvalidToken`
- `TC11CreateUserWithUnauthorizedUser`
- `TC12CreateUserWithNameMoreThan200Char`
- `TC13CreateUserWithEmailMoreThan200Char`
- `TC14CreateUserWithGenderMoreThan200Char`
- `TC15CreateUserWithStatusMoreThan200Char`

### UPDATE Operations
- `TC01UpdateExistingUser`
- `TC02UpdateUserUsingBlankData`
- `TC03UpdateUserUsingInvalidData`
- `TC04UpdateNonExistingUser`
- `TC05UpdateExistingUserWithInvalidToken`
- `TC06UpdateExistingUserWithUnauthorizedUser`
- `TC07UpdateExistingUserWithNameMoreThan200Char`
- `TC08UpdateExistingUserWithEmailMoreThan200Char`
- `TC09UpdateExistingUserWithGenderMoreThan200Char`
- `TC10UpdateExistingUserWithStatusMoreThan200Char`

### DELETE Operations
- `TC01CreateAndDeleteUser`
- `TC02DeleteNonExistingUser`
- `TC03DeleteUserWithUnauthorizedUser`
- `TC04DeleteUserWithInvalidToken`

---

## Prerequisites

Before running the script, ensure the following prerequisites are met:

1. **Install Java**:
   - Download and install the latest version of Java (JDK) from the official website: [Java Downloads](https://www.oracle.com/java/technologies/javase-downloads.html).
   - Set the `JAVA_HOME` environment variable.
   - Verify the installation by running `java -version` in the command line.

2. **Install Maven**:
   - Download and install the latest version of Maven from the official website: [Maven Downloads](https://maven.apache.org/download.cgi).
   - Set the `MAVEN_HOME` environment variable.
   - Verify the installation by running `mvn -version` in the command line.

---

## How to Run the API Automation Test

Follow these steps to fetch the Go Rest API Automation test from GitHub, install the necessary requirements, and run the script:

1. Open a command prompt or terminal window.
2. Navigate to the directory where you want to store the script.
3. Clone the repository using the following command:
  ```
   git clone https://github.com/chanosky/GoRestAPITest.git
```
4. Alternatively, download the repository manually and extract it to your desired location.  
5. Navigate into the cloned repository (ensure you are in the `/GoTestExam` folder).  
6. Install the necessary dependencies by running:
 ```
   mvn clean install
```
7. Run the automation tests using one of the following commands:
```
mvn test -Dgroups="alltests"
- used to run all tests

mvn test -Dgroups="negative"
- used to run negative test cases

mvn test -Dgroups="positive"
- used to negative test cases

mvn test -Dgroups="get"
- used to run all View User tests

mvn test -Dgroups="create"
- used to run all Create User tests
 
mvn test -Dgroups="update"
- used to run all Update User tests

mvn test -Dgroups="delete"
-used to run all Delete User tests
```

## Limitations

This script has the following limitations:

1.  The script has only been tested on a  **Windows 11**  operating system. Compatibility issues may arise when running the script on other operating systems such as macOS or Linux.
    
2.  CI/CD integration for this script has not yet been set up.

## Bugs and Issues Found

1.  **Validation Message Bugs**:
    
    -   `"can't be blank"` is returned when testing the status field using many characters OR when the status is not `'active'` or `'inactive'`
        
    - `"can't be blank, can be male of female"` wrong validation message
        
2.  **Name Field Validation**:
    
    -   The  `name`  field accepts any characters without proper validation, which should not be the case.

## Notes

-   Do not modify files inside the folders. You  **CAN**  change the  `TOKEN`  in the  `Tests/`  scripts to use your own token for authorization purposes. The provided token may expire in the future.
    
-   A function has been created to randomize data when creating users for testing purposes.