package Model;

public class Meatball {

    float x, y = 0;
    float tx, ty = 0;
    float strength = 1;

    public Meatball(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Meatball(float x, float y, float strength){
        this.x = x;
        this.y = y;
        this.strength = strength;
    }

    public float tryInside(float px, float py){
        float dist = (float)(1/(Math.abs(Math.pow(lx - px, 2)) + Math.abs(Math.pow(ly - py, 2))));
        return dist*strength;
    }

    float lx, ly = 0;

    public void update(float t){
        lx = x + ((tx-x)*t);
        ly = y + ((ty-y)*t);
    }

    public void target(float tx, float ty){
        this.tx = tx;
        this.ty = ty;
    }

    public void setPos(){
        x = tx;
        y = ty;
    }

    public float[] getPos(){
        float[] ret = {lx, ly, strength};
        return ret;
    }


}
