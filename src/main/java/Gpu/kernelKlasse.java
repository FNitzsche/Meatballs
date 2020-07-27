package Gpu;

import Model.MeatScene;
import com.aparapi.Kernel;
import main.AppLaunch;

public class kernelKlasse extends Kernel {
    MeatScene scene;
    public final float[] colors;
    public float[] balls;;
    public final int countB;;
    public int resX = AppLaunch.resX;
    public int resY = AppLaunch.resY;

    public kernelKlasse(MeatScene scene) {
        this.scene = scene;
        balls = new float[scene.getMeatballs().size()*3];
        countB = scene.getMeatballs().size();
        colors = new float[AppLaunch.resX*AppLaunch.resY*3];
    }

    @Override
    public void run() {
        int gid = getGlobalId();
        colors[gid*3] = 1;
        colors[gid*3+1] = 1;
        colors[gid*3+2] = 1;

        int px = gid % resY;
        int py = gid / resY;

        float x = 6f*px/resX -3;
        float y = 6f*py/resY -3;
        float v = 0;
        for (int i = 0; i < countB; i++) {
            v += (float)(1/(Math.abs(Math.pow(balls[i*3] - x, 2)) + Math.abs(Math.pow(balls[i*3+1] - y, 2))))*balls[i*3+2];
        }

        if (v > 10) {
            colors[gid*3] = 1;
            colors[gid*3+1] = 0.8f;
            colors[gid*3+2] = 1;
        } else if (v > 9.5){
            float m = (float)Math.min(1, (v-9.5)*2);
            colors[gid*3] = m;
            colors[gid*3+1] = m*0.8f;
            colors[gid*3+2] = 1;
        } else if (v > 3){
            colors[gid*3] = 0;
            colors[gid*3+1] = 0;
            colors[gid*3+2] = 1;
        } else if (v > 2.9){

            colors[gid*3] = 0;
            colors[gid*3+1] = 0;
            colors[gid*3+2] = Math.min(1, (v-2.9f)*10);
        } else if (v > 2.8){
            float m = (float)Math.min(1, (2.9-v)*10);
            colors[gid*3] = m;
            colors[gid*3+1] = m;
            colors[gid*3+2] = m;
        } else if (v < -10) {
            colors[gid*3] = 0.8f;
            colors[gid*3+1] = 1;
            colors[gid*3+2] = 0.7f;
        } else if (v < -9.5){
            float m = (float)Math.min(1, (v+9.5)*-2);
            colors[gid*3] = 0.8f*m;
            colors[gid*3+1] = 1;
            colors[gid*3+2] = 0.7f*m;
        } else if (v < -3){ ;
            colors[gid*3] = 0;
            colors[gid*3+1] = 1;
            colors[gid*3+2] = 0;
        } else if (v < -2.9){
            colors[gid*3] = 0;
            colors[gid*3+1] = Math.min(1, (v+2.9f)*-10);
            colors[gid*3+2] = 0;
        } else if (v < -2.8){
            float m = (float)Math.min(1, (2.9+v)*10);
            colors[gid*3] = m;
            colors[gid*3+1] = m;
            colors[gid*3+2] = m;
        }

        //colors[gid*3] = Math.min(1, Math.max(0, v));
    }
}
