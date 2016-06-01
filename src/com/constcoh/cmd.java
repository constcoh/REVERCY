package com.constcoh;

import java.awt.Color;
import java.awt.Point;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

interface WindowOptions
{
	public final int windowBorder=20;
	public final int windowTop=100;
	public final int windowLeft=500;
	public final int windowMenuHeight=2*windowBorder+windowBorder/2;
	//public final String strCountUser="User";
	//public final String strCountAuto="Auto";
	//public final String strTitle="Revercy";
	//public final String btnOk="Ok";
	public final String strCountUser="Игрок";
	public final String strCountAuto="Компьютер";
	public final String strTitle="Реверси";
	public final String btnOk="Ok";
	public final int btnHeight=2*windowBorder;
	public final int btnWidth=7*windowBorder;
}

interface MsgConsts
{
	//public final String strWinUser="You win!";
	//public final String strWinAuto="Computer wins.";
	//public final String strWaitUser="There arn't go able points for user.";
	//public final String strWaitAuto="There arn't go able points for computer.";
	//public final String strWinDraw="Win draw!";
	public final String strWinUser="Вы выиграли!";
	public final String strWinAuto="Компьютер выиграл.";
	public final String strWaitUser="Для Вас нет возможных ходов.";
	public final String strWaitAuto="Для компьютера нет возможных ходов.";
	public final String strWinDraw="Ничья!";
}


interface MenuConsts
{
	//public final String strMenuNewGame		="New game";
	//public final String strMenuExit			="Exit";
	//public final String strMenuNGLevels		="Levels";
	//public final String strMenuNG			="New game";
	//public final String strMenuLevel1		="Level 1";
	//public final String strMenuLevel2		="Level 2";
	//public final String strMenuLevel3		="Level 3";
	//public final String strMenuLevel4		="Level 4";
	//public final String strMenuLevel5		="Level 5";
	//public final String strMenuLevel6		="Level 6";
	//public final String strMenuLevel7		="Level 7";
	//public final String strMenuNGStartPos	="Start position";
	//public final String strMenuNGSPHor		="Horizontal";
	//public final String strMenuNGSPDiag		="Diagonal";	
	//public final String strMenuNGColor		="Color";
	//public final String strMenuNGColorFirst	="First";
	//public final String strMenuNGColorSecond="Senond";
	public final String strMenuNewGame		="Новая игра";
	public final String strMenuExit			="Выход";
	public final String strMenuNGLevels		="Сложность";
	public final String strMenuNG			="Новая игра";
	public final String strMenuLevel1		="cложность 1";
	public final String strMenuLevel2		="cложность 2";
	public final String strMenuLevel3		="cложность 3";
	public final String strMenuLevel4		="cложность 4";
	public final String strMenuLevel5		="cложность 5";
	public final String strMenuLevel6		="cложность 6";
	public final String strMenuLevel7		="cложность 7";
	public final String strMenuNGStartPos	="Начальная позиция";
	public final String strMenuNGSPHor		="Горизонтальная";
	public final String strMenuNGSPDiag		="Диагональная";	
	public final String strMenuNGColor		="Цвет";
	public final String strMenuNGColorFirst	="Белый";
	public final String strMenuNGColorSecond="Черный";
}
interface BoardOptions
{
	public static enum ClBlock
	{
		white,black,red,green,blue,yellow;
		public Color toColor()
		{
			Color res=new Color(0,0,0);
			switch (this) {
			case white:	res=new Color(255,255,255);	break;
			case black:	res=new Color(  0,  0,  0);	break;
			case   red:	res=new Color(255,  0,  0);	break;
			case green:	res=new Color(  0,255,  0);	break;
			case  blue:	res=new Color(  0,  0,255);	break;
			case yellow:res=new Color(255,255,  0);	break;
			}
			return res;
		}
	}
	public final double velocityAnimate=0.1;//[0,1]
	public final int sleepTime=50;
	public final int szElement=50;
	public final int widthBorder=2;
	public final Color clBackground=new Color(158,103,74);
	public final ClBlock clFirst=ClBlock.white;
	public final ClBlock clSecond=ClBlock.black;
	public final ClBlock clAdd=ClBlock.yellow;
	public final Color clBorder=new Color(255,255,255);
}


class BoardDefaultOptions
{
	public final int sizeBoard=8;
	public final int cntThreads=1;
	public int level;	
}

class Options extends BoardDefaultOptions implements MsgConsts,BoardOptions,WindowOptions,MenuConsts
{
	public Options()
	{
		super();
		isHorizontalNewGame=false;
		isPrintAbleForGo=true;
		level=2;
		idUser=Board.BOARD_FIRST;
		idAuto=Board.BOARD_SECOND;
	}
	private volatile Integer idUser,idAuto;
	public void setIdsUserAuto(boolean isRevers)
	{
		if(isRevers)
		{
			idUser=Board.BOARD_SECOND;
			idAuto=Board.BOARD_FIRST;
		}
		else
		{
			idUser=Board.BOARD_FIRST;
			idAuto=Board.BOARD_SECOND;			
		}
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println(this);
		//out.print(idUser);out.print(':');out.println(idAuto);
		//out.flush();
	}
	public int getIdUser(){
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println(this);
		//out.flush();
		return idUser;
		}
	public int getIdAuto(){return idAuto;}	
	public boolean isHorizontalNewGame;
	public boolean isPrintAbleForGo;
	public MsgConsts getMsgConsts(){return this;}
	public BoardDefaultOptions getBoardDefaultOptions(){return this;}
}

class CmdControl implements Runnable
{
	private ControlGame ctrlGame;
	private Options options;
	public CmdControl(ControlGame ctrlGame,Options options) {
		this.ctrlGame=ctrlGame;
		this.options=options;
	}
	private void initialNewGame()
	{
		if(options.isHorizontalNewGame)
			ctrlGame.setNewGameHorizontal( options.sizeBoard, Game.ID_1, Game.ID_2, options.cntThreads, options.level);
		else
			ctrlGame.setNewGameDiagonal( options.sizeBoard, Game.ID_1, Game.ID_2, options.cntThreads, options.level);
	}
	public void playGame(Scanner in)
	{
		Point purpose=new Point();
		this.initialNewGame();
		while(!ctrlGame.isFinished())
		{
			purpose.x=in.nextInt();
			purpose.y=in.nextInt();
			ctrlGame.goUser(purpose);
		}
	}
	public boolean askGame(Scanner in)
	{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		out.println("Do you want start new game? Y/N");
		out.flush();
		char character=in.nextLine().charAt(0);

		while(character!='Y' && character!='N')
			character=in.nextLine().charAt(0);
		if(character=='Y') return true;
		return false;
	}
	public void run()
	{
		Scanner in = new Scanner(System.in);
		while(askGame(in))
			playGame(in);
	}
}

class CmdView implements View
{
	private Game game;
	private Options options;
	public CmdView(Game game,Options options) {
		// TODO Auto-generated constructor stub
		this.options=options;
		this.game=game;
	}
	public void initialNewGame()
	{
		if(options.isHorizontalNewGame)
			game.setNewGameHorizontal( options.sizeBoard, Game.ID_1, Game.ID_2, options.cntThreads, options.level);
		else
			game.setNewGameDiagonal( options.sizeBoard, Game.ID_1, Game.ID_2, options.cntThreads, options.level);
	}
	public void showWin(UserOrAutoOrNone arg,int cntUser,int cntAuto)
	{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		out.print(cntUser+":"+cntAuto+" ");
		if(arg==UserOrAutoOrNone.auto)
			out.println(options.strWinAuto);
		if(arg==UserOrAutoOrNone.user)
			out.println(options.strWinUser);
		if(arg==UserOrAutoOrNone.none)
			out.println(options.strWinDraw);
		out.flush();
	}
	
	public void showWaitMessage(UserOrAuto arg)
	{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		if(arg==UserOrAuto.auto)
			out.println(options.strWaitAuto);
		if(arg==UserOrAuto.user)
			out.println(options.strWaitUser);
		out.flush();
	}
	
	public void updateView()
	{
		print();
	}
	public void print()
	{
		DrawBoard drawBoard=game.getDrawBoard();
		int size=options.sizeBoard;
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		out.println();
		for(int y=0;y<size;++y)
		{
			for(int x=0;x<size;++x)
			{
				int point=drawBoard.getM(x, y);
				switch(point)
				{
				case DrawBoard.BOARD_FIRST:
					out.print('X'); break;
				case DrawBoard.BOARD_SECOND:
					out.print('O'); break;
				case DrawBoard.BOARD_FREE:
					out.print('.'); break;
				}
				out.print(' ');
			}
			out.println();
		}
		out.flush();
	}
}