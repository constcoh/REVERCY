package com.constcoh;

import java.util.*;
import java.lang.*;



interface Task
{
	public void run();
}

class TaskStack
{
	private Stack<Runnable> pull;
	public TaskStack()
	{
		pull=new Stack<Runnable>();
	}
	public synchronized void push(Runnable task)
	{
		pull.push(task);
	}
	public synchronized Runnable pop()
	{
		if(pull.empty()) return null;
		return pull.pop();
	}
	public synchronized boolean isNotEmpty()
	{
		return !pull.empty();
	}
	public synchronized boolean isEmpty()
	{
		return pull.empty();
	}
	public synchronized int getCount()
	{
		return pull.size();
	}
}

class FlagTasksDone
{
	private /*volatile*/ int cntAddTasks;
	private volatile int cntDoneTasks;
	public FlagTasksDone()
	{
		cntAddTasks=cntDoneTasks=0;
	}
	public boolean isAllTasksDone()
	{
		return cntDoneTasks==cntAddTasks;
	}
	public synchronized void incCntDone()
	{
			++cntDoneTasks;
	}
	public void incCntAdd()
	{
			++cntAddTasks;		
	}
	
	public int getCntAdd(){return cntAddTasks;}
	public int getCntDone(){return cntDoneTasks;}
}

class EasyThread implements Runnable
{
	protected int sleepIterations;
	protected TaskStack taskStack;
	private Thread thread;
	private FlagTasksDone flagTasksDone;
	public EasyThread(TaskStack taskStack,FlagTasksDone flagTasksDone,int sleepIterations)
	{
		this.sleepIterations=sleepIterations;
		this.taskStack=taskStack;
		this.flagTasksDone=flagTasksDone;
		isThreadWait=true;
		thread=new Thread(this);
		thread.start();
	}
	private boolean isThreadWait;
	public boolean isWait()
	{
		return isThreadWait;
	}
	public void run()
	{
		Runnable task=null;
		Thread tekThread=Thread.currentThread();
		while(tekThread==thread)
		{
			task=taskStack.pop();
			if(task!=null)
			{
				task.run();
				flagTasksDone.incCntDone();
			}
			{
				int sum=0;
				for(int i=0;i<sleepIterations;++i) sum+=i;
			}
			//try{
			//	Thread.sleep(sleepTime);
			//}catch(InterruptedException er)
			//{
				
			//	er.getMessage();
			//}
		}
	}
	public void stop()
	{
		thread=null;
	}
}

class MultyTask
{
	public final int MAX_THREADS_COUNT=10;
	public final int SLEEP_ITERATIONS=1000;
	protected FlagTasksDone flagTasksDone;
	protected TaskStack taskStack;
	Vector<EasyThread> threads;
	public MultyTask(int cntThreads)
	{
		taskStack=new TaskStack();
		threads=new Vector<EasyThread>();
		flagTasksDone=new FlagTasksDone();
		EasyThread newThread;
		if(cntThreads>0 && cntThreads<=MAX_THREADS_COUNT)
		{
			for(int i=0;i<cntThreads;++i)
			{
				newThread=new EasyThread(taskStack,flagTasksDone, SLEEP_ITERATIONS);
				newThread.run();
				threads.add(newThread);
			}
		}
	}
	public void addTask(Runnable task)
	{
		flagTasksDone.incCntAdd();
		taskStack.push(task);
	}
	public int getCntThreads()
	{
		return threads.size();
	}
	public boolean isDoneAllTasks()
	{
		return flagTasksDone.isAllTasksDone();
		//return taskStack.isEmpty();
	}
	public boolean isNotDoneAllTasks()
	{
		return !flagTasksDone.isAllTasksDone();
		//return !taskStack.isEmpty();
	}
	public void addThreads(int newThreadsCount)
	{
		EasyThread newThread;
		if(newThreadsCount>0 && newThreadsCount<=MAX_THREADS_COUNT)
		{
			for(int i=0;i<newThreadsCount;++i)
			{
				newThread=new EasyThread(taskStack,flagTasksDone, SLEEP_ITERATIONS);
				newThread.run();
				threads.add(newThread);
			}
		}		
	}
	public void addThread()
	{
		EasyThread newThread=new EasyThread(taskStack,flagTasksDone, SLEEP_ITERATIONS);
		newThread.run();
		threads.add(newThread);
	}
	public void dispose()
	{
		if(!threads.isEmpty())
		{
			EasyThread thread;
			for(Iterator<EasyThread> i=threads.iterator();i.hasNext();){
				thread=i.next();
				thread.stop();
			}
			threads.clear();
		}
	}
	public void Doing()
	{
		Runnable task=null;
		task=taskStack.pop();
		while(task!=null)
		{
			task.run();
			flagTasksDone.incCntDone();			
			task=taskStack.pop();
		}
		
		this.waitDoing();
	}
	public void waitDoing()
	{
		while(this.isNotDoneAllTasks()){
			for(int i=0;i<SLEEP_ITERATIONS;++i){}
		}
	}
	public int getCntAdd(){return flagTasksDone.getCntAdd();}
	public int getCntDone(){return flagTasksDone.getCntDone();}
}