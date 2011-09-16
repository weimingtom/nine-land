package FightChess;
//Download by http://www.codefans.net
import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.Image;
public class People extends Sprite
{
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	private int[][] moveFrame;
	private int moveSpeed=4;
	protected int frameIndex = 0;
	protected int direction = UP;
	private TiledLayer barrierLayer=null;
	public People(Image img,int width,int height,int [][] move)
	{
		super(img,width,height);
		moveFrame = move;
	}
	public void setBarrierLayer(TiledLayer bLayer)
	{
		barrierLayer = bLayer;
	}
	public void moveStop()
	{
		frameIndex = 0;
		setFrame(moveFrame[direction][0]);
	}
	private void setMoveFrame()
	{
		if(moveFrame==null||moveFrame[direction].length<=1)return;
		setFrame(moveFrame[direction][frameIndex++]);
		if(frameIndex>=moveFrame[direction].length)frameIndex = 0;
	}
	public void moveLeft()
	{
		direction = LEFT;
		setMoveFrame();
		move(-moveSpeed,0);
		if(collidesWith(barrierLayer,false))move(moveSpeed,0);
	}
	public void moveRight()
	{
		direction = RIGHT;
		setMoveFrame();
		move(moveSpeed,0);
		if(collidesWith(barrierLayer,false))move(-moveSpeed,0);
	}
	public void moveUp()
	{
		direction = UP;
		setMoveFrame();
		move(0,-moveSpeed);
		if(collidesWith(barrierLayer,false))move(0,moveSpeed);
	}
	public void moveDown()
	{
		direction = DOWN;
		setMoveFrame();
		move(0,moveSpeed);
		if(collidesWith(barrierLayer,false))move(0,-moveSpeed);
	}
}