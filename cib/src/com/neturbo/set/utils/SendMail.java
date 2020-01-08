package com.neturbo.set.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.neturbo.set.core.*;

public class SendMail {

    private MimeMessage mimeMsg; // MIME�ʼ�����

    private Session session; // �ʼ��Ự����

    private Properties props; // ϵͳ����

    private String username = ""; // smtp��֤�û��������

    private String password = "";

    private String defaultCharset = ""; // ȱʡCharset

    private Multipart mp; // Multipart����,�ʼ�����,����,���������ݾ���ӵ����к������MimeMessage����

    private static SendMail SystemMailProcessor; //

    public static SendMail getSystemMailProcessor() {
        /*
        if (SystemMailProcessor != null) {
            SystemMailProcessor.createMimeMessage();
            return SystemMailProcessor;
        }
*/
        String strSMTP = Config.getProperty("SMTPServer");
        String strUserName = Config.getProperty("SMTPUserName");
        String strPassword = Config.getProperty("SMTPPassword");
        String strDefaultFromName = Config.getProperty("DefaultFromName");
        String strDefaultCharSet = Config.getProperty("DefaultCharSet");
        String strSMTPNeedAuth = Config.getProperty("SMTPNeedAuth");
        boolean boolSMTPNeedAuth = ("true".equals(strSMTPNeedAuth) || "yes"
                                    .equals(strSMTPNeedAuth));

        SystemMailProcessor = new SendMail(strSMTP, boolSMTPNeedAuth);
        SystemMailProcessor.setNamePass(strUserName, strPassword);
        SystemMailProcessor.setFrom(strDefaultFromName);
        SystemMailProcessor.setDefaultCharset(strDefaultCharSet);

        return SystemMailProcessor;
    }

    public SendMail(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    public SendMail(String smtp, boolean authFlag) {
        setSmtpHost(smtp);
        createMimeMessage();
        setNeedAuth(authFlag);
    }

    /**
     * @param hostName
     *            String
     */
    public void setSmtpHost(String hostName) {
        if (props == null) {
            props = System.getProperties(); // ���ϵͳ���Զ���
        }
        props.put("mail.smtp.host", hostName); // ����SMTP����
    }

    /**
     * @return boolean
     */
    public boolean createMimeMessage() {
        try {
            session = Session.getDefaultInstance(props, null); // ����ʼ��Ự����
        } catch (Exception e) {
            Log.error("Error Creating Mime Message", e);
            return false;
        }

        try {
            mimeMsg = new MimeMessage(session); // ����MIME�ʼ�����
            mp = new MimeMultipart();

            return true;
        } catch (Exception e) {
            Log.error("Create Mime Message Failed", e);
            return false;
        }
    }

    /**
     * @param need
     *            boolean
     */
    public void setNeedAuth(boolean need) {
        if (props == null) {
            props = System.getProperties();
        }
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
     * @param name
     *            String
     * @param pass
     *            String
     */
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    /**
     * @param mailSubject
     *            String
     * @return boolean
     */
    public boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject, defaultCharset);
            return true;
        } catch (Exception e) {
            Log.error("Error setting mail subject", e);
            return false;
        }
    }

    /**
     * @param mailBody
     *            String
     */
    public boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent(
                    "<meta http-equiv=Content-Type content=text/html; charset="
                    + defaultCharset + ">" + mailBody,
                    "text/html;charset=" + defaultCharset);
            mp.addBodyPart(bp);

            return true;
        } catch (Exception e) {
            Log.error("Error setting mail body", e);
            return false;
        }
    }

    /**
     * @param name
     *            String
     * @param pass
     *            String
     */
    public boolean addFileAffix(String filename) {

        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());

            mp.addBodyPart(bp);

            return true;
        } catch (Exception e) {
            Log.error("Error adding mail attachment", e);
            return false;
        }
    }

    /**
     * ���÷�����
     *
     * @param name
     *            String
     * @param pass
     *            String
     */
    public boolean setFrom(String from) {
        try {
            mimeMsg.setFrom(new InternetAddress(from)); // ���÷�����
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param name
     *            String
     * @param pass
     *            String
     */
    public boolean setTo(String to) {
        if (to == null) {
            return false;
        }

        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress
                                  .parse(to));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * @param name
     *            String
     * @param pass
     *            String
     */
    public boolean setCopyTo(String copyto) {
        if (copyto == null) {
            return false;
        }
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC,
                                  (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param name
     *            String
     * @param pass
     *            String
     */
    public boolean send() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username,
                              password);
            transport.sendMessage(mimeMsg, mimeMsg
                                  .getRecipients(Message.RecipientType.TO));
            // transport.send(mimeMsg);

            transport.close();

            return true;
        } catch (Exception e) {
            Log.error("Send mail failed", e);
            return false;
        }
    }

    /**
     * Just do it as this
     */
    public static void main(String[] args) {

        String mailbody =
                "<meta http-equiv=Content-Type content=text/html; charset=utf-8>"
                +
                "<div align=center><a href=http://192.168.40.110> A test mail </a></div>";

        SendMail themail = new SendMail("192.168.40.12", false);
        themail.setDefaultCharset("utf-8");

        if (!themail.setSubject("A test mail")) {
            return;
        }
        if (!themail.setBody(mailbody)) {
            return;
        }
        if (!themail.setTo("felix@[192.168.40.12]")) {
            return;
        }
        if (!themail.setFrom("bankonline@bank.com.mo")) {
            return;
        }

        if (!themail.send()) {
            return;
        }
    }
}
