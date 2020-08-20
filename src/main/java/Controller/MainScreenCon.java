package Controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class MainScreenCon {

    @FXML
    Canvas viewport;

    public void setImage(Image img){
        viewport.getGraphicsContext2D().clearRect(0, 0, img.getWidth(), img.getHeight());
        viewport.getGraphicsContext2D().drawImage(img, 0, 0, img.getWidth(), img.getHeight());
    }

}
