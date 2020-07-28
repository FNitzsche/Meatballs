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
            float factor = 1;
            float distS = (float)(Math.pow(balls[i*3]-x, 2) + Math.pow(balls[i*3+1]-y, 2));
            if (distS > 1){
                factor = 0;
            }
            v += Math.pow(1 - Math.min(1, distS), 2)*balls[i*3+2]*factor;
            dx += (float)(4 *balls[i*3+2]* (balls[i*3] - x) * (-Math.pow((balls[i*3] - x), 2) -Math.pow(balls[i*3+1] - y, 2) + 1))*factor;
            dy += (float)(4 *balls[i*3+2]* (balls[i*3+1] - y) * (-Math.pow((balls[i*3+1] - y), 2) -Math.pow(balls[i*3] - x, 2) + 1))*factor;
        }
        float steep = 1-(float)(Math.sqrt(Math.pow(dx, 2f)+Math.pow(dy, 2f)));
        steep = Math.max(0, Math.min(1, steep));
        if (v > 0.1){
            values[gid*3] = 1;
            values[gid*3+1] = steep;
            values[gid*3+2] = 1;
        } else if (v < -0.1){
            values[gid*3] = steep;
            values[gid*3+1] = 1;
            values[gid*3+2] = steep;
        } else {
            values[gid*3] = 1;
            values[gid*3+1] = 1;
            values[gid*3+2] = 1;
        }
        //values[gid*3] = Math.max(0, Math.min(1, v));
        //values[gid*3+1] = Math.max(0, Math.min(1, -v));
        //values[gid*3+2] = steep;
    }

}
