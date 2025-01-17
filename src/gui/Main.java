package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import pimperium.Game;

import java.io.IOException;

/**
 * La classe principale permettant de lancer l'application avec une interface graphique en utilisant JavaFX.
 */

public class Main extends Application {

    /**
     * Point d'entrée principal de l'application JavaFX.
     * Configure et affiche l'interface graphique principale de l'application.
     *
     * @param primaryStage La fenêtre principale de l'application.
     * @throws IOException Si le fichier FXML ne peut pas être chargé.
     */

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Charger la vue depuis le fichier FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/hello-view.fxml"));
        Parent root = fxmlLoader.load();

        // Configurer le contrôleur et lier le jeu à l'interface graphique
        Controller controller = fxmlLoader.getController();
        Game game = Game.getInstance();
        controller.setGame(game);
        game.setController(controller);

        // Initialiser le jeu dans un thread séparé
        Thread guiThread = new Thread(game::setupGame);
        guiThread.setDaemon(true); // Permet au programme de se fermer si le thread principal termine
        guiThread.start();

        // Créer et configurer la scène
        Scene scene = new Scene(root);
        primaryStage.setResizable(false); // Empêcher le redimensionnement
        primaryStage.setFullScreen(false); // Désactiver le mode plein écran par défaut

        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.setFullScreen(false);
            }
        });

        // Bloquer le raccourci clavier pour le plein écran
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                event.consume(); // Bloque l'action
            }
        });

        // Configurer les propriétés de la fenêtre principale
        primaryStage.setTitle("Pocket Imperium");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Méthode principale pour lancer l'application.
     *
     * @param args Les arguments de la ligne de commande.
     */

    public static void main(String[] args) {
        launch(args);
    }
}
