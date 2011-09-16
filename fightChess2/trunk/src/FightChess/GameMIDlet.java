package FightChess;
//Download by http://www.codefans.net
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
public class GameMIDlet extends MIDlet implements CommandListener
{
	private Display display;
	private MyGameCanvas mgc;
	public GameMIDlet()
	{
		display = Display.getDisplay(this);
	}
	public void startApp()
	{
		if(mgc==null)
		{
			mgc=new MyGameCanvas();
			mgc.addCommand(new Command("Start",Command.OK,1));
			mgc.addCommand(new Command("EXIT",Command.EXIT,2));
			mgc.setCommandListener(this);
			display.setCurrent(mgc);
		}
		display.setCurrent(mgc);
	}
	public void pauseApp()
	{
	}
	public void destroyApp(boolean unconditional)
	{
		mgc.exit();
		notifyDestroyed();
	}	
	public void commandAction(Command c,Displayable s)
	{
		String cmd = c.getLabel();
		if(cmd.equals("Start"))
		{
			mgc.start();
		}
		else if(cmd.equals("EXIT"))
		{
			mgc.exit();
			notifyDestroyed();
		}
	}
};
