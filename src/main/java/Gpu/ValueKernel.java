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

        float mRes = Math.max(resX, resY);

        float x = 6f*(py/mRes) -3*(resX/mRes);
        float y = 6f*(px/mRes) -3*(resY/mRes);
        float v = 0;
        float dx = 0;
        float dy = 0;
        for (int i = 0; i < countB; i++) {
            float factor = 1;
            float strengthPos = Math.max(balls[i*3+2], balls[i*3+2]*(-1));
            float strengthSig = Math.signum(balls[i*3+2]);//(balls[i*3+2]/Math.max(balls[i*3+2], balls[i*3+2]*(-1)));
            float distS = (float)((Math.pow(balls[i*3]-x, 2) + Math.pow(balls[i*3+1]-y, 2)))*strengthPos;
            if (distS > 1){
                factor = 0;
            }
            v += (Math.pow(distS, 2)-2*distS +1)*factor*strengthSig;
            dx += (float)(4 * strengthPos * (balls[i*3] - x) * (-strengthPos *(Math.pow((balls[i*3] - x), 2) +Math.pow(balls[i*3+1] - y, 2) )+ 1))*factor*strengthSig;
            dy += (float)(4 * strengthPos *(balls[i*3+1] - y) * (-strengthPos *(Math.pow((balls[i*3+1] - y), 2) +Math.pow(balls[i*3] - x, 2)) + 1))*factor*strengthSig;
        }
        float steepO = (float)(Math.sqrt(Math.pow(dx, 2f)+Math.pow(dy, 2f)));
        float steep = Math.max(0, Math.min(1, 1-steepO));
        float steepI = Math.max(0, Math.min(1, 1-0.1f*steepO));
        if (v > 0.01){
            values[gid*3] = steepI;
            values[gid*3+1] = steep;
            values[gid*3+2] = steepI;
        } else if (v < -0.01){
            values[gid*3] = steep;
            values[gid*3+1] =  steepI;
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
