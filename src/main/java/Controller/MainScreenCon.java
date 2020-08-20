package Controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;

public class MainScreenCon {

    @FXML
    Canvas viewport;

    @FXML
    CheckBox comic;

    @FXML
    CheckBox play;

    public void setImage(Image img){
        viewport.getGraphicsContext2D().clearRect(0, 0, img.getWidth(), img.getHeight());
        viewport.getGraphicsContext2D().drawImage(img, 0, 0, img.getWidth(), img.getHeight());
    }

    public boolean getComic(){
        return comic.isSelected();
    }

    public boolean getPlay(){
        return play.isSelected();
    }

}
