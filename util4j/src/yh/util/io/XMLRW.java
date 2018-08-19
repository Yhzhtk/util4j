package yh.util.io;

import java.io.*;
import java.util.*;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;


/**
 * 读写xml类，所给函数均为例子，根据不同情况改造
 * @author yh
 * 时间：2012-3-13 16:08
 * 
 */
public class XMLRW {

	//SAX读
	private SAXReader reader;
	//xml文档
	private Document document;
	private File xmlFile;

	/**
	 * 获取xml的文档document
	 * 
	 * @param path
	 *            xml文件路径名
	 * @return
	 */
	public Document readFrontXml(String path) {
		xmlFile = new File(path);
		reader = new SAXReader();
		try {
			document = reader.read(xmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 示例代码，根据不同xml改变
	 * 
	 * @param fileName
	 *            xml文件路径名
	 * @return
	 * @throws Exception
	 */
	public String ReadXml(String fileName) throws Exception {
		xmlFile = new File(fileName);
		reader = new SAXReader();
		try {
			document = reader.read(xmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		/*
		 * Document document=new SAXReader.reader(“xml文路径/文件名xxx.xml”);//得到Document对象
		 * Element root = document.getRootElement()//获得根节点
		 * Iterator iterator=root.elementIterator(); //从根节点遍历子节点
		 * Iterator iterator=Element.elementIterator(); //再从子节点在遍历其子节点
		 * 对节点访问其属性用：Attribute leaderAttr =Element. attribute(“xxx”)
		 * 对节点访问其某个属性leaderAttr的名称：leaderAttr.getName()；
		 * 对节点访问其某个属性leaderAttr的值：leaderAttr.getValue()
		 * 对节点访问其名称：Element.getName();
		 * 对节点访问其文本：Element. getText();
		 * 
		 * */

		String readStr = null;

		// 获取根节点
		Element root = document.getRootElement();

		// 选择名为channel下的所有节点
		List<?> list = root.selectNodes("channel");
		for (Object obj : list) {
			// 转换元素
			Element element = (Element) obj;
			
			//选择channel下节点的属性
			Attribute attr =element.attribute("xx");
			readStr=attr.getText();
			
			// 选择channel下的节点id
			Node node2 = element.selectSingleNode("id");
			// 获取节点内容
			readStr = node2.getText();

			// 嵌套操作
			List<?> list1 = element.selectNodes("data-source");
			for (Object obj1 : list1) {
				Element element1 = (Element) obj1;
				Node node7 = element1.selectSingleNode("data-sourceparseid");
				readStr = node7.getText();
			}
		}
		return readStr;
	}

	/**
	 * 写xml文件
	 * 
	 * @param fileName
	 *            xml文件名
	 */
	public static void WirteXml(String fileName) {
		Document doc = DocumentHelper.createDocument();
		Element rootElement = doc.addElement("GameNames");

		String content = "内容";

		// 穿件rootElement的子节点
		Element game = rootElement.addElement("name");
		// 添加节点内容
		game.setText(content);

		// 写入xml
		try {
			OutputFormat format = new OutputFormat("  ", true);
			format.setEncoding("utf8");
			File file = new File(fileName);

			// 创建文件
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();

			Writer wr = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf8"));

			// 将doc的数据写入wr中
			doc.write(wr);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}