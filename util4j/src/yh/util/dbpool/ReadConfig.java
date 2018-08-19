package yh.util.dbpool;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 读取xml的数据库配置文件
 * @author yh
 * 创建时间：2012-05-10
 */
public class ReadConfig {

	public Vector<ConfigBean> readConfigInfo(String path) {
		// SAX读
		SAXReader reader;
		// xml文档
		Document doc = null;
		File xmlFile;

		xmlFile = new File(path);
		reader = new SAXReader();
		try {
			doc = reader.read(xmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		Vector<ConfigBean> dsConfig=new Vector<ConfigBean>(); 
		Element root = doc.getRootElement();
		List<?> pools = root.selectNodes("pool");
		Element pool = null;
		Iterator<?> allPool = pools.iterator();
		while (allPool.hasNext()) {
			pool = (Element) allPool.next();
			ConfigBean dscBean = new ConfigBean();
			dscBean.setType(pool.selectSingleNode("type").getText());
			dscBean.setName(pool.selectSingleNode("name").getText());
			System.out.println(dscBean.getName());
			dscBean.setDriver(pool.selectSingleNode("driver").getText());
			dscBean.setUrl(pool.selectSingleNode("url").getText());
			dscBean.setUsername(pool.selectSingleNode("username").getText());
			dscBean.setPassword(pool.selectSingleNode("password").getText());
			dscBean.setMaxconn(Integer.parseInt(pool
					.selectSingleNode("maxconn").getText()));
			dsConfig.add(dscBean);
		}
		return dsConfig;
	}
}