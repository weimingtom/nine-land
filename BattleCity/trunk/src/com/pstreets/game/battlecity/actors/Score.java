//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19JAN2008  James Shen                 	      Initial Creation
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

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * The classes displays score number when an enemy tank is destoryed or player
 * obtains an powerup.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 19/01/08
 * @author      Guidebee, Inc.
 */
public final class Score extends Sprite implements Actor{
    
    /**
     * score 100
     */
    public final static int SCORE_100 =0;
    
    /**
     * score 200
     */
    public final static int SCORE_200 =1;
    
    /**
     * score 300
     */
    public final static int SCORE_300 =2;
    
    /**
     * score 400
     */
    public final static int SCORE_400 =3;
    
    /**
     * score 500
     */
    public final static int SCORE_500 =4;
    
    /**
     * the score value.
     */
    private int scoreValue=-1;
    
    /**
     * maximun number of score in the battle field.
     */
    private static final int POOL_SIZE = 10;
    
    /**
     * This pool store all scores.
     */
    private static Score SCORE_POOL[];
    
    /**
     * the start time of the displaying the score.
     */
    private long startTime=0;
    /**
     * the score live time, default 1 second
     */
    private static long livePeriod=3000;
    
    /**
     * Tank should know about the layer manager.
     */
    private static LayerManager layerManager;
    
    /**
     * initial the score pool.
     */
    static {
        SCORE_POOL = new Score[POOL_SIZE];
        for(int i=0;i<POOL_SIZE;i++){
            SCORE_POOL[i]=new Score();
            SCORE_POOL[i].setVisible(false);
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the value of the score.
     * @param value new value.
     */
    public void setValue(int value){
        this.scoreValue=value;
        setFrame(value);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return the value for the score.
     * @return the value of the score.
     */
    public int getValue(){
        return this.scoreValue;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    private Score() {
        super(ResourceManager.getInstance().getImage(ResourceManager.SCORE),
                ResourceManager.getInstance().
                    getImage(ResourceManager.SCORE).getWidth()/5,
                ResourceManager.getInstance().
                    getImage(ResourceManager.SCORE).getHeight());
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
        if(isVisible()){
            long tickTime = System.currentTimeMillis();
            if(startTime>0){
                if(tickTime-startTime>livePeriod){
                    setVisible(false);
                    startTime=0;
                    return;
                }
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
     * In give position, display score.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param value the score value.
     * @return the score object.
     */
    public static Score show(int x, int y,int value) {
        for (int i = 0; i < POOL_SIZE; ++i) {
            Score score = SCORE_POOL[i];
            if (!score.isVisible()) {
                score.startTime=System.currentTimeMillis();
                score.setRefPixelPosition(x, y);
                score.setValue(value);
                score.setVisible(true);
                return score;
            }
        }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the layerManager for scores.
     */
    public static void setLayerManager(LayerManager manager) {
        layerManager = manager;
        if (layerManager != null){
            
            for (int i = 0; i < POOL_SIZE; i++)
                layerManager.append(SCORE_POOL[i]);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the Battle field for scores.
     */
    public static void setBattleField(BattleField field) {
    }
    
}
