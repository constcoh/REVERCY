package com.constcoh;

import java.awt.Point;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

//Генератор случайных чисел
class RndGenerator
{
	private Random rnd;
	public RndGenerator()
	{
		rnd=new Random(System.currentTimeMillis());
	}
	public int getInt(int MaxPlusOne)
	{
		return rnd.nextInt(MaxPlusOne);
	}
	public boolean getBoolean()
	{
		return rnd.nextBoolean();		
	}
}
interface DrawBoard
{
	public static final int BOARD_FREE=0;
	public static final int BOARD_FIRST=1;
	public static final int BOARD_SECOND=-1;
	public int getM(Point arg);
	public int getM(int x,int y);
	public Vector<Point> getAbleForGo(int id);
}


class Board implements DrawBoard
{
	public static final int BOARD_BORDER=2;
	public static final int WIN_NONE=0;
	public static final int WIN_FIRST=1;
	public static final int WIN_SECOND=-1;
	public static final int WIN_DRAW=3;
	
	protected int size;
	protected int M[][];

	private int cntFirst;
	private int cntSecond;
	private int cntNone;
	
	public Board(int size)
	{
		this.size=size;
		this.M=null;
		this.clear();
	}
	public void clear()
	{
		M=new int[size][size];
		for(int x=0;x<size;++x)
			for(int y=0;y<size;++y)
			{
				M[x][y]=BOARD_FREE;
			}
		cntFirst=0; cntSecond=0; cntNone=size*size;		
	}
	public void checkCounters()
	{
		cntFirst=0; cntSecond=0; cntNone=0;
		for(int x=0;x<size;++x)
			for(int y=0;y<size;++y)
			{
				if(M[x][y]==BOARD_FIRST) ++cntFirst;
				if(M[x][y]==BOARD_SECOND) ++cntSecond;
				if(M[x][y]==BOARD_FREE) ++cntNone;
			}
	}
	public void setMwithCounter(int id,Point arg)
	{
		if(id==BOARD_FIRST || id==BOARD_SECOND || id==BOARD_FREE)
		{
			switch(M[arg.x][arg.y])
			{
			case BOARD_FIRST:
				--cntFirst;
				break;
			case BOARD_SECOND:
				--cntSecond;
				break;
			case BOARD_FREE:
				--cntNone;
			}

			switch(id)
			{
			case BOARD_FIRST:
				++cntFirst;
				break;
			case BOARD_SECOND:
				++cntSecond;
				break;
			case BOARD_FREE:
				++cntNone;
				break;
			}
			M[arg.x][arg.y]=id;
		}
	}
	public void setMwithCounter(int id,int x, int y)
	{
		if(id==BOARD_FIRST || id==BOARD_SECOND || id==BOARD_FREE)
		{
			switch(M[x][y])
			{
			case BOARD_FIRST:
				--cntFirst;
				break;
			case BOARD_SECOND:
				--cntSecond;
				break;
			case BOARD_FREE:
				--cntNone;
			}

			switch(id)
			{
			case BOARD_FIRST:
				++cntFirst;
				break;
			case BOARD_SECOND:
				++cntSecond;
				break;
			case BOARD_FREE:
				++cntNone;
				break;
			}
			M[x][y]=id;
		}
	}
	public void setM(int id,Point arg)
	{
			M[arg.x][arg.y]=id;
	}
	public void setM(int id,int x,int y)
	{
		M[x][y]=id;
	}
	public int getM(Point arg)
	{
		return M[arg.x][arg.y];
	}
	public int getM(int x,int y)
	{
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.print(M[x][y]);
		//out.flush();	
		return M[x][y];
	}
	public Board(Board arg)
	{
		this.size=arg.size;
		this.M=new int[size][size];
		this.setAs(arg);
	}
	public void setAs(Board arg)
	{
		if(this.size!=arg.size)
		{
			size=arg.size;
			M=new int[size][size];
		}
		for(int x=0;x<size;++x)
			for(int y=0;y<size;++y)
				M[x][y]=arg.M[x][y];
		this.cntFirst=arg.cntFirst;
		this.cntSecond=arg.cntSecond;
		this.cntNone=arg.cntNone;
	}
	public int getIdWinCounter()
	{
		if(!isAbleForGo())
		{
			if(cntFirst>cntSecond) return WIN_FIRST;
			if(cntFirst<cntSecond) return WIN_SECOND;
			if(cntFirst==cntSecond) return WIN_DRAW;
		}
		return WIN_NONE;		
	}
	public int getIdWinCounterNotAbleForGo()
	{
		if(cntFirst>cntSecond) return WIN_FIRST;
		if(cntFirst<cntSecond) return WIN_SECOND;
		if(cntFirst==cntSecond) return WIN_DRAW;
		return WIN_NONE;		
	}
	public int getIdWin()
	{
		this.checkCounters();
		return this.getIdWinCounter();
	}
	public int getIdWinNotAbleForGo()
	{
		this.checkCounters();
		return this.getIdWinCounterNotAbleForGo();
	}
	public int getCountFromCounters(int id)
	{
		switch(id)
		{
		case BOARD_FIRST:
			return cntFirst;
		case BOARD_SECOND:
			return cntSecond;
		case BOARD_FREE:
			return cntNone;
		}
		return 0;
	}
	public int getCount(int id)
	{
		int cntRes=0;
		for(int x=0;x<size;++x)
			for(int y=0;y<size;++y)
				if(M[x][y]==id) ++cntRes;
		return cntRes;
	}
	public int getSize(){return size;}
	public void print(PrintWriter out)
	{
		out.print("None:"); out.print(cntNone);
		out.print(" First:"); out.print(cntFirst);
		out.print(" Second:"); out.println(cntSecond);
		for(int y=0;y<size;++y)
		{
			for(int x=0;x<size;++x)
			{
				switch (M[x][y]) {
				case BOARD_FREE:
					out.print('.');
					break;
				case BOARD_FIRST:
					out.print('0');
					break;
				case BOARD_SECOND:
					out.print('X');
					break;
				}
				out.print(' ');
			}
			out.println();
		}
	}
	public boolean isAbleForGo(int id, Point pos)
	{
		//Проверка: если точка занята, то ход невозможен
		if(M[pos.x][pos.y]!=BOARD_FREE) return false;
		//Проверка: по соседу есть точки противника
		{
			Point p=new Point();
			boolean isOneEnemyPoint;
			//Up
			if(pos.y>0){
				p.setLocation(pos.x,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.y;
					isOneEnemyPoint=true;
					if(!(p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.y>=0) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Up,Right
			if(pos.x<size-1 && pos.y>0){
				p.setLocation(pos.x+1,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x; --p.y;
					isOneEnemyPoint=true;
					if(!(p.x<size && p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size && p.y>=0) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Right
			if(pos.x<size-1){
				p.setLocation(pos.x+1,pos.y);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x;
					isOneEnemyPoint=true;
					if(!(p.x<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Right,Down
			if(pos.x<size-1 && pos.y<size-1){
				p.setLocation(pos.x+1,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x; ++p.y;
					isOneEnemyPoint=true;
					if(!(p.x<size && p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size && p.y<size) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Down
			if(pos.y<size-1){
				p.setLocation(pos.x,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.y;
					isOneEnemyPoint=true;
					if(!(p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.y<size) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Down,Left
			if(pos.x>0 && pos.y<size-1){
				p.setLocation(pos.x-1,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x; ++p.y;
					isOneEnemyPoint=true;
					if(!(p.x>=0 && p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0 && p.y<size) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Left
			if(pos.x>0){
				p.setLocation(pos.x-1,pos.y);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x;
					isOneEnemyPoint=true;
					if(!(p.x>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
			//Left,Up
			if(pos.x>0 && pos.y>0){
				p.setLocation(pos.x-1,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x; --p.y;
					isOneEnemyPoint=true;
					if(!(p.x>=0 && p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0 && p.y>=0) if(M[p.x][p.y]==id)
				{
					return true;
				}
			}
		}//Конец проверки
		
		return false;
	}
	public boolean isAbleForGo(int id)
	{
		Point p=new Point();
		for(p.x=0;p.x<size;++p.x)
			for(p.y=0;p.y<size;++p.y)
				if(isAbleForGo(id,p)) return true;
		return false;
	}
	public boolean isAbleForGo()
	{
		return isAbleForGo(BOARD_FIRST)|| isAbleForGo(BOARD_SECOND);
	}
	public int getAbleForGo(int id,Point res[])
	{
		int cntRes=0;
		Point p=new Point();
		for(p.x=0;p.x<size;++p.x)
			for(p.y=0;p.y<size;++p.y)
				if(isAbleForGo(id,p)) res[cntRes++]=new Point(p);		
		return cntRes;
	}
	public Vector<Point> getAbleForGo(int id)
	{
		Vector<Point> res=new Vector<Point>();
		Point p=new Point();
		for(p.x=0;p.x<size;++p.x)
			for(p.y=0;p.y<size;++p.y)
				if(isAbleForGo(id,p)) res.add(new Point(p));
		return res;
	}
	public void go(int id,int x,int y)
	{
		go(id,new Point(x,y));
	}
	public void go(int id,Point pos)
	{
		//Проверка: если точка занята, то ход невозможен
		if(M[pos.x][pos.y]!=BOARD_FREE) return;
		Point Change[]=new Point[4*size];
		int cntChange=0;
		//Проверка: по соседу есть точки противника
		{
			Point p=new Point();
			boolean isOneEnemyPoint;
			//Up
			if(pos.y>0){
				p.setLocation(pos.x,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.y;
					isOneEnemyPoint=true;
					if(!(p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.y>=0) if(M[p.x][p.y]==id)
				{
					++p.y;
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						++p.y;
					}
				}
			}
			//Up,Right
			if(pos.x<size-1 && pos.y>0){
				p.setLocation(pos.x+1,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x; --p.y;
					isOneEnemyPoint=true;
					if(!(p.x<size && p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size && p.y>=0) if(M[p.x][p.y]==id)
				{
					--p.x; ++p.y;
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						--p.x; ++p.y;
					}
				}
			}
			//Right
			if(pos.x<size-1){
				p.setLocation(pos.x+1,pos.y);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x;
					isOneEnemyPoint=true;
					if(!(p.x<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size) if(M[p.x][p.y]==id)
				{
					--p.x;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						--p.x;
					}
				}
			}
			//Right,Down
			if(pos.x<size-1 && pos.y<size-1){
				p.setLocation(pos.x+1,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.x; ++p.y;
					isOneEnemyPoint=true;
					if(!(p.x<size && p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x<size && p.y<size) if(M[p.x][p.y]==id)
				{
					--p.x; --p.y;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						--p.x; --p.y;
					}
				}
			}
			//Down
			if(pos.y<size-1){
				p.setLocation(pos.x,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					++p.y;
					isOneEnemyPoint=true;
					if(!(p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.y<size) if(M[p.x][p.y]==id)
				{
					--p.y; 
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						--p.y;
					}
				}
			}
			//Down,Left
			if(pos.x>0 && pos.y<size-1){
				p.setLocation(pos.x-1,pos.y+1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x; ++p.y;
					isOneEnemyPoint=true;
					if(!(p.x>=0 && p.y<size)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0 && p.y<size) if(M[p.x][p.y]==id)
				{
					++p.x; --p.y; 
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x; --p.y;
					}
				}
			}
			//Left
			if(pos.x>0){
				p.setLocation(pos.x-1,pos.y);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x;
					isOneEnemyPoint=true;
					if(!(p.x>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0) if(M[p.x][p.y]==id)
				{
					++p.x;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x;
					}
				}
			}
			//Left,Up
			if(pos.x>0 && pos.y>0){
				p.setLocation(pos.x-1,pos.y-1);
				isOneEnemyPoint=false;
				while(M[p.x][p.y]==-id)
				{
					--p.x; --p.y;
					isOneEnemyPoint=true;
					if(!(p.x>=0 && p.y>=0)) break;
				}
				if(isOneEnemyPoint)
				if(p.x>=0 && p.y>=0) if(M[p.x][p.y]==id)
				{
					++p.x; ++p.y;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x; ++p.y;
					}
				}
			}
		}//Конец проверки
		if(cntChange>0){
			//Ставим точку на позицию
			M[pos.x][pos.y]=id;
			//Инвертируем соседей
			for(int i=0;i<cntChange;++i) M[Change[i].x][Change[i].y]=id;
		}
	}
	public void goWithCounter(int id,Point pos)
	{
		//Проверка: если точка занята, то ход невозможен
		if(M[pos.x][pos.y]!=BOARD_FREE) return;
		Point Change[]=new Point[4*size];
		int cntChange=0;
		//Проверка: по соседу есть точки противника
		{
			Point p=new Point();
			//Up
			if(pos.y>0){
				p.setLocation(pos.x,pos.y-1);
				while(M[p.x][p.y]==-id)
				{
					--p.y;
					if(!(p.y>=0)) break;
				}
				if(p.y>=0) if(M[p.x][p.y]==id)
				{
					++p.y;
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						++p.y;
					}
				}
			}
			//Up,Right
			if(pos.x<size-1 && pos.y>0){
				p.setLocation(pos.x+1,pos.y-1);
				while(M[p.x][p.y]==-id)
				{
					++p.x; --p.y;
					if(!(p.x<size && p.y>=0)) break;
				}
				if(p.x<size && p.y>=0) if(M[p.x][p.y]==id)
				{
					--p.x; ++p.y;
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						--p.x; ++p.y;
					}
				}
			}
			//Right
			if(pos.x<size-1){
				p.setLocation(pos.x+1,pos.y);
				while(M[p.x][p.y]==-id)
				{
					++p.x;
					if(!(p.x<size)) break;
				}
				if(p.x<size) if(M[p.x][p.y]==id)
				{
					--p.x;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						--p.x;
					}
				}
			}
			//Right,Down
			if(pos.x<size-1 && pos.y<size-1){
				p.setLocation(pos.x+1,pos.y+1);
				while(M[p.x][p.y]==-id)
				{
					++p.x; ++p.y;
					if(!(p.x<size && p.y<size)) break;
				}
				if(p.x<size && p.y<size) if(M[p.x][p.y]==id)
				{
					--p.x; --p.y;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						--p.x; --p.y;
					}
				}
			}
			//Down
			if(pos.y<size-1){
				p.setLocation(pos.x,pos.y+1);
				while(M[p.x][p.y]==-id)
				{
					++p.y;
					if(!(p.y<size)) break;
				}
				if(p.y<size) if(M[p.x][p.y]==id)
				{
					--p.y; 
					while(p.y!=pos.y)
					{
						Change[cntChange++]=new Point(p);
						--p.y;
					}
				}
			}
			//Down,Left
			if(pos.x>0 && pos.y<size-1){
				p.setLocation(pos.x-1,pos.y+1);
				while(M[p.x][p.y]==-id)
				{
					--p.x; ++p.y;
					if(!(p.x>=0 && p.y<size)) break;
				}
				if(p.x>=0 && p.y<size) if(M[p.x][p.y]==id)
				{
					++p.x; --p.y; 
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x; --p.y;
					}
				}
			}
			//Left
			if(pos.x>0){
				p.setLocation(pos.x-1,pos.y);
				while(M[p.x][p.y]==-id)
				{
					--p.x;
					if(!(p.x>=0)) break;
				}
				if(p.x>=0) if(M[p.x][p.y]==id)
				{
					++p.x;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x;
					}
				}
			}
			//Left,Up
			if(pos.x>0 && pos.y>0){
				p.setLocation(pos.x-1,pos.y-1);
				while(M[p.x][p.y]==-id)
				{
					--p.x; --p.y;
					if(!(p.x>=0 && p.y>=0)) break;
				}
				if(p.x>=0 && p.y>=0) if(M[p.x][p.y]==id)
				{
					++p.x; ++p.y;
					while(p.x!=pos.x)
					{
						Change[cntChange++]=new Point(p);
						++p.x; ++p.y;
					}
				}
			}
		}//Конец проверки
		if(cntChange>0){
			//Ставим точку на позицию
			this.setMwithCounter(id, pos);
			//Инвертируем соседей
			for(int i=0;i<cntChange;++i) this.setMwithCounter(id, Change[i]);
		}
	}
}

class Player
{
	protected int id;
	protected Board board;
	public Player(int id,Board board)
	{
		this.id=id;
		this.board=board;
	}
	public int getId(){return id;} 
}

interface Brain
{
	public Point getPointForGo(int level);
	public void dispose();
}

class BrainAuto implements Brain
{
	Board board;
	int sizeBoard;
	MultyTask mt;
	int idAuto;
	int idUser;
	class Task implements Runnable
	{
		public final static int WIN=10000; 
		private int level;
		private Point goFirst;
		private Board[] boards;
		private int size;
		private int id1;
		private int id2;
		int ResM[][];
		public Task(int level,Board board,int id1,int id2,Point goFirst,int[][] ResM) 
		{
			this.level=level;
			this.goFirst=new Point(goFirst);
			boards=new Board[level+1];
			this.size=board.getSize();
			for(int i=0;i<level+1;++i) boards[i]=new Board(size);
			boards[level].setAs(board);
			this.id1=id1;
			this.id2=id2;
			this.ResM=ResM;
		}
		private int SearchForAll(int level,int id1,int id2)
		{
			Board board=boards[level];
			Board newBoard=boards[level-1];
			int res=Integer.MIN_VALUE;
			Point AbleForGo[]=new Point[4*size];
			int cntAbleForGo=board.getAbleForGo(id1, AbleForGo);
			int cntOldForID1=board.getCount(id1);
			
			for(int i=0;i<cntAbleForGo;++i)
			{
				Point newPos=AbleForGo[i];
				newBoard.setAs(board);
				newBoard.go(id1, newPos);
				int cntNewForID1=newBoard.getCount(id1);
				int newRes=cntNewForID1-cntOldForID1;
				if(level>1) newRes-=this.SearchForAll(level-1, id2, id1);
				if(newRes>res) res=newRes;
			}
			
			return res;
		}
		public void run()
		{
			Board board=boards[level];
			Board newBoard=boards[level-1];
			int res=Integer.MIN_VALUE;
			//Point AbleForGo[]=new Point[4*size];
			//int cntAbleForGo=board.getAbleForGo(id1, AbleForGo);
			int cntOldForID1=board.getCount(id1);
			
			//for(int i=0;i<cntAbleForGo;++i)
			{
				Point newPos=goFirst;
				newBoard.setAs(board);
				newBoard.go(id1, newPos);
				int cntNewForID1=newBoard.getCount(id1);
				int newRes=cntNewForID1-cntOldForID1;
				if(level>1) newRes-=this.SearchForAll(level-1, id2, id1);
				if(newRes>res) res=newRes;
			}
			
			ResM[goFirst.x][goFirst.y]=res;
		}
	}
	public BrainAuto(Board board,int cntThreads,int idAuto,int idUser)
	{
		this.board=board;
		mt=new MultyTask(cntThreads);
		sizeBoard=board.getSize();
		this.idAuto=idAuto;
		this.idUser=idUser;
	}
	private void GenerateTasks(int level,int[][] ResM)
	{
		Point AbleForGo[]=new Point[4*sizeBoard];
		int cntAbleForGo=board.getAbleForGo(idAuto, AbleForGo);
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.print("Point: size:"); out.println(cntAbleForGo);
		for(int i=0;i<cntAbleForGo;++i)
		{
			//out.print("x:"); out.print(AbleForGo[i].x); 
			//out.print(" y:"); out.println(AbleForGo[i].y); 
			mt.addTask(new Task(level,board,idAuto,idUser,AbleForGo[i],ResM));
		}
		//out.flush();
	}
	private Point getMaxPoint(int[][] ResM)
	{
		RndGenerator rnd=new RndGenerator();
		Point res=new Point(-1,-1);
		int maxValue=Integer.MIN_VALUE;
		int cntMax=0;
		for(int x=0;x<sizeBoard;++x)
			for(int y=0;y<sizeBoard;++y)
			{
				if(maxValue<ResM[x][y]) 
				{
					maxValue=ResM[x][y];
					res.setLocation(x,y);
					cntMax=1;
				}
				if(maxValue==ResM[x][y]) ++cntMax;
			}
		int indexRes=rnd.getInt(cntMax);
		cntMax=0;
		for(int x=0;x<sizeBoard;++x)
			for(int y=0;y<sizeBoard;++y)
			{
				if(maxValue==ResM[x][y])
				{
					if(cntMax==indexRes)
					{
						res.setLocation(x,y);
					}
					++cntMax;
				}
			}
		
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//for(int y=0;y<sizeBoard;++y)
		//{
			//for(int x=0;x<sizeBoard;++x)
				//if(ResM[x][y]==Integer.MIN_VALUE)
				//{
					//out.print(". ");
				//}
				//else
				//{
					//out.print(ResM[x][y]); out.print(' ');
				//}
			//out.println();
		//}
		//out.flush();
		return res;
	}
	public Point getPointForGo(int level)
	{
		int[][] ResM=new int[sizeBoard][sizeBoard];
		for(int x=0;x<sizeBoard;++x)
			for(int y=0;y<sizeBoard;++y)
				ResM[x][y]=Integer.MIN_VALUE;
		GenerateTasks(level, ResM);
		mt.Doing();
		return getMaxPoint(ResM);
	}
	protected void finalize() throws Throwable {
		super.finalize();
		mt.dispose();
	}
	public void dispose()
	{
		mt.dispose();
	}
}
class PlayerAuto extends Player
{
	//protected int id;
	//protected Board board;
	private Brain brain;
	private int level;
	public PlayerAuto(int id,Board board,int cntThreads,int level)
	{
		super(id,board);
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println();
		//out.print("Id Player Auto:");
		//out.println(id);
		//out.flush();
		brain=new BrainAuto(board, cntThreads, id, -id);
		this.level=level;
	}
	public void go()
	{
		board.goWithCounter(id, brain.getPointForGo(level));
	}
	public void dispose()
	{
		brain.dispose();
	}
}
class PlayerGamer extends Player
{
	//protected int id;
	//protected Board board;
	public PlayerGamer(int id,Board board)
	{
		super(id,board);
	}	
	public void go(Point purpose)
	{
		board.goWithCounter(id, purpose);
	}
}
class Game implements ControlGame
{
	//public static final int ID_1=Board.BOARD_FIRST;
	//public static final int ID_2=Board.BOARD_SECOND;
	private Board board;
	private PlayerAuto playerAuto;
	private PlayerGamer playerGamer;
	private Vector<View> views;
	private boolean isInGame;
	public void setNewGameHorizontal(int sizeBoard,int idAuto,int idUser,int cntThreads,int level)
	{
		this.initialNewGame(sizeBoard, idAuto, idUser, cntThreads, level);
		if(sizeBoard>4)
		{
			int szDiv2=sizeBoard/2;
			board.setMwithCounter(idAuto, szDiv2-1, szDiv2-1);
			board.setMwithCounter(idAuto, szDiv2, szDiv2-1);
			board.setMwithCounter(idUser, szDiv2-1, szDiv2);
			board.setMwithCounter(idUser, szDiv2, szDiv2);
		}
		updateViews();
		if(board.BOARD_SECOND==playerGamer.getId())
		{
			playerAuto.go();
		}
		updateViews();
	}
	public void setNewGameDiagonal(int sizeBoard,int idAuto,int idUser,int cntThreads,int level)
	{
		this.initialNewGame(sizeBoard, idAuto, idUser, cntThreads, level);
		if(sizeBoard>4)
		{
			int szDiv2=sizeBoard/2;
			board.setMwithCounter(idUser, szDiv2-1, szDiv2-1);
			board.setMwithCounter(idAuto, szDiv2, szDiv2-1);
			board.setMwithCounter(idAuto, szDiv2-1, szDiv2);
			board.setMwithCounter(idUser, szDiv2, szDiv2);

		}
		updateViews();
		if(board.BOARD_SECOND==playerGamer.getId())
		{
			playerAuto.go();
		}
		updateViews();
	}
	private void initialNewGame(int sizeBoard,int idAuto,int idUser,int cntThreads,int level)
	{
		
		board.clear(); 
		playerAuto.dispose();
		playerAuto=new PlayerAuto(idAuto,board,cntThreads,level);
		playerGamer=new PlayerGamer(idUser,board);
		this.isInGame=true;
	}
	public Game(int sizeBoard,int idAuto,int idUser,int cntThreads,int level)
	{
		board=new Board(sizeBoard);
		playerAuto=new PlayerAuto(idAuto,board,cntThreads,level);
		this.initialNewGame(sizeBoard, idAuto, idUser, cntThreads, level);
		views=new Vector<View>();
	}
	public DrawBoard getDrawBoard(){return board;}
	public Vector<Point> getAbleForUser()
	{
		return board.getAbleForGo(playerGamer.getId());
	}
	private void sendWaitMessage(View.UserOrAuto arg)
	{
		for(View view:views) view.showWaitMessage(arg);
	}
	private void sendWinMessage(int idWin)
	{
		int idUser=playerGamer.getId();
		int idAuto=playerAuto.getId();
		int cntUser=board.getCountFromCounters(idUser);
		int cntAuto=board.getCountFromCounters(idAuto);
		for(View view:views)
		{
			if(idWin==Board.WIN_FIRST && idUser==Board.BOARD_FIRST)
				view.showWin(View.UserOrAutoOrNone.user, cntUser, cntAuto);
			if(idWin==Board.WIN_SECOND && idUser==Board.BOARD_SECOND)
				view.showWin(View.UserOrAutoOrNone.user, cntUser, cntAuto);
			if(idWin==Board.WIN_FIRST && idAuto==Board.BOARD_FIRST)
				view.showWin(View.UserOrAutoOrNone.auto, cntUser, cntAuto);
			if(idWin==Board.WIN_SECOND && idAuto==Board.BOARD_SECOND)
				view.showWin(View.UserOrAutoOrNone.auto, cntUser, cntAuto);
			if(idWin==Board.WIN_DRAW)
				view.showWin(View.UserOrAutoOrNone.none, cntUser, cntAuto);
		}
	}
	public void update()
	{
		updateViews();
	}
	private void updateViews()
	{
		
		//PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		//out.println("UPDATE");
		//out.print("playerAuto:");out.print(playerAuto.getId()); out.print(' '); out.println();
		//out.print("playerUser:");out.print(playerGamer.getId()); out.print(' '); out.println();
		//out.print(this.getCountUser());out.print(' ');out.println(this.getCountAuto());
		//for(int x=0;x<8;++x) for(int y=0;y<8;++y) out.print(board.getM(x,y));
		//out.println();
		//out.flush();
		for(View view:views) view.updateView();
	}
	private void goAuto()
	{
		playerAuto.go();
		updateViews();
		while(!board.isAbleForGo(playerGamer.getId()))
		{
			if(!board.isAbleForGo(playerAuto.getId()))
			{
				int idWin=board.getIdWinCounterNotAbleForGo();
				sendWinMessage(idWin);
				this.setFinished();
				return;
			}
			sendWaitMessage(View.UserOrAuto.user);
			playerAuto.go();
			updateViews();
		}
	}
	public void goUser(Point purpose)//return id Win user
	{
		if(!isInGame) return;
		if(!board.isAbleForGo(playerGamer.getId(), purpose)) return;
		playerGamer.go(purpose);
		updateViews();
		if(!board.isAbleForGo(playerAuto.getId()))
		{
			if(!board.isAbleForGo(playerGamer.getId()))
			{
				int idWin=board.getIdWinCounterNotAbleForGo();
				sendWinMessage(idWin);
				this.setFinished();
				return;
			}
			sendWaitMessage(View.UserOrAuto.auto);
			return;
		}
		else
		{
			goAuto();
			return;
		}
	}
	private void setFinished(){isInGame=false;}
	public boolean isFinished(){return !isInGame;}	
	public void addView(View view){	views.add(view);}
	public void removeView(View view){views.removeElement(view);}
	public void clearViews(){views.clear();}
	public int getCountUser(){return board.getCount(playerGamer.getId());}
	public int getCountAuto(){return board.getCount(playerAuto.getId());}
	public void dispose()
	{
		playerAuto.dispose();
	}
}

interface ControlGame
{
	public static final int ID_1=Board.BOARD_FIRST;
	public static final int ID_2=Board.BOARD_SECOND;
	public void goUser(Point purpose);
	public void setNewGameHorizontal(int sizeBoard,int idAuto,int idUser,int cntThreads,int level);
	public void setNewGameDiagonal(int sizeBoard,int idAuto,int idUser,int cntThreads,int level);
	public boolean isFinished();
	public int getCountUser();
	public int getCountAuto();
	public void dispose();
	public void update();
}

interface View
{
	public enum UserOrAuto { user,auto;}
	public enum UserOrAutoOrNone {user,auto,none;}
	public void updateView();
	public void showWaitMessage(UserOrAuto arg);
	public void showWin(UserOrAutoOrNone arg,int cntUser,int cntAuto);
	//public void initialNewGame();
}
