package Controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;

public class MainScreenCon {

    @FXML
    Canvas viewport;

    @FXML
    Button comic;

    @FXML
    Button tDim;

    @FXML
    ToggleButton play;

    @FXML
    ChoiceBox<String> modeBox;


    public void initialize(){
        comic.setOnAction(t -> mode = true);
        tDim.setOnAction(t -> mode = false);
        modeBox.getItems().add("Green & Magenta & Reflect");
        modeBox.getItems().add("GreyV & Reflect");
        modeBox.getSelectionModel().select(0);
    }

    public void setImage(Image img){
        viewport.getGraphicsContext2D().clearRect(0, 0, img.getWidth(), img.getHeight());
        viewport.getGraphicsContext2D().drawImage(img, 0, 0, img.getWidth(), img.getHeight());
    }


    boolean mode = false;

    public boolean getComic(){
        return mode;
    }

    public boolean getPlay(){
        return play.isSelected();
    }

    public int getModeInt(){
        System.out.println(modeBox.getValue());
        return modeBox.getSelectionModel().getSelectedIndex();
    }

}
