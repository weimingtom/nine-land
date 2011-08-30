//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
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
package com.pstreets.game.battlecity.actors;

import com.mapdigit.game.LayerManager;
import com.mapdigit.game.Sprite;
import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.tank.PlayerTank;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Power up class which can upgrade player's tank.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 17/01/08
 * @author      Guidebee, Inc.
 */
public final class Powerup extends Sprite implements Actor{
    
    /**
     * invulnerable animation.
     */
    public static final int INVULNERABLE=1;
    
    /**
     * Player's home
     */
    public static final int HOME=2;
    
    /**
     * Player's home been destroyed.
     */
    public static final int HOME_DESTROYED=3;
    
    /** 
     * Tank symbol gives an extra life. 
     */
    public static final int TANK = 4;
    
    /** 
     * Clock freezes all enemy tanks for a period of time. 
     */
    public static final int CLOCK = 5;
    
    /** 
     * Shovel adds steel walls around the base for a period of time. 
     */
    public static final int SHOVEL = 6;
    
    /** 
     * Bomb destroys all visible enemy tanks. 
     */
    public static final int BOMB = 7;
    
    /** 
     * Star improves player's tank. maxium 8 grades.
     */
    public static final int STAR = 8;
    
    /** 
     * Shield makes player's tank invulnerable to attack for a period of time. 
     */
    public static final int SHIELD = 9;
    
    /**
     * Tank should know about the battle field.
     */
    private static BattleField battleField;
    
    /**
     * Tank should know about the layer manager.
     */
    private static LayerManager layerManager;
    
    /** 
     * No image. 
     */
    private static final int NOTHING = 10;
    
    /**
     * maximun number of power ups in the battle field.
     */
    private static final int POOL_SIZE = 10;
    
    /**
     * This pool store all powerups.
     */
    private static Powerup POWERUP_POOL[];
    
    /**
     * time monitored to avoid the powerup flashes too fast.
     */
    private long timeTaken = MILLIS_PER_TICK;
    
    /**
     * minimum time period between each flash
     */
    private static final int MILLIS_PER_TICK = 50; 
    
    /**
     * initial the powerup pool.
     */
    static {
        POWERUP_POOL = new Powerup[POOL_SIZE];
        for(int i=0;i<POOL_SIZE;i++){
            POWERUP_POOL[i]=new Powerup(NOTHING);
            POWERUP_POOL[i].setVisible(false);
        }
        POWERUP_POOL[0].type=INVULNERABLE;
    }

    /**
     * the type of the powerup
     */
    private int type=NOTHING;
    
    /**
     * varible to toggle the powerup image to make it animation.
     */
    private boolean showNextFrame=false;
    
    /**
     * the start time of the powup.
     */
    private long startTime=0;
    /**
     * the poweup live time, default 3 minutes.
     */
    private static long livePeriod=180000;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the new type for the poweup.
     * @param type new type.
     */
    public void setType(int type){
        this.type=type;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the type for the poweup.
     * @return the type of the powerup.
     */
    public int getType(){
        return this.type;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param type the type of the powerup.
     */
    private Powerup(int type) {
        super(ResourceManager.getInstance().getImage(ResourceManager.BONUS),
                ResourceManager.TILE_WIDTH,ResourceManager.TILE_WIDTH);
        defineReferencePixel(ResourceManager.TILE_WIDTH/8,
                ResourceManager.TILE_WIDTH/8);
        this.type=type;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if bullet hit Player's home,if so, display the destoryed home.
     */
    public static boolean isHomeDestroyed(){
        boolean bRet=false;
        for(int i=1;i<POOL_SIZE;i++){
            if(POWERUP_POOL[i].isVisible() &&
                   POWERUP_POOL[i].getType()==Powerup.HOME_DESTROYED ){
                bRet=true;
                break;
            }
        }
        return bRet;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if bullet hit Player's home,if so, display the destoryed home.
     */
    public static boolean isHittingHome(Bullet bullet){
        boolean bRet=false;
        Powerup home=null;
        for(int i=1;i<POOL_SIZE;i++){
            if(POWERUP_POOL[i].isVisible() &&
                   POWERUP_POOL[i].getType()==Powerup.HOME ){
                home=POWERUP_POOL[i];
            }
        }
        if(home!=null){
            bRet=bullet.collidesWith(home,false);
            if(bRet){
                home.setType(Powerup.HOME_DESTROYED);
            }
        }
        return bRet;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new power up in the battle field.
     * @param type the type of the powerup.
     */
    public static void putNewPowerup(int type){
        for(int i=1;i<POOL_SIZE;i++){
            if(!POWERUP_POOL[i].isVisible()){
                POWERUP_POOL[i].setType(type);
                battleField.initPowerupPos(POWERUP_POOL[i]);
                POWERUP_POOL[i].setVisible(true);
                POWERUP_POOL[i].startTime=System.currentTimeMillis();
                break;
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Operation be done in each tick.
     */
    public void tick(){
        if(type==NOTHING || !isVisible()) return;
        long tickTime = System.currentTimeMillis();
        long refreshPeriod=MILLIS_PER_TICK;
        
        //invulnerable powerup is controlled by the player tank.
        if(type!=INVULNERABLE &&type!=HOME && type!=HOME_DESTROYED){
            if(tickTime-startTime>livePeriod){
                setFrame(NOTHING);
                setVisible(false);
                return;
            }
        }else{
            refreshPeriod=MILLIS_PER_TICK/10;
        }
        if(timeTaken>=refreshPeriod){
            showNextFrame=!showNextFrame;
            if(type==INVULNERABLE){
               if(showNextFrame){
                    setFrame(0);
                }else{
                    setFrame(1);
                } 
            }else if(type==HOME || type==HOME_DESTROYED){
               setFrame(type); 
            }else{
                if(showNextFrame){
                    setFrame(type);
                }else{
                    setFrame(NOTHING);
                }
            }
            timeTaken = System.currentTimeMillis() - tickTime;
        } else{
            timeTaken+=1;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if player's tank move above the powerup.
     * @param tank player's tank.
     */
    public static void checkPlayerTank(PlayerTank tank){
        Powerup powerup;
        for(int i=1;i<POOL_SIZE;i++){
            if(POWERUP_POOL[i].isVisible()){
                powerup=POWERUP_POOL[i];
                if(powerup.collidesWith(tank,false)){
                    tank.upgrade(powerup);
                    if(!(powerup.type==Powerup.HOME ||
                            powerup.type==Powerup.HOME_DESTROYED)){
                        powerup.setVisible(false);
                        Score.show(tank.getX(),tank.getY(),Score.SCORE_500);
                        tank.addScore(500);
                    }
                }
                
            }
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the invulnerable powerup. When player collects this powerup. it shows
     * invulnerable animation.
     * @return the invulnerable powerup.
     */
    public static Powerup getInvulnerable(){
        return POWERUP_POOL[0];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Player has collected {@see Powerup.BOMB}. Enemies should explode immediately.
     */
    public static void removeAllPowerups() {
        for (int i = 1; i < POOL_SIZE; ++i) {
            Powerup powerup = POWERUP_POOL[i];
            powerup.setVisible(false);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the layerManager for powerups.
     */
    public static void setLayerManager(LayerManager manager) {
        layerManager = manager;
        if (layerManager != null){
            
            for (int i = 0; i < POOL_SIZE; i++)
                layerManager.append(POWERUP_POOL[i]);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the Battle field for powerups.
     */
    public static void setBattleField(BattleField field) {
        battleField = field;
    }

}
