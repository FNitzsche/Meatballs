package Gpu;

import Model.MeatScene;
import com.aparapi.Kernel;
import main.AppLaunch;


public class ValueKernel extends Kernel {

    MeatScene scene;
    public final float[] values;
    public float[] balls;;
    public final int countB;;
    public int resX = AppLaunch.resX;
    public int resY = AppLaunch.resY;

    public ValueKernel(MeatScene scene) {
        this.scene = scene;
        balls = new float[scene.getMeatballs().size()*3];
        countB = scene.getMeatballs().size();
        values = new float[AppLaunch.resX*AppLaunch.resY*3];
    }


    @Override
    public void run() {
        int gid = getGlobalId();

        int px = gid % resY;
        int py = gid / resY;

        float x = 6f*px/resX -3;
        float y = 6f*py/resY -3;
        float v = 0;
        float dx = 0;
        float dy = 0;
        for (int i = 0; i < countB; i++) {
            //v += (float)(1/(Math.pow(balls[i*3] - x, 2)) +(Math.pow(balls[i*3+1] - y, 2)))*balls[i*3+2];
            v += (float)(1/(Math.abs(Math.pow(balls[i*3] - x, 2)) + Math.abs(Math.pow(balls[i*3+1] - y, 2))))*balls[i*3+2];
            dx += (float)(2*(balls[i*3] - x)/Math.pow((Math.pow(balls[i*3] - x, 2)) +(Math.pow(balls[i*3+1] - y, 2)), 2))*balls[i*3+2];
            dy += (float)(2*(balls[i*3+1] - y)/Math.pow((Math.pow(balls[i*3] - x, 2)) +(Math.pow(balls[i*3+1] - y, 2)), 2))*balls[i*3+2];
        }
        float steep = 1-(float)(Math.sqrt(Math.pow(dx, 2f)+Math.pow(dy, 2f)))*0.01f;
        steep = Math.max(0, Math.min(1, steep));
        values[gid*3] = Math.max(0, Math.min(1, v*0.2f+steep));
        values[gid*3+1] = steep;
        values[gid*3+2] = steep;
    }

}
