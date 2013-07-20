package org.mvnsearch.wx;

import java.util.Date;

/**
 * weixin message
 *
 * @author linux_china
 */
public class WeixinMessage {
    /**
     * text message
     */
    public static final String type_text = "txt";
    /**
     * image message
     */
    public static final String type_image = "image";
    /**
     * location message
     */
    public static final String type_location = "location";
    /**
     * link message
     */
    public static final String type_link = "link";
    /**
     * news message
     */
    public static final String type_news = "news";
    /**
     * music message
     */
    public static final String type_music = "music";

    /**
     * subscribe message
     */
    public static final String type_subscribe = "subscribe";
    /**
     * unsubscribe message
     */
    public static final String type_unsubscribe = "unsubscribe";
    /**
     * click message
     */
    public static final String type_click = "click";
    /**
     * message id
     */
    private String id;
    /**
     * sender，微信号
     */
    private String sender;
    /**
     * receiver，微信号
     */
    private String receiver;
    /**
     * created timestamp
     */
    private Date createdTime;
    /**
     * type: text, image, location, link
     */
    private String type;
    /**
     * content, such as text, image url, link etc
     */
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedSeconds() {
        return this.createdTime.getTime() / 1000;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * fill message
     *
     * @param type    type
     * @param content content
     */
    public void fill(String type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * get reply message
     *
     * @return reply message
     */
    public WeixinMessage getReply() {
        WeixinMessage reply = new WeixinMessage();
        reply.setReceiver(this.sender);
        reply.setSender(this.receiver);
        reply.setCreatedTime(new Date());
        return reply;
    }

    /**
     * get response xml
     *
     * @param type    type
     * @param content content
     * @return response xml
     */
    public String getResponseXml(String type, String content) {
        String template = "<xml>\n" +
                "    <ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "    <FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "    <CreateTime>%s</CreateTime>\n" +
                "    <MsgType><![CDATA[%s]]></MsgType>\n" +
                "    <Content><![CDATA[%s]]></Content>\n" +
                "    <FuncFlag>0</FuncFlag>\n" +
                "</xml>";
        return String.format(template, sender, receiver, String.valueOf(System.currentTimeMillis() / 1000), type, content);
    }

}
