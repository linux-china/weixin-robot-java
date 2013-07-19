package org.mvnsearch.wx;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

/**
 * weixin message
 *
 * @author linux_china
 */
public class WeixinMessage {
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
     * parse xml to get weixin message
     *
     * @param xmlText xml text
     * @return weixin message
     * @throws Exception exception
     */
    public static WeixinMessage parseXML(String xmlText) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlText));
        return constructMsg(document);
    }

    /**
     * parse xml to get weixin message
     *
     * @param inputStream xml text
     * @return weixin message
     * @throws Exception exception
     */
    public static WeixinMessage parseXML(InputStream inputStream) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        return constructMsg(document);
    }

    /**
     * construct weixin message from xml document
     *
     * @param document xml document
     * @return weixin message
     */
    private static WeixinMessage constructMsg(Document document) {
        Element rootElement = document.getRootElement();
        WeixinMessage msg = new WeixinMessage();
        msg.id = rootElement.getChildTextTrim("MsgId");
        msg.receiver = rootElement.getChildTextTrim("ToUserName");
        msg.sender = rootElement.getChildTextTrim("FromUserName");
        msg.type = rootElement.getChildTextTrim("MsgType");
        msg.content = rootElement.getChildTextTrim("Content");
        String createTime = rootElement.getChildTextTrim("CreateTime");
        if (createTime != null) {
            msg.createdTime = new Date(Long.valueOf(createTime) * 1000);
        }
        //event message
        if ("event".equals(msg.type)) {
            String event = rootElement.getChildText("EventKey");
            String eventKey = rootElement.getChildText("EventKey");
            msg.type = event;
            msg.content = eventKey;
        }
        return msg;
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
