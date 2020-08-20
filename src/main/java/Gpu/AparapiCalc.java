package Gpu;

import Controller.MainScreenCon;
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

    KernelKlasse kernel;
    ValueKernel vKernel;
    final int resX = AppLaunch.resX;
    final int resY = AppLaunch.resY;
    MeatScene scene;
    MainScreenCon mainScreenCon;

    public AparapiCalc(MeatScene scene, MainScreenCon mainScreenCon){
        this.scene = scene;
        kernel = new KernelKlasse(scene);
        vKernel = new ValueKernel(scene);
        this.mainScreenCon = mainScreenCon;
        System.out.println("kernel");
    }

    public float[] calcColors(MeatScene scene){
        float[] ret = null;
        if (mainScreenCon.getComic()){
            ret = runComicKernel(scene);
        } else {
            ret = runVKernel(scene);
        }

        return ret;
    }

    public float[] runVKernel(MeatScene scene){
        ArrayList<Meatball> tmp = scene.getMeatballs();
        vKernel.mode = mainScreenCon.getModeInt();
        vKernel.lightPos = mainScreenCon.getCoords();
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

    public float[] runComicKernel(MeatScene scene){
        ArrayList<Meatball> tmp = scene.getMeatballs();
        for (int i = 0; i < kernel.countB; i++){
            kernel.balls[i*3] = tmp.get(i).getPos()[0];
            kernel.balls[i*3+1] = tmp.get(i).getPos()[1];
            kernel.balls[i*3+2] = 1/tmp.get(i).getPos()[2];
        }
        long s = System.currentTimeMillis();
        //System.out.println("blubb");
        kernel.execute(Range.create(AppLaunch.resX*AppLaunch.resY));
        System.out.println("Execution mode = "+ vKernel.getTargetDevice());
        System.out.println("RT: " + (System.currentTimeMillis()-s));

        return kernel.values;
    }

    public void dispo(){
        vKernel.dispose();
    }


}
