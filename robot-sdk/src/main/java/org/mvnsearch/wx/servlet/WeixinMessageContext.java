package org.mvnsearch.wx.servlet;

import org.mvnsearch.wx.WeixinMessage;

import java.util.Map;

/**
 * weixin message context
 *
 * @author linux_china
 */
public class WeixinMessageContext {
    /**
     *
     */
    private Map<String, String> query;

    /**
     * weixin message
     */
    private WeixinMessage message;

    /**
     * construct method
     */
    public WeixinMessageContext() {

    }

    /**
     * construct method
     *
     * @param queryString query string
     * @param message     message
     */
    public WeixinMessageContext(String queryString, WeixinMessage message) {
        this.message = message;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public WeixinMessage getMessage() {
        return message;
    }

    public void setMessage(WeixinMessage message) {
        this.message = message;
    }
}
