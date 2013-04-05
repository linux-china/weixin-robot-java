package org.mvnsearch.wx;

import junit.framework.TestCase;

/**
 * weixin message test case
 *
 * @author linux_china
 */
public class WeixinMessageTest extends TestCase {

    /**
     * test to prse xml
     *
     * @throws Exception exception
     */
    public void testParseXML() throws Exception {
        String msgXml = "<xml>\n" +
                "<ToUserName><![CDATA[gh_1ad0d508f75a]]></ToUserName>\n" +
                "<FromUserName><![CDATA[o7LbXjsg_wdw9TYYzek9FOy9x4yk]]></FromUserName>\n" +
                "<CreateTime>1365073034</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[再发]]></Content>\n" +
                "<MsgId>5862944037781496076</MsgId>\n" +
                "</xml>";
        WeixinMessage msg = WeixinMessage.parseXML(msgXml);
        System.out.println(msg.getResponseXml("text", "我能帮助你什么？"));
    }
}
