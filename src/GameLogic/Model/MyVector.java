package src.GameLogic.Model;

import java.io.Serializable;
public class MyVector implements Serializable {
    private double value;
    private double angle;
    public MyVector(double val, double degree) {
        this.value = val / 10;
        this.angle = Math.toRadians(degree);
    }
    public double getX() {
        return value * Math.cos(angle);
    }
    public double getY() {
        return value * Math.sin(angle);
    }
    public void rotate(double degree) {
        angle += Math.toRadians(degree);
    }
    public void setValue(double val) {
        this.value = val / 10;
    }
    public void setAngle(int degree) {
        this.angle = (double) degree / 180 * Math.PI;
    }
    public int getAngle() {
        return (int) (angle / Math.PI * 180);
    }
    public MyVector getUnit() {
        return new MyVector(10, Math.toDegrees(angle));
    }
}
