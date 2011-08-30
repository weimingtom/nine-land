//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19JAN2008  James Shen                 	      Initial Creation
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
package com.pstreets.game.battlecity.screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

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
 * Gameover screen.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 19/01/08
 * @author      Guidebee, Inc.
 */


public final class GameoverScreen extends View {
    
    
    /**
     * image game over
     */
    private Bitmap imgGameover=null;
    
    /**
     * image x origin, center the image on the screen.
     */
    private int offsetX=0;
    
    /**
     * image y origin.
     */
    private int offsetY=0;
    

    private Paint paint=new Paint();
    
    final Handler mHandler = new Handler();
   
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public GameoverScreen(Context context) {
        super(context);
        setVisibility(INVISIBLE);
        
        
    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		imgGameover = ResourceManager.getInstance().getImage(
				ResourceManager.GAME_OVER_BIG);

		offsetX = (w - imgGameover.getWidth()) / 2;
		offsetY = (h - imgGameover.getHeight()) / 2;

	}
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Start the animation.
     */
    public void show(){
       
        if(ResourceManager.isPlayingSound){
            ResourceManager.playSound(ResourceManager.GAMEOVER_SOUND);
        }

       
        
       mHandler.post(mUpdateResults);
    }
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * paint.
     */
    @Override protected void onDraw(Canvas g){
        //clear background to black

    	paint.setColor(0xFF000000);
    	paint.setStyle(Paint.Style.FILL);
        g.drawRect(0,0,getWidth(),getHeight(),paint);
        g.drawBitmap(imgGameover,offsetX,offsetY,null);
    }
    
    
    final Runnable mUpdateResults = new Runnable() {       
    	public void run() {   
    		 try {
    				Thread.sleep(3000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		 ResourceManager.splashScreen.show();
    	        ResourceManager.setCurrentScreen(ResourceManager.splashScreen);
    	}
    	};
    
}


