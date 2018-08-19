package yh.db;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import yh.db.DbManager;
import yh.db.DbUtil;
import yh.db.DbUtil.SqlParasObj;

/**
 * 数据库和实体映射 使用时复制该类。<br>
 * 修改tableName, 将所有的TempBean替换为对应的实体类, 根据需求设置需要过滤的属性字段。
 * 
 * @author:gudaihui
 * @date:2013-4-16
 */
public class TempOrm {

	// 使用时可以注释掉，将所有的TempBean换成对应的实体类
	static class TempBean {
	}

	private static boolean needFilter;
	private static String tableName;
	private static HashSet<String> filterFields;

	/**
	 * 初始化操作表名和需要过滤的字段
	 */
	static {
		// 修改表名
		tableName = "CaipuBean";
		// 使用时根据是否需要过滤设置，设置过滤注意添加过滤的字段
		needFilter = true;
		if (needFilter) {
			filterFields = new HashSet<String>();
			// 使用时按此规则添加需要过滤的属性名称，注意大小写一致
			filterFields.add("serialVersionUID");
		}
	}

	public static TempBean getTempBean(String sql) throws SQLException {
		ResultSetHandler<?> rsh = new BeanHandler<TempBean>(TempBean.class);
		TempBean bean = (TempBean) DbManager.getObject(sql, rsh);
		return bean;
	}

	public static List<TempBean> getTempBeans(String sql) throws SQLException {
		ResultSetHandler<?> rsh = new BeanListHandler<TempBean>(TempBean.class);
		@SuppressWarnings("unchecked")
		List<TempBean> bean = (List<TempBean>) DbManager.getObject(sql, rsh);
		return bean;
	}

	public static int insertTempBean(TempBean bean) throws SQLException,
			IllegalArgumentException, IllegalAccessException {
		SqlParasObj t = DbUtil.getInsertObject(tableName, bean, filterFields);
		int n = DbManager.update(t.insertSql, t.paras.get(0));
		return n;
	}

	public static int[] insertTempBean(List<TempBean> beans)
			throws SQLException, IllegalArgumentException,
			IllegalAccessException {
		SqlParasObj t = DbUtil.getInsertObjects(tableName, beans, filterFields);
		Object[][] p = t.paras.toArray(new Object[][] {});
		int[] n = DbManager.batch(t.insertSql, p);
		return n;
	}
}
