package yh.util.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import net.sf.json.JSONArray;

/**
 * 需要jar包
 * jakarta commons-lang 2.5
jakarta commons-beanutils 1.8.0
jakarta commons-collections 3.2.1
jakarta commons-logging 1.1.1
ezmorph 1.0.6
json-lib-2.3-jdk15.jar
 * @author yh
 *
 */
public class JsonParse {

	public static void main(String[] args) {
		String str = PostEasouSource("531116","5118642","1","UTF-8");
		System.out.println(str);
		
		JSONArray json = JSONArray.fromObject(str);
		System.out.println(json);
		
//		JSONArray obj = json.getJSONArray("str");
		System.out.println(json.get(0));
	}

	public static String PostEasouSource(String gid,String nid,String pageNum,String charset){
		String content = null;
		String cp = (Integer.parseInt(pageNum)-1)+ "";
		String url = "http://book.easou.com/w/commentList/" + gid + "_" + nid + "_" + cp + "_" + pageNum + ".html?wver=dsp";
		HttpClient http = new HttpClient();
		PostMethod getMethod = new PostMethod(url);
		getMethod.setParameter("gid", gid);
		getMethod.setParameter("nid", nid);
		getMethod.setParameter("cp", cp);
		getMethod.setParameter("p", pageNum);
		try {
			int statu = http.executeMethod(getMethod);
			GZIPInputStream gzin = null;
			BufferedReader bf = null;
			if(statu == HttpStatus.SC_OK){
				InputStream in = getMethod.getResponseBodyAsStream();
				if(getMethod.getResponseHeader("Content-Encoding") != null && (getMethod.getResponseHeader("Content-Encoding")).getValue().equalsIgnoreCase("gzip")){
					gzin = new GZIPInputStream(in);  
					bf = new BufferedReader(new InputStreamReader(gzin,getMethod.getRequestCharSet()));
				}else{
					bf = new BufferedReader(new InputStreamReader(in,getMethod.getRequestCharSet()));
				}
				StringBuffer sb = new StringBuffer();
				char ch[] = new char[1024];
				int len;
				while((len = bf.read(ch))>0){
					
					sb.append(ch,0,len);
				}	
				
				String body = sb.toString();
				body = new String(body.getBytes("iso-8859-1"),charset);				
				bf.close();
				
				return body;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
		return content;
	}
}
