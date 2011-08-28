package my.first.view;

import my.first.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.View;

public class MyView extends View {
    private Paint paint;
    private Context context;
    private int x = 50, y = 50;

    public MyView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
        // paint.setAntiAlias(true);//设置画笔无锯齿(如果不设置可以看到效果很差)
        this.setKeepScreenOn(true);// 设置背景常亮
        paint.setColor(Color.RED);
        setFocusable(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            x -= 10;
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            x += 10;
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            y -= 10;
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            y += 10;
            break;

        }
        postInvalidate();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap png1 = BitmapFactory.decodeResource(this.context.getResources(),
                R.drawable.png1);
        canvas.drawBitmap(png1, 1, 1, new Paint());
        canvas.drawText("游戏游戏", x, y, paint);
        // canvas.drawColor(Color.WHITE);//设置刷屏颜色
        // Rect rect = new Rect(30,30,50,50); //这里最后两个参数不是宽高、而是矩形右下角的坐标
        // canvas.drawRect(rect, paint);
        // RectF rectF = new RectF(70f,30f,90f,90f);//RectF 只是矩形 float形式
        // 只是跟Rect精确度不一样
        // canvas.drawArc(rectF, 0, 360, true, paint);
        // canvas.drawCircle(150, 30, 20, paint);//这也是画圆 第三个参数为半径
        // float[] points =new float[]{200f,10f,200f,40f,300f,30f,400f,70f};
        // canvas.drawLines(points, paint);
        // canvas.drawLines(points, 1, 4, paint);//选取特定点数组中两点来画出一条直线
        // canvas.drawText("Himi", 230, 30, paint);

        super.onDraw(canvas);
    }

}
