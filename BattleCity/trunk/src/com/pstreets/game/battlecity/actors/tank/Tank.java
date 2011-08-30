//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 16JAN2008  James Shen                 	      Initial Creation
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
package com.pstreets.game.battlecity.actors.tank;

import android.graphics.Bitmap;

import com.mapdigit.game.LayerManager;
import com.mapdigit.game.Sprite;
import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.Actor;
import com.pstreets.game.battlecity.actors.BattleField;
import com.pstreets.game.battlecity.actors.Bullet;
import com.pstreets.game.battlecity.actors.Explosion;

//--------------------------------- IMPORTS ------------------------------------



//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 16JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Base class for all tanks.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 16/01/08
 * @author      Guidebee, Inc.
 */
public abstract class Tank extends Sprite implements Actor{
    
    /**
     * Tank should know about the battle field.
     */
    protected static BattleField battleField;
    
    /**
     * Tank should know about the layer manager.
     */
    protected static LayerManager layerManager;
    
    /**
     * the speed of the tank, default speed is 6 ,half of the
     * width of each tile.
     */
    protected int speed=DEFAULT_SPEED;
    
    /**
     * The direction in which player is driving.
     */
    protected int direction = BattleField.NONE;
    
    /**
     * default speed for tank.
     */
    protected static final int DEFAULT_SPEED=ResourceManager.TILE_WIDTH/4;
    
    /**
     * maximun number of tanks in the battle field.
     */
    public static final int POOL_SIZE = 21;
    
    /**
     * This pool store all tanks include player and enemy tanks.
     */
    protected static Tank TANK_POOL[];
    
    /**
     * time monitored to avoid the tank move to fast.
     */
    protected long driveStartTime = 0;
    
    /**
     * minimum time period between each move
     */
    private static final long minimumDrivePeriod = 40; 
    
    /**
     * Should the tank shoot?
     */
    protected boolean shoot = false;
    
    /**
     * new boun timer
     */
    protected int newBornTimer=0;
    
    /**
     * initial the tank pool ,the game will resume the tank object.
     * the first tank is the player's tank.
     */
    static {
        TANK_POOL = new Tank[POOL_SIZE];
        TANK_POOL[0] = new PlayerTank();
        //max 7 simple tanks
        for(int i=1;i<8;i++){
            TANK_POOL[i] = new SimpleTank(false);
        }
        //max 4 fast tanks
        for(int i=8;i<12;i++){
            TANK_POOL[i] = new FastTank(false);
        }
        //max 4 smart tanks
        for(int i=12;i<16;i++){
            TANK_POOL[i] = new SmartTank(false);
        }
        //max 4 heavy tanks
        for(int i=16;i<21;i++){
            TANK_POOL[i] = new HeavyTank(false);
        }
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates a new animated tank using frames contained in the provided Image.
     * @param battleField the battle field where the tank in.
     * @param image  the Image to use for Sprite.
     * @param frameWidth  the width, in pixels, of the individual raw frames.
     * @param frameHeight  the height, in pixels, of the individual raw frames.
     */
    protected Tank(Bitmap image,int frameWidth,int frameHeight){
        super(image,frameWidth,frameHeight);
        defineReferencePixel(frameWidth / 2, frameHeight / 2);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank moves.
     */
    public void drive(){
        long tickTime = System.currentTimeMillis();
        boolean canDrive=(tickTime-driveStartTime)>minimumDrivePeriod;
        boolean onSnow=battleField.isOnSnow(getX(),getY());
        int extraPace=0;
        if(onSnow) extraPace=speed;
        if(canDrive){
            switch (direction) {
                case BattleField.NORTH:
                    if ((getY() > 0)&&
                            !battleField.containsImpassableArea
                            (getX(),getY() - speed, getWidth(), speed)) {
                        tryMove(0, -speed-extraPace);
                    }
                    break;
                case BattleField.EAST:
                    if ((getX() < battleField.getWidth() - getWidth()) &&
                            !battleField.containsImpassableArea
                            (getX() + getWidth(), getY(), speed, getHeight())) {
                        tryMove(speed+extraPace, 0);
                    }
                    break;
                case BattleField.SOUTH:
                    if ((getY() < battleField.getHeight() - getHeight())&&
                            !battleField.containsImpassableArea
                            (getX(),getY() + getHeight(), getWidth(), speed)) {
                        tryMove(0, speed+extraPace);
                    }
                    break;
                case BattleField.WEST:
                    if ((getX() > 0)&&
                            !battleField.containsImpassableArea
                            (getX() - speed,getY(), speed, getHeight())) {
                        tryMove(-speed-extraPace, 0);
                    }
                    break;
            }
            driveStartTime = tickTime;
        }
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return player tank object.
     * @return player tank object.
     */
    public static PlayerTank getPlayerTank(){
        return (PlayerTank)TANK_POOL[0];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the layerManager for tanks.
     */
    public static void setLayerManager(LayerManager manager) {
        layerManager = manager;
        if (layerManager != null){
            
            for (int i = 0; i < POOL_SIZE; i++)
                layerManager.append(TANK_POOL[i]);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the Battle field for tanks.
     */
    public static void setBattleField(BattleField field) {
        battleField = field;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Operation be done in each tick.
     */
    public void tick(){
        if(isVisible()){
            think();
            drive();
            shoot();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank thinks before move.
     */
    public abstract void think();
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank shoots.
     * @return the bullet the tank shoots.
     */
    public abstract Bullet shoot();
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initialize tank status.
     */
    public abstract void initTank();
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank try to move (dx,dy)
     * @param dx the delta x 
     * @param dy the delta y
     */
    private void tryMove(int dx, int dy) {
        move(dx, dy);
        if (overlapsTank(this))
            move(-dx, -dy);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Explode a tank.
     */
    protected void explode() {
        Explosion.explode(getRefPixelX(), getRefPixelY(), Explosion.BIG);
        setVisible(false);
    }
	
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get an tank object.
     * @param i the index of tank.
     */
    public static Tank getTank(int i) {
        if(i>POOL_SIZE-1){
            return null;
        }
        if(TANK_POOL[i]!=null){
            return  TANK_POOL[i];
        }else{
            return null;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if there's overlap of tanks.
     * @param sprite the tank need to be checked
     * @return true, overlap.
     */
    protected static boolean overlapsTank(Sprite sprite) {
        for (int i = 0; i < POOL_SIZE; i++) {
            if (sprite != TANK_POOL[i] && 
                    TANK_POOL[i].isVisible() &&
                    sprite.collidesWith(TANK_POOL[i], false))
                return true;
        }
        return false;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * change the direction of current tank.
     * @param direction new direction for the tank.
     */
    protected void changeDirection(int direction) {
        this.direction = direction;
    }
    
    
}
