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

    public int mode = 1;
    public float vFactor = 1;

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
            float[] adds = calcSingleBall(i, x, y);
            v += adds[0];
            dx += adds[1];
            dy += adds[2];
        }
        setReturn(v, dx, dy, gid);
    }

    private float[] calcSingleBall(int i, float x, float y){
        float factor = 1;
        float strengthPos = Math.max(balls[i*3+2], balls[i*3+2]*(-1));
        float strengthSig = Math.signum(balls[i*3+2]);//(balls[i*3+2]/Math.max(balls[i*3+2], balls[i*3+2]*(-1)));
        float distS = (float)((Math.pow(balls[i*3]-x, 2) + Math.pow(balls[i*3+1]-y, 2)))*strengthPos;
        if (distS > 1){
            factor = 0;
        }
        float v = (float)(Math.pow(distS, 2)-2*distS +1)*factor*strengthSig;
        float dx = (float)(4 * strengthPos * (balls[i*3] - x) * (-strengthPos *(Math.pow((balls[i*3] - x), 2) +Math.pow(balls[i*3+1] - y, 2) )+ 1))*factor*strengthSig;
        float dy = (float)(4 * strengthPos *(balls[i*3+1] - y) * (-strengthPos *(Math.pow((balls[i*3+1] - y), 2) +Math.pow(balls[i*3] - x, 2)) + 1))*factor*strengthSig;

        return new float[]{v, dx, dy};
    }

    private void setReturn(float v, float dx, float dy, int gid){
        if (mode == 0){
            returnColors(v, dx, dy, gid);
        } else if (mode == 1){
            returnGrey(v, dx, dy, gid);
        }
    }

    private void returnColors(float v, float dx, float dy, int gid){
        v *= vFactor;
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
    }

    private void returnGrey(float v, float dx, float dy, int gid){
        float limitedV = Math.max(-5, Math.min(5, v*vFactor))*0.1f;
        float steepO = (float)(Math.sqrt(Math.pow(dx, 2f)+Math.pow(dy, 2f)));
        float steep = Math.max(0, Math.min(1, (1-steepO)*(float)Math.pow(limitedV, 2)))*Math.signum(limitedV);
        limitedV += 0.5f;
        float comb = Math.max(0, Math.min(1,limitedV+steep));
        values[gid*3] = comb;
        values[gid*3+1] = comb;
        values[gid*3+2] = comb;
    }

}
