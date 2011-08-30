//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
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
package com.pstreets.game.battlecity.actors;

import com.mapdigit.game.LayerManager;
import com.mapdigit.game.Sprite;
import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.tank.EnemyTank;
import com.pstreets.game.battlecity.actors.tank.PlayerTank;
import com.pstreets.game.battlecity.actors.tank.Tank;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Bullet class. Bullet can be shoot by player and enemy tank.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 15/01/08
 * @author      Guidebee, Inc.
 */
public final class Bullet extends Sprite implements Actor {
    
    /**
     * bullet can break brick walls and enmeny tanks. ****
     */
    public static final int GRADE_DEFAULT=1;
    /**
     * bullet can break concrete walls. ****
     */
    public static final int GRADE_BREAK_CONCRETE_WALL=2;
    
    /**
     * bullet can break water and snow. *****
     */
    public static final int GRADE_BREAK_WATER=3;
    
    
    /**
     * Tank should know about the battle field.
     */
    private static BattleField battleField;
    
    /**
     * Tank should know about the layer manager.
     */
    private static LayerManager layerManager;
    
    /**
     * bullet strength
     */
    private int strength=GRADE_DEFAULT;
    
    /**
     * shot by player or enmeny tanks
     */
    private boolean friendly=false;
    
    /**
     * maximun number of tanks in the battle field.
     */
    private static final int POOL_SIZE = 20;
    
    /**
     * This pool store all tanks include player and enemy tanks.
     */
    private static Bullet BULLET_POOL[];
    
    /**
     * bullet direction
     */
    private int direction=BattleField.NONE;
    
    /**
     * bullet direction
     */
    private int speed=ResourceManager.TILE_WIDTH/2;
    
    /**
     * when move, the delta distance, dx,dy can not be nozero at the same time.
     */
    private int dx, dy;
    
    /**
     * initial the bullet pool ,the game will resume the bullet object.
     */
    static {
        BULLET_POOL = new Bullet[POOL_SIZE];
        for(int i=0;i<POOL_SIZE;i++){
            BULLET_POOL[i]=new Bullet(GRADE_DEFAULT,BattleField.NONE,0);
            BULLET_POOL[i].setVisible(false);
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor
     * @param strength the bullet's hitting strength.
     * @param direction the bullet's moving direction.
     * @param speed the bullet's moving speed.
     */
    private Bullet(int strength,int direction,int speed) {
        super(ResourceManager.getInstance().getImage(ResourceManager.BULLET),
                ResourceManager.TILE_WIDTH/4,ResourceManager.TILE_WIDTH/4);
        defineReferencePixel(ResourceManager.TILE_WIDTH/8,
                ResourceManager.TILE_WIDTH/8);
        this.strength=strength;
        this.direction=direction;
        this.speed=speed;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the bullet friendship. If true, shot by player
     * @param friend true,shot by player,otherwize by enemy tank.
     */
    public void setFriendly(boolean friend){
        this.friendly=friend;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the bullet's speed.
     * @param speed the bullet's moving speed.
     */
    public void setSpeed(int speed){
        this.speed=speed;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the bullet's direction.
     * @param direction the bullet's moving direction.
     */
    public void setDirection(int direction){
        if(direction==BattleField.NONE) return;
        this.direction=direction;
        setFrame(direction);
        switch(direction){
            case BattleField.NORTH:
                dx = 0;
                dy = -speed;
                break;
            case BattleField.EAST:
                dx = speed;
                dy = 0;
                break;
            case BattleField.SOUTH:
                dx = 0;
                dy = speed;
                break;
            case BattleField.WEST:
                dx = -speed;
                dy = 0;
                break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the bullet's strength.
     * @param strength the bullet's hitting strength.
     */
    public void setStrength(int strength){
        this.strength=strength;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * the bullet hit something and explodes
     */
    public void explode() {
        setVisible(false);
        Explosion.explode(getRefPixelX(), getRefPixelY(), Explosion.SMALL);
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
        if (!isVisible() || direction==BattleField.NONE)
            return;
        // Move the bullet.
        move(dx, dy);
        int x = getRefPixelX();
        int y = getRefPixelY();
        PlayerTank playerTank=(PlayerTank)Tank.getTank(0);
        //outside the battle field, hitting the border
        if (x <= 0 || x >= battleField.getWidth() || y <= 0
                || y >= battleField.getHeight()) {

            //this is to avoid explosition outside the battlefield.
            if(x<=0) x=0;
            if(x >= battleField.getWidth()) x= battleField.getWidth();
            if(y<=0) y=0;
            if(y>= battleField.getHeight())y=battleField.getHeight();
            setPosition(x,y);
            explode();
            return;
        }
        
        // See if it hit a tank.
        if (friendly) {
            // See if it hit an enemy tank.
            for (int i = 1; i < Tank.POOL_SIZE; i++) {
                EnemyTank enemy = (EnemyTank)Tank.getTank(i);
                if (enemy!=null && enemy.isVisible() && 
                        collidesWith(enemy, false)) {
                    enemy.explode();
                    explode();
                    return;
                }
            }
        } else {
            // See if it hit player tank.
            
            if (collidesWith(playerTank, false)) {
                playerTank.explode();
                explode();
                return;
            }
        }
        
        //check to see if hit player's home
        if(Powerup.isHittingHome(this)){
            //TODO: Game Over
            explode();
            return; 
        }
        // See if it hit a wall.
        if (battleField.hitWall(x, y, strength)) {
            explode();
            return;
        }
        
        // See if it hit another bullet.
        for (int i = 0; i < POOL_SIZE; i++) {
            Bullet anotherBullet=BULLET_POOL[i];
            if(this!=anotherBullet && anotherBullet.isVisible()){
                if (collidesWith(anotherBullet, false)) {
                    explode();
                    BULLET_POOL[i].explode();
                    return;
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
     * Set the layerManager for tanks.
     */
    public static void setLayerManager(LayerManager manager) {
        layerManager = manager;
        if (layerManager != null){
            for (int i = 0; i < POOL_SIZE; i++)
                layerManager.append(BULLET_POOL[i]);
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
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get how many bullets the player shot.the player can shoot 3 bullets 
     * at most.
     */
    public static int getPlayerBulletCount(){
        int count=0;
        for(int i=0;i<POOL_SIZE;i++){
            if(BULLET_POOL[i].isVisible() && BULLET_POOL[i].friendly ){
                count++;
            }
        }
        return count;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get a free bullet from the pool.
     */
    public static Bullet getFreeBullet(){
        for(int i=0;i<POOL_SIZE;i++){
            if(!BULLET_POOL[i].isVisible()){
                BULLET_POOL[i].setVisible(true);
                return  BULLET_POOL[i];
            }
        }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stop all bullets in the game scene.
     */
    public static void stopAllBullets() {
        for (int i = 0; i < POOL_SIZE; ++i)
        {
            if(!BULLET_POOL[i].friendly){
                BULLET_POOL[i].setVisible(false);
            }
        }
        
    }
   
}
