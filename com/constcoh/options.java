package com.constcoh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class GetImages
{
	public static BufferedImage getImage(BoardOptions.ClBlock clBlock,Color bgColor,int newWidth,int newHeight)
	{
		BufferedImage res=null;
		if(clBlock==BoardOptions.ClBlock.white)	res=newWhiteImage(bgColor);
		if(clBlock==BoardOptions.ClBlock.black)	res=newBlackImage(bgColor);
		if(clBlock==BoardOptions.ClBlock.red)	res=newRedImage(bgColor);
		if(clBlock==BoardOptions.ClBlock.green)	res=newGreenImage(bgColor);
		if(clBlock==BoardOptions.ClBlock.blue)	res=newBlueImage(bgColor);
		if(clBlock==BoardOptions.ClBlock.yellow)	res=newYellowImage(bgColor);
		return scaleImage(res, newWidth, newHeight);
	}
	private static BufferedImage newYellowImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>200) ind=200; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>0,ind>>4));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>0,ind>>4));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}
	private static BufferedImage newRedImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>200) ind=200; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>4,ind>>4));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>4,ind>>4));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}
	private static BufferedImage newGreenImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>200) ind=200; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>4,ind>>0,ind>>4));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>4,ind>>0,ind>>4));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}
	private static BufferedImage newBlueImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>200) ind=200; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>4,ind>>4,ind>>0));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>4,ind>>4,ind>>0));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}
	private static BufferedImage newWhiteImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>255) ind=255; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>0,ind>>0));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=55+8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>0,ind>>0,ind>>0));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}
	private static BufferedImage newBlackImage(Color bgColor)
	{
		BufferedImage img=new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics gr=img.getGraphics();
		gr.setColor(bgColor);
		gr.fillRect(0, 0, 100, 100);
		for(int i=0;i<25;++i)
		{
			int ind=100+16*(i); if(ind>255) ind=255; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>2,ind>>2,ind>>2));
			gr.fillOval(i, i, 100-2*i, 100-2*i);
		}
		for(int i=0;i<25;++i)
		{
			int ind=55+8*25-8*i; if(ind<150) ind=150; ind=ind>>0;
			gr.setColor(new Color(ind>>2,ind>>2,ind>>2));
			gr.fillOval(25+i, 25+i, 100-2*i-50, 100-2*i-50);
		}
		return img;
	}	
	private static BufferedImage scaleImage(BufferedImage src,int newWidth,int newHeight)
	{
		BufferedImage img=new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr=(Graphics2D)img.getGraphics();
		int oldWidth=src.getWidth(),oldHeight=src.getHeight();
		AffineTransform at=AffineTransform.getScaleInstance(newWidth/(double)oldWidth, newHeight/(double)oldHeight);
		gr.setTransform(at);
		gr.drawImage(src, 0, 0, null);
		return img;
	}
	public static Image scaleImage(Image src,int newWidth,int newHeight,int oldWidth,int oldHeight)
	{
		BufferedImage img=new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr=(Graphics2D)img.getGraphics();
		AffineTransform at=AffineTransform.getScaleInstance(newWidth/(double)oldWidth, newHeight/(double)oldHeight);
		gr.setTransform(at);
		gr.drawImage(src, 0, 0, null);
		return img;
	}
}
