package yh.util.js;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import yh.util.io.ReadTxt;

public class RunJs {

	public static void main(String[] args) {
		String js="var a=1,b=5;println(a+b);";
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("a", 2);
		map.put("c", 3);
		
		Object obj=execJs(js,map);
		System.out.println(obj);
		
		
//		Object obj=execJs(new String[]{"a.js","b.js","c.js"},"cal",2,3);
//		System.out.println(obj);
	}

	/**
	 * 运行js，绑定map参数，key及为变量
	 * @param js js内容
	 * @param map 变量
	 * @return
	 * @throws Exception
	 */
	public static Object execJs(String js, Map<String, Object> map){
		System.out.println(js);
		
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			System.out.println("Map:" + entry.getKey() + "---"+ entry.getValue());
		}

		Object obj = null;
		
		if ("".equals(js) || js == null) {
			System.out.println("Javascript content is null");
		} else if (map == null || map.size() <= 0) {
			System.out.println("Map conteng is null");
		} else {
			// 获取脚本引擎
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("javascript");
			// 绑定数据
			ScriptContext newContext = new SimpleScriptContext();
			Bindings bind = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
			bind.putAll(map);
			try {
				engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
				obj=engine.eval(js);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return obj;
	}
	
	/**
	 * 运行js的某个函数
	 * @param jsFile js文件路径
	 * @param funName 运行函数名
	 * @param args 运行函数参数
	 * @return 执行结果
	 */
	public static Object execJs(String jsFile,String funName, Object... args) {
		
		Object obj=null;
		if(!new File(jsFile).exists()){
			System.out.println("JSFile is not exist");
			return null;
		}
		
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			ScriptEngine engine = m.getEngineByName("javascript");
			if (engine != null) {
				Reader reader=new FileReader(jsFile);
				
				engine.eval(reader);
				Invocable invocableEngine = (Invocable) engine;
				obj=invocableEngine.invokeFunction(funName,args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 执行一系列js文件内的函数并返回结果
	 * @param jsFiles js文件数组
	 * @param funName 执行入口函数
	 * @param args 执行函数参数
	 * @return 结果
	 */
	public static Object execJs(String[] jsFiles,String funName, Object... args) {
		
		String jsContent="";
		
		for(String jsFile:jsFiles){
			jsContent+=ReadTxt.readTxt(jsFile,"utf-8");
		}
		
		Object obj=null;
		
		try {
			ScriptEngineManager m = new ScriptEngineManager();
			ScriptEngine engine = m.getEngineByName("javascript");
			if (engine != null) {
				engine.eval(jsContent);
				Invocable invocableEngine = (Invocable) engine;
				obj=invocableEngine.invokeFunction(funName,args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
