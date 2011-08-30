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
 * Splash screen.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 19/01/08
 * @author      Guidebee, Inc.
 */
public final class SplashScreen extends View implements Runnable{
    
    /**
     * Splash image.
     */
    private Bitmap imgSplash=null;
    
    /**
     * Guidebee image.
     */
    private Bitmap imgGuidebee=null;
    
    /**
     * pointer image (tank.)
     */
    private Bitmap imgPointer=null;
    
    /**
     * pointer ,user can move this pointer to select 1 player ,2 player etc.
     */
    private Sprite pointer=null;
    
    /**
     * offset X, where is the origin of the picture.
     */
    private int offsetX=0;
    
    /**
     * offset X,
     */
    private int offsetY=0;
    
    /**
     * option position.
     */
    private int[][]pointerPos=new int[3][2];
    
    /**
     * current selection.
     */
    private int currentPointerIndex=0;
    
    /**
     * splash scroll in start time.
     */
    private long animationStartTime=0;
    
    /**
     * splash scroll in period.
     */
    private static long animationPeriod=5000;
    
    /**
     * anmation postion y
     */ 
    private int imgPosY=0;
    
    /**
     * if user click ,stop animation.
     */
    private boolean stopThread=false;
    
    /**
     * the animation thread.
     */
    private Thread animationThread=null;
    
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
    public SplashScreen(Context context) {
        super(context);
        setVisibility(INVISIBLE);
        setFocusable(true);
        

    }
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		imgSplash = ResourceManager.getInstance().getImage(
				ResourceManager.SPLASH);
		imgGuidebee = ResourceManager.getInstance().getImage(
				ResourceManager.GUIDEBEE_LOGO);

		offsetX = (w - imgSplash.getWidth()) / 2;
		offsetY = (h - imgSplash.getHeight()) / 2;
		imgPointer = Bitmap.createBitmap(ResourceManager.getInstance()
				.getImage(ResourceManager.PLAYER), 0, 12, 24, 12);
		pointer = new Sprite(imgPointer, 12, 12);
		pointerPos[0][0] = offsetX + imgSplash.getWidth() / 2 - 50;
		pointerPos[0][1] = offsetY + imgSplash.getHeight()
				- imgGuidebee.getHeight() - 32;
		pointerPos[1][0] = offsetX + imgSplash.getWidth() / 2 - 50;
		pointerPos[1][1] = offsetY + imgSplash.getHeight()
				- imgGuidebee.getHeight() - 22;
		pointerPos[2][0] = offsetX + imgSplash.getWidth() / 2 - 50;
		pointerPos[2][1] = offsetY + imgSplash.getHeight()
				- imgGuidebee.getHeight() - 12;
		pointer.setPosition(pointerPos[0][0], pointerPos[0][1]);
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
        animationStartTime=System.currentTimeMillis();
        imgPosY=getHeight()-offsetY;
        animationThread=new Thread(this);
        animationThread.start();
        pointer.setVisible(false);
        stopThread=false;
        int w=getWidth();int h=getHeight();
        if(w!=0 && h!=0){
	        offsetX = (w - imgSplash.getWidth()) / 2;
			offsetY = (h - imgSplash.getHeight()) / 2;
			imgPointer = Bitmap.createBitmap(ResourceManager.getInstance()
					.getImage(ResourceManager.PLAYER), 0, 12, 24, 12);
			pointer = new Sprite(imgPointer, 12, 12);
			pointerPos[0][0] = offsetX + imgSplash.getWidth() / 2 - 50;
			pointerPos[0][1] = offsetY + imgSplash.getHeight()
					- imgGuidebee.getHeight() - 32;
			pointerPos[1][0] = offsetX + imgSplash.getWidth() / 2 - 50;
			pointerPos[1][1] = offsetY + imgSplash.getHeight()
					- imgGuidebee.getHeight() - 22;
			pointerPos[2][0] = offsetX + imgSplash.getWidth() / 2 - 50;
			pointerPos[2][1] = offsetY + imgSplash.getHeight()
					- imgGuidebee.getHeight() - 12;
			pointer.setPosition(pointerPos[0][0], pointerPos[0][1]);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * animation.
     */
    public void run(){
        Thread thread=Thread.currentThread();
        if(thread==animationThread){
            long tickTime=System.currentTimeMillis();
            while(!stopThread){
                if(tickTime-animationStartTime<animationPeriod){
                    if(imgPosY>0) {
                        imgPosY-=8;
                    }else{
                        break;
                    }
                    postInvalidate();
                    try{
                        Thread.sleep(60);
                    }catch(Exception e){}
                }else{
                    break;
                }
            }
            pointer.setVisible(true);
            stopThread=true;
            imgPosY=0;
            postInvalidate();
        }
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
        g.drawBitmap(imgSplash,offsetX,offsetY+imgPosY,null);
        g.drawBitmap(imgGuidebee,offsetX,offsetY+imgSplash.getHeight()-
               imgGuidebee.getHeight()+imgPosY,null);
        pointer.paint(g);
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * key press.
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)  {
        if(!stopThread) 
        {
            stopThread=true;
            return true;
        }
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
            case KeyEvent.KEYCODE_DPAD_DOWN :
                currentPointerIndex++;
                if(currentPointerIndex>2)
                    currentPointerIndex=0;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                currentPointerIndex--;
                if(currentPointerIndex<0)
                    currentPointerIndex=2;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                ResourceManager.stageScreen.show();
                ResourceManager.setCurrentScreen(ResourceManager.stageScreen);
                break;
        }
        pointer.setPosition(pointerPos[currentPointerIndex][0],
                pointerPos[currentPointerIndex][1]);
        postInvalidate(pointerPos[0][0],pointerPos[0][1],pointerPos[0][0]+40,pointerPos[0][1]+40);
        return true;
    }
}
