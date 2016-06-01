package com.constcoh;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import com.sun.media.sound.Toolkit;

import java.awt.Image;
import java.io.PrintWriter;

class ImageBoard extends JPanel
{
	interface Element
	{
		public void paint(Graphics gr,int x,int y);
	}
	class NoneElement implements Element
	{
		private Color bgColor;
		private int borderSize;
		private int blockSize;
		public NoneElement(Color bgColor,int borderSize,int blockSize)
		{
			this.bgColor=bgColor;
			this.borderSize=borderSize;
			this.blockSize=blockSize;
		}
		public void paint(Graphics gr,int x,int y)
		{
			int top=borderSize; if(y>0) top+=(borderSize+blockSize)*y;
			int left=borderSize; if(x>0) left+=(borderSize+blockSize)*x;
			gr.setColor(bgColor);
			gr.fillRect(left, top, blockSize, blockSize);
		}
	}
	class ConstElement implements Element
	{
		private Image src;
		private int borderSize;
		private int blockSize;
		private Color bgColor;
		public ConstElement(Color bgColor,int borderSize,int blockSize,Image src)
		{
			this.src=src;
			this.borderSize=borderSize;
			this.blockSize=blockSize;
			this.bgColor=bgColor;
		}
		public void paint(Graphics gr,int x,int y)
		{
			int top=borderSize; if(y>0) top+=(borderSize+blockSize)*y;
			int left=borderSize; if(x>0) left+=(borderSize+blockSize)*x;
			gr.setColor(bgColor);
			gr.fillRect(left, top, blockSize, blockSize);
			gr.drawImage(src, left, top, null);
		}
	}
	class DifferentElement implements Element
	{
		private Image srcOld,srcNew;
		private int borderSize;
		private int blockSize;
		private Color bgColor;
		private double faze;
		public double getFaze(){return faze;}
		public void setFaze(double arg)
		{
			faze=arg;
			if(faze>1) faze=1;
			if(faze<=0) faze=0;
		}
		public DifferentElement(Color bgColor,int borderSize,int blockSize,Image srcOld,Image srcNew)
		{
			this.srcOld=srcOld;
			this.srcNew=srcNew;
			this.borderSize=borderSize;
			this.blockSize=blockSize;
			this.bgColor=bgColor;
			faze=1;
		}
		private void paintPreFaze(Graphics gr,int top,int left)
		{
			double sx=1-faze*2,sy=1; if(sx==0) sx=0.1;
			gr.setColor(bgColor);
			Image tmpImg=GetImages.scaleImage(srcOld, (int)(blockSize*sx), blockSize, blockSize, blockSize);
			gr.drawImage(tmpImg, left+(int)((1-sx)/2*blockSize), top,null);
		}
		private void paintPostFaze(Graphics gr,int top,int left)
		{
			double sx=faze*2-1,sy=1; if(sx==0) sx=0.1;
			gr.setColor(bgColor);
			Image tmpImg=GetImages.scaleImage(srcNew, (int)((double)blockSize*sx), blockSize, blockSize, blockSize);
			gr.drawImage(tmpImg, left+(int)((1-sx)/2*blockSize), top,null);
		}
		public void paint(Graphics gr,int x,int y)
		{
			int top=borderSize; if(y>0) top+=(borderSize+blockSize)*y;
			int left=borderSize; if(x>0) left+=(borderSize+blockSize)*x;
			gr.setColor(bgColor);
			gr.fillRect(left, top, blockSize, blockSize);
			if(faze<0.5) 
				paintPreFaze(gr, top, left);
			else
				paintPostFaze(gr, top, left);
		}
	}
	private NoneElement elNone;
	private ConstElement elFirst,elSecond;
	private DifferentElement elFirstToSecond,elSecondToFirst;
	private int sleepTime;
	private double velocityAnimate;
	private Element elM[][];
	private int iM[][];
	private int szBoard,szBorder,szBlock;
	private Color clBorder;
	private DrawBoard model;
	private Options opt;
	public void initNewImageBoard(Options opt)
	{
		this.opt=opt;
		szBoard=opt.sizeBoard;
		szBlock=opt.szElement;
		szBorder=opt.widthBorder;
		clBorder=opt.clBorder;
		velocityAnimate=opt.velocityAnimate;
		sleepTime=opt.sleepTime;
		Image imgFirst,imgSecond;
		imgFirst=GetImages.getImage(opt.clFirst, opt.clBackground, szBlock, szBlock);
		imgSecond=GetImages.getImage(opt.clSecond, opt.clBackground, szBlock, szBlock);
		elNone=new NoneElement(opt.clBackground, szBorder, szBlock);
		elFirst=new ConstElement(opt.clBackground, szBorder, szBlock, imgFirst);
		elSecond=new ConstElement(opt.clBackground, szBorder, szBlock, imgSecond);
		elFirstToSecond=new DifferentElement(opt.clBackground, szBorder, szBlock, imgFirst, imgSecond);
		elSecondToFirst=new DifferentElement(opt.clBackground, szBorder, szBlock, imgSecond, imgFirst);
		elM=new Element[szBoard][szBoard];
		iM=new int[szBoard][szBoard];
		for(int x=0;x<szBoard;++x)
			for(int y=0;y<szBoard;++y)
				{elM[x][y]=elNone;iM[x][y]=DrawBoard.BOARD_FREE;}
	}
	public ImageBoard(Options opt,Rectangle rect,DrawBoard model,ControlGame cgame)
	{
		initNewImageBoard(opt);
		this.setLocation(new Point(rect.x, rect.y));
		this.setSize(rect.width, rect.height);
		this.setVisible(true);
		this.setBackground(new Color(0,0,0));
		this.model=model;
		this.addMouseListener(new BoardListener(opt,cgame,this));
	}

	private void animatPlay()
	{
		Graphics gr=this.getGraphics();
		for(double faze=0;faze<1;faze+=velocityAnimate)
		{
			elFirstToSecond.setFaze(faze);
			elSecondToFirst.setFaze(faze);
			for(int x=0;x<szBoard;++x)
				for(int y=0;y<szBoard;++y)
					elM[x][y].paint(gr, x, y);
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException er){return;}
		}
		//this.paintAbleForGo(gr);
	}
	private void updateM()
	{
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println("updateM");
		//out.flush();
		int newiMxy;
		boolean isChanged=false;
		//загрузка новой доски newiM
		//обновление elM: выставка изменяющихся
		for(int x=0;x<szBoard;++x)
			for(int y=0;y<szBoard;++y)
			{
				newiMxy=model.getM(x, y);
				{
				//out = new PrintWriter(new OutputStreamWriter(System.out));
				//out.print(newiMxy);
				//out.flush();
				}
				if(newiMxy!=iM[x][y])
				{
					//out = new PrintWriter(new OutputStreamWriter(System.out));
					//out.print(x); out.print(' '); out.println(y);
					//out.flush();
					isChanged=true;
					switch(newiMxy)
					{
					case DrawBoard.BOARD_FREE:
						elM[x][y]=elNone;
						break;
					case DrawBoard.BOARD_FIRST:
						elM[x][y]=elFirst;
						break;
					case DrawBoard.BOARD_SECOND:
						elM[x][y]=elSecond;
						break;
					}
					if(newiMxy==DrawBoard.BOARD_FIRST && iM[x][y]==DrawBoard.BOARD_SECOND)
						elM[x][y]=elSecondToFirst;
					if(newiMxy==DrawBoard.BOARD_SECOND && iM[x][y]==DrawBoard.BOARD_FIRST)
						elM[x][y]=elFirstToSecond;
					iM[x][y]=newiMxy;
				}
			}
		//играем анимацию
		if(!isChanged) return;
		this.animatPlay();
		//обновление elM: выставка постоянных
		for(int x=0;x<szBoard;++x)
			for(int y=0;y<szBoard;++y)
			{
				if(elM[x][y]==elFirstToSecond) elM[x][y]=elSecond;
				if(elM[x][y]==elSecondToFirst) elM[x][y]=elFirst;
			}
	}
	private void paintBorders(Graphics gr)
	{

		int szTotal=szBoard*(szBlock+szBorder)+szBorder;
		gr.setColor(clBorder);
		for(int i=0;i<=szBoard;++i)
		{
			gr.fillRect(0, 0+i*(szBlock+szBorder), szTotal, szBorder);
			gr.fillRect(0+i*(szBlock+szBorder), 0, szBorder, szTotal);
		}
	}
	public void updatePrint()
	{
		int newiM[][]=new int[szBoard][szBoard];
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.print("UPDATE PRINT ");
		for(int x=0;x<szBoard;++x)
			for(int y=0;y<szBoard;++y)
				{newiM[x][y]=model.getM(x, y); }
		
		//out.println(); out.flush();
		this.paintComponent(this.getGraphics());
	}
	private void paintM(Graphics gr)
	{
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println("paintM");
		//out.flush();
		updateM();
		for(int x=0;x<szBoard;++x)
			for(int y=0;y<szBoard;++y)
				elM[x][y].paint(gr, x, y);
	}

	private void paintAbleForGo(Graphics gr)
	{
		Vector<Point> vect=model.getAbleForGo(opt.getIdUser());
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println("paintAbleForGo");
		Iterator<Point> iter=vect.iterator();
		//out.print("len:");out.println(vect.size());
		gr.setColor(Options.clAdd.toColor());
		while(iter.hasNext())
		{
			Point p=iter.next();
			//out.print(p.x); out.print(' '); out.println(p.y);
			int x=(p.x)*(szBlock+szBorder)+szBorder;
			int y=(p.y)*(szBlock+szBorder)+szBorder;
			gr.drawArc(x, y, szBlock, szBlock, 0, 360);
			
		}
		//gr.fillRect(0, 0, 100, 100);
		//out.println("paintAbleForGo is painted");
		//out.flush();
	}
	public void paintComponent(Graphics gr)
	{
		//super.paintComponent(gr);
		//Image offImg=createImage(this.getWidth(), this.getHeight());
		//Graphics gr=offImg.getGraphics();
		
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println("REPAINT");
		//out.flush();
		//Color clOld=g.getColor();
		paintM(gr);
		//paintAbleForGo(gr);
		paintBorders(gr);
		
		if(opt.isPrintAbleForGo) paintAbleForGo(gr);
		
		//g.drawImage(offImg, 0, 0, this);
	}

	class BoardListener extends MouseAdapter
	{
		private int szElement,widthBorder;
		private int minSize,maxSize;
		private int scale;
		private ControlGame cgame;
		private ImageBoard owner;
		public BoardListener(Options opt,ControlGame cgame,ImageBoard owner)
		{
			this.owner=owner;
			this.cgame=cgame;
			szElement=opt.szElement;
			widthBorder=opt.widthBorder;
			minSize=widthBorder;//[
			scale=widthBorder+szElement;
			maxSize=opt.sizeBoard*scale;//)
		}
		private int setScale(int arg)
		{
			int res=0;
			while(arg>scale){++res;arg-=scale;}
			return res;
		}
		public void mouseClicked(MouseEvent e)
		{
			boolean oldIsPrintAbleForGo=opt.isPrintAbleForGo;
			opt.isPrintAbleForGo=false;

			int x=setScale(e.getX()), y=setScale(e.getY()); 
			//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			//out.print(x);out.print(' ');out.println(y);
			//out.flush();

			cgame.goUser(new Point(x,y));

			opt.isPrintAbleForGo=oldIsPrintAbleForGo;
			owner.paintComponent(owner.getGraphics());
		}
	}
}

class DialogWindow extends JDialog
{
	private JFrame owner;
	private WindowOptions wopt;
	public DialogWindow(JFrame owner,WindowOptions wopt)
	{
		this.owner=owner;
		this.wopt=wopt;
	}
	public void showMessage(String arg)
	{
		String buttons[]=new String[1];
		buttons[0]=new String(wopt.btnOk);
		JOptionPane.showOptionDialog(owner, arg, wopt.strTitle, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
	}
}

class MainMenu extends JMenuBar
{
	private JMenu mainNew;
	private class MenuListener implements ActionListener
	{
		private ControlGame cgame;
		private MenuConsts mc;
		private Options opt;
		private MainWindow mainWindow;
		public MenuListener(Options opt, ControlGame cgame)
		{
			this.cgame=cgame;
			this.mc=opt;
			this.opt=opt;
			this.mainWindow=mainWindow;
		}
		private void initNewGame()
		{
			//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			//out.print("isHorizontalGame=");out.println(opt.isHorizontalNewGame);
			
			//out.flush();
			if(opt.isHorizontalNewGame)
				cgame.setNewGameHorizontal(opt.sizeBoard, opt.getIdAuto(), opt.getIdUser(), opt.cntThreads, opt.level);
			else
				cgame.setNewGameDiagonal(opt.sizeBoard, opt.getIdAuto(), opt.getIdUser(), opt.cntThreads, opt.level);
			cgame.update();
		}
		public void actionPerformed(ActionEvent ae)
		{
			String cmd=ae.getActionCommand();
			{
				//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
				//out.print("cmd:"); out.println(cmd);
				//out.flush();
			}
			if(cmd.compareTo(mc.strMenuNG)==0) this.initNewGame();
			if(cmd.compareTo(mc.strMenuNGSPHor)==0)
			{
				//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
				//out.println("HOR");
				//out.flush();
				opt.isHorizontalNewGame=true;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuNGSPDiag)==0)
			{
				//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
				//out.println("DIAG");
				//out.flush();
				opt.isHorizontalNewGame=false;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel1)==0)
			{
				opt.level=1;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel2)==0)
			{
				opt.level=2;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel3)==0)
			{
				opt.level=3;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel4)==0)
			{
				opt.level=4;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel5)==0)
			{
				opt.level=5;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel6)==0)
			{
				opt.level=6;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuLevel7)==0)
			{
				opt.level=7;
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuExit)==0)
			{
				cgame.dispose();
				//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
				//out.flush();
				System.exit(0);
			}
			if(cmd.compareTo(mc.strMenuNGColorFirst)==0)
			{
				opt.setIdsUserAuto(false);
				this.initNewGame();
			}
			if(cmd.compareTo(mc.strMenuNGColorSecond)==0)
			{
				opt.setIdsUserAuto(true);
				this.initNewGame();
			}
		}
	}
	public MainMenu(Options opt,ControlGame cgame)
	{
		super();
		MenuConsts mc=opt;
		JMenu mNewGame=new JMenu(mc.strMenuNewGame);
			JMenuItem 	mNG=new JMenuItem(mc.strMenuNG); mNG.setActionCommand(mc.strMenuNG); mNG.addActionListener(new MenuListener(opt,cgame));
			JMenu 		mNGLevels		=new	JMenu(mc.strMenuNGLevels);
				JRadioButtonMenuItem	mNGLevel1		=new	JRadioButtonMenuItem(mc.strMenuLevel1); mNGLevel1.setActionCommand(mc.strMenuLevel1); mNGLevel1.addActionListener(new MenuListener(opt,cgame));mNGLevel1.setSelected(true);
				JRadioButtonMenuItem	mNGLevel2		=new	JRadioButtonMenuItem(mc.strMenuLevel2); mNGLevel2.setActionCommand(mc.strMenuLevel2); mNGLevel2.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGLevel3		=new	JRadioButtonMenuItem(mc.strMenuLevel3); mNGLevel3.setActionCommand(mc.strMenuLevel3); mNGLevel3.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGLevel4		=new	JRadioButtonMenuItem(mc.strMenuLevel4); mNGLevel4.setActionCommand(mc.strMenuLevel4); mNGLevel4.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGLevel5		=new	JRadioButtonMenuItem(mc.strMenuLevel5); mNGLevel5.setActionCommand(mc.strMenuLevel5); mNGLevel5.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGLevel6		=new	JRadioButtonMenuItem(mc.strMenuLevel6); mNGLevel6.setActionCommand(mc.strMenuLevel6); mNGLevel6.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGLevel7		=new	JRadioButtonMenuItem(mc.strMenuLevel7); mNGLevel7.setActionCommand(mc.strMenuLevel7); mNGLevel7.addActionListener(new MenuListener(opt,cgame));
				ButtonGroup bgNGLevels=new ButtonGroup();
				bgNGLevels.add(mNGLevel1);
				bgNGLevels.add(mNGLevel2);
				bgNGLevels.add(mNGLevel3);
				bgNGLevels.add(mNGLevel4);
				bgNGLevels.add(mNGLevel5);
				bgNGLevels.add(mNGLevel6);
				bgNGLevels.add(mNGLevel7);
			JMenu		mNGColor		=new 	JMenu(mc.strMenuNGColor);
				JRadioButtonMenuItem 	mNGColor1		=new	JRadioButtonMenuItem(mc.strMenuNGColorFirst);  mNGColor1.setActionCommand(mc.strMenuNGColorFirst); mNGColor1.addActionListener(new MenuListener(opt,cgame));mNGColor1.setSelected(true);
				JRadioButtonMenuItem	mNGColor2		=new	JRadioButtonMenuItem(mc.strMenuNGColorSecond);  mNGColor2.setActionCommand(mc.strMenuNGColorSecond); mNGColor2.addActionListener(new MenuListener(opt,cgame));
				ButtonGroup bgNGColor=new ButtonGroup();
				bgNGColor.add(mNGColor1);
				bgNGColor.add(mNGColor2);
			JMenu		mNGStartPos		=new 	JMenu(mc.strMenuNGStartPos);
				JRadioButtonMenuItem 	mNGSPHor		=new	JRadioButtonMenuItem(mc.strMenuNGSPHor);  mNGSPHor.setActionCommand(mc.strMenuNGSPHor); mNGSPHor.addActionListener(new MenuListener(opt,cgame));
				JRadioButtonMenuItem	mNGSPDiag		=new	JRadioButtonMenuItem(mc.strMenuNGSPDiag);  mNGSPDiag.setActionCommand(mc.strMenuNGSPDiag); mNGSPDiag.addActionListener(new MenuListener(opt,cgame));mNGSPDiag.setSelected(true);
				ButtonGroup bgNGStartPos=new ButtonGroup();
				bgNGStartPos.add(mNGSPHor);
				bgNGStartPos.add(mNGSPDiag);
				
		JMenuItem mExit=new JMenuItem(mc.strMenuExit);   mExit.setActionCommand(mc.strMenuExit); mExit.addActionListener(new MenuListener(opt,cgame));
		this.add(mNewGame);
			mNewGame.add(mNG);
			mNewGame.addSeparator();
			mNewGame.add(mNGLevels);
				mNGLevels.add(mNGLevel1);
				mNGLevels.add(mNGLevel2);
				mNGLevels.add(mNGLevel3);
				mNGLevels.add(mNGLevel4);
				mNGLevels.add(mNGLevel5);
				mNGLevels.add(mNGLevel6);
				mNGLevels.add(mNGLevel7);
			mNewGame.add(mNGColor);
				mNGColor.add(mNGColor1);
				mNGColor.add(mNGColor2);
			mNewGame.add(mNGStartPos);
				mNGStartPos.add(mNGSPHor);
				mNGStartPos.add(mNGSPDiag);
		this.add(mExit);
		this.setVisible(true);
		
		
		mNGLevel1.setSelected(true);
	}
}

class CountLabels
{
	private JLabel lblUser;
	private JLabel lblAuto;
	private JLabel lblCount;
	private ControlGame cgame;
	FontMetrics fmFontMetrics;
	private Options opt;
	private int getWidth(String str, FontMetrics fm)
	{
		int res=0;
		for(int i=0;i<str.length();++i)
			res+=fm.charWidth(str.charAt(i));
		return res;
	}
	public CountLabels(JFrame forAdd,Options opt,ControlGame cgame, int left,int top)
	{
		this.opt=opt;
		this.cgame=cgame;
		JPanel pnl=new JPanel(); pnl.setSize(200,100);
		pnl.setLayout(null);
		pnl.setBackground(opt.clBackground);
		pnl.setLocation(left, top);
		lblUser=new JLabel(opt.strCountUser); lblUser.setForeground(opt.clBackground);
		lblAuto=new JLabel(opt.strCountAuto);
		lblCount=new JLabel("000/000");
		int x=opt.windowBorder,y=opt.windowBorder/2;
		fmFontMetrics=lblUser.getFontMetrics(lblUser.getFont());
		heightLabels=fmFontMetrics.getHeight();
		lblUser.setLocation(x, y);
		lblUser.setSize(getWidth(opt.strCountUser, fmFontMetrics), heightLabels);
		//lblUser.setForeground(opt.clFirst.toColor());
		x+=lblUser.getWidth()+opt.windowBorder;
		lblCount.setLocation(x, y);
		lblCount.setSize(getWidth("00/00", fmFontMetrics), heightLabels);
		lblCount.setForeground(opt.clAdd.toColor());
		x+=lblCount.getWidth()+opt.windowBorder;
		lblAuto.setLocation(x, y);
		lblAuto.setSize(getWidth(opt.strCountAuto, fmFontMetrics), heightLabels);
		x+=lblAuto.getWidth()+opt.windowBorder;
		y+=lblAuto.getHeight()+opt.windowBorder/2;
		pnl.setSize(x,y); pnl.setLocation(left+((opt.widthBorder+opt.szElement)*opt.sizeBoard+opt.widthBorder)/2-x/2, top);
		pnl.add(lblUser); lblUser.setVisible(true); 
		pnl.add(lblAuto); lblAuto.setVisible(true);
		pnl.add(lblCount);lblCount.setVisible(true);
		forAdd.add(pnl); pnl.setVisible(true);
		heightLabels=y;
		this.setForeground();
	}
	private void setForeground()
	{
		if(opt.getIdUser()==Board.BOARD_FIRST)
		{
			lblUser.setForeground(opt.clFirst.toColor());
			lblAuto.setForeground(opt.clSecond.toColor());
		}
		else
		{
			lblAuto.setForeground(opt.clFirst.toColor());
			lblUser.setForeground(opt.clSecond.toColor());			
		}
	}
	private int heightLabels;
	public int getHeight(){	return heightLabels;}
	public void updateLabels()
	{
		Integer cntUser= new Integer(cgame.getCountUser());
		Integer cntAuto= new Integer(cgame.getCountAuto());
		String newCnt=new String("");
		if(cntUser<10) newCnt+="  ";
		newCnt+=cntUser.toString();
		newCnt+="/";
		newCnt+=cntAuto.toString();
		if(cntAuto<10) newCnt+="  ";
		newCnt+="";
		lblCount.setText(newCnt);
		this.setForeground();
	}
}


class MainWindow extends JFrame implements View
{
	private DrawBoard board;
	private ImageBoard imagebrd;
	private CountLabels countlbls;
	private Options opt;
	class WndListener extends WindowAdapter//слушатель закрытия окна
	{
		ControlGame cgame;
		public WndListener(ControlGame cgame)
		{
			this.cgame=cgame;
		}
		public void windowClosing(WindowEvent ev)
		{
			cgame.dispose();
			System.exit(0);
		}
	}
	public MainWindow(Options opt,DrawBoard brd,ControlGame cgame)
	{
		super();
		this.setLayout(null);
		this.setTitle(opt.strTitle);
		this.opt=opt;
		this.board=brd;
		Rectangle rectBoard=new Rectangle();
		rectBoard.x=rectBoard.y=opt.windowBorder;
		countlbls=new CountLabels(this, opt, cgame, rectBoard.x, rectBoard.y);
		rectBoard.y+=countlbls.getHeight()+opt.windowBorder;
		rectBoard.height=rectBoard.width=(opt.szElement+opt.widthBorder)*opt.sizeBoard+opt.widthBorder;
		imagebrd=new ImageBoard(opt, rectBoard,brd,cgame);
		this.add(imagebrd);
		JMenuBar menu=new MainMenu(opt,cgame);
		this.setJMenuBar(menu);
		menu.setVisible(true);
		this.addWindowListener(new WndListener(cgame));
		//this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Rectangle totalRect=new Rectangle(rectBoard);
		totalRect.width+=rectBoard.x+opt.windowBorder;
		//totalRect.height+=opt.windowBorder+rectBoard.y+menu.getHeight()+opt.windowMenuHeight;
		totalRect.height+=rectBoard.y+opt.windowBorder;
		totalRect.height+=menu.getHeight()+opt.windowMenuHeight;
		totalRect.x=opt.windowLeft; totalRect.y=opt.windowTop;
		this.setBounds(totalRect);
		this.setResizable(false);
		
		ImageIcon img= new ImageIcon(GetImages.getImage(BoardOptions.ClBlock.green, opt.clBackground, 32, 32));
		this.setIconImage(img.getImage());
		//this.getOwner().setIconImage(this.getIconImage());
		//this.getOwner().setMaximumSize(new Dimension(totalRect.width, totalRect.height));
		this.setVisible(true);

		if(opt.isHorizontalNewGame)
			cgame.setNewGameHorizontal(opt.sizeBoard, opt.getIdAuto(), opt.getIdUser(), opt.cntThreads, opt.level);
		else
			cgame.setNewGameDiagonal(opt.sizeBoard, opt.getIdAuto(), opt.getIdUser(), opt.cntThreads, opt.level);

		countlbls.updateLabels();
		//(new DialogWindow(this,opt)).showMessage(opt.strWinDraw);
	}

	public void updateView()
	{
		imagebrd.updatePrint();
		countlbls.updateLabels();
	}
	public void showWaitMessage(UserOrAuto arg)
	{
		DialogWindow dlg=new DialogWindow(this, opt);
		if(UserOrAuto.auto==arg)
			dlg.showMessage(opt.strWaitAuto);
		if(UserOrAuto.user==arg)
			dlg.showMessage(opt.strWaitUser);
		
	}
	private void showWin(UserOrAutoOrNone arg,Integer cntUser,Integer cntAuto)
	{
		String msg=new String();
		if(UserOrAutoOrNone.auto==arg)
			msg=msg.concat(opt.strWinAuto);
		if(UserOrAutoOrNone.user==arg)
			msg=msg.concat(opt.strWinUser);
		if(UserOrAutoOrNone.none==arg)
			msg=msg.concat(opt.strWinDraw);
		String newCnt=new String(" ");
		{
			if(cntUser<10) newCnt+="  ";
			newCnt+=cntUser.toString();
			newCnt+="/";
			newCnt+=cntAuto.toString();
			if(cntAuto<10) newCnt+="  ";
			newCnt+="";			
		}
		msg=msg.concat(newCnt);
		(new DialogWindow(this, opt)).showMessage(msg);
	}
	public void showWin(UserOrAutoOrNone arg,int cntUser,int cntAuto)
	{
		this.showWin(arg,new Integer(cntUser),new Integer(cntAuto));
	}
}