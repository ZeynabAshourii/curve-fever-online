package src.GameLogic.Model.Gift;

import src.GameLogic.Controller.Player;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
public class PowerUp extends Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;
    private Player owner;
    private boolean active = false;
    private boolean show = true;

    public PowerUp(int x , int y , int width , int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Timer timerShow = new Timer();
        TimerTask timerTaskShow = new TimerTask() {
            @Override
            public void run() {
                if(show) show = false;
            }
        };
        timerShow.schedule(timerTaskShow, 2*8000);
    }
    public void setActivity(){
        active = true;
        show = false;
        if(!( this instanceof ClearPowerUp)) {
            Timer timerActive = new Timer();
            TimerTask timerTaskActive = new TimerTask() {
                @Override
                public void run() {
                    if (active) active = false;
                    owner.setPower(null);
                }
            };
            timerActive.schedule(timerTaskActive, 5000);
        }
    }
    public boolean isActive() {
        return active;
    }
    public boolean isShow() {
        return show;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public int getPX() {
        return x;
    }
    public int getPY() {
        return y;
    }
    public int getPWidth() {
        return width;
    }
    public int getPHeight() {
        return height;
    }
}
