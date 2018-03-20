package com.pxy.pangjiao.mvp.ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProxy
{
	private ThreadPoolExecutor mExecutor; // 只需创建一次
	int mCorePoolSize;
	int mMaximumPoolSize;
	long mKeepAliveTime;

	public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime)
	{
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	private ThreadPoolExecutor initThreadPoolExecutor()
	{
		if(mExecutor == null)
		{
			synchronized (ThreadPoolProxy.class)
			{
				if(mExecutor == null)
				{
					TimeUnit unit = TimeUnit.MILLISECONDS;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 无界队列
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();// 丢弃任务并抛出RejectedExecutionException异常。
					mExecutor = new ThreadPoolExecutor(//
							mCorePoolSize,// 核心的线程数
							mMaximumPoolSize,// 最大的线程数
							mKeepAliveTime, // 保持时间
							unit, // 保持时间对应的单位
							workQueue,// 缓存队列/阻塞队列
							threadFactory, // 线程工厂
							handler// 异常捕获器
					);
				}
			}
		}
		return mExecutor;
	}

	/** 执行任务 */
	public void execute(Runnable task)
	{
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	/** 提交任务 */
	public Future<?> submit(Runnable task)
	{
		initThreadPoolExecutor();
		return mExecutor.submit(task);
	}

	/** 移除任务 */
	public void removeTask(Runnable task)
	{
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}
}
