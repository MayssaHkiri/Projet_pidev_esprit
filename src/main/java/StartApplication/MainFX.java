package StartApplication;

import Controllers.MainStudentController;
import Controllers.MainTeacherController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainStudent.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainStudent.fxml"));
        stage.getIcons().add(new Image("/logo_esprit.png"));
        Parent root = null ;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Welcome page");
        stage.setScene(scene);
        stage.show();
/*
        String fxmlFile = loader.getLocation().getFile();
        if (fxmlFile.endsWith("mainTeacher.fxml")) {
            MainTeacherController controller = loader.getController();
            controller.setStage(stage);
        } else if (fxmlFile.endsWith("mainStudent.fxml")) {
            MainStudentController controller = loader.getController();
            controller.setStage(stage);
        }
*/

    }
}
