package com.pxy.pangjiao.mvp.ThreadPool;

public class ThreadPoolFactory
{
	static ThreadPoolProxy mNormalPool;
	static ThreadPoolProxy mDownLoadPool;

	/**
	 * 得到普通线程池
	 * 
	 */

	/**
	 * 得到多线程下载线程池
	 * 
	 */
	public static ThreadPoolProxy getDownLoadPool()
	{
		if (mDownLoadPool == null)
		{
			synchronized (ThreadPoolProxy.class)
			{
				if (mDownLoadPool == null)
				{
					mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
				}
			}
		}
		return mDownLoadPool;
	}
}
