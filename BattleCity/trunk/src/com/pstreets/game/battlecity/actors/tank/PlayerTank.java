//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17JAN2008  James Shen                 	      Initial Creation
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

import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.BattleField;
import com.pstreets.game.battlecity.actors.Bullet;
import com.pstreets.game.battlecity.actors.Powerup;

import android.view.KeyEvent;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Player's tank.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 17/01/08
 * @author      Guidebee, Inc.
 */
public final class PlayerTank extends Tank{
    
    /**
     * Is key pressed?
     */
    private boolean keyPressed=false;
    
    /**
     * player's current grade,if equal 0,mean player dies.
     */
    private int grade = MIN_GRADE;
    
    /**
     * Player's minimum grade
     */
    private static final int MIN_GRADE=1;
    
    /**
     * tank can shoot 2 bulltes.   **
     */
    private static final int GRADE_TWO_BULLETS=2;
    
    /**
     * tank can shoot 3 bulltes.   ***
     */
    private static final int GRADE_THREE_BULLETS=3;
    
    /**
     * tank can break concrete walls. ****
     */
    private static final int GRADE_BREAK_CONCRETE_WALL=4;
    
    /**
     * tank can break water and snow. *****
     */
    private static final int GRADE_BREAK_WATER=5;
    
    /**
     * tank add one layer of shell. ******
     */
    private static final int GRADE_SHELL_1=6;
    
    /**
     * tank move fast, go one star. *
     */
    private static final int GRADE_SPEED=7;
    
    /**
     * tank add two layers of shell. *******
     */
    private static final int GRADE_SHELL_2=8;
    
    /**
     * Player's maximum grade
     */
    private static final int MAX_GRADE=8;
    
    /**
     * the player's tank is just created.
     */
    private static final int NEW_BORN=9;
    
    /**
     * the Invulnerable sheild for the player tank.
     */
    private Powerup sheild;
    
    /**
     * player tank is invulnerable at the start of the level,
     * and can become invulnerable if collects {@see Powerup.SHIELD}.
     */
    private int invulnerabilityTicks;
    
    /**
     * the time begin invulnerable
     */
    private long invulnerableTime;
    
    /**
     * invulnerable period. for start ,the time is 7.5 seconds.
     */
    private static final int invulnerablePeriod=30000;
    
    /**
     * store current direction as shooting direction
     */
    private int currentDirection=BattleField.NONE;
    
    /**
     * how many bullets can player shoot at same time.
     */
    private int avaiableBullets=1;
    
    /**
     * same direction,switch 2 image to make tank move animation.
     */
    private boolean switchImage=false;
    
    /**
     * how many lifes player has.
     */
    private int avaiableLife=3;
    
    /**
     * the score the player gets.
     */
    private int score=0;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    protected PlayerTank() {
        super(ResourceManager.getInstance().getImage(ResourceManager.PLAYER),
                ResourceManager.TILE_WIDTH,ResourceManager.TILE_WIDTH);
        sheild=Powerup.getInvulnerable();
        speed=DEFAULT_SPEED*4;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add player's score with given value
     * @param value the score value.
     */
    public void addScore(int value) {
        score+=value;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set player's score with given value
     * @param value the score value.
     */
    public void setScore(int value) {
        score=value;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return player's score with given value.
     * @return player's score with given value.
     */
    public int getScore() {
        return score;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Intialize the player tank after been destoryed or first start.
     */
    public void initTank(){
        if(battleField!=null){
            battleField.initPlayerTankPos(this);
            setVisible(true);
            direction=BattleField.NONE;
            grade=NEW_BORN;
            newBornTimer=0;
            avaiableBullets=1;
            speed=DEFAULT_SPEED;
            shoot=false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Player tanks handle key press event.
     * @param gameAction the game key code.
     */
    public void keyPressed(int gameAction){
        keyPressed=true;
        if (gameAction == KeyEvent.KEYCODE_DPAD_UP) {
            direction = BattleField.NORTH;
        } else if (gameAction == KeyEvent.KEYCODE_DPAD_RIGHT) {
            direction = BattleField.EAST;
        } else if (gameAction == KeyEvent.KEYCODE_DPAD_LEFT) {
            direction = BattleField.WEST;
        } else if (gameAction == KeyEvent.KEYCODE_DPAD_DOWN) {
            direction = BattleField.SOUTH;
        } else if (gameAction == KeyEvent.KEYCODE_DPAD_CENTER) {
            shoot = true;
        }
        if(direction!=BattleField.NONE){
            currentDirection=direction;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Player tanks handle key release event.
     */
    public void keyReleased(int gameAction){
        keyPressed=false;
        switch (gameAction) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if(direction == BattleField.NORTH){
                    //direction = BattleField.NONE;
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(direction == BattleField.SOUTH){
                    //direction = BattleField.NONE;
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(direction == BattleField.WEST){
                    //direction = BattleField.NONE;
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(direction == BattleField.EAST){
                    //direction = BattleField.NONE;
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_CENTER:
                //shoot = false;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Upgrade player's tank
     */
    public void upgrade(Powerup powerup){
        switch(powerup.getType()){
            case Powerup.CLOCK:
                EnemyTank.immobilizedStartTime=System.currentTimeMillis();
                break;
            case Powerup.STAR:
                grade++;
                if(grade>MAX_GRADE ) grade=MAX_GRADE;
                switch(grade){
                    case GRADE_SPEED:
                    {
                        speed*=2;
                        int x=getX();
                        int y=getY();
                        x=(x / speed) *speed;
                        y=(y / speed) *speed;
                        setPosition(x,y);
                    }
                    break;
                    case GRADE_TWO_BULLETS:
                        avaiableBullets=2;
                        break;
                    case GRADE_THREE_BULLETS:
                        avaiableBullets=3;
                        break;
                }
        
                break;
            case Powerup.SHIELD:
                invulnerableTime=System.currentTimeMillis();
                invulnerabilityTicks=invulnerablePeriod;
                sheild.setPosition(getX(),getY());
                sheild.setVisible(true);
                break;
            case Powerup.BOMB:
                EnemyTank.explodeAllEmenies();
                break;
            case Powerup.TANK:
                avaiableLife++;
                break;
            case Powerup.SHOVEL:
                battleField.makeHomeConcreteWall();
                break;
                
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * set player avaiable lives.
     * @param live new life count.
     */
    public void setAvaiableLives(int live){
        avaiableLife=live;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get player avaiable lives.
     */
    public int getAvaiableLives(){
        return avaiableLife;
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
    public void think(){
        if(grade==NEW_BORN){
            newBornTimer++;
            if(newBornTimer>4){
                grade= MIN_GRADE;
                direction=BattleField.NONE;
                currentDirection=BattleField.NORTH;
                newBornTimer=0;
                setFrame(0);
                invulnerableTime=System.currentTimeMillis();
                invulnerabilityTicks=invulnerablePeriod/4;
                sheild.setPosition(getX(),getY());
                sheild.setVisible(true);
                
            }else{
                try{
                    setFrame(newBornTimer*9-1);
                }catch(Exception e){
                    //System.out.println("Playertank");
                }
            }
            
        } else{
            long tickTime = System.currentTimeMillis();
            if(tickTime-invulnerableTime>invulnerabilityTicks){
                sheild.setVisible(false);
            }else{
                sheild.setPosition(getX(),getY());
                sheild.setVisible(true);
            }
            changeDirection(direction);
            if(currentDirection!=BattleField.NONE){
                switchImage=!switchImage;
                int offset=switchImage? 0:1;
                try{
                    setFrame(currentDirection*9+((int)(grade-1)/2)*2 +offset);
                }catch(Exception e){
                    //System.out.println("Playertank1");
                }
                
            }
        }
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 16JAN200 8  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if the player's tank is invulnerable
     * @return true,the tank is invulnerable.
     */
    public boolean isInvulnerable(){
        return sheild.isVisible();
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
        
        int bulletCount=Bullet.getPlayerBulletCount();
        if(shoot && bulletCount<avaiableBullets){
        	shoot = false;
        	direction=BattleField.NONE;
            int step=ResourceManager.TILE_WIDTH;
            bullet=Bullet.getFreeBullet();
            if(bullet!=null){
                if(ResourceManager.isPlayingSound){
                    ResourceManager.playSound(ResourceManager.SHOOT_SOUND);
                }
                int x = getRefPixelX();
                int y = getRefPixelY();
                
                switch (currentDirection) {
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
                bullet.setDirection(currentDirection);
                if(grade>=GRADE_BREAK_WATER){
                    bullet.setStrength(Bullet.GRADE_BREAK_WATER);
                }else if(grade>=GRADE_BREAK_CONCRETE_WALL){
                    bullet.setStrength(Bullet.GRADE_BREAK_CONCRETE_WALL);
                }else{
                    bullet.setStrength(Bullet.GRADE_DEFAULT);
                }
                
                bullet.setRefPixelPosition(x-1, y-1);
                bullet.setFriendly(true);
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
     * Stop the player tank
     */
    public void stop(){
        direction=BattleField.NONE;
        shoot=false;
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
        if(!isInvulnerable()){
            if(grade>=GRADE_BREAK_WATER){
                grade--;
            }else{
                avaiableLife--;
                super.explode();
            }
        }
    }
}
