import java.util.regex.Pattern;

public class registerUser {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // Check the username format (underscore and ≤5 characters)
    public boolean checkUserName(String username) {
        // Returns true if the username is NOT correctly formatted.
        // Correct format: contains '_' AND length <= 5
        return username == null || !username.contains("_") || username.length() > 5;
    }

    // Check password complexity (contain uppercase, digit and a special character)
    public boolean checkPasswordComplexity(String password) {
        // Regex: at least 8 characters, at least one uppercase, at least one digit, at least one special character (@$!%*?&)
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        // Returns true if the password is NOT correctly formatted (i.e., does not match the regex).
        return password == null || !Pattern.matches(regex, password);
    }

    // Validate SA cellphone number
    public boolean checkCellPhoneNumber(String cellphone) {
        // Regex: starts with +27 followed by 9 digits
        String regex = "^\\+27\\d{9}$";
        // Returns true if the cellphone number is NOT correctly formatted (i.e., does not match the regex).
        return cellphone == null || !Pattern.matches(regex, cellphone);
    }

    // Register user with validation messages
    public String registerUser(String username, String password, String cellphone, String firstName, String lastName) {
        if (checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that username contains an underscore and is no more than five characters in length.";
        }
        if (checkPasswordComplexity(password)) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (checkCellPhoneNumber(cellphone)) {
            return "Cellphone number incorrectly formatted or does not contain international code.";
        }
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        return "User registered successfully.";
    }

    // Verify login credentials
    public boolean loginUser(String username, String password) {
        // Ensure that stored username and password are not null before comparison
        return this.username != null && this.username.equals(username) &&
                this.password != null && this.password.equals(password);
    }

    // Return login status message
    public String returnLoginStatus(boolean isLoggedIn) {
        return isLoggedIn ? "Welcome " + firstName + " " + lastName + " it is great to see you again" : "Username or password is incorrect, please try again.";
    }
}

