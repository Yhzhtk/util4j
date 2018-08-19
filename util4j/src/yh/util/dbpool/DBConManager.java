package yh.util.dbpool;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * 连接池管理类
 * @author yh
 * 创建时间：2012-05-10
 */
public class DBConManager {
	
	static private DBConManager instance;// 唯一数据库连接池管理实例类
	private Vector<ConfigBean> drivers = new Vector<ConfigBean>();// 驱动信息
	private Hashtable<String, DBConPool> pools = new Hashtable<String, DBConPool>();// 连接池

	/**
	 * 实例化管理类
	 */
	public DBConManager() {
		this.init();
	}

	/**
	 * 得到唯一实例管理类
	 * 
	 * @return
	 */
	static synchronized public DBConManager getInstance() {
		if (instance == null) {
			instance = new DBConManager();
		}
		return instance;
	}

	/**
	 * 释放连接
	 * 
	 * @param name
	 * @param con
	 */
	public void freeConnection(String name, Connection con) {
		DBConPool pool = (DBConPool) pools.get(name);// 根据关键名字得到连接池
		if (pool != null)
			pool.freeConnection(con);// 释放连接
	}

	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection(String name) {
		DBConPool pool = null;
		Connection con = null;
		pool = (DBConPool) pools.get(name);// 从名字中获取连接池
		con = pool.getConnection();// 从选定的连接池中获得连接
		if (con != null)
			System.out.println("得到连接。。。");
		return con;
	}

	/**
	 * 得到一个连接，根据连接池的名字和等待时间
	 * 
	 * @param name
	 * @param time
	 * @return
	 */
	public Connection getConnection(String name, long timeout) {
		DBConPool pool = null;
		Connection con = null;
		pool = (DBConPool) pools.get(name);// 从名字中获取连接池
		con = pool.getConnection(timeout);// 从选定的连接池中获得连接
		System.out.println("得到连接。。。");
		return con;
	}

	/**
	 * 释放所有连接
	 */
	public synchronized void release() {
		Enumeration<DBConPool> allpools = pools.elements();
		while (allpools.hasMoreElements()) {
			DBConPool pool = (DBConPool) allpools.nextElement();
			if (pool != null)
				pool.release();
		}
		pools.clear();
	}

	/**
	 * 创建连接池
	 * 
	 * @param props
	 */
	private void createPools(ConfigBean dsb) {
		DBConPool dbpool = new DBConPool();
		dbpool.setName(dsb.getName());
		dbpool.setDriver(dsb.getDriver());
		dbpool.setUrl(dsb.getUrl());
		dbpool.setUser(dsb.getUsername());
		dbpool.setPassword(dsb.getPassword());
		dbpool.setMaxConn(dsb.getMaxconn());
		System.out.println("ioio:" + dsb.getMaxconn());
		pools.put(dsb.getName(), dbpool);
	}

	/**
	 * 初始化连接池的参数
	 */
	private void init() {
		// 加载配置信息
		this.loadDrivers();
		// 创建连接池
		Iterator<ConfigBean> alldriver = drivers.iterator();
		while (alldriver.hasNext()) {
			this.createPools((ConfigBean) alldriver.next());
			System.out.println("创建连接池。。。");
		}
		System.out.println("创建连接池完毕。。。");
	}

	/**
	 * 加载驱动程序
	 * 
	 * @param props
	 */
	private void loadDrivers() {
		ReadConfig pd = new ReadConfig();
		// 读取数据库配置文件
		drivers = pd.readConfigInfo("config/ds.config.xml");
		System.out.println("加载驱动程序。。。");
	}
}