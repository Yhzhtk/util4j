package yh.db;  

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 使用C3P0连接池获取数据源
 *@author:gudaihui
 *@date:2013-4-16
 */
public class C3P0Util {
	
	private static String namedConfig;
	
	private static DataSource dataSource;
	
	private C3P0Util(){
	}
	
	/**
	 * 获取数据源，如果未设置namedConfig或者namedConfig不存在，则使用默认配置
	 * @return
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static DataSource getDataSource(){
		if(dataSource == null){
			if(namedConfig == null){
				dataSource = new ComboPooledDataSource();
			}else{
				dataSource = new ComboPooledDataSource(namedConfig);
			}
		}
		return dataSource;
	}

	public static String getNamedConfig() {
		return namedConfig;
	}

	/**
	 * 设置数据源名称，仅在getDataSource()之前有效
	 * @param namedConfig
	 * @date:2013-4-16
	 * @author:gudaihui
	 */
	public static void setNamedConfig(String namedConfig) {
		C3P0Util.namedConfig = namedConfig;
	}
}
