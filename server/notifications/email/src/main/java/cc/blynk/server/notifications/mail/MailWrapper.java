package cc.blynk.server.notifications.mail;

import cc.blynk.utils.FileLoaderUtil;
import cc.blynk.utils.properties.MailProperties;
import cc.blynk.utils.properties.Placeholders;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 06.04.15.
 */
public class MailWrapper {

    private final MailClient client;
    private final String reportBody;
    private final String productName;

    public MailWrapper(MailProperties mailProperties, String productName) {
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.setProperty("mail.smtp.ssl.enable", "true");
        client = new ThirdPartyMailClient(mailProperties, productName);

        this.reportBody = FileLoaderUtil.readReportEmailTemplate();
        this.productName = productName;
    }

    public void sendReportEmail(String to,
                                String subj,
                                String downloadUrl,
                                String dynamicSection) throws Exception  {
        String body = reportBody
                .replace(Placeholders.DOWNLOAD_URL, downloadUrl)
                .replace(Placeholders.DYNAMIC_SECTION, dynamicSection)
                .replace(Placeholders.PRODUCT_NAME, productName);
        sendHtml(to, subj, body);
    }

    public void sendText(String to, String subj, String body) throws Exception {
        client.sendText(to, subj, body);
    }

    public void sendHtml(String to, String subj, String body) throws Exception {
        client.sendHtml(to, subj, body);
    }

    public void sendWithAttachment(String to, String subj, String body, QrHolder attachment) throws Exception {
        client.sendHtmlWithAttachment(to, subj, body, new QrHolder[] {attachment});
    }

    public void sendWithAttachment(String to, String subj, String body, QrHolder[] attachments) throws Exception {
        client.sendHtmlWithAttachment(to, subj, body, attachments);
    }

}
