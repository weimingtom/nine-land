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
 * Stage screen.
 * <p>
 * <hr><b>&copy; Copyright 2008 Guidebee, Inc. All Rights Reserved.</b>
 * @version     1.00, 19/01/08
 * @author      Guidebee, Inc.
 */
public final class StageScreen extends View implements Runnable{
    
    /**
     * stage image
     */
    private Bitmap imgStage=null;
    
    /**
     * number image
     */
    private Bitmap imgNumberBlack=null;
    
    /**
     * offset X, where is the origin of the picture.
     */
    private int offsetX=0;
    
    /**
     * offset X,
     */
    private int offsetY=0;
    
    /**
     * splash scroll in start time.
     */
    private long animationStartTime=0;
    
    /**
     * splash scroll in period.
     */
    private static long animationPeriod=50;
    
    /**
     * anmation postion y
     */ 
    private int imgPosY=0;
    
    /**
     * anmation postion y
     */ 
    private int imgPosX=0;
    
    /**
     * if user click ,stop animation.
     */
    private boolean stopThread=false;
    
    /**
     * the animation thread.
     */
    private Thread animationThread=null;
    
    /**
     * scroll in from top to bottom? or from left to right
     */
    private boolean fromTop2Bottom=false;
    
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
    public StageScreen(Context context) {
        super(context);
        setVisibility(INVISIBLE);
        setFocusable(true);
        
    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		imgStage = ResourceManager.getInstance()
				.getImage(ResourceManager.STAGE);
		imgNumberBlack = ResourceManager.getInstance().getImage(
				ResourceManager.NUMBER_BLACK);

		offsetX = (w - imgStage.getWidth()) / 2;
		offsetY = (h - imgStage.getHeight()) / 2;
	}
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20JAN2008  James Shen                 	          Initial Creation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * draw number in score bar
     * @param g the graphics object
     * @param number the number need to be drawn
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    private void drawNumber(Canvas g, int number,int x,int y){
        Bitmap imageNumber=imgNumberBlack;
        String strNumber=String.valueOf(number);
        int numberWidth=imageNumber.getHeight();
        for(int i=0;i<strNumber.length();i++){
            char ch=strNumber.charAt(i);
            int index=(ch-'0') % 10;
            Bitmap oneNumber=Bitmap.createBitmap(imageNumber,index*numberWidth,0,
                    numberWidth,numberWidth);
            g.drawBitmap(oneNumber,x+i*numberWidth,y,null);
        }
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
    	int w=getWidth();
    	int h=getHeight();
    	if(w!=0 && h!=0){
	    	offsetX = (w - imgStage.getWidth()) / 2;
			offsetY = (h - imgStage.getHeight()) / 2;
	        animationStartTime=System.currentTimeMillis();
	        imgPosY=getHeight()-offsetY;
	        animationThread=new Thread(this);
	        animationThread.start();
	        stopThread=false;
	        fromTop2Bottom=!fromTop2Bottom;
	        imgPosY=imgPosX=0;

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
                    if(fromTop2Bottom){
                        if(imgPosY<getHeight()/2+8) {
                            imgPosY+=8;
                        }else{
                            break;
                        }
                    }else{
                        if(imgPosX<getWidth()/2+8) {
                            imgPosX+=8;
                        }else{
                            break;
                        }
                    }
                    postInvalidate();
                    try{
                        Thread.sleep(50);
                    }catch(Exception e){}
                }else{
                    break;
                }
            }
            stopThread=true;
            imgPosY=getHeight()/2+8;
            imgPosX=getWidth()/2+8;
            postInvalidate(0,offsetY,getWidth(),offsetY+40);
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
    	paint.setColor(0xFF808080);
    	paint.setStyle(Paint.Style.FILL);
        g.drawRect(0,0,getWidth(),getHeight(),paint);
        paint.setColor(0xFF808080);
        int length=String.valueOf(ResourceManager.gameLevel).length();
        if(fromTop2Bottom){
            g.drawRect(0,0,getWidth(),imgPosY,paint);
            g.drawRect(0,getHeight()-imgPosY,getWidth(),getHeight(),paint);
        }else{
            g.drawRect(0,0,imgPosX,getHeight(),paint);
            g.drawRect(getWidth()-imgPosX,0,getWidth(),getHeight(),paint);
        }
        g.drawBitmap(imgStage,offsetX-(length+1)*imgNumberBlack.getHeight(),offsetY,null);
        drawNumber(g,ResourceManager.gameLevel,imgStage.getWidth()+
                offsetX-length*imgNumberBlack.getHeight()
                ,offsetY);

        
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
            case KeyEvent.KEYCODE_DPAD_DOWN:
                ResourceManager.gameLevel--;
                if(ResourceManager.gameLevel<1) ResourceManager.gameLevel=1;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                ResourceManager.gameLevel++;
                if(ResourceManager.gameLevel>50) ResourceManager.gameLevel=50;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                ResourceManager.gameScene.newGame();
                ResourceManager.setCurrentScreen(ResourceManager.gameScene);
                break;
        }
        postInvalidate();
        return true;
        
    }
}
