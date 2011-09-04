package org.loon.test.slg;

import org.loon.framework.android.game.LGameAndroid2DActivity;


public class Main extends LGameAndroid2DActivity {
	

	public void onMain() {
		this.initialization(true);
		this.setFPS(60);
		this.setScreen(new SLG());
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.showScreen();

	}
	
}