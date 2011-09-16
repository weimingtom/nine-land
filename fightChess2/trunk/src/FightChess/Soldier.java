package FightChess;
//Download by http://www.codefans.net
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Date;
import java.util.Random;
import javax.microedition.lcdui.game.*;
public class Soldier extends Sprite
{
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	private int [][] moveFrame;
	private boolean isDeath = false;
	private boolean isBeFighted = false;
	private int showHpCount = 0;
	private int hpNow = 100;
	private int fightPower = 10;
	private int defencePower = 5;
	private int grade = 0;
	private int bloodIndex=0;
	private int thisRow;
	private int thisCol;
	private int oldRow;
	private int oldCol;
	private int moveRangeR = 2;
	private int moveRangeC = 2;
	private int[][]checkMsg;
	private int frameIndex = 0;
	private int direction = UP;
	public Sprite sptSword;
	private boolean isHero;
	private int fightType=0;
	private int [][] fightRange;
	private int fightCurIndex;
	private int thisIndex;
	private Random r=new Random(new Date().getTime());
	public Soldier(Image img,int width,int height,
			int [][] move,boolean ishero,int fighttype,int thisindex)
	{
		super(img,width,height);
		moveFrame = move;
		isHero = ishero;
		fightType = fighttype;
		thisIndex=thisindex;
		switch(fightType)
		{
//		[][0]:row,[][1]:col,[][2]:up,[][3]:down,[][4]:left,[][5]:down
			case 1:
				/*0#1
				 *#*#
				 *2#3*/
				fightRange = new int[][]{{0,0,0,2,0,1},
									 	 {0,0,1,3,0,1},
										 {0,0,0,2,2,3},
										 {0,0,1,3,2,3},};
				break;
			case 2:
				/*102
				 *3*4
				 *567*/
				fightRange = new int[][]{{0,0,0,6,1,2},
									 	 {0,0,1,3,1,0},
										 {0,0,2,4,0,2},
										 {0,0,1,5,3,4},
										 {0,0,2,7,3,4},
										 {0,0,3,5,5,6},
										 {0,0,0,6,5,7},
										 {0,0,4,7,6,7}};
				break;
			default:
				/*#0#
				 *2*3
				 *#1#*/
				fightRange = new int[][]{{0,0,0,1,2,3},
										 {0,0,0,1,2,3},
										 {0,0,0,1,2,3},
										 {0,0,0,1,2,3}};
				break;
		}
	}
	public void setFightRange(int fRow,int fCol)
	{
		switch(fightType)
		{
			case 1:
				/*0#1
				 *#*#
				 *2#3*/
				fightRange[0][0]=fRow-1;
				fightRange[0][1]=fCol-1;
				fightRange[1][0]=fRow-1;
				fightRange[1][1]=fCol+1;
				fightRange[2][0]=fRow+1;
				fightRange[2][1]=fCol-1;
				fightRange[3][0]=fRow+1;
				fightRange[3][1]=fCol+1;
				break;
			case 2:
				/*102
				 *3*4
				 *567*/
				fightRange[0][0]=fRow-1;
				fightRange[0][1]=fCol;
				fightRange[1][0]=fRow-1;
				fightRange[1][1]=fCol-1;
				fightRange[2][0]=fRow-1;
				fightRange[2][1]=fCol+1;
				fightRange[3][0]=fRow;
				fightRange[3][1]=fCol-1;
				fightRange[4][0]=fRow;
				fightRange[4][1]=fCol+1;
				fightRange[5][0]=fRow+1;
				fightRange[5][1]=fCol-1;
				fightRange[6][0]=fRow+1;
				fightRange[6][1]=fCol;
				fightRange[7][0]=fRow+1;
				fightRange[7][1]=fCol+1;
				break;
			default:
				/*#0#
				 *2*3
				 *#1#*/
				fightRange[0][0]=fRow-1;
				fightRange[0][1]=fCol;
				fightRange[1][0]=fRow+1;
				fightRange[1][1]=fCol;
				fightRange[2][0]=fRow;
				fightRange[2][1]=fCol-1;
				fightRange[3][0]=fRow;
				fightRange[3][1]=fCol+1;
				break;
		}
	}
	public boolean isInFightRange(Soldier s)
	{
		for(int i=0;i<fightRange.length;i++)
		{
			if(s.getCheckRow()==fightRange[i][0]&&
					s.getCheckCol()==fightRange[i][1])
			{
				fightCurIndex = i;
				return true;
			}
		}
		return false;
	}
	public void runAction()
	{
		if(moveFrame==null)return;
		setFrame(moveFrame[direction][frameIndex++]);
		if(frameIndex>=moveFrame[direction].length)frameIndex = 0;
	}
	public void setCheckMsg(int [][]c)
	{
		checkMsg = c;
	}
	public void fightStatus(Image img)
	{
		sptSword = new Sprite(img);
		setFightRange(thisRow,thisCol);
		moveFight();
	}
	public void moveStop()
	{
		oldRow = thisRow;
		oldCol = thisCol;
		checkMsg[thisRow][thisCol] = thisIndex;
		frameIndex = 0;
		setFrame(moveFrame[direction][0]);
	}
	public boolean moveLeft()
	{
		if(thisCol-1<oldCol-moveRangeC||thisCol-1<0)return false;
		if(checkMsg[thisRow][thisCol-1]!=-1)return false;
		thisCol--;
		direction = LEFT;
		move();
		checkMsg[thisRow][thisCol+1]=-1;
		return true;
	}
	public boolean moveRight()
	{
		if(thisCol+1>oldCol+moveRangeC||thisCol+1>=checkMsg[thisRow].length)return false;
		if(checkMsg[thisRow][thisCol+1]!=-1)return false;
		thisCol++;
		direction = RIGHT;
		move();
		checkMsg[thisRow][thisCol-1]=-1;
		return true;
	}
	public boolean moveUp()
	{
		if(thisRow-1<oldRow-moveRangeR||thisRow-1<0)return false;
		if(checkMsg[thisRow-1][thisCol]!=-1)return false;
		thisRow--;
		direction = UP;
		move();
		checkMsg[thisRow+1][thisCol]=-1;
		return true;
	}
	public boolean moveDown()
	{
		if(thisRow+1>oldRow+moveRangeR||thisRow+1>=checkMsg.length)return false;
		if(checkMsg[thisRow+1][thisCol]!=-1)return false;
		thisRow++;
		direction = DOWN;
		move();
		checkMsg[thisRow-1][thisCol]=-1;
		return true;
	}
	private void move()
	{
		setPosition(thisCol*MyGameCanvas.CHECKWIDTH,thisRow*MyGameCanvas.CHECKHEIGHT);
	}
	private boolean canMoveFight(int temp)
	{
		if(fightRange[temp][0]<0||fightRange[temp][0]>=checkMsg.length
				||fightRange[temp][1]<0||fightRange[temp][1]>=checkMsg[0].length)
				return false;
		else return true;
	}
	public void moveFightLeft()
	{
		int temp = fightRange[fightCurIndex][4];
		if(!canMoveFight(temp))return;
		fightCurIndex=temp;
		moveFight();
	}
	public void moveFightRight()
	{
		int temp = fightRange[fightCurIndex][5];
		if(!canMoveFight(temp))return;
		fightCurIndex=temp;
		moveFight();
	}
	public void moveFightUp()
	{
		int temp = fightRange[fightCurIndex][2];
		if(!canMoveFight(temp))return;
		fightCurIndex=temp;
		moveFight();
	}
	public void moveFightDown()
	{
		int temp = fightRange[fightCurIndex][3];
		if(!canMoveFight(temp))return;
		fightCurIndex=temp;
		moveFight();
	}
	public void fightDo(Soldier []enemy,LayerManager lm)
	{
		
		lm.remove(sptSword);
		sptSword=null;
		int msg = checkMsg[fightRange[fightCurIndex][0]][fightRange[fightCurIndex][1]];
		if(msg<0)return;
		if(isHero)
		{
			if(msg<100)return;
			else msg-=100;
		}
		else
		{
			if(msg>=100)return;
		}
		int delHp = (grade+1)*fightPower+5-(enemy[msg].getGrade()+1)*enemy[msg].getDefencePwoer();
		if(delHp<=0)delHp=1;
		enemy[msg].setIsBeFightd(delHp);
		thisCol=oldCol;
		thisRow=oldRow;
	}
	public void moveFight()
	{
		sptSword.setPosition(fightRange[fightCurIndex][1]*MyGameCanvas.CHECKWIDTH,fightRange[fightCurIndex][0]*MyGameCanvas.CHECKHEIGHT);
		int dR=fightRange[fightCurIndex][0]-thisRow;
		int dC=fightRange[fightCurIndex][1]-thisCol;
		if(abs(dR)>abs(dC))
		{
			if(dR>0)direction=DOWN;
			else direction=UP;
		}
		else
		{
			if(dC>0)direction=RIGHT;
			else direction=LEFT;
		}
		setFrame(moveFrame[direction][0]);
	}
	public void setShowHpCount(int c)
	{
		showHpCount = c;
	}
	public void setIsBeFightd(int hpDelete)
	{
		if(isDeath)return;
		isBeFighted = true;
		if(hpDelete<=0)hpDelete = 1;
		showHpCount = 1;
		hpNow-=hpDelete;
		if(hpNow<=0)
		{
			isDeath = true;
		}
	}
	public void setPosRowCol(int row,int col)
	{
		thisRow = row;
		thisCol = col;
		oldRow = row;
		oldCol = col;
		move();
	}
	public int getCheckRow()
	{
		return thisRow;
	}
	public int getCheckCol()
	{
		return thisCol;
	}
	public boolean getIsDeath()
	{
		return isDeath;
	}
	public void setFightPower(int p)
	{
		fightPower = p;
	}
	public int getFightPower()
	{
		return fightPower;
	}
	public void setDefencePwoer(int d)
	{
		defencePower = d;
	}
	public int getDefencePwoer()
	{
		return defencePower;
	}
	public int getGrade()
	{
		return grade;
	}
	public void setHp(int h)
	{
		hpNow = h;
	}
	public void setGrade(int g)
	{
		grade = g;
	}
	public boolean getIsHero()
	{
		return isHero;
	}
	public void setDirection(int d)
	{
		direction = d;
		runAction();
	}
	public int getMoveRangeRC(boolean isMoveRangeR)
	{
		if(isMoveRangeR)return moveRangeR;
		else return moveRangeC;
	}
	public void drawHp(Graphics g,int viewX,int viewY)
	{
		if(isDeath)return;
		if(showHpCount==0) return;
		showHpCount++;
		if(showHpCount>40)showHpCount=0;
		g.setColor(0,0,255);
		g.drawString(grade+"",getX()-viewX+getWidth()/2,getY()-viewY-3,Graphics.TOP|Graphics.LEFT);
		g.setColor(174,174,174);
		g.drawRect(getX()-viewX,getY()-viewY-2,getWidth(),2);
		g.setColor(128,128,128);
		g.fillRect(getX()-viewX,getY()-viewY-2,getWidth(),2);
		g.setColor(255,0,0);
		if(hpNow<0)hpNow=0;
		int width = hpNow* getWidth()/100;
		g.fillRect(getX()-viewX,getY()-viewY-2,width,2);
	}
	public void drawDeath(Graphics g,int viewX,int viewY)
	{
		if(!isDeath)return;
		g.setColor(255,0,0);
		g.drawLine(getX()-viewX,getY()-viewY,getX()+getWidth()-viewX,getY()+getHeight()-viewY);
		g.drawLine(getX()-viewX,getY()+getHeight()-viewY,getX()+getWidth()-viewX,getY()-viewY);
	}
	public void drawBlood(Graphics g,int viewX,int viewY)
	{
		if(isBeFighted)
		{
			int circleX = getX()+getWidth()/2-viewX;
			int circleY = getY()+getHeight()/2-viewY;
			g.setColor(255,0,0);
			for(int i=0;i<10;i++)
			{
				int x = circleX - 10 + abs(r.nextInt()%20);
				int y = circleY - 10 + abs(r.nextInt()%20);
				g.fillArc(x,y,3,3,360,360);
			}
			if(++bloodIndex==10)
			{
				isBeFighted = false;
				bloodIndex = 0;
			}
		}
	}
	public void drawMoveCheck(Graphics g,int viewX,int viewY)
	{
		g.setColor(255,255,255);
		int startR = oldRow - moveRangeR;
		if(startR<0)startR=0;
		int endR = oldRow + moveRangeR;
		if(endR>=checkMsg.length)endR=checkMsg.length-1;
		int startC = oldCol - moveRangeC;
		if(startC<0)startC=0;
		int endC = oldCol + moveRangeC;
		if(endC>=checkMsg[0].length)endC=checkMsg[0].length-1;
		int x,y;
		for(int i=startR;i<=endR;i++)
		{
			for(int j=startC;j<=endC;j++)
			{
				if(checkMsg[i][j]!=-1)continue;
				x = j*MyGameCanvas.CHECKWIDTH;
				y = i*MyGameCanvas.CHECKHEIGHT;
				g.drawRect(x-viewX,y-viewY,MyGameCanvas.CHECKWIDTH,MyGameCanvas.CHECKHEIGHT);
			}
		}
	}
	public void drawFightCheck(Graphics g,int viewX,int viewY)
	{
		g.setColor(255,255,255);
		int x,y;
		for(int i=0;i<fightRange.length;i++)
		{
			if(!canMoveFight(i))continue;
			x = fightRange[i][1]*MyGameCanvas.CHECKWIDTH;
			y = fightRange[i][0]*MyGameCanvas.CHECKHEIGHT;
			g.drawRect(x-viewX,y-viewY,MyGameCanvas.CHECKWIDTH,MyGameCanvas.CHECKHEIGHT);
		}
	}
	public int abs(int a)
	{
		if(a<0)return -a;
		else return a;
	}
}