package Gpu;

import Model.MeatScene;
import Model.Meatball;
import com.aparapi.Kernel;
import com.aparapi.Range;
import main.AppLaunch;
import scala.App;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class AparapiCalc {

    kernelKlasse kernel;
    final int resX = AppLaunch.resX;
    final int resY = AppLaunch.resY;
    MeatScene scene;

    public AparapiCalc(MeatScene scene){
        this.scene = scene;
        kernel = new kernelKlasse(scene);
        System.out.println("kernel");
    }

    public float[] calcColors(MeatScene scene){
        ArrayList<Meatball> tmp = scene.getMeatballs();
        for (int i = 0; i < kernel.countB; i++){
            kernel.balls[i*3] = tmp.get(i).getPos()[0];
            kernel.balls[i*3+1] = tmp.get(i).getPos()[1];
            kernel.balls[i*3+2] = tmp.get(i).getPos()[2];
        }
        long s = System.currentTimeMillis();
        //System.out.println("blubb");
        kernel.execute(Range.create(AppLaunch.resX*AppLaunch.resY));
        //System.out.println("Execution mode = "+ kernel.getTargetDevice());
        System.out.println("RT: " + (System.currentTimeMillis()-s));

        return kernel.colors;
    }

    public void dispo(){
        kernel.dispose();
    }


}
