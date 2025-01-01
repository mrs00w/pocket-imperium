package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pimperium.Game;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    private Game game;
    // FXML fields pour chaque label dans le fichier FXML
    @FXML
    private Label hex11Label;
    @FXML
    private Label hex21Label;
    @FXML
    private Label hex31Label;
    @FXML
    private Label hex41Label;
    @FXML
    private Label hex51Label;
    @FXML
    private Label hex61Label;
    @FXML
    private Label hex12Label;
    @FXML
    private Label hex22Label;
    @FXML
    private Label hex32Label;
    @FXML
    private Label hex42Label;
    @FXML
    private Label hex52Label;
    @FXML
    private Label hex13Label;
    @FXML
    private Label hex23Label;
    @FXML
    private Label hex33Label;
    @FXML
    private Label hex43Label;
    @FXML
    private Label hex53Label;
    @FXML
    private Label hex63Label;
    @FXML
    private Label hex14Label;
    @FXML
    private Label hex24Label;
    @FXML
    private Label hex34Label;
    @FXML
    private Label hex44Label;
    @FXML
    private Label hex54Label;
    @FXML
    private Label hex15Label;
    @FXML
    private Label hex25Label;
    @FXML
    private Label hex35Label;
    @FXML
    private Label hex45Label;
    @FXML
    private Label hex55Label;
    @FXML
    private Label hex65Label;
    @FXML
    private Label hex16Label;
    @FXML
    private Label hex26Label;
    @FXML
    private Label hex36Label;
    @FXML
    private Label hex46Label;
    @FXML
    private Label hex56Label;
    @FXML
    private Label hex17Label;
    @FXML
    private Label hex27Label;
    @FXML
    private Label hex37Label;
    @FXML
    private Label hex47Label;
    @FXML
    private Label hex57Label;
    @FXML
    private Label hex67Label;
    @FXML
    private Label hex18Label;
    @FXML
    private Label hex28Label;
    @FXML
    private Label hex38Label;
    @FXML
    private Label hex48Label;
    @FXML
    private Label hex58Label;
    @FXML
    private Label hex19Label;
    @FXML
    private Label hex29Label;
    @FXML
    private Label hex39Label;
    @FXML
    private Label hex49Label;
    @FXML
    private Label hex59Label;
    @FXML
    private Label hex69Label;


    // Map pour lier les IDs des hexes aux labels
    private final Map<Integer, Label> hexLabels = new HashMap<>();

    @FXML
    public void initialize() {
        // Associer chaque ID d'hex au label correspondant
        hexLabels.put(11, hex11Label);
        hexLabels.put(21, hex21Label);
        hexLabels.put(31, hex31Label);
        hexLabels.put(41, hex41Label);
        hexLabels.put(51, hex51Label);
        hexLabels.put(61, hex61Label);

        hexLabels.put(12, hex12Label);
        hexLabels.put(22, hex22Label);
        hexLabels.put(32, hex32Label);
        hexLabels.put(42, hex42Label);
        hexLabels.put(52, hex52Label);

        hexLabels.put(13, hex13Label);
        hexLabels.put(23, hex23Label);
        hexLabels.put(33, hex33Label);
        hexLabels.put(43, hex43Label);
        hexLabels.put(53, hex53Label);
        hexLabels.put(63, hex63Label);

        hexLabels.put(14, hex14Label);
        hexLabels.put(24, hex24Label);
        hexLabels.put(34, hex34Label);
        hexLabels.put(44, hex44Label);
        hexLabels.put(54, hex54Label);

        hexLabels.put(15, hex15Label);
        hexLabels.put(25, hex25Label);
        hexLabels.put(35, hex35Label);
        hexLabels.put(45, hex45Label);
        hexLabels.put(55, hex55Label);
        hexLabels.put(65, hex65Label);

        hexLabels.put(16, hex16Label);
        hexLabels.put(26, hex26Label);
        hexLabels.put(36, hex36Label);
        hexLabels.put(46, hex46Label);
        hexLabels.put(56, hex56Label);

        hexLabels.put(17, hex17Label);
        hexLabels.put(27, hex27Label);
        hexLabels.put(37, hex37Label);
        hexLabels.put(47, hex47Label);
        hexLabels.put(57, hex57Label);
        hexLabels.put(67, hex67Label);

        hexLabels.put(18, hex18Label);
        hexLabels.put(28, hex28Label);
        hexLabels.put(38, hex38Label);
        hexLabels.put(48, hex48Label);
        hexLabels.put(58, hex58Label);

        hexLabels.put(19, hex19Label);
        hexLabels.put(29, hex29Label);
        hexLabels.put(39, hex39Label);
        hexLabels.put(49, hex49Label);
        hexLabels.put(59, hex59Label);
        hexLabels.put(69, hex69Label);

    }

    public void setGame(Game game) {
        this.game = game;
    }

    // Mettre à jour le texte et la visibilité du Label
    public void updateHexLabel(int hexId, int shipCount) {
        Platform.runLater(() -> {
            Label label = hexLabels.get(hexId);
            if (label != null) {
                if (shipCount > 0) {
                    label.setText(String.valueOf(shipCount));
                    label.setVisible(true);
                } else {
                    label.setVisible(false); // Cacher le label s'il n'y a pas de vaisseaux
                }
            }
        });
    }
}
