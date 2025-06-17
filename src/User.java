import javax.swing.JOptionPane;

public class User {
    // This method is called by QuickChatApp to handle the user registration and login flow.
    // returns true if a user successfully registers and logs in, false otherwise.
    public static boolean handleUserRegistrationAndLogin() {
        registerUser user = new registerUser();
        String username;
        String password;
        String cellphone;
        String firstName;
        String lastName;
        String registrationResult;

        // --- Registration Flow ---
        boolean registered = false;
        while (!registered) {
            username = JOptionPane.showInputDialog("Enter username (must include _ and be ≤ 5 characters):");
            if (username == null) return false; // User cancelled registration

            password = JOptionPane.showInputDialog("Enter password (must contain uppercase, digit, special character, and at least 8 characters):");
            if (password == null) return false; // User cancelled registration

            cellphone = JOptionPane.showInputDialog("Enter cellphone number (format: +27831234567):");
            if (cellphone == null) return false; // User cancelled registration

            firstName = JOptionPane.showInputDialog("Enter first name:");
            if (firstName == null) return false; // User cancelled registration

            lastName = JOptionPane.showInputDialog("Enter last name:");
            if (lastName == null) return false; // User cancelled registration

            registrationResult = user.registerUser(username, password, cellphone, firstName, lastName);
            JOptionPane.showMessageDialog(null, registrationResult);

            if (registrationResult.equals("User registered successfully.")) {
                registered = true;
            } else {
                // If registration failed, give the option to retry or cancel
                int retryOption = JOptionPane.showConfirmDialog(null, "Registration failed. Do you want to try again?", "Registration Error", JOptionPane.YES_NO_OPTION);
                if (retryOption == JOptionPane.NO_OPTION) {
                    return false; // User chose not to retry registration
                }
            }
        }

        // --- Login Flow (only if registered successfully) ---
        String loginUsername;
        String loginPassword;
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            loginUsername = JOptionPane.showInputDialog("Login: Enter username:");
            if (loginUsername == null) return false; // User cancelled login

            loginPassword = JOptionPane.showInputDialog("Login: Enter password:");
            if (loginPassword == null) return false; // User cancelled login

            isLoggedIn = user.loginUser(loginUsername, loginPassword);
            JOptionPane.showMessageDialog(null, user.returnLoginStatus(isLoggedIn));

            if (!isLoggedIn) {
                // If login failed, give the option to retry or cancel
                int retryOption = JOptionPane.showConfirmDialog(null, "Login failed. Do you want to try again?", "Login Error", JOptionPane.YES_NO_OPTION);
                if (retryOption == JOptionPane.NO_OPTION) {
                    return false; // User chose not to retry login
                }
            }
        }
        return isLoggedIn; // Return true if logged in successfully
    }
}