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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/hello-view.fxml"));
        Parent root = fxmlLoader.load();

        // Configurer la scène
        Controller controller = fxmlLoader.getController();
        Game game = Game.getInstance();
        controller.setGame(game);
        game.setController(controller);

        // Si tu utilises JavaFX
        Thread guiThread = new Thread(game::setupGame);
        guiThread.setDaemon(true); // Permet au programme de se fermer si le thread principal termine
        guiThread.start();

        Scene scene = new Scene(root);

        primaryStage.setResizable(false);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
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

        primaryStage.setTitle("Pocket Imperium");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
