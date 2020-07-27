package main;

import Gpu.AparapiCalc;
import Model.MeatScene;
import Model.Meatball;
import Model.PixelGen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class AppLaunch extends Application {

    public static final int resX = 750;
    public static final int resY = 750;

    Canvas canvas = new Canvas(resX, resY);

    MeatScene scene = new MeatScene();

    AparapiCalc calc;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new HBox(canvas)));
        stage.show();
        Random ran = new Random();
        for (int i = 0; i < 20; i++){
            scene.addBall(new Meatball(ran.nextFloat()*6-3, ran.nextFloat()*6-3, ran.nextFloat()*4-2));
        }
        calc = new AparapiCalc(scene);
        //canvas.getGraphicsContext2D().drawImage(render2D(scene, 0), 0, 0);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                step();
            }
        };

        ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
        exe.scheduleWithFixedDelay(run, 0, 1, TimeUnit.MILLISECONDS);
        stage.setOnCloseRequest(t -> {
            calc.dispo();
            exe.shutdown();
        });
    }

    Image renderGpu(MeatScene scenetr, float t){
        scenetr.update(t);
        WritableImage wimg = new WritableImage(resX, resY);
        float[] colors = calc.calcColors(scene);
        for(int i = 0; i < resX; i++) {
            for (int j = 0; j < resY; j++) {
                wimg.getPixelWriter().setColor(i, j, Color.color(colors[(j*resY+i)*3], colors[(j*resY+i)*3+1], colors[(j*resY+i)*3+2]));
            }
        }
        return wimg;
    }

    Image render2D(MeatScene scenetr, float t){
        float[][] pixel = new float[resX][resY];
        int[][] positions = new int[resY*resX][2];
        for (int i = 0; i < resX; i++){
            for (int j = 0; j < resY; j++){
                positions[i*resY + j][0] = i;
                positions[i*resY + j][1] = j;
                //System.out.println(positions[i*resY + j][0] + ";" + positions[i*resY + j][1] );
            }
        }
        scenetr.update(t);
        Arrays.stream(positions).parallel().forEach(pos -> pixel[pos[0]][pos[1]] = scenetr.inReach2D((10f*pos[0]/resX)-5, (10f*pos[1]/resY)-5));

        WritableImage wimg = new WritableImage(resX, resY);
        for(int i = 0; i < resX; i++){
            for (int j = 0; j < resY; j++){
                if (pixel[i][j] > 10) {
                    wimg.getPixelWriter().setColor(i, j, Color.color(1, 0.8, 1));
                } else if (pixel[i][j] > 9.5){
                    float v = (float)Math.min(1, (pixel[i][j]-9.5)*2);
                    wimg.getPixelWriter().setColor(i, j, Color.color(v, v*0.8, 1));
                } else if (pixel[i][j] > 3){
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, 0, 1));
                } else if (pixel[i][j] > 2.9){
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, 0, Math.min(1, (pixel[i][j]-2.9)*10)));
                } else if (pixel[i][j] > 2.8){
                    float v = (float)Math.min(1, (2.9-pixel[i][j])*10);
                    wimg.getPixelWriter().setColor(i, j, Color.color(v, v, v));
                } else if (pixel[i][j] < -10) {
                    wimg.getPixelWriter().setColor(i, j, Color.color(0.8, 1, 0.7));
                } else if (pixel[i][j] < -9.5){
                    float v = (float)Math.min(1, (pixel[i][j]+9.5)*-2);
                    wimg.getPixelWriter().setColor(i, j, Color.color(0.8*v, 1, 0.7*v));
                } else if (pixel[i][j] < -3){
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, 1, 0));
                } else if (pixel[i][j] < -2.9){
                    wimg.getPixelWriter().setColor(i, j, Color.color(0, Math.min(1, (pixel[i][j]+2.9)*-10), 0));
                } else if (pixel[i][j] < -2.8){
                    float v = (float)Math.min(1, (2.9+pixel[i][j])*10);
                    wimg.getPixelWriter().setColor(i, j, Color.color(v, v, v));
                }

            }
        }

        return wimg;
    }

    int mSteps = 100;
    int stepsLeft = 0;

    void step(){
        //long s = System.currentTimeMillis();
        if (stepsLeft > mSteps){
            stepsLeft = 0;
            scene.setPosition();
            scene.randomize();
        } else {
            stepsLeft++;
        }
        //Image img = render2D(scene, stepsLeft/(float)mSteps);
        Image img = renderGpu(scene, stepsLeft/(float)mSteps);
        Platform.runLater(() ->{
            canvas.getGraphicsContext2D().clearRect(0, 0, resX, resY);
            canvas.getGraphicsContext2D().drawImage(img, 0, 0);
        });
        //System.out.println("Time:" + (System.currentTimeMillis()-s));
    }


}
