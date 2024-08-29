package src.GameLogic.Controller;

import src.GameLogic.Model.Gift.*;
import src.GameLogic.Model.MyPoint;
import src.GameLogic.Model.MyVector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.Random;
public class Player extends Ellipse2D.Double implements Serializable {
    public int wrong = 0;
    private int score = 0;
    private int turning;
    private boolean alive;
    private String name;
    private MyPoint headPoint;
    private MyVector vector;
    private Player pairedPlayer;
    private Color bodyColor;
    private PowerUp power;
    private PowerUpType powerUpType;
    public Player(String name){
        this.name = name;

        bodyColor = new Color(new Random().nextInt(255) , new Random().nextInt(255) , new Random().nextInt(255));
        vector = new MyVector(12, new Random().nextInt(360));
        headPoint = new MyPoint(new Random().nextInt(600) + 100, new Random().nextInt(600) + 100);
        init();
    }
    public void move() {
        vector.rotate(turning * 1.9);
        if(pairedPlayer.powerUpType == PowerUpType.BOOST) {
            vector.setValue(18);
        } else if (pairedPlayer.powerUpType == PowerUpType.FREEZE) {
            vector.setValue(6);
        } else {
            vector.setValue(12);
        }
        headPoint.translate(vector);
        super.x = headPoint.getX();
        super.y = headPoint.getY();
    }
    public void setTurningDirection(int dir) {
        if(pairedPlayer.powerUpType == PowerUpType.CONFUSE){
            this.turning = -dir;
        }else {
            this.turning = dir;
        }
    }
    public MyPoint getFront(double distanceOffset) {
        double radius = width / 2;
        MyVector unit = vector.getUnit();
        unit.setValue((radius + distanceOffset) * 10);
        double x = this.x + radius + unit.getX();
        double y = this.y + radius + unit.getY();
        return new MyPoint(x, y);
    }
    public void reset() {
        headPoint.setX(new Random().nextInt(600) + 100);
        headPoint.setY(new Random().nextInt(600) + 100);
        vector.setAngle(new Random().nextInt(360));
        vector.setValue(12);
        init();
    }
    public void init(){
        alive = true;
        turning = 0;
        this.width = 8;
        this.height = 8;

        power = null;
        powerUpType = PowerUpType.NONE;

        this.x = headPoint.getX();
        this.y = headPoint.getY();
    }
    public void setPower(PowerUp power) {
        if(power == null){
            powerUpType = PowerUpType.NONE;
        } else if (power instanceof BoostPowerUp) {
            powerUpType = PowerUpType.BOOST;
        } else if (power instanceof ClearPowerUp) {
            powerUpType = PowerUpType.CLEAR;
        } else if (power instanceof ConfusePowerUp) {
            powerUpType = PowerUpType.CONFUSE;
        } else if (power instanceof FreezePowerUp) {
            powerUpType = PowerUpType.FREEZE;
        }
        this.power = power;
    }

    public MyVector getVector() {
        return vector;
    }

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
    public Color getBodyColor() {
        return bodyColor;
    }
    public void setPairedPlayer(Player pairedPlayer) {
        this.pairedPlayer = pairedPlayer;
    }
    public Player getPairedPlayer() {
        return pairedPlayer;
    }
}
