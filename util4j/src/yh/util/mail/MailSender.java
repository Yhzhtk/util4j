package yh.util.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 邮件验证器，验证用户名密码
 * 
 * @author yh 2011-12-19 10:10
 */
class MailAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MailAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	/**
	 * 获取密码验证器
	 */
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}

/**
 * 发送邮件类 使用示例： MailBean mailInfo = new MailBean(); mailInfo.setMailServerHost("smtp.163.cn");
 * mailInfo.setMailServerPort("25"); mailInfo.setValidate(true); mailInfo.setUserName("xx@163.cn");
 * mailInfo.setPassword("****"); mailInfo.setFromAddress("xx@163.com");
 * mailInfo.setToAddress("yy@163.cn;xx@163.cn");
 * 
 * mailInfo.setSubject("主题"); mailInfo.setContent("邮件正文");
 * 
 * // 发送邮件 if(MailSender.sendTextMail(mailInfo)) System.out.println("发送邮件成功！"); else System.out.println("发送邮件失败！");
 * 
 * @author yh 2011-12-19 10:10
 */
public class MailSender {

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailBean mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();

		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);

			// 创建邮件的接收者地址，并设置到邮件消息中
			String[] toStr = mailInfo.getToAddress().split(";");
			Address[] tos = new InternetAddress[toStr.length];
			for (int i = 0; i < toStr.length; i++) {
				tos[i] = new InternetAddress(toStr[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, tos);

			// 设置邮件消息的主题
			String sub = mailInfo.getSubject();
			mailMessage.setSubject(sub);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// 添加邮件附件
			Multipart mp = new MimeMultipart();
			String fileNames[] = mailInfo.getAttachFileNames();
			MimeBodyPart mbp = null;
			if (fileNames != null && fileNames.length > 0) {
				for (int i = 0; i < fileNames.length; i++) {
					mbp = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(fileNames[i]);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName(),
								"UTF-8", "B"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}
			}
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mbp = new MimeBodyPart();  
            mbp.setContent(mailContent, "text/plain;charset=UTF-8");  
            mp.addBodyPart(mbp); 
			mailMessage.setContent(mp);

			// 发送邮件
			Transport.send(mailMessage);

			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以html的形式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @return 发送是否成功
	 */
	public static boolean sendHtmlMail(MailBean mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);

			// 创建邮件的接收者地址，并设置到邮件消息中
			String[] toStr = mailInfo.getToAddress().split(";");
			Address[] tos = new InternetAddress[toStr.length];
			for (int i = 0; i < toStr.length; i++) {
				tos[i] = new InternetAddress(toStr[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, tos);

			// 设置邮件消息的主题
			String sub = mailInfo.getSubject();
			mailMessage.setSubject(sub);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// 添加邮件附件
			Multipart mp = new MimeMultipart();
			String fileNames[] = mailInfo.getAttachFileNames();
			MimeBodyPart mbp = null;
			if (fileNames != null && fileNames.length > 0) {
				for (int i = 0; i < fileNames.length; i++) {
					mbp = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(fileNames[i]);
					mbp.setDataHandler(new DataHandler(fds));
					try {
						mbp.setFileName(MimeUtility.encodeText(fds.getName(),
								"UTF-8", "B"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mp.addBodyPart(mbp);
				}
			}
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mbp = new MimeBodyPart();  
            mbp.setContent(mailContent, "text/html;charset=UTF-8");  
            mp.addBodyPart(mbp); 
			mailMessage.setContent(mp);
			
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
