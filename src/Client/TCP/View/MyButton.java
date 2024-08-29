package src.Client.TCP.View;

import src.Message.MyString;

import javax.swing.*;
import java.awt.*;
public class MyButton extends JButton {
    private Integer i;
    private MyString name;
    private boolean ready;
    public MyButton(MyString name , boolean ready){
        this.name = name;
        this.ready = ready;
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        if(ready){
            this.setBackground(Color.GREEN);
        }
        else {
            this.setBackground(Color.RED);
        }
        this.setText(name.username);
        this.i = name.id;
    }

    public Integer getI() {
        return i;
    }

    public MyString getMyName() {
        return name;
    }
}
