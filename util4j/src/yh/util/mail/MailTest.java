 /**  
 *@Description:     
 */ 
package yh.util.mail;  
  
  
/**  
 *@Description:
 *@Author:yh
 *@Date:2013-2-16
 *@Version:1.1.0
 */
public class MailTest {

	/**  
	 * @param args
	 * @Date:2013-2-16
	 * @Author:163
	 * @Description:
	 */
	public static void main(String[] args) {
		MailBean bean = new MailBean();
	
		bean.setMailServerHost("smtp.163.cn");
		bean.setMailServerPort("25");
		bean.setValidate(true);
		
		bean.setUserName("163@163.cn");
		bean.setPassword("");
		
		bean.setFromAddress("163@163.cn");
		bean.setToAddress("163@163.cn;yhzhtk@gmail.com");
		
		bean.setSubject("主题");
		bean.setContent("正文");
		bean.setAttachFileNames(new String[]{});
		
		if(MailSender.sendTextMail(bean)){
			System.out.println("发送邮件成功");
		}else{
			System.out.println("发送邮件失败 ");
		}
	}
}
