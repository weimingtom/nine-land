package yi.activity;

import yi.game.text2.GameView2;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class GameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView=new GameView2(this);
		setContentView(gameView);
	}
	private GameView2 gameView;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		gameView.onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}
}
