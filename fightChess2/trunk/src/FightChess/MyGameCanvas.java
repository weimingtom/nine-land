package FightChess;
//Download by http://www.codefans.net
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
public class MyGameCanvas extends GameCanvas implements Runnable
{
	public static final int CHECKWIDTH = 24;
	public static final int CHECKHEIGHT = 24;
	public static final int DIALOGHEIGHT = 40;
	public static final int DIALOGMAXLINE = 2;
	public static final int STATUS_FIGHT_WAIT = 0;
	public static final int STATUS_FIGHT_MOVE = 1;
	public static final int STATUS_FIGHT_FIGHT = 2;
	public static final int STATUS_MENU = 3;
	public static final int STATUS_MOVE = 4;
	public static final int STATUS_VILLAGE = 5;
	public static final int STATUS_DIALOG = 6;
	public static final int DIALOG_FIGHTEND = 1;
	private LayerManager lm;
	private Graphics g;
	private int rate = 100;
	private boolean conti = false;
	private TiledLayer fightLayer;
	private TiledLayer mapLayer;
	private TiledLayer mapbrLayer;
	private TiledLayer villLayer;
	private TiledLayer villbrLayer;
	private int viewX = 0;
	private int viewY = 0;
	private int mapWidth;
	private int mapHeight;
	private int maxRow;
	private int maxCol;
	private int [][] checkMsg;
	private Soldier hero[];
	private Soldier enemy[];
	private Soldier curSoldier;
	private People myhero;
	private Villager villager[];
	private int heroP=0;
	private int enemyP=0;
	private int status=STATUS_MOVE;
	private int statusOld;
	private int[][] battlefield;
	private int[][] villagefield;
	private int[][] villdoorfield;
	private int[] saveTemp;
	private Image imgSword;
	private String dialog[];
	private int dialogIdx=0;
	private int dlgFromFlg=0;
	private String ftDlgStart[];//dialogStart
	private String ftDlgEnd[];//dialogEnd
	private Random r=new Random(new Date().getTime());
	private Menu menu;
	private EnemyAction enemyAction;
	private String eventKey ="";
	public MyGameCanvas()
	{
		super(false);
		//setFullScreenMode(true);
		g=getGraphics();
		lm=new LayerManager();
		menu = new Menu(g);
		enemyAction = new EnemyAction();
	}
	public void start()
	{
		if(conti==true)return;
		Image imgSource = loadImage("/FightChess/hero1.png");
		Image imgMyHero = Image.createImage(imgSource,0,0,CHECKWIDTH*2,imgSource.getHeight(),Sprite.TRANS_NONE);
		myhero = createPeople(imgMyHero);
		hero = new Soldier[4];
		hero[0] = createSoldier(imgMyHero,true,0,0);
		hero[0].setFightPower(11);
		hero[0].setDefencePwoer(6);
		saveTemp = new int[4];//0mapWidth,1mapHeight,2myheroX,3myheroY
		readMap("map0");
		conti = true;
		Thread t = new Thread(this);
		t.start();
	}
	public void exit()
	{
	}
	private String readFile(String fileName,int fileSize,String tag)
	{
		String strReturn="";
		try{
			InputStream is = this.getClass().getResourceAsStream(fileName);
			byte word_uni[]=new byte[fileSize];
			is.read(word_uni);
			is.close();
			strReturn = new String(word_uni,"UTF-8");
			strReturn = readString(strReturn,tag);
			maxRow = Integer.parseInt(readString(strReturn,"maxRow"));
			maxCol = Integer.parseInt(readString(strReturn,"maxCol"));
			mapWidth = CHECKWIDTH * maxCol;
	        mapHeight= CHECKHEIGHT * maxRow;
		}catch(Exception e){e.printStackTrace();}
		return strReturn;
	}
	/**
	 * @param flg :"map":bigmap ,"vil":villagemap
	 * @param data
	 * @param back
	 * @param imgMap
	 */
	private void setBackLayer(String flg, int[]data,int[]back,Image imgMap)
	{
		int[] backgroundMap = new int[data.length];
		int[] barrierMap = new int[data.length];
		for(int i=0;i<data.length;i++)
		{
			boolean isBack = false;
			for(int j=0;j<back.length;j++)
			{
				if(data[i]==back[j])
				{
					isBack=true;
					break;
				}
			}
			if(isBack)
			{
				backgroundMap[i] = data[i];
				barrierMap[i] = 0;
			}
			else
			{
				backgroundMap[i] = 0;
				barrierMap[i] = data[i];
			}
		}
		if(flg.equals("map"))
		{
			mapLayer = new TiledLayer(maxCol, maxRow, imgMap, CHECKWIDTH, CHECKHEIGHT);
	        mapbrLayer = new TiledLayer(maxCol, maxRow, imgMap, CHECKWIDTH, CHECKHEIGHT);
		}
		else if(flg.equals("vil"))
		{
			villLayer = new TiledLayer(maxCol, maxRow, imgMap, CHECKWIDTH, CHECKHEIGHT);
	        villbrLayer = new TiledLayer(maxCol, maxRow, imgMap, CHECKWIDTH, CHECKHEIGHT);
		}
        for (int i = 0; i < backgroundMap.length; i++) 
        {
	        int column = i % maxCol;
	        int row = (i - column) / maxRow;
	        if(flg.equals("map"))
	        	mapLayer.setCell(column, row, backgroundMap[i]);
	        else if(flg.equals("vil"))
	        	villLayer.setCell(column, row, backgroundMap[i]);
	    }
        for (int i = 0; i < barrierMap.length; i++) 
        {
	        int column = i % maxCol;
	        int row = (i - column) / maxRow;
	        if(flg.equals("map"))
	        	mapbrLayer.setCell(column, row, barrierMap[i]);
	        else if(flg.equals("vil"))
	        	villbrLayer.setCell(column, row, barrierMap[i]);
        }
	}
	private void readMap(String mapTag)
	{
		try{
			String strReturn=readFile("/FightChess/map.txt",1024*3,mapTag);
			int back[] = splitStringA(readString(strReturn,"back"),',');
			int data[] = splitStringA(readString(strReturn,"data"),',');
			Image imgMap = loadImage(readString(strReturn,"picMap"));
			setBackLayer("map",data,back,imgMap);
	        /*read battlefield*/
	        int battlefieldNum = Integer.parseInt(readString(strReturn,"battlefieldNum"));
	        battlefield = new int[battlefieldNum][6];
	        for(int i=0;i<battlefieldNum;i++)
	        {
	        	battlefield[i]=splitStringA(readString(strReturn,"battlefield"+i),',');
	        }
	        /*read villagefield*/
	        int villageFieldNum = Integer.parseInt(readString(strReturn,"villageFieldNum"));
	        villagefield = new int[villageFieldNum][5];
	        for(int i=0;i<villageFieldNum;i++)
	        {
	        	villagefield[i] = splitStringA(readString(strReturn,"villageField"+i),',');
	        }
	        int [] heroRC = splitStringA(readString(strReturn,"heroRC"),',');
	        myhero.setBarrierLayer(mapbrLayer);
	        myhero.setPosition(heroRC[1]*CHECKWIDTH,heroRC[0]*CHECKHEIGHT);
	        layer_MOVE();
	        setView(myhero);
		}catch(Exception e){e.printStackTrace();}
	}
	private void readVillageMap(String mapTag)
	{
		String strReturn=readFile("/FightChess/villageMap.txt",1024*2,mapTag);
		int back[] = splitStringA(readString(strReturn,"back"),',');
		int data[] = splitStringA(readString(strReturn,"data"),',');
		Image imgMap = loadImage(readString(strReturn,"picMap"));
		setBackLayer("vil",data,back,imgMap);
		for(int i=0;i<hero.length;i++)
		{
			if(hero[i]==null)break;
			hero[i].setHp(100);
		}
		/*read picture*/
	    Image imgSource = loadImage(readString(strReturn,"picName"));
	    /*read dialog*/
	    int dialogNum = Integer.parseInt(readString(strReturn,"dialogNum"));
	    String [] peodialog = new String[dialogNum];
	    for(int i=0;i<dialogNum;i++)
	    {
	    	peodialog[i] = readString(strReturn,"dialog"+i);
	    }
	    /*read people*/
        int peopleNum = Integer.parseInt(readString(strReturn,"peopleNum"));
	    villager=new Villager[peopleNum];
	    Image imgPeople = null;
	    for(int i=0;i<peopleNum;i++)
	    {
	    	int peopleMsg[]=splitStringA(readString(strReturn,"people"+i),',');
	    	int startX = peopleMsg[1]*CHECKWIDTH;
	    	int startY = peopleMsg[0]*CHECKHEIGHT;
	    	int endX = peopleMsg[3]*CHECKWIDTH;
	    	int endY = peopleMsg[2]*CHECKHEIGHT;
	    	imgPeople = Image.createImage(imgSource,peopleMsg[4]*CHECKWIDTH*2,0,CHECKWIDTH*2,imgSource.getHeight(),Sprite.TRANS_NONE);
	    	villager[i] = createVillager(imgPeople,splitStringB(peodialog[peopleMsg[5]],'/'),
	    			startX,startY,endX,endY);
	    	villager[i].setBarrierLayer(villbrLayer);
			lm.append(villager[i]);
	    }
	    /*read villdoorfield*/
        int doorNum = Integer.parseInt(readString(strReturn,"doorNum"));
        villdoorfield = new int[doorNum][6];
        for(int i=0;i<doorNum;i++)
        {
        	villdoorfield[i] = splitStringA(readString(strReturn,"door"+i),',');
        }
		int posRC[]=splitStringA(readString(strReturn,"myHeroRC"),',');
        myhero.setPosition(posRC[1]*CHECKWIDTH,posRC[0]*CHECKHEIGHT);
        myhero.setBarrierLayer(villbrLayer);
        lm.remove(mapLayer);
		lm.remove(mapbrLayer);
        lm.append(villLayer);
        lm.append(villbrLayer);
        setView(myhero);
        status = STATUS_VILLAGE;
	}
	private void readFightMap(String mapTag)
	{
		try{
			imgSword = loadImage("/FightChess/fight.png");
			String strReturn=readFile("/FightChess/fightMap.txt",1024*4,mapTag);
			int back[] = splitStringA(readString(strReturn,"back"),',');
			int data[] = splitStringA(readString(strReturn,"data"),',');
			checkMsg = new int[maxRow][maxCol];
			for(int i=0;i<data.length;i++)
			{
				int col = i % maxCol;
		        int row = (i - col) / maxRow;
				boolean isBack = false;
				for(int j=0;j<back.length;j++)
				{
					if(data[i]==back[j])
					{
						isBack=true;
						break;
					}
				}
				if(isBack)checkMsg[row][col]=-1;
				else checkMsg[row][col]=-2;
			}
			Image imgMap = loadImage(readString(strReturn,"picMap"));
			fightLayer = new TiledLayer(maxCol, maxRow, imgMap, CHECKWIDTH, CHECKHEIGHT);
	        for (int i = 0; i < data.length; i++) 
	        {
		        int column = i % maxCol;
		        int row = (i - column) / maxRow;
		        fightLayer.setCell(column, row, data[i]);
		    }
	        String heroPos[]=splitStringB(readString(strReturn,"heroPosRC"),'/');
	        for(int i=0;i<hero.length;i++)
	        {
	        	if(hero[i]==null)break;
	        	int posRC[]=splitStringA(heroPos[i],',');
	        	hero[i].setPosRowCol(posRC[0],posRC[1]);
	        	checkMsg[posRC[0]][posRC[1]]=i;
	        	hero[i].setShowHpCount(0);
	        }
		    /*read picture*/
		    Image imgSource = loadImage(readString(strReturn,"picName"));
		    /*read enemy*/
	        int enemyNum = Integer.parseInt(readString(strReturn,"enemyNum"));
		    enemy=new Soldier[enemyNum];
		    Image imgEnemy = null;
		    int direction = Integer.parseInt(readString(strReturn,"direction"));
		    for(int i=0;i<enemyNum;i++)
		    {
		    	int enemyMsg[]=splitStringA(readString(strReturn,"enemy"+i),',');
		    	if(enemyMsg[1]>=0)
		    	{
			    	imgEnemy = Image.createImage(imgSource,enemyMsg[1]*CHECKWIDTH,0,CHECKWIDTH,imgSource.getHeight(),Sprite.TRANS_NONE);
					enemy[i] = createSoldier(imgEnemy,false,enemyMsg[2],i+100);
		    	}
		    	else
		    	{
		    		enemy[i] = createSoldier(imgSource,false,enemyMsg[2],i+100);
		    	}
				enemy[i].setGrade(enemyMsg[0]);
				enemy[i].setFightPower(enemyMsg[3]);
				enemy[i].setDefencePwoer(enemyMsg[4]);
				enemy[i].setPosRowCol(enemyMsg[5],enemyMsg[6]);
				checkMsg[enemyMsg[5]][enemyMsg[6]]=i+100;
				enemy[i].setDirection(direction);
		    }
		    curSoldier=enemy[0];
	        curSoldier.setCheckMsg(checkMsg);
	        status=STATUS_FIGHT_WAIT;
	        /*read endEvent*/
	        eventKey = readString(strReturn,"endEvent");
		    /*read dialog*/
		    ftDlgStart = splitStringB(readString(strReturn,"dialogStart"),'/');
		    ftDlgEnd = splitStringB(readString(strReturn,"dialogEnd"),'/');
		    layer_FIGHT();
		    setView(curSoldier);
		    if(ftDlgStart!=null)
		    {
		    	statusOld = status;
				status = STATUS_DIALOG;
				dialog = ftDlgStart;
				dialogIdx = 0;
		    }
		}catch(Exception e){e.printStackTrace();}
	}
	private void setView(Sprite my)
	{
		viewX = my.getX()-(this.getWidth()/2);
		viewY = my.getY()-(this.getHeight()/2);
		if(viewX<0)viewX=0;
		if(viewX+getWidth()>=mapWidth)
			viewX = mapWidth - getWidth();
		if(viewY<0)viewY = 0;
		if(viewY+getHeight()>=mapHeight)
			viewY = mapHeight - getHeight();
		lm.setViewWindow(viewX,viewY,getWidth(),getHeight());
		lm.paint(g,0,0);
	}
	private People createPeople(Image img)
	{
		int [][]move={{0,1},{4,5},{6,7},{2,3}};
		People my = new People(img,CHECKWIDTH,CHECKHEIGHT,move);
		return my;
	}
	private Villager createVillager(Image img,String dialog[],int startX,int startY,int endX,int endY)
	{
		int [][]move={{0,1},{4,5},{6,7},{2,3}};
		Villager my = new Villager(img,CHECKWIDTH,CHECKHEIGHT,move,dialog,
				startX,startY,endX,endY);
		return my;
	}
	private Soldier createSoldier(Image img,boolean isHero,int fighttype,int index)
	{
		int [][]move;
		int n = img.getWidth()/CHECKWIDTH;
		if(n==2)
			move = new int[][]{{0,1},{4,5},{6,7},{2,3}};
		else
			move = new int[][]{{0},{2},{3},{1}};
		Soldier soldier = new Soldier(img,CHECKWIDTH,CHECKHEIGHT,move,isHero,fighttype,index);
		return soldier;
	}
	public void run()
	{
		long st=0;
		long et=0;
		while(conti)
		{
			st=System.currentTimeMillis();
			g.setColor(255,255,255);
			g.fillRect(0,0,getWidth(),getHeight());
			lm.setViewWindow(viewX,viewY,getWidth(),getHeight());
			lm.paint(g,0,0);
			if(status==STATUS_MOVE)
			{
				input();
				moveView(myhero);
				isInBattleField();
				isInVillageField();
			}
			if(status==STATUS_VILLAGE)
			{
				input();
				villagerAction();
				moveView(myhero);
				isInVilldoorField();
			}
			if(status==STATUS_DIALOG)drawDialog();
			if(status==STATUS_FIGHT_WAIT)waitDo();
			if(status==STATUS_FIGHT_MOVE)
			{
				if(!curSoldier.getIsHero())status = enemyAction.move();
				curSoldier.runAction();
				curSoldier.drawMoveCheck(g,viewX,viewY);
				moveView(curSoldier);
			}
			if(status==STATUS_FIGHT_FIGHT)
			{
				if(!curSoldier.getIsHero())
				{
					status = enemyAction.fight(hero,lm,imgSword);
					layer_FIGHT();
				}
				curSoldier.drawFightCheck(g,viewX,viewY);
			}
			if(status==STATUS_MENU)
			{
				menu.drawMenu();
			}
			if(status==STATUS_FIGHT_WAIT||status==STATUS_FIGHT_MOVE
					||status==STATUS_FIGHT_FIGHT||status==STATUS_MENU)
				drawBloodHp();
			flushGraphics();
			et=System.currentTimeMillis();
			if(et-st<rate)
			{
				try
				{
					Thread.sleep(rate-(et-st));
				}
				catch(Exception exp)
				{
				}
			}
		}
	}
	private void villagerAction()
	{
		for(int i=0;i<villager.length;i++)
		{
			if(villager[i].action(myhero))
			{
				statusOld = status;
				status = STATUS_DIALOG;
				dialog = villager[i].getDialog();
				dialogIdx = 0;
			}
		}
	}
	private void drawDialog()
	{
		g.setColor(255,255,255);
	    g.fillRect(1,getHeight()-DIALOGHEIGHT-1,getWidth()-2,DIALOGHEIGHT);
	    g.setColor(0,136,222);
	    g.drawRect(1,getHeight()-DIALOGHEIGHT-1,getWidth()-2,DIALOGHEIGHT);
	    for(int i=dialogIdx,j=0;i<dialogIdx+DIALOGMAXLINE;i++,j++)
	    {
	    	if(i>=dialog.length)break;
	    	g.drawString(dialog[i],10,getHeight()-DIALOGHEIGHT+j*15,Graphics.TOP|Graphics.LEFT);
	    }
	}
	private boolean isInField(int[]field)
	{
		int x = myhero.getX(),y=myhero.getY(),w=myhero.getWidth(),h=myhero.getHeight();
		if(x>=field[1]*CHECKWIDTH&&x+w<=field[3]*CHECKWIDTH+CHECKWIDTH
		   &&y>=field[0]*CHECKHEIGHT&&y+h<=field[2]*CHECKHEIGHT+CHECKHEIGHT)
	    {
			return true;
	    }
		else return false;
	}
	private void isInVillageField()
	{
		if(villagefield==null)return;
		for(int i=0;i<villagefield.length;i++)
		{
			if(isInField(villagefield[i]))
			{
				saveTemp[0]=mapWidth;
				saveTemp[1]=mapHeight;
				saveTemp[2]=(villagefield[i][1]-1)*CHECKWIDTH;//myhero.getX();
				saveTemp[3]=(villagefield[i][0]-1)*CHECKHEIGHT;//myhero.getY();
				readVillageMap("villageMap"+villagefield[i][4]);
				status=STATUS_VILLAGE;
				break;
			}
		}
	}
	private void isInVilldoorField()
	{
		if(villdoorfield==null)return;
		for(int i=0;i<villdoorfield.length;i++)
		{
			if(isInField(villdoorfield[i]))
			{
				mapWidth=saveTemp[0];
				mapHeight=saveTemp[1];
				for(int j=0;j<villager.length;j++)
				{
					if(villager[j]!=null)
					{
						lm.remove(villager[j]);
						villager[j] = null;
					}
				}
				if(villLayer!=null)
				{
					lm.remove(villLayer);
					villLayer = null;
				}
				if(villbrLayer!=null)
				{
					lm.remove(villbrLayer);
					villbrLayer = null;
				}
				lm.append(mapLayer);
				lm.append(mapbrLayer);
				myhero.setBarrierLayer(mapbrLayer);
				myhero.setPosition(villdoorfield[i][5]*CHECKWIDTH,villdoorfield[i][4]*CHECKHEIGHT);
				setView(myhero);
				status=STATUS_MOVE;
				break;
			}
		}
	}
	private void isInBattleField()
	{
		if(battlefield==null)return;
		for(int i=0;i<battlefield.length;i++)
		{
			if(isInField(battlefield[i])&&battlefield[i][5]==0)
			{
				saveTemp[0]=mapWidth;
				saveTemp[1]=mapHeight;
				saveTemp[2]=myhero.getX();
				saveTemp[3]=myhero.getY();
				readFightMap("fightMap"+battlefield[i][4]);
				battlefield[i][5]=1;
				break;
			}
		}
	}
	private void waitDo()
	{
		if(enemyP>=enemy.length&&heroP>=hero.length)
		{
			heroP=0;
			enemyP=0;
		}
		if(r.nextInt()%2==0)
		{
			if(enemyP<enemy.length&&enemy[enemyP]!=null&&!enemy[enemyP].getIsDeath())
			{
				curSoldier=enemy[enemyP];
				curSoldier.setCheckMsg(checkMsg);
				status=enemyAction.think(curSoldier,checkMsg,hero);
			}
			enemyP++;
		}
		else
		{
			if(heroP<hero.length&&hero[heroP]!=null&&!hero[heroP].getIsDeath())
			{
				curSoldier=hero[heroP];
				curSoldier.setCheckMsg(checkMsg);
				status=STATUS_FIGHT_MOVE;
			}
			heroP++;
		}
		boolean isEnd = true;
		for(int i=0;i<enemy.length;i++)
		{
			if(enemy[i]!=null)
			{
				if(!enemy[i].getIsDeath())isEnd = false;
			}
		}
		if(isEnd)
		{
			status = STATUS_DIALOG;
			dialog = ftDlgEnd;
			dialogIdx = 0;
			dlgFromFlg = DIALOG_FIGHTEND;
			return;
		}
		isEnd = true;
		for(int i=0;i<hero.length;i++)
		{
			if(hero[i]==null)break;
			if(!hero[i].getIsDeath())isEnd = false;
		}
		if(isEnd)
		{
			//game over
		}
	}
	private boolean moveView(Sprite myMove)
	{
		boolean bool = false;
		int left = viewX + 40;
		int top = viewY +40;
		int right = viewX + getWidth() - 40;
		int down = viewY + getHeight() - 40;
		if(myMove.getX()<left)
		{
			if(viewX > 0)
			{
				viewX -= 4;
				if(viewX<0)viewX=0;
				bool = true;
			}
		}
		if(myMove.getX()+myMove.getWidth()>right)
		{
			if(viewX +getWidth() < mapWidth)
			{
				viewX += 4;
				if(viewX+getWidth()>=mapWidth)
					viewX = mapWidth - getWidth();
				bool = true;
			}
		}
		if(myMove.getY()<top)
		{
			if(viewY > 0)
			{
				viewY -= 4;
				if(viewY<0)viewY = 0;
				bool = true;
			}
		}
		if(myMove.getY()+myMove.getHeight()>down)
		{
			if(viewY +getHeight() < mapHeight)
			{
				viewY += 4;
				if(viewY+getHeight()>=mapHeight)
					viewY = mapHeight - getHeight();
				bool = true;
			}
		}
		return bool;
	}
	protected synchronized void keyPressed(int keyCode)
	{
		if(keyCode == 0) return;
		int gameCode = getGameAction(keyCode);
		if(status==STATUS_FIGHT_MOVE)
		{
			key_FIGHT_MOVE(gameCode);
		}
		else if(status==STATUS_FIGHT_FIGHT)
		{
			key_FIGHT_FIGHT(gameCode);
		}else if(status==STATUS_MENU)
		{
			key_MENU(gameCode);
		}else if(status==STATUS_DIALOG)
		{
			if(++dialogIdx+DIALOGMAXLINE>dialog.length)
			{
				status = statusOld;
				if(dlgFromFlg == DIALOG_FIGHTEND)
				{
					if(!eventKey.equals(""))event();
					dlgFromFlg = 0;
					mapWidth=saveTemp[0];
					mapHeight=saveTemp[1];
					myhero.setPosition(saveTemp[2],saveTemp[3]);
					layer_MOVE();
					setView(myhero);
					status=STATUS_MOVE;
				}
			}
		}
	}
	private void input()
	{
		int key=getKeyStates();
		if((key&UP_PRESSED)!=0)
		{
			myhero.moveUp();
		}
		if((key&DOWN_PRESSED)!=0)
		{
			myhero.moveDown();
		}
		if((key&LEFT_PRESSED)!=0)
		{
			myhero.moveLeft();
		}
		if((key&RIGHT_PRESSED)!=0)
		{
			myhero.moveRight();
		}
	}
	private void key_FIGHT_MOVE(int gameCode)
	{
		if(!curSoldier.getIsHero())return;
		switch(gameCode) {
			case UP:
				curSoldier.moveUp();
				break;
			case LEFT:	
				curSoldier.moveLeft();
				break;
			case DOWN:
				curSoldier.moveDown();
				break;
			case RIGHT:	
				curSoldier.moveRight();
				break;
			case FIRE:
				curSoldier.moveStop();
				showMenu1();
				break;
		}
	}
	private void key_FIGHT_FIGHT(int gameCode)
	{
		if(!curSoldier.getIsHero())return;
		switch(gameCode) {
			case UP:
				curSoldier.moveFightUp();
				break;
			case LEFT:	
				curSoldier.moveFightLeft();
				break;
			case DOWN:
				curSoldier.moveFightDown();
				break;
			case RIGHT:	
				curSoldier.moveFightRight();
				break;
			case FIRE:
				curSoldier.fightDo(enemy,lm);
				status=STATUS_FIGHT_WAIT;
				break;
		}
	}
	private void key_MENU(int gameCode)
	{
		switch(gameCode) {
		case UP:
			menu.moveItemUp();
			break;
		case DOWN:
			menu.moveItemDown();
			break;
		case FIRE:
			if(menu.getCurrentItem()=="fight")
			{
				status=STATUS_FIGHT_FIGHT;
				curSoldier.fightStatus(imgSword);
				layer_FIGHT();
			}else if(menu.getCurrentItem()=="cancel1")
			{
				status=STATUS_FIGHT_WAIT;
			}
		}
	}
	private void showMenu1()
	{
		status = STATUS_MENU;
		menu.initMenu();
		menu.addMenuItem("fight","fight");
		menu.addMenuItem("cancel1","cancel");
		menu.setMenuPosition(true,getWidth(),getHeight(),3);
	}
	private void drawBloodHp()
	{
		for(int i=0;i<hero.length;i++)
		{
			if(hero[i]==null)break;
			hero[i].drawBlood(g,viewX,viewY);
			hero[i].drawHp(g,viewX,viewY);
			hero[i].drawDeath(g,viewX,viewY);
		}
		for(int i=0;i<enemy.length;i++)
		{
			if(enemy[i]!=null)
			{
				enemy[i].drawBlood(g,viewX,viewY);
				enemy[i].drawHp(g,viewX,viewY);
				enemy[i].drawDeath(g,viewX,viewY);
			}
		}
	}
	private void layer_MOVE()
	{
		if(hero!=null)
		{
			for(int i=0;i<hero.length;i++)
			{
				if(hero[i]!=null)lm.remove(hero[i]);
			}
		}
		if(enemy!=null)
		{
			for(int i=0;i<enemy.length;i++)
			{
				if(enemy[i]!=null)
				{
					lm.remove(enemy[i]);
					enemy[i]=null;
				}
			}
		}
		if(fightLayer!=null)
		{
			lm.remove(fightLayer);
			fightLayer = null;
		}
		lm.append(myhero);
        lm.append(mapLayer);
        lm.append(mapbrLayer);
	}
	private void layer_FIGHT()
	{
		lm.remove(myhero);
		lm.remove(mapLayer);
		lm.remove(mapbrLayer);
		if(curSoldier.sptSword!=null)lm.append(curSoldier.sptSword);
		for(int i=0;i<hero.length;i++)
		{
			if(hero[i]==null)break;
			lm.append(hero[i]);
		}
		for(int i=0;i<enemy.length;i++)
		{
			if(enemy[i]==null)break;
			lm.append(enemy[i]);
		}
        lm.append(fightLayer);
	}
	private String readString(String source,String tag)
	{
		String strReturn = "";
		try{
		int i = source.indexOf("<"+tag);
		if(i==-1)return "";
		i = source.indexOf(">",i+1);
		int j = source.indexOf("</"+tag+">",i+1);
		if(j==-1)return "";
		strReturn = source.substring(i+1,j);
		}catch(Exception e){System.out.println("error tag: "+tag);}
		return strReturn;
	}
	private int[] splitStringA(String source,char c)
	{
		Vector vRetrun = new Vector();
		String obj="";
		for(int i=0;i<source.length();i++)
		{
			char a = source.charAt(i);
			if(a!=c)
			{
				obj+=a;
			}
			else
			{
				vRetrun.addElement(obj);
				obj="";
			}
		}
		vRetrun.addElement(obj);
		int [] intRetrun = new int[vRetrun.size()];
		try{
		for(int i=0;i<intRetrun.length;i++)
		{
			intRetrun[i] = Integer.parseInt((String)vRetrun.elementAt(i));
		}
		}catch(Exception e){System.out.println("error:"+source);}
		return intRetrun;
	}
	private String[] splitStringB(String source,char c)
	{
		Vector vRetrun = new Vector();
		String obj="";
		for(int i=0;i<source.length();i++)
		{
			char a = source.charAt(i);
			if(a!=c)
			{
				obj+=a;
			}
			else
			{
				vRetrun.addElement(obj);
				obj="";
			}
		}
		vRetrun.addElement(obj);
		String [] strRetrun = new String[vRetrun.size()];
		for(int i=0;i<strRetrun.length;i++)
		{
			strRetrun[i] = (String)vRetrun.elementAt(i);
		}
		return strRetrun;
	}
	private Image loadImage(String pic)
	{
		Image img=null;
		try{
			img=Image.createImage(pic);
		}catch(Exception exp){exp.printStackTrace();}
		return img;
	}
	private void event()
	{
		if(eventKey.equals("event1"))
		{
			Image imgSource = loadImage("/FightChess/hero1.png");
			Image imgMyHero = Image.createImage(imgSource,CHECKWIDTH*2,0,CHECKWIDTH*2,imgSource.getHeight(),Sprite.TRANS_NONE);
			hero[1] = createSoldier(imgMyHero,true,2,1);
			hero[1].setFightPower(10);
			hero[1].setDefencePwoer(5);
		}
		else if(eventKey.equals("event2"))
		{
			Image imgSource = loadImage("/FightChess/hero1.png");
			Image imgMyHero = Image.createImage(imgSource,CHECKWIDTH*4,0,CHECKWIDTH*2,imgSource.getHeight(),Sprite.TRANS_NONE);
			hero[2] = createSoldier(imgMyHero,true,0,2);
			hero[2].setFightPower(12);
			hero[2].setDefencePwoer(6);
			imgMyHero = Image.createImage(imgSource,CHECKWIDTH*6,0,CHECKWIDTH*2,imgSource.getHeight(),Sprite.TRANS_NONE);
			hero[3] = createSoldier(imgMyHero,true,2,3);
			hero[3].setFightPower(8);
			hero[3].setDefencePwoer(4);
		}
		eventKey = "";
	}
}