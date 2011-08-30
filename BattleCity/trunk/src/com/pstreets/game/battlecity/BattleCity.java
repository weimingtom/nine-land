package com.pstreets.game.battlecity;

import com.pstreets.game.battlecity.screen.GameScene;
import com.pstreets.game.battlecity.screen.GameoverScreen;
import com.pstreets.game.battlecity.screen.ScoreScreen;
import com.pstreets.game.battlecity.screen.SplashScreen;
import com.pstreets.game.battlecity.screen.StageScreen;
import com.pstreets.game.battlecity.screen.StartScreen;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

public class BattleCity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 

        ResourceManager.loadResources(this);
        ResourceManager.startScreen=new StartScreen(this);
        ResourceManager.gameScene = new GameScene(this);
		ResourceManager.splashScreen = new SplashScreen(this);
		ResourceManager.gameoverScreen = new GameoverScreen(this);
		ResourceManager.scoreScreen = new ScoreScreen(this);
		ResourceManager.stageScreen = new StageScreen(this);

		
        setContentView(R.layout.main);
        mainWindow=(FrameLayout)findViewById(R.id.mainwindow);
        ResourceManager.theDisplay=mainWindow;
        ResourceManager.theDisplay.addView(ResourceManager.startScreen);
        ResourceManager.theDisplay.addView(ResourceManager.gameScene);
		ResourceManager.theDisplay.addView(ResourceManager.splashScreen);
		ResourceManager.theDisplay.addView(ResourceManager.gameoverScreen);
		ResourceManager.theDisplay.addView(ResourceManager.scoreScreen);
		ResourceManager.theDisplay.addView(ResourceManager.stageScreen);
        
        if(ResourceManager.currentScreen==null){
            ResourceManager.currentScreen=ResourceManager.startScreen;
         }
         ResourceManager.setCurrentScreen(ResourceManager.currentScreen); 
    }
    
    private FrameLayout mainWindow;
    
}