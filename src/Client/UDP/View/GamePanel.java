package src.Client.UDP.View;

import src.Client.UDP.Handler.KeyInput;
import src.Client.UDP.UDPClient;
import src.GameLogic.Controller.Player;
import src.GameLogic.Model.Gift.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.LinkedList;
public class GamePanel extends JPanel implements Runnable , Serializable {
    private int rightKey;
    private int leftKey;
    private int turning = 0;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private String usernamePlayer1;
    private String usernamePlayer2;
    private boolean running = false;
    private boolean shouldPaint = true;
    private boolean clearPage = false;
    private Player player;
    private UDPClient udpClient;
    private Graphics2D graphics2D;
    private Graphics2D screenGraphic;
    private BufferedImage bufferedImage;
    private LinkedList<PowerUp> powerUp = new LinkedList<>();
    public GamePanel(UDPClient udpClient){
        this.udpClient = udpClient;
        udpClient.setGamePanel(this);
        this.rightKey = udpClient.getRightKey();
        this.leftKey = udpClient.getLeftKey();
        this.setSize(1080, 771);
        this.setLayout(null);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        this.start();
    }
    public void init(){
        addKeyListener(new KeyInput(this));
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        bufferedImage = new BufferedImage(980, 771, BufferedImage.TYPE_INT_RGB);
        graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0, 0, 980, 771);
    }
    public synchronized void start(){
        if(running){
            return;
        }
        running = true;
        Thread thread = new Thread(this , "Thread");
        thread.start();
    }
    public synchronized void stop(){
        if(!running){
            return;
        }
        running = false;
    }

    @Override
    public void run() {
        init();
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0*3.2/60.0;
        int frames = 0;
        int updates = 0;
        while (running){
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            while (delta >= 1){
                update();
                updates++;
                delta--;
            }
            repaint();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }
    @Override
    public void paint(Graphics g) {
        paintBody(player);
        if(player != null){
            paintBody(player.getPairedPlayer());
        }
        g.drawImage(bufferedImage, 0, 0, null);

        screenGraphic = (Graphics2D) g;
        screenGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        screenGraphic.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        screenGraphic.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        screenGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        paintHead(player);
        if(player != null){
            paintHead(player.getPairedPlayer());
            paintGift(g);
        }
        paintScores(g);
        if(clearPage){
            graphics2D.setColor(Color.black);
            graphics2D.fillRect(0, 0, 980, 771);
            shouldPaint = false;
            clearPage = false;
        }
    }
    public void paintBody(Player player){
        if (shouldPaint) {
            if (player != null) {
                graphics2D.setColor(player.getBodyColor());
                graphics2D.fill(player);
            }
        }
    }
    public void paintHead(Player player){
            if (player != null) {
                double radius = player.width / 2;
                double frontX = player.getFront(-radius / 3).getX() - radius + 0.15 * radius;
                double frontY = player.getFront(-radius / 3).getY() - radius + 0.15 * radius;
                Ellipse2D.Double head = new Ellipse2D.Double(frontX, frontY, 0.85 * radius * 2, 0.85 * radius * 2);

                screenGraphic.setColor(Color.RED);
                screenGraphic.fill(head);
            }
    }
    public void paintGift(Graphics g){
        for (int i = 0; i < powerUp.size(); i++) {
            PowerUp power = powerUp.get(i);
            if (power.isShow()) {
                if (power instanceof BoostPowerUp) g.setColor(Color.WHITE);
                else if (power instanceof ClearPowerUp) g.setColor(Color.PINK);
                else if (power instanceof ConfusePowerUp) g.setColor(Color.CYAN);
                else if (power instanceof FreezePowerUp) g.setColor(Color.ORANGE);
                g.fillRect(power.getPX(), power.getPY(), power.getPWidth(), power.getPHeight());
            }
        }
    }
        public void paintScores(Graphics g){
        if (player != null) {
            scorePlayer1 = player.getScore();
            scorePlayer2 = player.getPairedPlayer().getScore();
            this.usernamePlayer1 = player.getName();
            this.usernamePlayer2 = player.getPairedPlayer().getName();
        }
        g.setFont(new Font("Courier", Font.BOLD, 14));
        g.setColor(Color.RED);
        g.drawString(usernamePlayer1 + " : " + scorePlayer1, 985, 50 + 100);
        g.drawString(usernamePlayer2 + " : " + scorePlayer2, 985, 50 * 2 + 100);
        g.setColor(Color.black);
    }

    public void update(){
        String string = udpClient.getUsername() + " turning is : " + turning;
        udpClient.Send(string.getBytes());
        String st = "send gifts";
        udpClient.Send(st.getBytes());
    }
    public void clear() {
        powerUp.clear();
        player = null;
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0, 0, 980, 771);
        shouldPaint = true;
        clearPage = false;
    }
    public void setTurning(int turning) {
        this.turning = turning;
    }
    public int getRightKey() {
        return rightKey;
    }
    public int getLeftKey() {
        return leftKey;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public void setPowerUp(LinkedList<PowerUp> powerUp) {
        this.powerUp = powerUp;
    }
    public void setClearPage(boolean clearPage) {
        this.clearPage = clearPage;
    }
}

