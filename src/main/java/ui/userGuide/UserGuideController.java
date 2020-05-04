package ui.userGuide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserGuideController implements Initializable {
    @FXML
    private Text guideText;

    @FXML
    private ImageView guideImage;

    @FXML
    private Label searchLabel;

    @FXML
    private Label editInfoLabel;

    @FXML
    private Label issuedLabel;

    @FXML
    private Label allBooksLabel;

    @FXML
    private Label changePassLabel;

    @FXML
    private Label generalLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image general = new Image("/fxml/userGuideImages/general.jpg");
        String generalText = "Welcome to the User Guide for your Personal Library Manager.\n" +
                "The features on the right are for managing your 'personal corner' of the library.\n" +
                "For more information on each of them, click on their respective guide sections.";
        Image issuedBooks = new Image("/fxml/userGuideImages/issuedBooks1.jpg");
        Image searchBooks = new Image("/fxml/userGuideImages/searchBooks.jpg");
        Image allBooks = new Image("/fxml/userGuideImages/allBooks.jpg");
        Image editPassword = new Image("/fxml/userGuideImages/editPassword.jpg");
        Image editInfo = new Image("/fxml/userGuideImages/editInfo.jpg");

        Map<Label, Image> pictureMap = new LinkedHashMap<>();
        Map<Label, String> descriptionMap = new LinkedHashMap<>();
        pictureMap.put(generalLabel, general);
        pictureMap.put(issuedLabel, issuedBooks);
        pictureMap.put(searchLabel, searchBooks);
        pictureMap.put(allBooksLabel, allBooks);
        pictureMap.put(changePassLabel, editPassword);
        pictureMap.put(editInfoLabel, editInfo);
        descriptionMap.put(generalLabel, generalText);
        descriptionMap.put(issuedLabel, "The 'My Issued Books' button opens a list of all your currently issued (borrowed) books.\n" +
                "Renewing your Book(s): By right-clicking on a book from the list, then selecting the Renew option.\n" +
                "You may renew each book up to 3 times, which adds 2 weeks to the Date Due (date it must be returned by) each time.");
        descriptionMap.put(searchLabel, "The 'Search Books' button opens a window where you can search for any books in the library.\n" +
                "You may search books according to the Title(or parts of), Author(or parts of), Publisher, Year Released, or Book ID.");
        descriptionMap.put(allBooksLabel, "The 'All Books' button opens a list of all the books currently in the library.\n" +
                "The list also displays the availability of each book.");
        descriptionMap.put(changePassLabel, "The 'Edit User Information' button opens a window where you can change your password or open the User Details editor.\n" +
                "Follow the instructions to change your password correctly, then click on the 'Save' button.");
        descriptionMap.put(editInfoLabel, "To change your User Details, open the 'Edit User Information' window, then click on the 'User Details' button.\n" +
                "You may modify your Full Name, Email address, Home address, Phone number.\n" +
                "Click on the 'Save' button to save your changes.");
        guideImage.setImage(general);
        guideText.setText(generalText);
        for(Label label : pictureMap.keySet()){
            label.setOnMouseClicked(e -> {
                guideImage.setImage(pictureMap.get(label));
                guideText.setText(descriptionMap.get(label));
            });

        }
    }
}
