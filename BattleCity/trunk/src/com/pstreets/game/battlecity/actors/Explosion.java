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

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 15JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Explosion class. Display explosion image when bullet or tank explodes.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 15/01/08
 * @author      Guidebee, Inc.
 */
public final class  Explosion extends Sprite implements Actor{
    
    /**
     * Explosion should know about the layer manager.
     */
    private static LayerManager layerManager;
    
    /**
     * Small explosion. when bullets hit wall.
     */
    public static final int SMALL = 0;
    
    /**
     * Big explosion, when tank explodes.
     */
    public static final int BIG = 1;
    
    /**
     * The width of the explosion image.
     */
    private static final int WIDTH = 24;
    
    /**
     * The height of the explosion image.
     */
    private static final int HEIGHT = 24;
    
    /**
     * Explosition sequence.
     */
    private static final int[][] FRAME_SEQ = new int[][] {
        { 0, 1, 1,2,2 },
        { 0, 1, 1, 2, 2, 3, 3, 4,4,5,5 },
    };
    
    /**
     * The pool size of explosion.
     */
    private static final int POOL_SIZE = 10;
    
    /**
     * the explosion pool.
     */
    private static Explosion[] EXPLOSIONS_POOL;
    
    /**
     * Initialized the explostion pool.
     */
    static {
        EXPLOSIONS_POOL = new Explosion[POOL_SIZE];
        for (int i = 0; i < POOL_SIZE; ++i)
            EXPLOSIONS_POOL[i] = new Explosion(SMALL);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private Constructor.
     * @param strength SMALL or BIG explosion.
     */
    private Explosion(int strength) {
        super(ResourceManager.getInstance().getImage(ResourceManager.EXPLODE),
                WIDTH, HEIGHT);
        defineReferencePixel(WIDTH / 2, HEIGHT / 2);
        setVisible(false);
        setStrength(strength);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set explosion strength
     * @param strength SMALL or BIG explosion.
     */
    private void setStrength(int strength) {
        setFrameSequence(FRAME_SEQ[strength]);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * In give position, display explosion animation.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param strenght SMALL or BIG explosion.
     */
    public static Explosion explode(int x, int y, int strength) {
        for (int i = 0; i < POOL_SIZE; ++i) {
            Explosion explosion = EXPLOSIONS_POOL[i];
            if (!explosion.isVisible()) {
                explosion.setRefPixelPosition(x, y);
                explosion.setFrame(0);
                explosion.setStrength(strength);
                explosion.setVisible(true);
                if(ResourceManager.isPlayingSound){
                    ResourceManager.playSound(ResourceManager.EXPLODE_SOUND);
                }
                return explosion;
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
     * Operation be done in each tick.
     */
    public void tick() {
        if (!isVisible())
            return;
        nextFrame();
        if (getFrame() == 0){
            setVisible(false);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 15JAN2008  James Shen                 	          Initial Creation 
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Stop all explosion.
     */
    public static void stopAllExplosions() {
        for (int i = 0; i < POOL_SIZE; i++)
            EXPLOSIONS_POOL[i].setVisible(false);
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
                layerManager.append(EXPLOSIONS_POOL[i]);
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
    }
    
}
