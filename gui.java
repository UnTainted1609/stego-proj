import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class gui extends Application {

    private TextField sentenceField;
    private TextField inputVigenereKeyField;
    private PasswordField inputAesKeyField;
    private TextField outputVigenereKeyField;
    private PasswordField outputAesKeyField;
    private TextArea decryptedSentenceArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Steganography Application");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels and TextFields/PasswordField for input
        Label sentenceLabel = new Label("Enter Sentence:");
        GridPane.setConstraints(sentenceLabel, 0, 0);
        sentenceField = new TextField();
        GridPane.setConstraints(sentenceField, 1, 0);

        Label inputVigenereKeyLabel = new Label("Input Vigenere Key:");
        GridPane.setConstraints(inputVigenereKeyLabel, 0, 1);
        inputVigenereKeyField = new TextField();
        GridPane.setConstraints(inputVigenereKeyField, 1, 1);

        Label inputAesKeyLabel = new Label("Input AES Key (16 characters):");
        GridPane.setConstraints(inputAesKeyLabel, 0, 2);
        inputAesKeyField = new PasswordField();
        GridPane.setConstraints(inputAesKeyField, 1, 2);

        // Labels and TextFields/PasswordField for output
        Label outputVigenereKeyLabel = new Label("Output Vigenere Key:");
        GridPane.setConstraints(outputVigenereKeyLabel, 0, 3);
        outputVigenereKeyField = new TextField();
        GridPane.setConstraints(outputVigenereKeyField, 1, 3);

        Label outputAesKeyLabel = new Label("Output AES Key (16 characters):");
        GridPane.setConstraints(outputAesKeyLabel, 0, 4);
        outputAesKeyField = new PasswordField();
        GridPane.setConstraints(outputAesKeyField, 1, 4);

        // Encode and Decode buttons
        Button encodeButton = new Button("Encode");
        GridPane.setConstraints(encodeButton, 0, 5);
        encodeButton.setOnAction(e -> encodeButtonClicked());

        Button decodeButton = new Button("Decode");
        GridPane.setConstraints(decodeButton, 1, 5);
        decodeButton.setOnAction(e -> decodeButtonClicked());

        // TextArea to display decrypted sentence
        decryptedSentenceArea = new TextArea();
        decryptedSentenceArea.setEditable(false);
        decryptedSentenceArea.setWrapText(true);
        GridPane.setConstraints(decryptedSentenceArea, 0, 6, 2, 1);

        grid.getChildren().addAll(sentenceLabel, sentenceField, inputVigenereKeyLabel, inputVigenereKeyField,inputImagePathkey,
                inputAesKeyLabel, inputAesKeyField, outputVigenereKeyLabel, outputVigenereKeyField,
                outputAesKeyLabel, outputAesKeyField, encodeButton, decodeButton, decryptedSentenceArea);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void encodeButtonClicked() {
        // Implement encryption process here
        String sentence = sentenceField.getText();
        String inputVigenereKey = inputVigenereKeyField.getText();
        String inputAesKey = inputAesKeyField.getText();
        String inputImagePath = inputImagePathkey.getText();
    }

    private void decodeButtonClicked() {
        // Implement decryption process here
        String outputVigenereKey = outputVigenereKeyField.getText();
        String outputAesKey = outputAesKeyField.getText();
        // Perform decryption process
        // If output keys match input keys, display decrypted sentence
        decryptedSentenceArea.setText("Decrypted sentence will be displayed here.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}