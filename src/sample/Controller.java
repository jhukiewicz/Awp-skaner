package sample;

import popouts.PopoutOptions;
import popouts.Popouts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import scanners.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {


    @FXML
    Button selectFolderButton;

    @FXML
    TextField folderLocation;

    @FXML
    AnchorPane anchorId;

    @FXML
    Button searchTexturesButton;

    @FXML
    Button openTxtButton;

    @FXML
    Label dropTextures;

    @FXML
    TextArea textArea;

    @FXML
    TextField oldNameText;

    @FXML
    TextField newNameText;

    @FXML
    Button changeNameButton;


    final DirectoryChooser directoryChooser = new DirectoryChooser();

    Popouts popouts;


    public void initialize() {
        popouts = new Popouts();
        createOutputDirectory();
        searchTexturesButton.setOnAction(event -> handleSelectTexturesButton());
        selectFolderButton.setOnAction(event -> handleSelectFolderButton());
        openTxtButton.setOnAction(event -> handleSelectTxtButton());
        changeNameButton.setOnAction(event -> handleChangeNameButton());
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    public void handleDragDropped(DragEvent event) {
        List<File> fileList = event.getDragboard().getFiles();
        File file = fileList.get(0);
        String filePath = file.getPath();
        String fileName = filePath.substring(filePath.indexOf("textures") + 9).replace("\\", "/");


        Set<String> files = (Set<String>) FileScanner.search(FileScanner.scanAllAwpFiles(folderLocation.getText(), ".awp"), "/"+ fileName, SearchOptions.SEARCH_TEXTURE);

        if (folderLocation.getText().isEmpty()) {
            popouts.showPopout(PopoutOptions.INVALID_PATH);
        } else {
            if (Writer.writeAwpFiles(files, file.getName())) {
                textArea.setText(file.getName() + ": \n");
                for (String s : files) {
                    textArea.appendText(s + "\n");
                }
                popouts.showPopout(PopoutOptions.ACTION_COMPLETE);
            } else {
                popouts.showPopout(PopoutOptions.NO_USAGE_FOUND);
            }
        }
    }
    public void createOutputDirectory() {
        Path path = Paths.get("output");
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
//        e.printStackTrace();
        }
    }


    public void handleSelectTxtButton() {
        try {
            Desktop.getDesktop().open(new File("output"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleSelectFolderButton() {
        File file = directoryChooser.showDialog(new Stage());

        if (file != null) {
            folderLocation.setText(file.getAbsolutePath());
        }
    }


    public void handleSelectTexturesButton() {
        Map<String, Integer> list = ValuesComparator.sortByValue(Counter.countTextures(folderLocation.getText(), ""));

        if (list.isEmpty()) {
            popouts.showPopout(PopoutOptions.INVALID_PATH);
        } else {
            Writer.writeTextures(list);
            textArea.appendText("Textures: \n");
            list.forEach((key, value) -> textArea.appendText(key + " : " + value + "\n"));
            popouts.showPopout(PopoutOptions.ACTION_COMPLETE);
        }
    }

    public void handleChangeNameButton() {

        List<File> fileList = FileScanner.scanAllAwpFiles(folderLocation.getText(), ".awp");

        if (!oldNameText.getText().isEmpty()) {
            if (Replacement.replace(fileList, oldNameText.getText(), newNameText.getText(),folderLocation.getText())) {
                popouts.showPopout(PopoutOptions.ACTION_COMPLETE);
            }else {
                popouts.showPopout(PopoutOptions.ACTION_FAILED);
            }
        }
    }

}
