package yh.util.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 数据库访问基础类
 * 包括连接数据库，使用数据库和关闭数据库三个模块
 * @author yh
 * 2011-12-19 9:40
 */
public class DBUtil {

	private Connection conn = null;
	private ResultSet rs = null;
	private PreparedStatement pstm = null;
	private Statement stmt = null;

	/**
	 * 获取数据库连接对象
	 * 
	 * @return conn对象
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * 连接数据库
	 * 
	 * @param driver
	 *            数据库驱动名如com.mysql.jdbc.Driver
	 * @param url
	 *            连接字符串如jdbc:mysql://192.168.12.34:3306/mp3
	 * @param user
	 *            连接用户名
	 * @param pwd
	 *            密码
	 * @return 连接是否成功
	 */
	public boolean Connect(String driver, String url, String user, String pwd) {
		// String driver = "com.mysql.jdbc.Driver";
		// String url = "jdbc:mysql://192.168.12.34:3306/mp3";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			conn = DriverManager.getConnection(url, user, pwd);

			if (!conn.isClosed()) {
				stmt = conn.createStatement();
				System.out.println("Succeeded connecting to the Database!");
				return true;
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
		return false;
	}

	/**
	 * 执行任意语句，返回true表示结果为ResultSet
	 * 
	 * @param sqlStr
	 *            任何sql语句
	 * @return true 如果执行后第一个结果是ResultSet，返回true，否则为false
	 * @throws SQLException
	 */
	public boolean execute(String sqlStr) throws SQLException {
		return stmt.execute(sqlStr);
	}

	/**
	 * 执行数据更新语句
	 * 
	 * @param sqlStr
	 *            update语句字符串
	 * @return 更新的数量，0表示未更新
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlStr) throws SQLException {
		return stmt.executeUpdate(sqlStr);
	}

	/**
	 * 执行查询语句
	 * 
	 * @param sqlStr
	 *            查询语句字符串
	 * @return 查询得到的结果集
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sqlStr) throws SQLException {
		rs = stmt.executeQuery(sqlStr);
		return rs;
	}

	/**
	 * 批处理执行sqlStr，其中sqlStr字符串中出现的?表示需要替换的量，替换?形成不同的语句，
	 * 
	 * @param sqlStr
	 *            sql字符串，可以含有?作为替换对象
	 * @param paras
	 *            String[]参数第一个为整型，后面的均为String。否则可能会出错
	 * @throws SQLException
	 */
	public void execuPatch(String sqlStr, List<String[]> paras)
			throws SQLException {
		conn.setAutoCommit(false);

		PreparedStatement pstat = conn.prepareStatement(sqlStr);
		for (String[] para : paras) {
			pstat.setLong(1, Integer.parseInt(para[0]));
			for (int i = 1; i < para.length; i++) {
				pstat.setString(i + 1, para[1]);
			}
			pstat.addBatch();
		}

		pstat.executeBatch();
		conn.commit();
		pstat.close();

		conn.setAutoCommit(true);
	}

	/**
	 * 关闭数据库
	 */
	public void close() {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pstm = null;
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}
}