package my.first.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private SurfaceHolder sufaceHolder;
    private boolean runStatus = true;
    private String tag = this.getClass().toString();

    public GameThread(SurfaceHolder surfaceHolder) {
        this.sufaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        int min, sec;
        try {
            while (this.runStatus) {
                Log.v(tag, "game thread is running");
                Canvas c = this.sufaceHolder.lockCanvas();
                c.drawColor(Color.WHITE);
                Date dt = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                min = dt.getMinutes();
                sec = dt.getSeconds();
                String dtString = sdf.format(dt);
                c.drawText(dtString, min * 3, sec * 3, new Paint());
                Thread.sleep(1000);
                if (c != null){
                    this.sufaceHolder.unlockCanvasAndPost(c);
                }
            }
        } catch (Exception e) {
            Log.v(tag, "something is wrong: " + e.toString());
        }
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }
}
