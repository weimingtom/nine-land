package yi.game.text2;

import android.graphics.Canvas;

public abstract class Layer {
	int x = 0; // Layer�ĺ�����
	int y = 0; // Layer��������
	int width = 0; // Layer�Ŀ��
	int height = 0; // Layer�ĸ߶�
	boolean visible = true; // Layer�Ƿ�ɼ�

	Layer(int width, int height) {
		setWidthImpl(width);
		setHeightImpl(height);
	}

	/**
	 * �趨Layer����ʾλ��
	 * 
	 * @param x
	 *            ������
	 * @param y
	 *            ������
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * ����ڵ�ǰ��λ���ƶ�Layer
	 * 
	 * @param dx
	 *            ������仯��
	 * @param dy
	 *            ������仯��
	 */
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	/**
	 * ȡ��Layer�ĺ�����
	 * 
	 * @return ������ֵ
	 */
	public final int getX() {
		return x;
	}

	/**
	 * ȡ��Layer��������
	 * 
	 * @return ������ֵ
	 */
	public final int getY() {
		return y;
	}

	/**
	 * ȡ��Layer�Ŀ��
	 * 
	 * @return ���ֵ
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * ȡ��Layer�ĸ߶�
	 * 
	 * @return �߶�ֵ
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * ����Layer�Ƿ�ɼ�
	 * 
	 * @param visible
	 *            true Layer�ɼ���false Layer���ɼ�
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * ���Layer�Ƿ�ɼ�
	 * 
	 * @return true Layer�ɼ���false Layer���ɼ�
	 */
	public final boolean isVisible() {
		return visible;
	}

	/**
	 * ����Layer�����뱻����
	 * 
	 * @param c
	 */
	public abstract void paint(Canvas c);

	/**
	 * ����Layer�Ŀ��
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
	 * ����Layer�ĸ߶�
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
