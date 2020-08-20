package Controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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


    public void initialize(){
        comic.setOnAction(t -> mode = true);
        tDim.setOnAction(t -> mode = false);
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

}
