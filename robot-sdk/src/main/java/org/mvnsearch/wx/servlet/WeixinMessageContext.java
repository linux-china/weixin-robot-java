package org.mvnsearch.wx.servlet;

import org.mvnsearch.wx.WeixinMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * weixin message context
 *
 * @author linux_china
 */
@SuppressWarnings("unchecked")
public class WeixinMessageContext {
    /**
     * empty map
     */
    private static Map<String, String> emptyMap = Collections.emptyMap();
    /**
     * thread local variable
     */
    private final static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    /**
     * set query string
     *
     * @param queryString query string
     */
    public static void setQueryString(String queryString) {
        if (queryString != null && queryString.contains("=")) {
            Map<String, String> query = new HashMap<String, String>();
            for (String pair : queryString.split("&")) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2 && !parts[1].isEmpty()) {
                    query.put(parts[0], parts[1]);
                }
            }
            threadLocal.get().put("query", query);
        }
    }

    /**
     * get query
     *
     * @return query
     */
    public static Map<String, String> getQuery() {
        Map<String, String> query = (Map<String, String>) threadLocal.get().get("query");
        return query == null ? emptyMap : query;
    }

    /**
     * set weixin message
     *
     * @param message message
     */
    public static void setWeixinMessage(WeixinMessage message) {
        threadLocal.get().put("message", message);
    }

    /**
     * get weixin message
     *
     * @return weixin message
     */
    public static WeixinMessage getWeixinMessage() {
        return (WeixinMessage) threadLocal.get().get("message");
    }

    /**
     * clear context
     */
    public static void clear() {
        threadLocal.remove();
    }
}
