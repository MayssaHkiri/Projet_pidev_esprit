package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VoirPlusController {

    @FXML
    private Label entrepriseLabel;
    @FXML
    private Label titreOffreLabel;
    @FXML
    private Label descriptionOffreLabel;
    @FXML
    private Label niveauEtudeLabel;
    @FXML
    private Label dureeContratLabel;
    @FXML
    private Label datePublicationLabel;
    @FXML
    private Label dateLimiteLabel;

    public void setEntreprise(String entreprise) {
        entrepriseLabel.setText(entreprise);
    }

    public void setTitreOffre(String titre) {
        titreOffreLabel.setText(titre);
    }

    public void setDescriptionOffre(String description) {
        descriptionOffreLabel.setText(description);
    }

    public void setNiveauEtude(String niveau) {
        niveauEtudeLabel.setText(niveau);
    }

    public void setDureeContrat(String duree) {
        dureeContratLabel.setText(duree);
    }

    public void setDatePublication(String date) {
        datePublicationLabel.setText(date);
    }

    public void setDateLimite(String date) {
        dateLimiteLabel.setText(date);
    }

    @FXML
    private void handlePostuler() {
        String recipient = "entreprise@gmail.com"; // Remplacez par l'adresse e-mail de destination
        String subject = "Candidature pour le poste";
        String body = "Bonjour,\n\nJe souhaite postuler pour le poste chez " + entrepriseLabel.getText() + ".\n\nCordialement.";

        String mailto = "mailto:" + recipient + "?subject=" + encodeURIComponent(subject) + "&body=" + encodeURIComponent(body);

        try {
            Desktop.getDesktop().mail(new URI(mailto));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String encodeURIComponent(String value) {
        String result;
        try {
            result = java.net.URLEncoder.encode(value, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (Exception e) {
            result = value;
        }
        return result;
    }
}
