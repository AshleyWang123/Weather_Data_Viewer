package weather_platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Login {

    @FXML private TextField txtUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Button btnSubmit;
    @FXML private Button quit;

    @FXML
    protected void handleSubmit(ActionEvent event){
        Stage stage1=(Stage)btnSubmit.getScene().getWindow();
        if(txtUsername.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, stage1, "Name Error!",
                    "Please enter your name");
            return;
        }
        else if(pfPassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, stage1, "Password Error!",
                    "Please enter a password");
            return;
        }else if (txtUsername.getText().equals("test")&&pfPassword.getText().equals("123456")){
            try{

                stage1.close();
                Stage stage2=new Stage();
                Parent root=FXMLLoader.load(this.getClass().getResource("resources/Index.fxml"));
                Scene scene = new Scene(root,900,1000);
                stage2.setScene(scene);
                scene.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
                stage2.show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            showAlert(Alert.AlertType.CONFIRMATION, stage1, "Login Fail!",
                    "Please enter the correct name and password!");
        }
    }

    @FXML
    private void quit(ActionEvent event){
        Stage stage1=(Stage)quit.getScene().getWindow();
        stage1.close();
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
