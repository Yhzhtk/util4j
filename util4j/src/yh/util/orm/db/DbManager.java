package yh.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 使用DbUtils简化数据库操作流程 默认使用C3P0连接池
 * 
 * @author:gudaihui
 * @date:2013-4-16
 */
public class DbManager {

//	QueryRunner run = new QueryRunner(C3P0Util.getDataSource());
//	ResultSetHandler<List<Bean>> h = new BeanListHandler<Bean>(Bean.class);
//	List<Bean> bs = run.query("SELECT * FROM s WHERE name < ?", h, 100);
	
	/**
	 * 获取默认的Handler
	 * 
	 * @return
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static ResultSetHandler<Object[]> getResultSetHandler() {
		ResultSetHandler<Object[]> h = new ResultSetHandler<Object[]>() {
			public Object[] handle(ResultSet rs) throws SQLException {
				if (!rs.next()) {
					return null;
				}
				ResultSetMetaData meta = rs.getMetaData();
				int cols = meta.getColumnCount();
				Object[] result = new Object[cols];
				for (int i = 0; i < cols; i++) {
					result[i] = rs.getObject(i + 1);
				}
				return result;
			}
		};

		return h;
	}

	/**
	 * 无参数查询SQL，返回rsh映射的类型
	 * 
	 * @param sql
	 * @param rsh
	 *            类型Handler
	 * @return
	 * @throws SQLException
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static Object getObject(String sql, ResultSetHandler<?> rsh)
			throws SQLException {
		QueryRunner run = new QueryRunner(C3P0Util.getDataSource());
		Object bs = run.query(sql, rsh);
		return bs;
	}

	/**
	 * 执行修改或插入
	 * 
	 * @param sql
	 * @param paras
	 * @return
	 * @throws SQLException
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static int update(String sql, Object[] paras) throws SQLException {
		QueryRunner run = new QueryRunner(C3P0Util.getDataSource());
		int n = run.update(sql, paras);
		return n;
	}

	/**
	 * 批量执行
	 * 
	 * @param sql
	 * @param paras
	 * @return
	 * @throws SQLException
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static int[] batch(String sql, Object[][] paras) throws SQLException {
		QueryRunner run = new QueryRunner(C3P0Util.getDataSource());
		int[] n = run.batch(sql, paras);
		return n;
	}

	/**
	 * 以一个Object集合存储数据
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static Object[] getResultSetObject(String sql) throws SQLException {
		QueryRunner run = new QueryRunner(C3P0Util.getDataSource());
		Object[] objs = run.query(sql, getResultSetHandler());
		return objs;
	}
}
