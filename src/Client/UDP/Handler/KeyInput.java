package src.Client.UDP.Handler;

import src.Client.UDP.View.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyInput implements KeyListener {
    private GamePanel gamePanel;
    private int rightKey;
    private int leftKey;
    public KeyInput(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.rightKey = gamePanel.getRightKey();
        this.leftKey = gamePanel.getLeftKey();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(key == rightKey){
            gamePanel.setTurning(1);
        } else if (key == leftKey) {
            gamePanel.setTurning(-1);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(key == rightKey || key == leftKey){
            gamePanel.setTurning(0);
        }
    }
}
