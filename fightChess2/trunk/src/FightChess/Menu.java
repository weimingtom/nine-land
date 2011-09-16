package FightChess;
//Download by http://www.codefans.net
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Graphics;

public class Menu
{
	private Graphics g;
	private Vector vectItem;
	private int currentItem = 0;
	private int positionX=0;
	private int positionY=0;
	private int itemHeight = 25;
	private int menuWidth = 40;
	private int bkColorR = 167;
	private int bkColorG = 167;
	private int bkColorB = 167;
	private int selColorR = 0;
	private int selColorG = 255;
	private int selColorB = 255;
	private int itemColorR = 0;
	private int itemColorG = 118;
	private int itemColorB = 163;
	private Font font;
	private int showItemNum = 3;
	public Menu(Graphics g)
	{
		this.g=g;
	}
	public void initMenu()
	{
		vectItem = new Vector();
		currentItem = 0;
	}
	public void addMenuItem(String name,String text)
	{
		String menuItem[]={name,text};
		vectItem.addElement(menuItem);
		font = g.getFont();
		int maxLength = 0;
		for(int i=0;i<vectItem.size();i++)
		{
			String []item = (String[])vectItem.elementAt(i);
			if(font.stringWidth(item[1])>maxLength)
				maxLength = font.stringWidth(item[1]);
		}
		menuWidth = maxLength +10;
	}
	public void moveItemUp()
	{
		currentItem -= 1;
		if(currentItem<0)currentItem = vectItem.size()-1;
		drawMenu();
	}
	public void moveItemDown()
	{
		currentItem += 1;
		if(currentItem>=vectItem.size())currentItem = 0;
		drawMenu();
	}
	public void drawMenu()
	{
		int startItem = currentItem-showItemNum+1;
		if(startItem<0)startItem=0;
		int endItem = startItem+showItemNum-1;
		if(endItem>vectItem.size()-1)endItem=vectItem.size()-1;
		for(int i=startItem,temp=0;i<=endItem;i++,temp++)
		{
			if(i==currentItem)g.setColor(selColorR,selColorG,selColorB);
			else g.setColor(bkColorR,bkColorG,bkColorB);
			g.fillRect(positionX,positionY+temp*itemHeight,menuWidth,itemHeight);
			g.setColor(itemColorR,itemColorG,itemColorB);
			g.drawRect(positionX,positionY+temp*itemHeight,menuWidth,itemHeight);
			String[]item = (String[])vectItem.elementAt(i);
			int itemW = font.stringWidth(item[1]);
			int x = positionX+(menuWidth - itemW)/2;
			int y = (positionY+temp*itemHeight) + (itemHeight- font.getHeight())/2;
			g.drawString(item[1],x,y,Graphics.TOP|Graphics.LEFT);
		}
	}
	public void setBkColor(int r,int g,int b)
	{
		bkColorR = r;
		bkColorG = g;
		bkColorB = b;
	}
	public void setSelColor(int r,int g,int b)
	{
		selColorR = r;
		selColorG = g;
		selColorB = b;
	}
	public void setItemColor(int r,int g,int b)
	{
		itemColorR = r;
		itemColorG = g;
		itemColorB = b;
	}
	public void setMenuPosition(boolean isCenter,int x,int y,int num)
	{
		if(num<vectItem.size())showItemNum=num;
		else showItemNum=vectItem.size();
		if(isCenter)
		{
			positionX = (x-menuWidth)/2;
			positionY = (y-itemHeight*showItemNum)/2;
		}
		else
		{
			positionX=x;
			positionY=y;
		}
	}
	public void setItemHeight(int h)
	{
		itemHeight = h;
	}
	public String getCurrentItem()
	{
		String item[]=(String[])vectItem.elementAt(currentItem);
		return item[0];
	}
}