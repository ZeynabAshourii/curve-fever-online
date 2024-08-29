package src.GameLogic.Controller;

import src.GameLogic.Model.Gift.*;
import src.GameLogic.Model.MyVector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class Handler {
    private int runTime = 0;
    private int giftSize = 32;
    private Player player1;
    private Player player2;
    private boolean shouldRun = true;
    private boolean end = false;
    private boolean clear = false;
    private boolean hitClearPowerUp = false;
    private BufferedImage bufferedImage;
    private Graphics2D graphics2D;
    private LinkedList<PowerUp> powerUp = new LinkedList<>();
    private boolean shouldPaint = true;
    public Handler(Player player1 , Player player2) {
        init();
        this.player1 = player1;
        this.player2 = player2;
    }
    public void init(){
        bufferedImage = new BufferedImage(980, 771, BufferedImage.TYPE_INT_RGB);
        graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0, 0, 980, 771);
        setTimerGift();
    }
    public void setTimerGift(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(shouldRun){
                    boolean rightXY = false;
                    while (!rightXY) {
                        Random random = new Random();
                        int code = random.nextInt(4);
                        int x = random.nextInt(700) + 100;
                        int y = random.nextInt(600) + 100;
                        if(isRightPlace(x , y)){
                            createNewGift(code , x , y);
                            rightXY = true;
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask , 8000 , 8000);
    }
    public void createNewGift(int code , int x , int y){
        switch (code) {
            case 0:
                powerUp.add( new BoostPowerUp(x, y , giftSize , giftSize));
                break;
            case 1:
                powerUp.add( new FreezePowerUp(x, y , giftSize , giftSize));
                break;
            case 2:
                powerUp.add ( new ConfusePowerUp(x, y , giftSize , giftSize));
                break;
            case 3:
                powerUp.add( new ClearPowerUp(x, y , giftSize , giftSize));
                break;
        }
    }
    public boolean isRightPlace(int x , int y){
        boolean rightPlace = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (!(new Color(bufferedImage.getRGB(x+4*i, y+4*j)).equals(Color.black))) {
                    rightPlace = false;
                    break;
                }
            }
        }
        return rightPlace;
    }
    public void update(String username1 , int turn1){
        paint();
        if(shouldRun) {
            if(player1.getName().equals(username1)){
                setTurn(player1 , turn1);
            }else {
                setTurn(player2 , turn1);
            }
            runUpdate();
        }else if(!end){
            runTime++;
            if(runTime > 60){
                reset();
            }
        }
    }
    public void reset(){
        powerUp.clear();
        runTime = 0;
        clear = false;
        shouldRun = true;
    }
    public void runUpdate(){
        if (player1.isAlive()) {
            player1.move();
        }
        if (player2.isAlive()) {
            player2.move();
        }
        checkForHits();
        checkGift();
    }
    public boolean pointOnRectangle(Rectangle rectangle , int x , int y){
        if(x >= rectangle.x && x <= rectangle.x + rectangle.width && y >= rectangle.y && y <= rectangle.y + rectangle.height){
            return true;
        }
        return false;
    }
    public boolean headOnRectangle(Rectangle rectangle ,  Player p){
        double radius = p.width / 2;
        for (int i = -70; i <= 70; i += 35) {
            MyVector vector = new MyVector(10 * radius, p.getVector().getAngle());
            vector.rotate(i);
            int frontX = (int) (p.getFront(-radius / 5 * 3).getX() + vector.getX());
            int frontY = (int) (p.getFront(-radius / 5 * 3).getY() + vector.getY());
            if(pointOnRectangle(rectangle , frontX , frontY)){
                return true;
            }

        }
        return false;
    }
    public void checkPlayerHitPower(Player player , PowerUp power){
        if(headOnRectangle(new Rectangle(power.getPX() , power.getPY(), power.getPWidth(), power.getPHeight()) , player)){
            power.setActivity();
            if(!(player.getPowerUpType() == PowerUpType.CLEAR)) {
                if(power instanceof ClearPowerUp){
                    hitClearPowerUp = true;
                    shouldPaint = false;
                }
                player.setPower(power);
                power.setOwner(player);
            }
        }
    }
    public void checkGift(){
        if(player1.isAlive() && player2.isAlive()){
            for(int i = 0; i < powerUp.size(); i++){
                PowerUp power = powerUp.get(i);
                if(!power.isActive() && power.isShow()){
                    checkPlayerHitPower(player1 , power);
                    checkPlayerHitPower(player2 , power);
                }
            }
        }
    }
    public void checkPlayer(Player player){
        if (player.isAlive()) {
            if (checkBorderHit(player) || checkPlayerHit(player)) {
                player.wrong++;
                if(player.wrong >= 3){
                    player.wrong = 0;
                    player.setAlive(false);
                    updateScores();
                }
            }
            else {
                player.wrong = 0;
            }
        }
    }
    public void checkForHits() {
        checkPlayer(player1);
        checkPlayer(player2);
        checkEndGame();
    }
    public void checkEndGame(){
        if (checkRoundOver() && !clear) {
            if(player1.getScore() > 5 || player2.getScore() > 5){
                end = true;
            }
            graphics2D.setColor(Color.black);
            graphics2D.fillRect(0, 0, 980, 771);
            powerUp.clear();
            hitClearPowerUp = false;
            shouldPaint = true;
            clear = true;
            player1.reset();
            player2.reset();
            shouldRun = false;
        }
    }
    public void paint(){
        if(shouldPaint) {
            graphics2D.setColor(player1.getBodyColor());
            graphics2D.fill(player1);
            graphics2D.setColor(player2.getBodyColor());
            graphics2D.fill(player2);
        }else {
            graphics2D.setColor(Color.black);
            graphics2D.fillRect(0, 0, 980, 771);
        }
    }
    public void setTurn(Player player , int x){
        if(x == 48){
            player.setTurningDirection(0);
        } else if (x == 45) {
            player.setTurningDirection(-1);
        } else if (x == 49) {
            player.setTurningDirection(1);
        }
    }
    public boolean checkPlayerHit(Player p) {
        double radius = p.width / 2;
        for (int i = -70; i <= 70; i += 35) {
            MyVector vector = new MyVector(10 * radius, p.getVector().getAngle());
            vector.rotate(i);
            int frontX = (int) (p.getFront(-radius / 5 * 3).getX() + vector.getX());
            int frontY = (int) (p.getFront(-radius / 5 * 3).getY() + vector.getY());
            if (!(new Color(bufferedImage.getRGB(frontX, frontY)).equals(Color.black))) {
                return true;
            }

        }
        return false;
    }

    public boolean checkBorderHit(Player player) {
        double radius = player.width / 2;

        for (int i = -70; i <= 70; i += 35) {
            MyVector vector = new MyVector(11 * radius, player.getVector().getAngle());
            vector.rotate(i);
            int frontX = (int) (player.getFront(-radius / 6 * 4).getX() + vector.getX());
            int frontY = (int) (player.getFront(-radius / 6 * 4).getY() + vector.getY());
            if (frontX <= 2 || frontX >= 977) {
                return true;
            }
            if (frontY <= 4 || frontY >= 725) {
                return true;
            }
        }
        return false;
    }
    public void updateScores() {
        if (player1.isAlive()) {
            player1.setScore(player1.getScore() + 1);
        }
        if (player2.isAlive()) {
            player2.setScore(player2.getScore() + 1);
        }
    }
    public boolean checkRoundOver() {
        if(player1.isAlive() && player2.isAlive()){
            return false;
        }else {
            return true;
        }
    }
    public LinkedList<PowerUp> getPowerUp() {
        return powerUp;
    }
    public boolean isEnd() {
        return end;
    }
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
    public boolean isClear() {
        return clear;
    }

    public boolean isHitClearPowerUp() {
        return hitClearPowerUp;
    }

    public void setHitClearPowerUp(boolean hitClearPowerUp) {
        this.hitClearPowerUp = hitClearPowerUp;
    }
}
