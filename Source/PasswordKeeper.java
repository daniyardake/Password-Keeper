/*
 Every User has it's ID 

 */
package passwordkeeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author daniyar
 */
public class PasswordKeeper extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pass Keeper | Login");

        // Create the registration form grid pane
        GridPane gridPane = loginFormPane();
        // Add UI controls to the registration form grid pane
        loginGUI(gridPane);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(gridPane, 800, 500);
        // Set the scene in primary stage	

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private GridPane loginFormPane() {//LOGIN GUI
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints
        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void loginGUI(GridPane gridPane) {

        //**********************GUI***************************
        // Add Header
        Label headerLabel = new Label("Login");
        headerLabel.setFont(Font.font("Times new Roman", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Email Label
        Label loginLabel = new Label("Login: ");
        gridPane.add(loginLabel, 0, 2);

        // Add Email Text Field
        TextField loginField = new TextField();
        loginField.setPrefHeight(40);
        gridPane.add(loginField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        // Add Submit Button
        Button submitButton = new Button("Login");

        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);

        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

    //----------------------GUI---------------------------
        submitButton.setOnAction((ActionEvent event) -> {
            if (loginField.getText().isEmpty()) { //if login field is empty
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops", "Please enter your Login");
                return;
            } else if (passwordField.getText().isEmpty()) { //if password field is empty
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", " Please enter your password");
                return;
            } else {
                try {
                    if (isMatchingCombination(loginField.getText(), passwordField.getText())) {
                        //if match
                        int i;
                        String tempLogin;
                        String tempPass;
                        try (FileReader fr = new FileReader("users.txt")) {
                            Scanner scan = new Scanner(fr);
                            String temp = scan.nextLine();
                            temp = scan.nextLine();
                            i = 1;
                            while (!loginField.getText().equals(temp)) {
                                temp = scan.nextLine();
                                temp = scan.nextLine();
                                temp = scan.nextLine();
                                i = i + 1;
                            }
                            int newId = i;
                            tempLogin = temp;
                            tempPass = scan.next();
                            fr.close();
                        }

                        Password pass = new Password(tempPass);

                        User user = new User(i, tempLogin, pass);

                        GridPane splashPane = new GridPane();
                        splashGUI(user, splashPane);

                    } else {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", " There is no matching Login or Password in our database");
                        return;
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Button registerButton = new Button("Register");

        registerButton.setPrefHeight(40);
        registerButton.setDefaultButton(true);
        registerButton.setPrefWidth(100);

        gridPane.add(registerButton, 0, 4, 30, 1);

        GridPane.setHalignment(registerButton, HPos.CENTER);

        registerButton.setOnAction((ActionEvent event) -> {

            GridPane regPane = loginFormPane();
            registrationGUI(regPane);

        });

    }

    public void splashGUI(User user, GridPane grid) throws IOException {

        //*****************GUI********************
        Label headerLabel = new Label("Splash");
        headerLabel.setFont(Font.font("Times new Roman", FontWeight.BOLD, 24));
        grid.add(headerLabel, 0, 0, 2, 1);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label displayResult = new Label();
        grid.add(displayResult, 2, 6);
        Scene scene = new Scene(new Group(), 1600, 800);
        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        ComboBox listWebsites = new ComboBox();
        //---------------GUI-------------------------
        int row = 1;

        user.addNewWebsiteGUI(grid, row, listWebsites); // ADD NEW WEBSITE FUNCTIONALITY
        user.showWebsiteGUI(grid, row, listWebsites); // SHOW WEBSITE FUNCTIONALITY
        user.websiteSearch(grid, row, scene, root); // SEARCH WEBSITE FUNCTIONALITY

        //*****************GUI********************    
        Stage newStage = new Stage();
        newStage.setTitle(user.getLogin() + " | Splash ");
        newStage.setScene(scene);
        newStage.show();
        newStage.setScene(scene);
    //---------------GUI-------------------------

    }

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        //showing alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public int getNumberOfUsers() throws FileNotFoundException, IOException {
        //loops through all lines of users.txt and returns number of users
        int i = 0;
        try (FileReader fr = new FileReader("users.txt")) {
            Scanner scan = new Scanner(fr);
            i = 0;
            String temp;
            while (scan.hasNextLine()) {
                temp = scan.nextLine();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i / 3;
    }

    public void registerNewUser(User user) throws IOException {
        //adds user to the users file
        try (FileWriter newUserAddtoDatabse = new FileWriter("users.txt", true)) {
            newUserAddtoDatabse.write(user.getId() + System.getProperty("line.separator"));
            newUserAddtoDatabse.write(user.getLogin() + System.getProperty("line.separator"));
            newUserAddtoDatabse.write(user.getPassword().toString() + System.getProperty("line.separator"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new java.io.File("user" + Integer.toString(user.getId()) + ".txt");//when user added file added

    }

    private boolean isMatchingCombination(String login, String password) throws FileNotFoundException, IOException {
        //checks if login matches password
        boolean isMatchingCombination = false;

        try ( //java.io.File usersFile = new java.io.File("users.txt");
                FileReader fr = new FileReader("users.txt")) {
            Scanner scan = new Scanner(fr);
            int i = 1;

            while (scan.hasNextLine()) {//looping through all lines of the users file
                String tempLogin = scan.nextLine();
                if (i % 3 == 2) {//login lines
                    if (tempLogin.equals(login)) {//if matches then check password
                        String tempPass = scan.nextLine();
                        if (tempPass.equals(password)) {
                            isMatchingCombination = true;
                        }
                    }
                }
                i++;
            }
        }

        return isMatchingCombination;
    }

    private boolean isValidLogin(String login) throws FileNotFoundException, IOException {
        //checks if login is taken
        boolean isValid = true;

        try (FileReader fr = new FileReader("users.txt")) {
            Scanner scan = new Scanner(fr);
            int i = 1;
            String tempLogin;
            while (scan.hasNextLine()) {//loop through all lines
                tempLogin = scan.nextLine();
                if ((i % 3 == 2) && (tempLogin.equals(login))) { //checks only login lines
                    isValid = false;
                    break;
                }
                i++;
            }

            fr.close(); //close 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public void registrationGUI(GridPane gridPane) {

        //*********GUI of registration******
        Stage newStage = new Stage();
        newStage.setTitle(" Registration ");

        Scene scene = new Scene(gridPane, 800, 500);

        newStage.setScene(scene);
        newStage.show();

        newStage.setScene(scene);

        Label headerLabel = new Label("Registration");
        headerLabel.setFont(Font.font("Times new Roman", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));
        //------------GUI---------------------

        Label loginLabel = new Label("New Login: "); //Login label
        gridPane.add(loginLabel, 0, 2);

        // Add Email Text Field
        TextField loginField = new TextField();
        loginField.setPrefHeight(40);
        gridPane.add(loginField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("New Password : "); //Password Label
        gridPane.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        // Add Submit Button
        Button newButton = new Button("Register"); //Register Button

        newButton.setPrefHeight(40);
        newButton.setDefaultButton(true);
        newButton.setPrefWidth(100);
        gridPane.add(newButton, 0, 4, 2, 1);

        GridPane.setHalignment(newButton, HPos.CENTER);
        GridPane.setMargin(newButton, new Insets(20, 0, 20, 0));

        newButton.setOnAction((ActionEvent event) -> {

            if (loginField.getText().isEmpty()) { //if login is not field
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", "Please enter your Login");
                return;
            } else if (passwordField.getText().isEmpty()) { //if password is not field
                showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", " Please enter your password");
                return;
            } else {

                try {

                    if (isValidLogin(loginField.getText())) {//if login is not taken

                        if (isValidPassword(passwordField.getText())) {
                            newStage.close();
                            showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "YOU ARE BRILIANT", " You succesfully registered");
                            Password pass = new Password(passwordField.getText());
                            User user = new User(getNumberOfUsers() + 1, loginField.getText(), pass); //create new user

                            registerNewUser(user); //register to database
                            return;
                        } else {
                            showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", " Sorry, Password must be between 8 and 10 alphanumeric characters, have at least 1 capital\n"
                                    + "letter, 1 number, and one special character NOT a space");
                        }

                    } else {
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops...", " Sorry, this login is already taken");
                        return;
                    }

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    private boolean isValidPassword(String password) {
        //this function checks if password is valid
        boolean hasUpperCase = false;
        boolean hasDigit = false;

        boolean isLong = (password.length() >= 8) && (password.length() <= 10); //has 8 to 10 characters

        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) { //has at least one upper case
            if (!isLowerCase(password.charAt(i))) {
                hasUpperCase = true;
            }
            if (isDigit(password.charAt(i))) { //has at least one digit
                hasDigit = true;
            }
            int cint = (int) password.charAt(i);
            if (cint < 48 || (cint > 57 && cint < 65) || (cint > 90 && cint < 97) || cint > 122) {
                hasSpecial = true; // has special characters
            }

        }

        boolean isValid = hasUpperCase && hasDigit && isLong && hasSpecial; //if all validation passed
        return isValid;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
