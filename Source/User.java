package passwordkeeper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class User {

    int id;
    String login;
    Password password; //TODO: Password validation
    java.io.File file;

    public User() {
    }

    public User(int id, String login, Password password) throws IOException {
        this.id = id;
        this.login = login;
        this.password = password;

        file = new java.io.File("user" + Integer.toString(id) + ".txt");//when user added file added

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter newUserAddtoDatabse = new FileWriter("users.txt", true); //add user to the users.txt file
        newUserAddtoDatabse.write(id); //
        newUserAddtoDatabse.write(login);
        newUserAddtoDatabse.write(password.toString());
    }

   //***********GETTERSETTERS******************
    int getId() {
        return id;
    }

    String getLogin() {
        return login;
    }

    Password getPassword() {
        return password;
    }

    void setId(int id) {
        this.id = id;
    }

    void setLogin(String login) {
        this.login = login;
    }

    void setPassword(Password password) {
        this.password = password;
    }

    //------------GETTERSETTERS--------------------
    public void addSite(Site site) throws IOException {
        //add new site to the userID file
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(Integer.toString(site.getID()) + System.getProperty("line.separator"));
            writer.write(site.getURL() + System.getProperty("line.separator"));
            writer.write(site.getLogin() + System.getProperty("line.separator"));
            writer.write(site.getPassword() + System.getProperty("line.separator"));
            writer.write(site.getNote() + System.getProperty("line.separator"));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void addNewWebsiteGUI(GridPane grid, int row, ComboBox listWebsites) {
        //************Attaching ADD NEW WEBSITE to gridpane************

        TextField URL = new TextField();
        TextField login = new TextField();
        PasswordField password = new PasswordField();
        TextField note = new TextField();
        Button addWebsite = new Button("Add");

        grid.add(new Label("Add new Website: "), 0, row);
        grid.add(new Label("URL:  "), 1, row);
        grid.add(URL, 2, row);
        grid.add(new Label("Login:  "), 3, row);
        grid.add(login, 4, row);
        grid.add(new Label("Password:  "), 5, row);
        grid.add(password, 6, row);
        grid.add(new Label("Notes:  "), 7, row);
        grid.add(note, 8, row);
        grid.add(addWebsite, 9, row);

        addWebsite.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                boolean isValidUrl = false;

                for (int i = URL.getText().length() - 1; i >= 1; i--) {//URL VALIDATION
                    if ((URL.getText().charAt(i) == '.') && (URL.getText().length() - i <= 4)) {

                        if (URL.getText().length() > 8) {
                            if ((URL.getText().substring(0, 8).equals("https://"))) {
                                isValidUrl = true;

                            }
                        }

                        if (URL.getText().length() > 8) {
                            if (URL.getText().substring(0, 7).equals("http://")) {
                                isValidUrl = true;

                            }
                        }

                    }
                }

                if ((URL.getText().isEmpty()) || (login.getText().isEmpty()) || (password.getText().isEmpty())) {
                    showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "Not Today", " Fill all forms");
                } else {
                    if (isValidUrl) {
                        showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "YOU ARE BRILIANT", " The website has been added to our database");
                        Site newSite = null;
                        try {
                            newSite = new Site(getNumberOfWebsites() + 1, URL.getText(), login.getText(), password.getText(), note.getText());

                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            addSite(newSite);
                            listWebsites.getItems().add(showWebsite(newSite.getID()).getURL());
                        } catch (IOException ex) {
                            Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, grid.getScene().getWindow(), "nooo", " URL Should start with http:// or https:// and should have .com (or any other) at the end");
                    }

                }

            }
        });

        //************NEW WEBSITE ADDED***************
    }

    public Site showWebsite(int websiteID) throws FileNotFoundException, IOException {
        //return SITE if only ID known
        String tempID;
        String tempURL;
        String tempLogin;
        String tempPassword;
        String tempNote;

        try (FileReader fr = new FileReader(file)) {
            Scanner scan = new Scanner(fr);
            tempID = null;
            tempURL = null;
            tempLogin = null;
            tempPassword = null;
            tempNote = null;

            for (int count = 1; count <= websiteID; count++) { //loops until websiteID is reached
                tempID = scan.nextLine();
                tempURL = scan.nextLine();
                tempLogin = scan.nextLine();
                tempPassword = scan.nextLine();
                tempNote = scan.nextLine();
            }
            fr.close();
        }
        int newTempID = Integer.parseInt(tempID);
        Site result = new Site(newTempID, tempURL, tempLogin, tempPassword, tempNote);
        return result;
        //list all websites from userID.txt
    }

    public void showWebsiteGUI(GridPane grid, int row, ComboBox listWebsites) throws IOException {
        //********SEARCH WEBSITE attached to gridpane************
        Button showCredentials = new Button("Show website's credentials");

        for (int i = 1; i <= getNumberOfWebsites(); i++) {
            listWebsites.getItems().add(showWebsite(i).getURL());
        }

        grid.add(new Label("Find password: "), 0, row + 2);
        grid.add(listWebsites, 1, row + 2);
        grid.add(showCredentials, 2, row + 2);

        showCredentials.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                GridPane searchCredentials = new GridPane();
                try {
                    String value = (String) listWebsites.getValue();

                    showCredentialsGUI(searchID(value), searchCredentials);
                } catch (IOException ex) {
                    Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //********SEARCH WEBSITE FINISH************
    }

    public void showCredentialsGUI(int websiteId, GridPane grid) throws IOException {
        //shows websites credentials
        Label headerLabel = new Label("Credentials for: " + showWebsite(websiteId).getURL());
        headerLabel.setFont(Font.font("Times new Roman", FontWeight.BOLD, 24));
        grid.add(headerLabel, 0, 0, 2, 1);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(new Label("Website: " + showWebsite(websiteId).getURL()), 1, 1);
        grid.add(new Label("Login: " + showWebsite(websiteId).getLogin()), 1, 2);
        grid.add(new Label("Password: " + showWebsite(websiteId).getPassword()), 1, 3);
        grid.add(new Label("Notes: " + showWebsite(websiteId).getNote()), 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        Stage newStage = new Stage();
        newStage.setTitle(showWebsite(websiteId).getURL() + " | Credentials ");

        newStage.setScene(scene);
        newStage.show();

        newStage.setScene(scene);
    }

    public void websiteSearch(GridPane grid, int row, Scene scene, Group root) throws IOException {
        //****************List al websites****************

        CheckBox showAll = new CheckBox("List all credentials");
        grid.add(showAll, 1, row + 4);

        GridPane showWebsites = new GridPane();

        for (int i = 1; i <= 19; i++) {
            showWebsites.add(new Label(""), 1, i);
        }

        listAllGUI(showWebsites, 20);

        showAll.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            if (new_val == true) {
                try {
                    listAllGUI(showWebsites, 20);
                } catch (IOException ex) {
                    Logger.getLogger(PasswordKeeper.class.getName()).log(Level.SEVERE, null, ex);
                }
                root.getChildren().add(showWebsites);
            }
            if (new_val == false) {
                root.getChildren().remove(showWebsites);
            }
        });

        //****************List al websites************
    }

    public int getNumberOfWebsites() throws FileNotFoundException, IOException {
        //returns number of websites
        int i;
        try (FileReader fr = new FileReader(file)) {
            Scanner scan = new Scanner(fr);
            i = 0;
            String temp;
            while (scan.hasNextLine()) {
                temp = scan.nextLine();
                i++;
            }
            fr.close();
        }

        return i / 5;

    }

    public int searchID(String url) throws FileNotFoundException, IOException {
        //search for websites id of url known
        int id1;
        boolean isFound = false;

        try (FileReader fr = new FileReader(file)) {
            Scanner scan = new Scanner(fr);
            String temp;

            id1 = 1;

            while (!isFound) {
                temp = scan.nextLine();//id
                temp = scan.nextLine(); //url
                if (temp.equals(url)) {
                    isFound = true;
                } else {
                    temp = scan.nextLine();//login
                    temp = scan.nextLine();//password
                    temp = scan.nextLine();//note
                    id1++;
                }
            }

            fr.close();
        }
        return id1;
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void listAllGUI(GridPane grid, int firstRow) throws IOException {
        //attaches websites to gridpane
        int k = 0;
        for (int i = 1; i <= getNumberOfWebsites(); i++) {
            grid.add(new Label("Website: " + showWebsite(i).getURL() + System.getProperty("line.separator")), 2, firstRow + k);
            grid.add(new Label("Login: " + showWebsite(i).getLogin() + System.getProperty("line.separator")), 2, firstRow + k + 1);
            grid.add(new Label("Password: " + showWebsite(i).getPassword() + System.getProperty("line.separator")), 2, firstRow + k + 2);
            grid.add(new Label("Notes: " + showWebsite(i).getNote() + System.getProperty("line.separator")), 2, firstRow + k + 3);
            grid.add(new Label(" "), 1, firstRow + k + 4);
            k = k + 5;
        }

    }

}
