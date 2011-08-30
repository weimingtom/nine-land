//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 18JAN2008  James Shen                 	      Initial Creation
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

import java.util.Random;

import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.BattleField;
import com.pstreets.game.battlecity.actors.Bullet;
import com.pstreets.game.battlecity.actors.Score;
import com.pstreets.game.battlecity.screen.GameScene;

//--------------------------------- IMPORTS ------------------------------------
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Base class for enemy tanks.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 18/01/08
 * @author      Guidebee, Inc.
 */
public abstract class EnemyTank extends Tank{
    
    /**
     * Whether the tank has prize,hit one has prize ,a powerup is put
     * in the battle field.
     */
    protected boolean hasPrize=false;
    
    /**
     * how many score if player destory this tank.
     */
    protected int score;
    
    /**
     * Enemy tank type.
     */
    protected int type;
    
    /**
     * Simple tank, move blindly.
     */
    public static final int TYPE_SIMPLE=0;
    
    /**
     * Fast tank, move blindly but much faster than simple tank.
     */
    public static final int TYPE_FAST=1;
    
    /**
     * Smart tank can detect where's player tank, and try to destory player.
     */
    public static final int TYPE_SMART=2;
    
    /**
     * Heavy tank, has more blood, player has to shoot more bullets to destory it.
     */
    public static final int TYPE_HEAVY=3;
    
    /**
     * the enemy tank is just created.
     */
    protected static final int STATUS_NEW_BORN=0;
    
    /**
     * Normal status.
     */
    protected static final int STATUS_LIVE=1;
    
    /**
     * the enemy tank has been destroyed.
     */
    protected static final int STATUS_DEAD=2;
    
    /**
     * same direction,switch 2 image to make tank move animation.
     */
    protected boolean switchImage=false;
    
    /**
     * The blood of enemy tank, the heavy thank has 4 blood.
     */
    protected int blood=1;
    /**
     * enemy's tanks current status,can be new created ,live, dead.
     */
    protected int status;
    
    /**
     * Random used to move tank randomly.
     */
    protected static Random rnd=new Random();
    
    /**
     * When tanks shoot last bullet, avoid enemy tank shoot too fast.
     */
    protected long startShootingTime=0;
    
    /**
     * The enemy tank shoot another bullet at least after 2 second since last
     * shooting.
     */
    protected static int minimumShootingPeriod=2000;
    
    /**
     * Enemies could be frozen by Powerup.CLOCK.
     */
    protected static long immobilizedStartTime=0;
    
    /**
     * Enemies frozen time, default 30 seconds.
     */
    protected static final int immobilizedPeriod=30000;
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private Constructor.
     * @param hasPrize if true, when player hit the tank, a new powerup is put
     *  in the battle field.
     */
    protected EnemyTank(boolean hasPrize) {
        super(ResourceManager.getInstance().getImage(ResourceManager.ENEMY),
                ResourceManager.TILE_WIDTH,ResourceManager.TILE_WIDTH);
        this.hasPrize=hasPrize;
        this.speed=ResourceManager.TILE_WIDTH/6;
        setFrame(22);
        setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN200 8  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank shoots.
     * @return the bullet the tank shoots.
     */
    public Bullet shoot(){
        Bullet bullet=null;
        long tickTime=System.currentTimeMillis();
        //Check the two shoots time periods is greate than give period.
        boolean canShoot=(tickTime-startShootingTime)>minimumShootingPeriod;
        canShoot &=direction!=BattleField.NONE;
        if(shoot && canShoot){
            startShootingTime=tickTime;
            int step=ResourceManager.TILE_WIDTH;
            bullet=Bullet.getFreeBullet();
            if(bullet!=null){
                
                int x = getRefPixelX();
                int y = getRefPixelY();
                
                switch (direction) {
                    case BattleField.NORTH:
                        y -= step / 2;
                        break;
                    case BattleField.EAST:
                        x += step / 2;
                        break;
                    case BattleField.SOUTH:
                        y += step / 2;
                        break;
                    case BattleField.WEST:
                        x -= step / 2;
                        break;
                }
                bullet.setSpeed(ResourceManager.TILE_WIDTH/2);
                bullet.setDirection(direction);
                //Heavy tank shoots more powerful bullets.
                if(type==TYPE_HEAVY){
                    bullet.setStrength(Bullet.GRADE_BREAK_CONCRETE_WALL);
                } else{
                    bullet.setStrength(Bullet.GRADE_DEFAULT);
                }
                bullet.setRefPixelPosition(x-1, y-1);
                bullet.setFriendly(false);
                bullet.setVisible(true);
            }
        }
        return bullet;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tank thinks before move. subclass shall call this as last statement in
     * this overriden function.
     */
    public void think(){
        if(status==STATUS_NEW_BORN){
            newBornTimer++;
            if(newBornTimer>4){
                status= STATUS_LIVE;
                direction=BattleField.NONE;
                setFrame(type*4+2);
                newBornTimer=0;
            }else{
                try{
                if(newBornTimer*23-1>=0){
                   setFrame(newBornTimer*23-1);
                }}
                catch(Exception e){
                    System.out.println("Enemythink");
                }
            }
        } else{
            changeDirection(direction);
            if(direction!=BattleField.NONE){
                setTankFrame();
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
     * Operation be done in each tick.
     */
    public void tick(){
        if(immobilizedStartTime>0){
            long tickTime=System.currentTimeMillis();
            if(tickTime-immobilizedStartTime>immobilizedPeriod){
                immobilizedStartTime=0;
            }
        }else{
            super.tick();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Intialize the player tank after been destoryed or first start.
     */
    public void initTank(){
        status=STATUS_NEW_BORN;
        switch(type){
            case TYPE_SIMPLE:
            case TYPE_SMART:
                speed=ResourceManager.TILE_WIDTH/6;
                blood=1;
                break;
            case TYPE_FAST:
                blood=1;
                speed=ResourceManager.TILE_WIDTH/2;
                break;
            case TYPE_HEAVY:
                speed=ResourceManager.TILE_WIDTH/6;
                blood=4;
                break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set if the tank has prize.
     * @param hasPrize true,has prize.
     */
    public void setHasPrize(boolean hasPrize){
        this.hasPrize=hasPrize;
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
    public static void explodeAllEmenies() {
        for (int i = 1; i < POOL_SIZE; ++i) {
            Tank tank = TANK_POOL[i];
            if (tank.isVisible()){
                ((EnemyTank)tank).blood=1;
                tank.explode();
            }
        }
        immobilizedStartTime=0;
    }
      
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the count of all visible enemy tanks.
     * @return the count of all visible enemy tanks.
     */
    public static int getVisibleEnemyTanks(){
        int count=0;
        for(int i=1;i<POOL_SIZE;i++){
            if(TANK_POOL[i].isVisible()){
                count++;
            }
        }
        return count;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new enemy tank.
     * @param type the type of enemy tank.
     * @return an enemy tank or null.
     */
    public static EnemyTank newEnemyTank(int type){
        EnemyTank enemyTank=getFreeEnemyTank(type);
        if(enemyTank!=null){
            enemyTank.initTank();
            enemyTank.setVisible(true);
        }
        return enemyTank;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set new tank frame based on type,direction,hasPrize.
     */
    protected void setTankFrame(){
        switchImage=!switchImage;
        int offset=switchImage ? 0: 1;
        int offset1=hasPrize ?2:0;
        int frame=direction*23+offset+offset1+type*4+(blood-1)*2;
        if(type==TYPE_HEAVY && hasPrize){
            frame=direction*23+offset+type*4+8;
        }
        try{
        if(frame>=0 || frame<92){
            setFrame(frame);
        }}
        catch(Exception e){
            //System.out.println("enemy:"+frame+","+blood);
        }
        
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
    public void explode() {
        
        if(type==TYPE_HEAVY && hasPrize && blood==4){
            GameScene.canPutPowerup=true;
            hasPrize=false;
        }
        blood--;
        if(blood==0){
            super.explode();
            Score.show(getX(),getY(),score);
            //this global variable approach is not that good.
            GameScene.enemyTankRemains--;
            if(hasPrize){
                GameScene.canPutPowerup=true;
            }
            GameScene.enemyTanksCount[type]++;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get a free enemy tank based on given type.
     * @param type the type of enemy tank.
     * @return an enemy tank or null.
     */
    private static EnemyTank getFreeEnemyTank(int type){
        EnemyTank enemyTank=null;
        int left=0,right=0;
        switch(type){
            case TYPE_SIMPLE:
                left=1;right=7;
                break;
            case TYPE_FAST:
                left=8;right=11;
                break;
            case TYPE_SMART:
                left=12;right=15;
                break;
            case TYPE_HEAVY:
                left=16;right=19;
                break;
        }
        for(int i=left;i<=right;i++){
            enemyTank=(EnemyTank)TANK_POOL[i];
            if(!enemyTank.isVisible()){
                battleField.initEnemyTankPos(enemyTank);
                enemyTank.setVisible(true);
                if(overlapsTank(enemyTank)){
                    enemyTank.setVisible(false);
                    enemyTank=null;
                    continue;
                }
                return enemyTank;
            }
        }
        return null;
    }
    
}
