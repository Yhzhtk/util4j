package yh.util.dbpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * 连接池
 * @author yh
 * 创建时间：2012-12-5
 */
public class DBConPool {// implements TimerListener {
	
	private Connection con = null;
	private int inUsed = 0; // 使用的连接数
	private ArrayList<Connection> freeConnections = new ArrayList<Connection>();// 容器，空闲连接
	
	//private int minConn; // 最小连接数
	private int maxConn; // 最大连接
	private String name; // 连接池名字
	private String password; // 密码
	private String url; // 数据库连接地址
	private String driver; // 驱动
	private String user; // 用户名
	public Timer timer; // 定时

	/**
	 * 创建连接池
	 * 
	 * @param driver
	 * @param name
	 * @param URL
	 * @param user
	 * @param password
	 * @param maxConn
	 */
	public DBConPool(String name, String driver, String URL,
			String user, String password, int maxConn) {
		this.name = name;
		this.driver = driver;
		this.url = URL;
		this.user = user;
		this.password = password;
		this.maxConn = maxConn;
	}

	public DBConPool() {
	}

	/**
	 * 用完，释放连接
	 * 
	 * @param con
	 */
	public synchronized void freeConnection(Connection con) {
		this.freeConnections.add(con);// 添加到空闲连接的末尾
		this.inUsed--;
	}

	/**
	 * timeout 根据timeout得到连接
	 * 
	 * @param timeout
	 * @return
	 */
	public synchronized Connection getConnection(long timeout) {
		Connection con = null;
		if (this.freeConnections.size() > 0) {
			con = (Connection) this.freeConnections.get(0);
			if (con == null)
				con = getConnection(timeout); // 继续获得连接
		} else {
			con = newConnection(); // 新建连接
		}
		if (this.maxConn == 0 || this.maxConn < this.inUsed) {
			con = null;// 达到最大连接数，暂时不能获得连接了。
		}
		if (con != null) {
			this.inUsed++;
		}
		return con;
	}

	/**
	 * 
	 * 从连接池里得到连接
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {
		Connection con = null;
		if (this.freeConnections.size() > 0) {
			con = (Connection) this.freeConnections.get(0);
			this.freeConnections.remove(0);// 如果连接分配出去了，就从空闲连接里删除
			if (con == null)
				con = getConnection(); // 继续获得连接
		} else {
			con = newConnection(); // 新建连接
		}
		if (this.maxConn == 0 || this.maxConn < this.inUsed) {
			con = null;// 等待 超过最大连接时
		}
		if (con != null) {
			this.inUsed++;
			System.out.println("得到　" + this.name + "　的连接，现有" + inUsed
					+ "个连接在使用!");
		}
		return con;
	}

	/**
	 * 释放全部连接
	 * 
	 */
	public synchronized void release() {
		Iterator<Connection> allConns = this.freeConnections.iterator();
		while (allConns.hasNext()) {
			Connection con = (Connection) allConns.next();
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.freeConnections.clear();
	}

	/**
	 * 创建新连接
	 * 
	 * @return
	 */
	private Connection newConnection() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("sorry can't find db driver!");
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("sorry can't create Connection!");
		}
		return con;
	}

	/**
	 * 定时处理函数
	 */
	public synchronized void TimerEvent() {
		// 暂时还没有实现以后会加上的
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}