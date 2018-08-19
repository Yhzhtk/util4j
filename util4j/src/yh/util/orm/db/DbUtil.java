package yh.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 数据库访问的公共方法提取，包含获取插入数据时用到的对象。<br>
 * 如果单独获取的话注意获取参数和插入语句的对应
 *@author:gudaihui
 *@date:2013-4-17
 */
public class DbUtil {

	/**
	 * 仅获取一个对象的插入语句，注意参数和外部的sql应该一致
	 * @param tableName
	 * @param bean
	 * @param filter
	 * @return
	 * @date:2013-4-18
	 * @author:gudaihui
	 */
	public static String getInsertSql(String tableName, Class<?> clazz, Set<String> filter) {
		Field[] fields = clazz.getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		StringBuffer a = new StringBuffer();
		StringBuffer d = new StringBuffer();
		for (Field f : fields) {
			if(filter != null && filter.contains(f.getName())){
				continue;
			}
			a.append(",`");
			a.append(f.getName());
			a.append("`");
			d.append(",?");
		}
		String sql = "insert into `" + tableName + "` (" + a.toString().substring(1) + ") values ("
				+ d.toString().substring(1) + ")";
		return sql;
	}
	
	/**
	 * 获取更新的sql语句，注意参数和外部的sql应该一致
	 * @param tableName
	 * @param clazz
	 * @param filter
	 * @return
	 * @date:2013-4-23
	 * @author:gudaihui
	 */
	public static String getUpdateSql(String tableName, Class<?> clazz, Set<String> filter) {
		Field[] fields = clazz.getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		StringBuffer a = new StringBuffer();
		for (Field f : fields) {
			if(filter != null && filter.contains(f.getName())){
				continue;
			}
			a.append(",`");
			a.append(f.getName());
			a.append("` = ?");
		}
		String sql = "update `" + tableName + "` set (" + a.toString().substring(1) + ")";
		return sql;
	}
	
	/**
	 * 获取一个对象的过滤后参数，其中的sql为空，注意参数和外部的sql应该一致
	 * @param tableName
	 * @param bean
	 * @param filter
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date:2013-4-18
	 * @author:gudaihui
	 */
	public static SqlParasObj getParaObject(String tableName, Object bean, Set<String> filter)
			throws IllegalArgumentException, IllegalAccessException {
		List<Object> objs = new ArrayList<Object>();
		Field[] fields = bean.getClass().getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		for (Field f : fields) {
			if(filter != null && filter.contains(f.getName())){
				continue;
			}
			objs.add(f.get(bean));
		}
		return new SqlParasObj(null, objs.toArray(new Object[]{}));
	}
	
	/**
	 * 获取多个对象的过滤后参数集，其中sql为空，，注意参数和外部的sql应该一致
	 * @param tableName
	 * @param beans
	 * @param filter
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date:2013-4-18
	 * @author:gudaihui
	 */
	public static SqlParasObj getParaObjects(String tableName, List<?> beans, Set<String> filter)
			throws IllegalArgumentException, IllegalAccessException {
		List<Object[]> paras = new ArrayList<Object[]>();
		Field[] fields = beans.get(0).getClass().getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		for(Object bean : beans){
			List<Object> objs = new ArrayList<Object>();
			for (Field f : fields) {
				if(filter != null && filter.contains(f.getName())){
					continue;
				}
				objs.add(f.get(bean));
			}
			paras.add(objs.toArray(new Object[]{}));
		}
		
		return new SqlParasObj(null, paras);
	}
	
	/**
	 * 获取一个bean的插入对象，包含插入的sql和参数
	 * @param tableName
	 * @param bean
	 * @param filter 过滤的属性，null表示不过滤
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date:2013-4-17
	 * @author:gudaihui
	 */
	public static SqlParasObj getInsertObject(String tableName, Object bean, Set<String> filter)
			throws IllegalArgumentException, IllegalAccessException {
		List<Object> objs = new ArrayList<Object>();
		Field[] fields = bean.getClass().getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		StringBuffer a = new StringBuffer();
		StringBuffer d = new StringBuffer();
		for (Field f : fields) {
			if(filter != null && filter.contains(f.getName())){
				continue;
			}
			a.append(",`");
			a.append(f.getName());
			a.append("`");
			d.append(",?");
			objs.add(f.get(bean));
		}
		String sql = "insert into `" + tableName + "` (" + a.toString().substring(1) + ") values ("
				+ d.toString().substring(1) + ")";
		return new SqlParasObj(sql, objs.toArray(new Object[]{}));
	}
	
	/**
	 * 获取多个bean的插入对象，包含插入的sql和参数
	 * @param tableName
	 * @param beans
	 * @param filter 过滤的属性，null表示不过滤
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @date:2013-4-17
	 * @author:gudaihui
	 */
	public static SqlParasObj getInsertObjects(String tableName, List<?> beans, Set<String> filter)
			throws IllegalArgumentException, IllegalAccessException {
		List<Object[]> paras = new ArrayList<Object[]>();
		Field[] fields = beans.get(0).getClass().getDeclaredFields();
		Field.setAccessible(fields, true); 
		
		StringBuffer a = new StringBuffer();
		StringBuffer d = new StringBuffer();
		String sql = null;
		for(Object bean : beans){
			List<Object> objs = new ArrayList<Object>();
			for (Field f : fields) {
				if(filter != null && filter.contains(f.getName())){
					continue;
				}
				a.append(",`");
				a.append(f.getName());
				a.append("`");
				d.append(",?");
				objs.add(f.get(bean));
			}
			paras.add(objs.toArray(new Object[]{}));
			if(sql == null){
				sql = "insert into `" + tableName + "` (" + a.toString().substring(1) + ") values ("
						+ d.toString().substring(1) + ")";
			}
		}
		
		return new SqlParasObj(sql, paras);
	}
	
	/**
	 * 存储数据库操作的sql和参数
	 *@author:gudaihui
	 *@date:2013-4-17
	 */
	public static class SqlParasObj {
		public String insertSql;
		public List<Object[]> paras;

		public SqlParasObj(String insertSql, Object[] para) {
			this.insertSql = insertSql;
			this.paras = new ArrayList<Object[]>();
			this.paras.add(para);
		}
		
		public SqlParasObj(String insertSql, List<Object[]> paras) {
			this.insertSql = insertSql;
			this.paras = paras;
		}
	}
}
