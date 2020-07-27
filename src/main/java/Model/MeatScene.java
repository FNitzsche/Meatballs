package Model;

import java.util.ArrayList;
import java.util.Random;

public class MeatScene {

    ArrayList<Meatball> balls = new ArrayList<>();

    public boolean addBall(Meatball meatball){
        if(!balls.contains(meatball)){
            balls.add(meatball);
            return true;
        }
        return false;
    }

    public ArrayList<Meatball> getMeatballs(){
        return balls;
    }

    public float inReach2D(float x, float y){
        float value = balls.stream().map(ball -> ball.tryInside(x, y)).reduce(0f, (f1, f2) -> f1+f2);
        return value;
    }

    Random ran = new Random();

    public void randomize(){
        for (Meatball meatball: balls){
            meatball.target(ran.nextFloat()*8-4, ran.nextFloat()*8-4);
            //meatball.target(0, 0);
        }
    }

    public void update(float t){
        balls.stream().forEach(b -> b.update(t));
    }

    public void setPosition(){
        for (Meatball meatball: balls){
            meatball.setPos();
        }
    }

}
