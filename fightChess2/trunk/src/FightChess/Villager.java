package FightChess;
//Download by http://www.codefans.net
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Villager extends People
{
	private String dialog[];
	private boolean dialogFlg = true;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	public Villager(Image img,int width,int height,int [][] move,String[] d,
			int startx,int starty,int endx,int endy)
	{
		super(img,width,height,move);
		dialog = d;
		startX=startx;
		startY=starty;
		endX=endx;
		endY=endy;
		this.setPosition(startX,startY);
	}
	public boolean action(People myHero)
	{
		if(dialogFlg&&abs(myHero.getX()-this.getX())<20
				&&abs(myHero.getY()-this.getY())<20)
		{
			dialogFlg = false;
			return true;
		}
		if(startX==endX)
		{
			if(this.getY()<=startY)
				direction = DOWN;
			else if(this.getY()>=endY)
				direction = UP;
		}else if(startY==endY)
		{
			if(this.getX()<=startX)
				direction = RIGHT;
			else if(this.getX()>=endX)
				direction = LEFT;
		}
		move();
		return false;
	}
	public String[] getDialog()
	{
		return dialog;
	}
	private void move()
	{
		switch(direction)
		{
			case UP:
				moveUp();
				break;
			case DOWN:
				moveDown();
				break;
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
		}
	}
	private int abs(int a)
	{
		if(a<0)return -a;
		else return a;
	}
}