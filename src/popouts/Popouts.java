package popouts;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Optional;

public class Popouts {

    public void showPopout(PopoutOptions options){
        Dialog dialog = new Dialog();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        switch (options){
            case ACTION_COMPLETE:
                dialog.setContentText("Action complete");
                dialog.setTitle("Success");
                break;

            case INVALID_PATH:
                dialog.setTitle("Error");
                dialog.setContentText("Invalid path");
                break;

            case RENAME_COMPLETE:
                dialog.setTitle("Success");
                dialog.setContentText("Renaming complete");
                break;

            case ACTION_FAILED:
                dialog.setTitle("Error");
                dialog.setContentText("Something went wrong");
                break;

            case NO_USAGE_FOUND:
                dialog.setTitle("Error");
                dialog.setContentText("No usage found");
        }

        Optional<ButtonType> show = dialog.showAndWait();
    }
}
