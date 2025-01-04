package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
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
        primaryStage.setTitle("Pocket Imperium");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
