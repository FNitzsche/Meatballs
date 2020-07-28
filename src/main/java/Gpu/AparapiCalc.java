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

    //kernelKlasse kernel;
    ValueKernel vKernel;
    final int resX = AppLaunch.resX;
    final int resY = AppLaunch.resY;
    MeatScene scene;

    public AparapiCalc(MeatScene scene){
        this.scene = scene;
        //kernel = new kernelKlasse(scene);
        vKernel = new ValueKernel(scene);
        System.out.println("kernel");
    }

    public float[] calcColors(MeatScene scene){
        ArrayList<Meatball> tmp = scene.getMeatballs();
        for (int i = 0; i < vKernel.countB; i++){
            vKernel.balls[i*3] = tmp.get(i).getPos()[0];
            vKernel.balls[i*3+1] = tmp.get(i).getPos()[1];
            vKernel.balls[i*3+2] = tmp.get(i).getPos()[2];
        }
        long s = System.currentTimeMillis();
        //System.out.println("blubb");
        vKernel.execute(Range.create(AppLaunch.resX*AppLaunch.resY));
        System.out.println("Execution mode = "+ vKernel.getTargetDevice());
        System.out.println("RT: " + (System.currentTimeMillis()-s));

        return vKernel.values;
    }

    public void dispo(){
        vKernel.dispose();
    }


}
