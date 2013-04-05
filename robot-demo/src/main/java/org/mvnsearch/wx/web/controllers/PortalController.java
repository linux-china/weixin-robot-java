package org.mvnsearch.wx.web.controllers;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.mvnsearch.wx.WeixinMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * portal controller
 *
 * @author linux_china
 */
public class PortalController extends ActionSupport implements ServletRequestAware {
    /**
     * http servlet request
     */
    HttpServletRequest request;

    /**
     * inject http servlet request
     *
     * @param httpServletRequest http servlet request
     */
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    /**
     * project front page
     *
     * @return welcome page
     */
    public String index() {
        return "index";
    }

    /**
     * 显示微信消息
     *
     * @return weixing response
     */
    public String weixin() {
        WeixinMessage wxMsg = (WeixinMessage) request.getAttribute("wxMsg");
        if (wxMsg == null) {
            wxMsg = new WeixinMessage();
            wxMsg.setSender("jacky");
            wxMsg.setReceiver("leijuan");
            wxMsg.setContent("txt");
            wxMsg.setCreatedTime(System.currentTimeMillis() / 1000);
            request.setAttribute("wxMsg", wxMsg);
        }
        request.setAttribute("content", "echo：" + wxMsg.getContent());
        return "weixin";
    }

    /**
     * 显示微信消息2
     *
     * @return weixing response
     */
    public String weixin2() {
        request.setAttribute("content", "weixin2");
        return "weixin";
    }
}
