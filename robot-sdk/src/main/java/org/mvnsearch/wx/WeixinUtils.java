package org.mvnsearch.wx;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

/**
 * weixin utils
 *
 * @author linux_china
 */
public class WeixinUtils {

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
        msg.setId(rootElement.getChildTextTrim("MsgId"));
        msg.setReceiver(rootElement.getChildTextTrim("ToUserName"));
        msg.setSender(rootElement.getChildTextTrim("FromUserName"));
        msg.setType(rootElement.getChildTextTrim("MsgType"));
        msg.setContent(rootElement.getChildTextTrim("Content"));
        String createTime = rootElement.getChildTextTrim("CreateTime");
        if (createTime != null) {
            msg.setCreatedTime(new Date(Long.valueOf(createTime) * 1000));
        }
        //event message
        if ("event".equals(msg.getType())) {
            String event = rootElement.getChildText("EventKey");
            String eventKey = rootElement.getChildText("EventKey");
            msg.setType(event);
            msg.setContent(eventKey);
        }
        return msg;
    }
}
