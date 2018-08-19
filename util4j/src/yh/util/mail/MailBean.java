package yh.util.mail;

import java.util.Properties;

/**
 * 邮件信息类，存放发送邮件的信息
 * @author yh
 * 2011-12-20 10:27
 */
public class MailBean {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "25";
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String toAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private String[] attachFileNames;

	/**
	 * 获得邮件会话属性
	 * @return 
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	/**
	 * 获取邮件服务主机名或者IP
	 * @return
	 */
	public String getMailServerHost() {
		return mailServerHost;
	}

	/**
	 * 设置邮件服务器
	 * @param mailServerHost
	 */
	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	/**
	 * 获得邮件服务器端口
	 * @return
	 */
	public String getMailServerPort() {
		return mailServerPort;
	}

	/**
	 * 设置邮件服务器端口
	 * @param mailServerPort
	 */
	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	/**
	 * 获取是否需要身份验证
	 * @return
	 */
	public boolean isValidate() {
		return validate;
	}

	/**
	 * 设置是否身份验证
	 * @param validate
	 */
	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	/**
	 * 获取附件文件名集
	 * @return
	 */
	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	/**
	 * 设置附件文件名集
	 * @param fileNames
	 */
	public void setAttachFileNames(String[] fileNames) {
		this.attachFileNames = fileNames;
	}

	/**
	 * 获取邮件发送地址
	 * @return
	 */
	public String getFromAddress() {
		return fromAddress;
	}

	/**
	 * 设置邮件发送者地址
	 * @param fromAddress
	 */
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * 获取邮件发送者密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置邮件发送者密码
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取邮件接受者地址
	 * @return
	 */
	public String getToAddress() {
		return toAddress;
	}

	/**
	 * 设置邮件接受者地址
	 * @param toAddress
	 */
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	/**
	 * 获取登录发送邮件服务器的用户名
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置登录邮件发送服务器的用户名
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取邮件主题
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置邮件主题
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 获取邮件内容
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置邮件内容
	 * @param textContent
	 */
	public void setContent(String textContent) {
		this.content = textContent;
	}
}