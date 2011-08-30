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

import com.pstreets.game.battlecity.ResourceManager;
import com.pstreets.game.battlecity.actors.BattleField;
import com.pstreets.game.battlecity.actors.Score;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Smart tank.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 18/01/08
 * @author      Guidebee, Inc.
 */
public final class SmartTank extends EnemyTank{
    
   ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param hasPrize if true, when player hit the tank, a new powerup is put
     *  in the battle field.
     */
    protected SmartTank(boolean hasPrize) {
        super(hasPrize);
        direction=BattleField.SOUTH;
        speed=ResourceManager.TILE_WIDTH/2;
        score=Score.SCORE_300;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Look for the player tank,if in the same row or col return true.
     * @param dir current direction.
     */
    private boolean lookforPlayer(int dir){
        PlayerTank playerTank=(PlayerTank)TANK_POOL[0];
        int playerX = playerTank.getRefPixelX();
        int playerY = playerTank.getRefPixelY();
        int dx=0;int dy=0;
        if(dir == BattleField.NORTH){
            dy = -speed;
        } else if(dir == BattleField.SOUTH){
            dy = speed;
        } else if(dir == BattleField.EAST){
            dx = speed;
        } else if(dir == BattleField.WEST){
            dx = -speed;
        } else{
            return false;
        }
        int myx = this.getRefPixelX();
        int myy = this.getRefPixelY();
        int width =  battleField.getWidth();
        int height = battleField.getHeight();
        while(myx > 0 && myx < width && myy > 0 && myy < height){
            //if(battleField.hitWall(myx,myy,0)){return false;}
            if(myx > playerX - speed && myx < playerX + speed && myy > playerY - speed && myy < playerY +speed){
                return true;
            }
            myx+=dx;
            myy+=dy;
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
     * Tank thinks before move.
     */
    public void think(){
        PlayerTank playerTank=(PlayerTank)TANK_POOL[0];
        int newdir = BattleField.SOUTH;
        int minDistance=Integer.MAX_VALUE;
        int distance=0;
        
        boolean blockedeast = battleField.containsImpassableArea
                (getX() + speed, getY(), getWidth(), getHeight());
        boolean blockedsouth = battleField.containsImpassableArea
                (getX(),getY() + speed, getWidth(), getHeight());
        boolean blockedwest = battleField.containsImpassableArea
                (getX()-speed,getY(),getWidth(), getHeight());
        boolean blockednorth = battleField.containsImpassableArea
                (getX(),getY() - speed, getWidth(), getHeight());
        
        if(direction == BattleField.NORTH){
            if(!blockedwest){
                distance=(getX()-speed-playerTank.getX())*
                        (getX()-speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.WEST;
                    minDistance=distance;
                }
            }
            if(!blockedeast){
                
                distance=(getX()+speed-playerTank.getX())*
                        (getX()+speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.EAST;
                    minDistance=distance;
                }
            }
            
            if(!blockednorth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()-speed-playerTank.getY())*
                        (getY()-speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.NORTH;
                    minDistance=distance;
                }
            }
            distance=(getX()-playerTank.getX())*
                    (getX()-playerTank.getX())+
                    (getY()-playerTank.getY())*
                    (getY()-playerTank.getY());
            if(distance<minDistance){
                newdir = BattleField.NONE;
                minDistance=distance;
            }
            
        }else if(direction == BattleField.WEST){
            if(!blockedwest){
                
                distance=(getX()-speed-playerTank.getX())*
                        (getX()-speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.WEST;
                    minDistance=distance;
                }
            }
            if(!blockedsouth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()+speed-playerTank.getY())*
                        (getY()+speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.SOUTH;
                    minDistance=distance;
                }
            }
            
            if(!blockednorth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()-speed-playerTank.getY())*
                        (getY()-speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.NORTH;
                    minDistance=distance;
                }
            }
            distance=(getX()-playerTank.getX())*
                    (getX()-playerTank.getX())+
                    (getY()-playerTank.getY())*
                    (getY()-playerTank.getY());
            if(distance<minDistance){
                newdir = BattleField.NONE;
                minDistance=distance;
            }
        }else if(direction == BattleField.SOUTH){
            if(!blockedwest){
                
                distance=(getX()-speed-playerTank.getX())*
                        (getX()-speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.WEST;
                    minDistance=distance;
                }
            }
            if(!blockedsouth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()+speed-playerTank.getY())*
                        (getY()+speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.SOUTH;
                    minDistance=distance;
                }
            }
            
            if(!blockedeast){
                
                distance=(getX()+speed-playerTank.getX())*
                        (getX()+speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.EAST;
                    minDistance=distance;
                }
            }
            distance=(getX()-playerTank.getX())*
                    (getX()-playerTank.getX())+
                    (getY()-playerTank.getY())*
                    (getY()-playerTank.getY());
            if(distance<minDistance){
                newdir = BattleField.NONE;
                minDistance=distance;
            }
        }else if(direction == BattleField.EAST){
            if(!blockednorth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()-speed-playerTank.getY())*
                        (getY()-speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.NORTH;
                    minDistance=distance;
                }
            }
            if(!blockedsouth){
                
                distance=(getX()-playerTank.getX())*
                        (getX()-playerTank.getX())+
                        (getY()+speed-playerTank.getY())*
                        (getY()+speed-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.SOUTH;
                    minDistance=distance;
                }
            }
            
            if(!blockedeast){
                
                distance=(getX()+speed-playerTank.getX())*
                        (getX()+speed-playerTank.getX())+
                        (getY()-playerTank.getY())*
                        (getY()-playerTank.getY());
                if(distance<minDistance){
                    newdir = BattleField.EAST;
                    minDistance=distance;
                }
            }
            distance=(getX()-playerTank.getX())*
                    (getX()-playerTank.getX())+
                    (getY()-playerTank.getY())*
                    (getY()-playerTank.getY());
            if(distance<minDistance){
                newdir = BattleField.NONE;
                minDistance=distance;
            }
        }else{
            if((Math.abs(rnd.nextInt()) %10) >5){
                //every so often, go crazy
                newdir = Math.abs(rnd.nextInt()) % 4;
            }
        }
        changeDirection(newdir);
        
        shoot=false;
        int shooting=Math.abs(rnd.nextInt()) % 100;
        if(shooting>=50 ||lookforPlayer(direction)) {
            shoot=true;
        }
        
        super.think();
    }
    
}
