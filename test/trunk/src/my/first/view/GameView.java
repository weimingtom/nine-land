package my.first.view;

import my.first.thread.GameThread;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback {

    private GameThread gt;
    private String tag = "GameView";

    public GameView(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        this.gt = new GameThread(this.getHolder());
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        Log.d(tag, "surface changed");

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(tag, "surface create");
        gt.start();
        Log.d(tag, "thread runing");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gt.setRunStatus(false);
        Log.d(tag, "surface destroyed");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(tag, "alive is:" + Boolean.toString(gt.isAlive()));
        gt.setRunStatus(false);
        return super.onKeyDown(1, event);

    }
}
