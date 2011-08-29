package yi.game.text2;

import android.graphics.Canvas;

public abstract class Layer {
	int x = 0; // Layer的横坐标
	int y = 0; // Layer的纵坐标
	int width = 0; // Layer的宽度
	int height = 0; // Layer的高度
	boolean visible = true; // Layer是否可见

	Layer(int width, int height) {
		setWidthImpl(width);
		setHeightImpl(height);
	}

	/**
	 * 设定Layer的显示位置
	 * 
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 相对于当前的位置移动Layer
	 * 
	 * @param dx
	 *            横坐标变化量
	 * @param dy
	 *            纵坐标变化量
	 */
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	/**
	 * 取得Layer的横坐标
	 * 
	 * @return 横坐标值
	 */
	public final int getX() {
		return x;
	}

	/**
	 * 取得Layer的纵坐标
	 * 
	 * @return 纵坐标值
	 */
	public final int getY() {
		return y;
	}

	/**
	 * 取得Layer的宽度
	 * 
	 * @return 宽度值
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * 取得Layer的高度
	 * 
	 * @return 高度值
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * 设置Layer是否可见
	 * 
	 * @param visible
	 *            true Layer可见，false Layer不可见
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 检测Layer是否可见
	 * 
	 * @return true Layer可见，false Layer不可见
	 */
	public final boolean isVisible() {
		return visible;
	}

	/**
	 * 绘制Layer，必须被重载
	 * 
	 * @param c
	 */
	public abstract void paint(Canvas c);

	/**
	 * 设置Layer的宽度
	 * 
	 * @param width
	 */
	void setWidthImpl(int width) {
		if (width < 0) {
			throw new IllegalArgumentException();
		}
		this.width = width;
	}

	/**
	 * 设置Layer的高度
	 * 
	 * @param height
	 */
	void setHeightImpl(int height) {
		if (height < 0) {
			throw new IllegalArgumentException();
		}
		this.height = height;
	}

}
