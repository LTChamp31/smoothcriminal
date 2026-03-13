package it.volta.smoothcriminal.gui;

import it.volta.smoothcriminal.console.GameConsole;
import it.volta.smoothcriminal.model.Gadget;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.sound.sampled.Clip;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class GUIApplication extends Application {

    private Stage primaryStage;
    private GUIGameController gameController;
    private GameRenderer gameRenderer;
    private Canvas gameCanvas;
    private Label infoLabel;
    private Clip bgMusic;

    public void start(Stage stage) throws Exception {
        // This looks for the file in your resources
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /*@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Smooth Criminal - JavaFX GUI");
        this.gameController = new GUIGameController(this);
        
        showMainMenu();
        primaryStage.show();
    }*/

    public void showMainMenu() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label title = new Label("SMOOTH CRIMINAL");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button btnStory = new Button("1) Storia");
        btnStory.setOnAction(e -> promptNameAndStartStory());

        Button btnTraining = new Button("2) Allenamento");
        btnTraining.setOnAction(e -> gameController.startTrainingMode());

        Button btnTournament = new Button("3) Torneo");
        btnTournament.setOnAction(e -> promptNameAndStartTournament());

        Button btnMusic = new Button("4) Musica");
        btnMusic.setOnAction(e -> toggleMusic());

        Button btnControls = new Button("5) Comandi");
        btnControls.setOnAction(e -> showControls());

        Button btnExit = new Button("6) Esci");
        btnExit.setOnAction(e -> System.exit(0));

        String buttonStyle = "-fx-font-size: 16px; -fx-background-color: #34495e; -fx-text-fill: white; -fx-pref-width: 200px; -fx-pref-height: 40px;";
        
        for (Button b : new Button[]{btnStory, btnTraining, btnTournament, btnMusic, btnControls, btnExit}) {
            b.setStyle(buttonStyle);
            b.setOnMouseEntered(e -> b.setStyle("-fx-font-size: 16px; -fx-background-color: #2c3e50; -fx-text-fill: white; -fx-pref-width: 200px; -fx-pref-height: 40px;"));
            b.setOnMouseExited(e -> b.setStyle(buttonStyle));
        }

        root.getChildren().addAll(title, btnStory, btnTraining, btnTournament, btnMusic, btnControls, btnExit);
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
    }

    private void promptNameAndStartStory() {
        TextInputDialog dialog = new TextInputDialog("Giocatore");
        dialog.setTitle("Modalità Storia");
        dialog.setHeaderText("Inserisci il tuo nome:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            int level = gameController.caricaProgressoStoria(name);
            if (level > 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Salvataggio Trovato");
                alert.setHeaderText("Hai raggiunto il livello " + level);
                alert.setContentText("Vuoi riprendere da questo livello (OK) o ricominciare (Cancel)?");
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.OK) {
                    gameController.startStoryMode(name, level);
                } else {
                    gameController.startStoryMode(name, 1);
                }
            } else {
                gameController.startStoryMode(name, 1);
            }
        });
    }

    private void promptNameAndStartTournament() {
        TextInputDialog dialog = new TextInputDialog("Giocatore");
        dialog.setTitle("Modalità Torneo");
        dialog.setHeaderText("Inserisci il tuo nome:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> gameController.startTournamentMode(name));
    }

    public void showGameScreen() {
        BorderPane root = new BorderPane();
        
        infoLabel = new Label("Usa Frecce/WASD per muoverti, numeri per i gadget. Premi X per uscire.");
        infoLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #ecf0f1; -fx-alignment: center;");
        infoLabel.setMaxWidth(Double.MAX_VALUE);
        root.setTop(infoLabel);

        int cols = gameController.getMaze().getColonne();
        int rows = gameController.getMaze().getRighe();
        gameCanvas = new Canvas(cols * 30, rows * 30);
        gameRenderer = new GameRenderer(gameCanvas);
        
        VBox canvasContainer = new VBox(gameCanvas);
        canvasContainer.setAlignment(Pos.CENTER);
        canvasContainer.setStyle("-fx-background-color: #bdc3c7; -fx-padding: 20px;");
        root.setCenter(canvasContainer);

        // Responsive scene size but min values
        Scene scene = new Scene(root, Math.max(800, cols * 30 + 100), Math.max(600, rows * 30 + 100));
        scene.setOnKeyPressed(event -> {
            gameController.handleKeyPress(event);
            event.consume();
        });
        
        primaryStage.setScene(scene);
        
        // Force focus on root to ensure key events are captured immediately
        root.requestFocus();
        
        updateGameScreen();
    }

    public void updateGameScreen() {
        gameRenderer.render(gameController.getMaze(), gameController.getCriminal());
        
        StringBuilder gadgets = new StringBuilder("Gadget disponibili: ");
        if (gameController.getCriminal().getGadgetCriminal()) {
            boolean hasGadgets = false;
            for (Gadget g : gameController.getCriminal().getGadgetUtilizzabili()) {
                if (g != null) {
                    hasGadgets = true;
                    gadgets.append(g.getNome()).append(" [Tasto: ").append(g.getTasto()).append("] | ");
                }
            }
            if (!hasGadgets) gadgets.append("Nessuno");
            else gadgets.setLength(gadgets.length() - 3); // remove trailing " | "
        } else {
            gadgets.append("Nessuno");
        }
        
        infoLabel.setText(gadgets.toString() + "\nUsa WASD per muoverti, numeri per i gadget. Premi X per uscire.");
    }

    public String askDirectionForGadget(String gadgetNome) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("W", "W", "A", "S", "D", "Q", "E", "Z", "C");
        dialog.setTitle("Usa Gadget");
        dialog.setHeaderText("Uso di " + gadgetNome);
        dialog.setContentText("Scegli la direzione dal menu a tendina e premi OK:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public void showDefeatScreen() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sconfitta");
        alert.setHeaderText("Hai perso!");
        alert.setContentText(readTextFile("resources/schermate/Sconfitta.txt"));
        alert.showAndWait();
        showMainMenu();
    }

    public void showVictoryScreen(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vittoria");
        alert.setHeaderText("Livello Completato!");
        alert.setContentText(message);
        alert.showAndWait();
        showMainMenu();
    }

    private void showControls() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Comandi");
        alert.setHeaderText("Comandi di Gioco");
        alert.setContentText(readTextFile("resources/schermate/comandi.txt"));
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }

    private void toggleMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
        } else {
            bgMusic = GameConsole.PlayMusic("resources/sound/smooth_criminal.wav");
        }
    }

    private String readTextFile(String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "File " + path + " non trovato oppure formato non testuale convenzionale.";
        }
        return sb.toString();
    }
}
