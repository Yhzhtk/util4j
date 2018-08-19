package yh.util.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 运行cmd类
 * @author yh
 * 2012-4-5 11:19
 */
public class RunCmd {

	public static void main(String[] args) {
		
		String[] cmds=new String[]{"getmac","/?"};
		
		String res=executeCmds(cmds);
		
		System.out.println(res);
	}

	/**
	 * 执行cmd
	 * @param cmds 命令及相关参数
	 * @return 返回显示结果
	 */
	private static String executeCmds(String[] cmds) {

		List<String> commend = new java.util.ArrayList<String>();
		
		for(String cmd:cmds){
		commend.add(cmd);
		}
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			Process p = builder.start();

			BufferedReader buf = null;
			String line = null;

			buf = new BufferedReader(new InputStreamReader(p.getInputStream(),"gbk"));
			StringBuilder sbu = new StringBuilder();
			while ((line = buf.readLine()) != null) {
				sbu.append(line);
				sbu.append("\n");
			}
			//等待运行完成
			p.waitFor();
			
			String res= sbu.toString();
			return res;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
