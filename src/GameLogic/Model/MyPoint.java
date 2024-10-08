package src.GameLogic.Model;

import java.io.Serializable;
public class MyPoint implements Serializable {
    private double x;
    private double y;
    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void translate(MyVector vector) {
        x += vector.getX();
        y += vector.getY();
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
}
