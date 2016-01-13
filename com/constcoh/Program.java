package com.constcoh;

import java.io.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.util.*;

import javax.swing.*;

class Wnd extends JFrame
{
	class Pnl extends JPanel
	{
		public Pnl()
		{
			super();
			this.setSize(100,100);
			this.setLocation(30, 30);
			this.setVisible(true);
			this.setBackground(new Color(0,0,0));
		}
	}
	public Wnd()
	{
		super();
		this.setLayout(null);
		this.setSize(300,300);
		this.setLocation(40, 40);
		this.add(new Pnl());
		this.setVisible(true);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		out.print("OK");
		out.flush();
	}
}

class Program
{
	public static void main(String[] args)
	{
		Options opt=new Options();
		Game game=new Game(opt.sizeBoard, ControlGame.ID_1, ControlGame.ID_1, opt.cntThreads, opt.level);
		
		game.addView(new MainWindow(new Options(),game.getDrawBoard(),game));
	}
	
}