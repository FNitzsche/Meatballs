package Model;

import main.AppLaunch;

import java.util.function.Supplier;

public class PixelGen implements Supplier {

    volatile int x = 0;
    volatile int y = 0;

    @Override
    public int[] get() {

        int[] ret = {x, y};
        y++;
        if (y >= AppLaunch.resY){
            y = 0;
            x++;
        }

        return ret;
    }
}
