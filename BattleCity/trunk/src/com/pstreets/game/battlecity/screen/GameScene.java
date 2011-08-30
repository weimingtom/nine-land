//------------------------------------------------------------------------------
//                         COPYRIGHT 2007 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 15JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//The Software shall be used for Good, not Evil.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.
//Any questions, feel free to drop me a mail at james.shen@guidebee.biz.
//--------------------------------- PACKAGE ------------------------------------
package com.pstreets.game.battlecity.screen;

import java.io.IOException;
import java.util.Random;

import com.mapdigit.game.Layer;
import com.mapdigit.game.LayerManager;
import com.mapdigit.game.Sprite;
import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.Actor;
import com.pstreets.game.battlecity.actors.BattleField;
import com.pstreets.game.battlecity.actors.Bullet;
import com.pstreets.game.battlecity.actors.Explosion;
import com.pstreets.game.battlecity.actors.Powerup;
import com.pstreets.game.battlecity.actors.Score;
import com.pstreets.game.battlecity.actors.tank.EnemyTank;
import com.pstreets.game.battlecity.actors.tank.PlayerTank;
import com.pstreets.game.battlecity.actors.tank.Tank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * The scene screen for the game.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 15/01/08
 * @author      Guidebee, Inc.
 */
public final class GameScene extends SurfaceView implements Runnable,SurfaceHolder.Callback{
    
    ////////////////////////////////////////////////////////////////////////////
    //Game content variables,here used class public static member variables.
    /**
     * enemy tank killed in each level for each tank type.
     */
    public static int []enemyTanksCount=new int[4];
    
    /**
     * Total enemy tanks in one leve.
     */
    private static final int TOTAL_ENEMY_TANKS=20;
    
    /**
     * total enemy tanks remains.
     */
    public static int enemyTankRemains=TOTAL_ENEMY_TANKS;
    
    /**
     * whether can put a poweup, it's because a red tank is been shot dead or
     * in the given period.
     */
    public static boolean canPutPowerup=false;
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * The layer manager manage all actors in the game.
     */
    private LayerManager layerManager=new LayerManager();
    
    /**
     * Player's tank.
     */
    private PlayerTank playerTank;
    
    /**
     * Resource Manager.
     */
    private static ResourceManager resourceManager=ResourceManager.getInstance();
    
    /**
     * The battle field object.
     */
    private BattleField battleField=null;
    
    /**
     * This thread is core game logic.
     */
    private volatile Thread animationThread = null;
    
    /**
     * time taken and MILLIS_PER_TICK control the game speed
     */
    private long timeTaken = 0;
    
    /**
     *time taken and MILLIS_PER_TICK control the game speed
     */
    private static final int MILLIS_PER_TICK = 1;
    
    /**
     * the width of the game scene.
     */
    private static int sceneWidth;
    
    /**
     * the height of the game scene.
     */
    private static int sceneHeight;
    
    /**
     * the height of the score bar.
     */
    private static int barHeight=32;
    
    /**
     * the X origin of the battle field.
     */
    private int battleFieldX;
    
    /**
     * the Y origin of the battle field.
     */
    private int battleFieldY;
    
    /**
     * default lives of player
     */
    private final static int DEFAULT_PLAYER_LIVE=9;
    
    /**
     *  maximum game leves.
     */
    private static final int TOTAL_GAME_LEVELS=50;
    
    /**
     * total actors in the scene.
     */
    private int totalLayers=0;
    
    /**
     * is game over?
     */
    private boolean isGameover=false;
    
    /**
     * game over image
     */
    private static Bitmap imgGameover=
            resourceManager.getImage(ResourceManager.GAME_OVER_SMALL);
    
    /**
     * pause image
     */
    private static Bitmap imgPause=
            resourceManager.getImage(ResourceManager.PAUSE);
    
    /**
     * black number image from 0 to 9
     */
    private static Bitmap imgNumberBlack=
            resourceManager.getImage(ResourceManager.NUMBER_BLACK);
    
    /**
     * enemy icon
     */
    private static Bitmap imgEnemyIcon=
            resourceManager.getImage(ResourceManager.ENEMY_ICON);
    
    /**
     * first player icon
     */
    private static Bitmap imgIP=
            resourceManager.getImage(ResourceManager.IP);
    
    /**
     * flag image
     */
    private static Bitmap imgFlag=
            resourceManager.getImage(ResourceManager.FLAG);
    
    /**
     * offset X where start to draw the score bar
     */
    private int marginX=0;
    
    /**
     * offset Y where start to draw the score bar
     */
    private int marginY=0;
    
    /**
     * Random object to create random numbers.
     */
    private static Random rnd=new Random();
    
    /**
     * time control to create new enemy tank
     */
    private long enemySpawnStartTime=0;
    
    /**
     * minimun spawn timer
     */
    private final static long enemySpawnPeriod=2000;
    
    /**
     * timer control when to put an poweup in the battle field
     */
    private long putPowerupStartTime=0;
    
    /**
     * put poweup minimum period, 2 minutes
     */
    private final static long putPowerupPeriod=120000;
    
    /**
     * display game over or pause
     */
    private static Sprite gameStatus=null;
    
    /**
     * timer control when game over to display game over image,from bottom
     * to the middle of the screen.
     */
    private long gameOverStartTime=0;
    
    /**
     * after this period, display the score screen,default 4 seconds.
     */
    private final static long gameOverPeriod=4000;
    
    private Paint paint=new Paint();
    
    final Handler mHandler = new Handler();
    
    private SurfaceHolder holder;
    
    private boolean hasSurface;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public GameScene(Context context) {
    	super(context);
    	setVisibility(INVISIBLE);
    	setFocusable(true);
    	holder=getHolder();
    	holder.addCallback(this);
    	hasSurface=false;
        
        
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	sceneWidth=w;
        sceneHeight=h;
        try{
            int xTiles=sceneWidth/ResourceManager.TILE_WIDTH;
            int yTiles=(sceneHeight-barHeight)/ResourceManager.TILE_WIDTH;
            if(xTiles % 2 ==0) xTiles--;
            xTiles=Math.min(xTiles,yTiles);
            yTiles=xTiles;
            battleFieldX=(sceneWidth-xTiles*ResourceManager.TILE_WIDTH)/2;
            battleFieldY=(sceneHeight-battleFieldX-xTiles*ResourceManager.TILE_WIDTH);
            battleField=new BattleField(xTiles,yTiles,getContext());
            battleField.initBattlefield(this.getClass().getResourceAsStream("/1.txt"));
            Powerup.setBattleField(battleField);
            Powerup.setLayerManager(layerManager);
            Tank.setBattleField(battleField);
            Tank.setLayerManager(layerManager);
            Bullet.setBattleField(battleField);
            Bullet.setLayerManager(layerManager);
            Explosion.setBattleField(battleField);
            Explosion.setLayerManager(layerManager);
            Score.setBattleField(battleField);
            Score.setLayerManager(layerManager);
            playerTank=Tank.getPlayerTank();
            layerManager.append(battleField);
            int offset=8;
            marginX=(sceneWidth-(imgEnemyIcon.getWidth()*10+offset*2
                    +imgIP.getWidth()+imgFlag.getWidth()+imgNumberBlack.getHeight()))/2;
            marginY=(sceneHeight-battleField.getHeight())/2;
            totalLayers=layerManager.getSize();
            gameStatus=new Sprite(imgGameover);
            gameStatus.setPosition((sceneWidth-gameStatus.getWidth())/2,
                    (sceneHeight-gameStatus.getHeight())/2);
            gameStatus.setVisible(false);
        }catch(IOException e){
            //Alert()
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start a new game.
     */
    public void newGame(){
        if(ResourceManager.isPlayingSound){
            ResourceManager.playSound(ResourceManager.OPEN_SOUND);
        }
        playerTank.initTank();
        playerTank.setAvaiableLives(DEFAULT_PLAYER_LIVE);
        EnemyTank.explodeAllEmenies();
        Bullet.stopAllBullets();
        Explosion.stopAllExplosions();
        Powerup.removeAllPowerups();
        Powerup.putNewPowerup(Powerup.HOME);
        enemyTankRemains=TOTAL_ENEMY_TANKS;
        timeTaken=0;
        canPutPowerup=false;
        enemySpawnStartTime=0;
        enemySpawnStartTime=0;
        putPowerupStartTime=0;
        gameOverStartTime=0;
        gameStatus.setVisible(false);
        //reset player's score to zero.
        if(isGameover){
            playerTank.setScore(0);
        }
        isGameover=false;
        rnd=new Random();
        for(int i=0;i<4;i++) enemyTanksCount[i]=0;
        //String fileName="/level" + ResourceManager.gameLevel;
        try{
            //battleField.initBattlefield(this.getClass().getResourceAsStream(fileName));
            battleField.readBattlefieldFromHZK(ResourceManager.gameLevels[
                    ResourceManager.gameLevel]);
        }catch(Exception e){
            //inglore the exception.
        }
        stop();
        start();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw number in score bar
     * @param g the graphics object
     * @param number the number need to be drawn
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private void drawNumber(Canvas g, int number,int x,int y){
        Bitmap imageNumber=imgNumberBlack;
        String strNumber=String.valueOf(number);
        int numberWidth=imageNumber.getHeight();
        for(int i=0;i<strNumber.length();i++){
            char ch=strNumber.charAt(i);
            int index=(ch-'0') % 10;
            Bitmap oneNumber=Bitmap.createBitmap(imageNumber,index*numberWidth,0,
                    numberWidth,numberWidth);
            g.drawBitmap(oneNumber,x+i*numberWidth,y,null);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * put an power up in the battle field.
     */
    private void putAnPowerup(){
        int powupSelection=Math.abs(rnd.nextInt()) %100;
        int type=Powerup.STAR;
        if(powupSelection>95){
            type=Powerup.TANK;
        }else if(powupSelection>80){
            type=Powerup.BOMB;
        }else if(powupSelection>70){
            type=Powerup.SHOVEL;
        }else if(powupSelection>60){
            type=Powerup.CLOCK;
        }else if(powupSelection>50){
            type=Powerup.SHIELD;
        }else{
            type=Powerup.STAR;
        }
        Powerup.putNewPowerup(type);
        putPowerupStartTime=System.currentTimeMillis();
        canPutPowerup=false;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Game over,either all player tanks or the home has been destroyed.
     */
    private void gameOver(){
        isGameover=true;
        gameStatus.setImage(imgGameover,imgGameover.getWidth(),
                imgGameover.getHeight());
        gameStatus.setVisible(true);
        gameStatus.setPosition(gameStatus.getX(),sceneHeight);
        playerTank.stop();
        
        gameOverStartTime=System.currentTimeMillis();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Game logic is here.
     */
    private void applyGameLogic(){
        //normal game sequence.
        long tickTime=System.currentTimeMillis();;
        if(!isGameover){
            //Check if player obtain some powerup.
            Powerup.checkPlayerTank(playerTank);
            //Spawn enemy tank if needed
            boolean canSpawnEnemyTank=false;
            if(enemyTankRemains-EnemyTank.getVisibleEnemyTanks()>0){
                if(EnemyTank.getVisibleEnemyTanks()<10){
                    if(enemySpawnStartTime>0){
                        if(tickTime-enemySpawnStartTime>enemySpawnPeriod){
                            canSpawnEnemyTank=true;
                        }
                    }else{
                        canSpawnEnemyTank=true;
                    }
                }
            }else{
                if(EnemyTank.getVisibleEnemyTanks()==0){
                    ResourceManager.gameLevel++;
                    showScoreScreen();
                }
            }
            if(canSpawnEnemyTank){
                EnemyTank enemyTank=null;
                int tankSelection=Math.abs(rnd.nextInt()) % 100;
                if(tankSelection>90-ResourceManager.gameLevel){
                    enemyTank=EnemyTank.newEnemyTank(EnemyTank.TYPE_HEAVY);
                }else if(tankSelection>75-ResourceManager.gameLevel){
                    enemyTank=EnemyTank.newEnemyTank(EnemyTank.TYPE_SMART);
                }else if(tankSelection>55-ResourceManager.gameLevel){
                    enemyTank=EnemyTank.newEnemyTank(EnemyTank.TYPE_FAST);
                }else {
                    enemyTank=EnemyTank.newEnemyTank(EnemyTank.TYPE_SIMPLE);
                }
                if(enemyTank!=null){
                    
                    tankSelection=Math.abs(rnd.nextInt()) % 100;
                    if(tankSelection+ResourceManager.gameLevel>90){
                        
                        enemyTank.setHasPrize(true);
                    }
                    enemySpawnStartTime=tickTime;
                }
                
            }
            //Check if player has been killed
            if(!playerTank.isVisible()){
                if(playerTank.getAvaiableLives()>0){
                    playerTank.initTank();
                    playerTank.setVisible(true);
                }else{
                    gameOver();
                }
            }
            //Check if home is been destoryed, game over
            if(Powerup.isHomeDestroyed()){
                gameOver();
            }
            //put an poweup in the battle field
            if((tickTime-putPowerupStartTime>putPowerupPeriod) || canPutPowerup){
                putAnPowerup();
            }
        }else{
            //game is over, display game over animation.
            if(((tickTime-gameOverStartTime)<gameOverPeriod) &&
                    gameOverStartTime>0){
                int finalY=(sceneHeight-gameStatus.getHeight())/2;
                if(gameStatus.getY()>finalY){
                    gameStatus.setPosition(gameStatus.getX(),
                            gameStatus.getY()-1);
                }
            }else{
            	mHandler.post(mUpdateResults);
                gameOverStartTime=0;
            }
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw the score bar
     * @param g the graphics object.
     */
    private void drawScoreBar(Canvas g){
        
        int offset=8;
        int x,y;
        
        for(int i=0;i<enemyTankRemains-EnemyTank.getVisibleEnemyTanks();i++){
            int changeRow=i>9 ? 1:0;
            x=marginX+(i %10) *imgEnemyIcon.getWidth();
            y=marginY+changeRow*imgEnemyIcon.getWidth();
            g.drawBitmap(imgEnemyIcon,x,y,null);
        }
        //draw IP
        x=marginX+imgEnemyIcon.getWidth()*10+offset;
        y=marginY;
        int lives=playerTank.getAvaiableLives();
        drawNumber(g,lives,x+imgIP.getWidth()-imgNumberBlack.getHeight(),
                y+imgIP.getHeight()-imgNumberBlack.getHeight());
        g.drawBitmap(imgIP,x,y,null);
        x+=imgIP.getWidth()+offset;
        y=marginY;
        g.drawBitmap(imgFlag,x,y,null);
        drawNumber(g,ResourceManager.gameLevel,x+imgFlag.getWidth(),
                y+imgIP.getHeight()-imgNumberBlack.getHeight());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * clear the back ground.
     * @param g the graphics object.
     */
    private void clearBackground(Canvas g){
    	paint.setColor(0xFF808080);
    	paint.setStyle(Paint.Style.FILL);
        g.drawRect(0,0,sceneWidth,sceneHeight,paint);
        paint.setColor(0xFF000000);
    	paint.setStyle(Paint.Style.FILL);
        g.drawRect(battleFieldX,battleFieldY,
        		battleFieldX+battleField.getWidth(),battleFieldY+battleField.getHeight(),paint);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint.
     * @param g the graphics object.
     */
    protected void updateGame(Canvas g){
        //Clear the background.
        clearBackground(g);
        layerManager.paint(g,battleFieldX,battleFieldY);
        drawScoreBar(g);
        if(gameStatus.isVisible()){
            gameStatus.paint(g);
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * start the game..
     */
    private synchronized void start() {
        animationThread = new Thread(this);
        animationThread.start();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * stop the game.
     */
    private synchronized void stop() {
        animationThread = null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * run.
     */
    public void run(){
        Thread currentThread = Thread.currentThread();
        
        try {
            while (currentThread == animationThread) {
                long startTime = System.currentTimeMillis();
                // Don't advance game or draw if canvas is covered by a system
                // screen.
                if (isShown()) {
                    tick();
                    if(hasSurface){
                    Canvas canvas=holder.lockCanvas();
                    updateGame(canvas);
                    holder.unlockCanvasAndPost(canvas);
                    }
                }
                timeTaken = System.currentTimeMillis() - startTime;
                if (timeTaken < MILLIS_PER_TICK) {
                    synchronized (this) {
                        if(MILLIS_PER_TICK > timeTaken){
                            wait(MILLIS_PER_TICK - timeTaken);
                            timeTaken = System.currentTimeMillis() - startTime;
                        }
                    }
                } else {
                    Thread.yield();
                }
            }
        } catch (InterruptedException e) {
        }
        
    }
    
 // Create runnable for posting    
    final Runnable mUpdateResults = new Runnable() {       
    	public void run() {            
    		showScoreScreen();       
    		}    
    	};
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * show the score screen.
     */
    private void showScoreScreen(){
   	
    	ResourceManager.scoreScreen.show(isGameover);
        ResourceManager.setCurrentScreen(ResourceManager.scoreScreen);
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * key press.
     */
    public boolean onKeyDown (int keyCode, KeyEvent event)  {
        int gameAction=0;
        if(isGameover){
            
            return true;
        }
        switch(keyCode){
        case KeyEvent.KEYCODE_2:
        case KeyEvent.KEYCODE_DPAD_UP:
                gameAction=KeyEvent.KEYCODE_DPAD_UP ;
                break;
        case KeyEvent.KEYCODE_8:
        case KeyEvent.KEYCODE_DPAD_DOWN:
                gameAction=KeyEvent.KEYCODE_DPAD_DOWN ;
                break;
        case KeyEvent.KEYCODE_5:
        case KeyEvent.KEYCODE_DPAD_CENTER:
                gameAction=KeyEvent.KEYCODE_DPAD_CENTER;
                break;
        case KeyEvent.KEYCODE_4:
        case KeyEvent.KEYCODE_DPAD_LEFT:
                gameAction=KeyEvent.KEYCODE_DPAD_LEFT;
                break;
        case KeyEvent.KEYCODE_6:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
                gameAction=KeyEvent.KEYCODE_DPAD_RIGHT;
                break;
        default:
                break;
    }
        if(!isGameover){
            playerTank.keyPressed(gameAction);
        }
        return true;
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * key release.
     */
    public boolean onKeyUp (int keyCode, KeyEvent event)  {
    	int gameAction=0;
        if(isGameover){
            
            return true;
        }
        switch(keyCode){
        case KeyEvent.KEYCODE_2:
        case KeyEvent.KEYCODE_DPAD_UP:
                gameAction=KeyEvent.KEYCODE_DPAD_UP ;
                break;
        case KeyEvent.KEYCODE_8:
        case KeyEvent.KEYCODE_DPAD_DOWN:
                gameAction=KeyEvent.KEYCODE_DPAD_DOWN ;
                break;
        case KeyEvent.KEYCODE_5:
        case KeyEvent.KEYCODE_DPAD_CENTER:
                gameAction=KeyEvent.KEYCODE_DPAD_CENTER;
                break;
        case KeyEvent.KEYCODE_4:
        case KeyEvent.KEYCODE_DPAD_LEFT:
                gameAction=KeyEvent.KEYCODE_DPAD_LEFT;
                break;
        case KeyEvent.KEYCODE_6:
        case KeyEvent.KEYCODE_DPAD_RIGHT:
                gameAction=KeyEvent.KEYCODE_DPAD_RIGHT;
                break;
        default:
                break;
    }
        if(!isGameover){
            playerTank.keyReleased(gameAction);
        }
        return true;
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * tick. one game step.
     */
    private void tick(){
        
        for(int i=0;i<totalLayers;i++){
            Layer layer=layerManager.getLayerAt(i);
            if(layer.isVisible()){
                Actor actor=(Actor)layer;
                
                actor.tick();
            }
        }
        applyGameLogic();
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface=true;
		newGame();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface=false;
		stop();
		
	}
    
    
}
