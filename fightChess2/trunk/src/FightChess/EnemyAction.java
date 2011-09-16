package FightChess;
//Download by http://www.codefans.net
import java.util.Date;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.LayerManager;

public class EnemyAction
{
	private Soldier enemy;
	private int endRow;
	private int endCol;
	private int direction = Soldier.UP;
	private int countMove = 0;
	private int countFight = 0;
	private boolean canFight;
	private Random r=new Random(new Date().getTime());
	public EnemyAction()
	{
		
	}
	public int think(Soldier e,int checkMsg[][],Soldier hero[])
	{
		enemy = e;
		int status = MyGameCanvas.STATUS_FIGHT_MOVE;
		int thisRow = enemy.getCheckRow();
		int thisCol = enemy.getCheckCol();
		endRow = -1;
		endCol = -1;
		enemy.setFightRange(thisRow,thisCol);
		for(int j=0;j<hero.length;j++)
		{
			if(hero[j]==null)break;
			if(!hero[j].getIsDeath()&&enemy.isInFightRange(hero[j]))
			{
				endRow = thisRow;
				endCol = thisCol;
				status = MyGameCanvas.STATUS_FIGHT_FIGHT;
				return status;
			}
		}
		
		int startR = thisRow - enemy.getMoveRangeRC(true);
		if(startR<0)startR=0;
		int endR = thisRow + enemy.getMoveRangeRC(true);
		if(endR>=checkMsg.length)endR=checkMsg.length-1;
		int startC = thisCol - enemy.getMoveRangeRC(false);
		if(startC<0)startC=0;
		int endC = thisCol + enemy.getMoveRangeRC(false);
		if(endC>=checkMsg[0].length)endC=checkMsg[0].length-1;
		int moveCheck[][] = new int[(endR-startR+1)*(endC-startC+1)][3];
		int k=0;
		for(int i=startR;i<=endR;i++)
		{
			for(int j=startC;j<=endC;j++)
			{
				if(checkMsg[i][j]!=-1||(i==thisRow&&j==thisCol))
				{
					moveCheck[k][0]=-1;
					moveCheck[k][1]=-1;
					moveCheck[k][2]=100;
				}
				else
				{
					moveCheck[k][0]=i;//row
					moveCheck[k][1]=j;//col
					moveCheck[k][2]=abs(i-thisRow)+abs(j-thisCol);//length
				}
				k++;
			}
		}
		sortMoveCheck(moveCheck);
		boolean breakFlg = false;
		int i;
		for(i=0;i<moveCheck.length;i++)
		{
			if(moveCheck[i][2]>=100)break;
			enemy.setFightRange(moveCheck[i][0],moveCheck[i][1]);
			for(int j=0;j<hero.length;j++)
			{
				if(hero[j]==null)break;
				if(!hero[j].getIsDeath()&&enemy.isInFightRange(hero[j]))
				{
					endRow = moveCheck[i][0];
					endCol = moveCheck[i][1];
					breakFlg = true;
					break;
				}
			}
			if(breakFlg)break;
		}
		canFight = true;
		if(endRow==-1||endCol==-1)
		{
			int ranIdx = abs(r.nextInt()%(i-1));
			endRow = moveCheck[ranIdx][0];
			endCol = moveCheck[ranIdx][1];
			canFight = false;
		}
		if(abs(enemy.getCheckRow()-endRow)>abs(enemy.getCheckCol()-endCol))
		{
			if(enemy.getCheckRow()>endRow)direction = Soldier.UP;
			else direction = Soldier.DOWN;
		}
		else 
		{
			if(enemy.getCheckCol()>endCol)direction = Soldier.LEFT;
			else direction = Soldier.RIGHT;
		}
		return status;
	}
	public int move()
	{
		int status = MyGameCanvas.STATUS_FIGHT_MOVE;
		if(enemy.getCheckCol()==endCol)
		{
			if(enemy.getCheckRow()>endRow)direction = Soldier.UP;
			else direction = Soldier.DOWN;
		}
		if(enemy.getCheckRow()==endRow)
		{
			if(enemy.getCheckCol()>endCol)direction = Soldier.LEFT;
			else direction = Soldier.RIGHT;
		}
		if(enemy.getCheckRow()==endRow&&enemy.getCheckCol()==endCol)
		{
			status = MyGameCanvas.STATUS_FIGHT_FIGHT;
			enemy.moveStop();
			if(!canFight)status = MyGameCanvas.STATUS_FIGHT_WAIT;
			return status;
		}
		if(++countMove>10)
		{
			countMove = 0;
			enemy.moveStop();
			status = MyGameCanvas.STATUS_FIGHT_WAIT;
			return status;
		}
		switch(direction)
		{
			case Soldier.UP:
				if(!enemy.moveUp())
				{
					if(enemy.getCheckCol()>endCol)direction = Soldier.LEFT;
					else direction = Soldier.RIGHT;
				}
				break;
			case Soldier.DOWN:
				if(!enemy.moveDown())
				{
					if(enemy.getCheckCol()>endCol)direction = Soldier.LEFT;
					else direction = Soldier.RIGHT;
				}
				break;
			case Soldier.LEFT:
				if(!enemy.moveLeft())
				{
					if(enemy.getCheckRow()>endRow)direction = Soldier.UP;
					else direction = Soldier.DOWN;
				}
				break;
			case Soldier.RIGHT:
				if(!enemy.moveRight())
				{
					if(enemy.getCheckRow()>endRow)direction = Soldier.UP;
					else direction = Soldier.DOWN;
				}
				break;
		}
		return status;
	}
	public int fight(Soldier []hero,LayerManager lm,Image imgSword)
	{
		if(countFight==0)enemy.fightStatus(imgSword);
		if(++countFight>2)
		{
			countFight = 0;
			enemy.fightDo(hero,lm);
			return MyGameCanvas.STATUS_FIGHT_WAIT;
		}
		return MyGameCanvas.STATUS_FIGHT_FIGHT;
	}
	private void sortMoveCheck(int moveCheck[][])
	{
		boolean exchange;
		int temp[] = new int[3];
		for(int i=0;i<moveCheck.length-1;i++)
		{
			exchange = false;
			for(int j=moveCheck.length-2;j>=i;j--)
			{
				if(moveCheck[j+1][2]<moveCheck[j][2])
				{
					temp[0]=moveCheck[j+1][0];
					temp[1]=moveCheck[j+1][1];
					temp[2]=moveCheck[j+1][2];
					
					moveCheck[j+1][0] = moveCheck[j][0];
					moveCheck[j+1][1] = moveCheck[j][1];
					moveCheck[j+1][2] = moveCheck[j][2];
					
					moveCheck[j][0] = temp[0];
					moveCheck[j][1] = temp[1];
					moveCheck[j][2] = temp[2];
					
					exchange = true;
				}
			}
			if(!exchange)break;
		}
	}
	private int abs(int a)
	{
		if(a<0)return -a;
		else return a;
	}
}