package Controllers;

import Entities.Offre;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierOffre {
    private Stage dialogStage;
    private boolean okClicked = false;
    private Offre offre;

    @FXML
    private TextField dateLimiteOffre;

    @FXML
    private TextField datePublication;

    @FXML
    private TextField descriptionOffre;

    @FXML
    private TextField dureeContrat;

    @FXML
    private TextField nomEntreprise;

    @FXML
    private TextField titreOffre;

    @FXML
    private ChoiceBox<String> niveauEtude;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;

        titreOffre.setText(offre.getTitreOffre());
        nomEntreprise.setText(offre.getEntreprise());
        dureeContrat.setText(offre.getDureeContrat());
        descriptionOffre.setText(offre.getDescriptionOffre());
        datePublication.setText(offre.getDatePublication());
        dateLimiteOffre.setText(offre.getDateLimite());
    }

    @FXML
    private void handleSave() {
        offre.setTitreOffre(titreOffre.getText());
        offre.setEntreprise(nomEntreprise.getText());
        offre.setDureeContrat(dureeContrat.getText());
        offre.setDescriptionOffre(descriptionOffre.getText());
        offre.setDatePublication(datePublication.getText());
        offre.setDateLimite(dateLimiteOffre.getText());

        okClicked = true;
        dialogStage.close();
    }
}
