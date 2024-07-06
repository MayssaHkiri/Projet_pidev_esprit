package Utils;

import javafx.stage.Stage;

public class StageManager {
    private static StageManager instance = null;
    private Stage mainTeacherStage;
    private Stage mainStudentStage;

    private StageManager() {}

    public static StageManager getInstance() {
        if (instance == null) {
            instance = new StageManager();
        }
        return instance;
    }

    public void setMainTeacherStage(Stage stage) {
        this.mainTeacherStage = stage;
    }

    public Stage getMainTeacherStage() {
        return mainTeacherStage;
    }
    public Stage getMainStudentStage() {
        return mainTeacherStage;
    }
    public void setMainStudentStage(Stage stage) {
        this.mainTeacherStage = stage;
    }
}