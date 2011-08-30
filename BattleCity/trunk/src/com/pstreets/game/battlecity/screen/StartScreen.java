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
package com.pstreets.game.battlecity.screen;

import com.mapdigit.game.Sprite;
import com.pstreets.game.battlecity.ResourceManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19JAN2008  James Shen                 	      Initial Creation
////////////////////////////////////////////////////////////////////////////////
/**
 * Startup screen.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 19/01/08
 * @author      Guidebee, Inc.
 */
public final class StartScreen extends View{
    
    /**
     * game help image.
     */
    private Bitmap imgGameHelp=null;
    /**
     * turn on sound image.
     */
    private Bitmap imgTurnOnSound=null;
    
    /** 
     * pointer image.
     */
    private Bitmap imgPointer=null;
    
    /**
     * pointer
     */
    private Sprite pointer=null;
    
    /**
     * image origin x,center images.
     */
    private int offsetX=0;
    
    /**
     * image origin y,center images.
     */
    private int offsetY=0;
    
    /**
     * pointer locations.
     */
    private int[][]pointerPos=new int[2][2];
    
    /**
     * current pointer index. 
     */
    private int currentPointerIndex=0;
     
    private Paint paint=new Paint();
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public StartScreen(Context context) {
        super(context);
        setVisibility(INVISIBLE);
        setFocusable(true);
        

    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		imgGameHelp = ResourceManager.getInstance().getImage(
				ResourceManager.GAME_HELP);
		imgTurnOnSound = ResourceManager.getInstance().getImage(
				ResourceManager.TURN_SOUND);
		offsetX = (w - imgGameHelp.getWidth()) / 2;
		offsetY = (h - imgGameHelp.getHeight()) / 2;
		imgPointer = Bitmap.createBitmap(ResourceManager.getInstance()
				.getImage(ResourceManager.PLAYER), 0, 12, 24, 12);
		pointer = new Sprite(imgPointer, 12, 12);
		pointerPos[0][0] = offsetX + imgGameHelp.getWidth() / 2 - 24;
		pointerPos[0][1] = offsetY + imgGameHelp.getHeight()
				- imgTurnOnSound.getHeight() - 21;
		pointerPos[1][0] = offsetX + imgGameHelp.getWidth() / 2 - 24;
		pointerPos[1][1] = offsetY + imgGameHelp.getHeight()
				- imgTurnOnSound.getHeight() - 1;
		pointer.setPosition(pointerPos[0][0], pointerPos[0][1]);
		
		
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
        g.drawBitmap(imgGameHelp,offsetX,offsetY,null);
        g.drawBitmap(imgTurnOnSound,offsetX,offsetY+imgGameHelp.getHeight()-
               imgTurnOnSound.getHeight()-50,null);
        pointer.paint(g);
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * key pressed.
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)  {
        int gameAction=0;
        switch(keyCode){
            case KeyEvent.KEYCODE_1:
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
                     
            default:
                    break;
        }
        switch(gameAction){
            case KeyEvent.KEYCODE_DPAD_DOWN:
                currentPointerIndex++;
                if(currentPointerIndex>1)
                    currentPointerIndex=0;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                currentPointerIndex--;
                if(currentPointerIndex<0)
                    currentPointerIndex=1;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if(currentPointerIndex==0){
                    ResourceManager.isPlayingSound=true;
                }else{
                   ResourceManager.isPlayingSound=false; 
                }
                ResourceManager.splashScreen.show();
                ResourceManager.setCurrentScreen(ResourceManager.splashScreen);
                break;
        }
        pointer.setPosition(pointerPos[currentPointerIndex][0],
                pointerPos[currentPointerIndex][1]);
        postInvalidate(pointerPos[0][0],pointerPos[0][1],pointerPos[0][0]+40,pointerPos[0][1]+40);
        return true;
        
    }
}
