package org.mvnsearch.wx.servlet;

import org.apache.commons.codec.digest.DigestUtils;
import org.mvnsearch.wx.WeixinMessage;
import org.mvnsearch.wx.rewrite.Conf;
import org.mvnsearch.wx.rewrite.RewrittenUrl;
import org.mvnsearch.wx.rewrite.UrlRewriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * weixin robot servlet
 *
 * @author linux_china
 */
public class WexinRobotServlet extends HttpServlet {
    /**
     * token
     */
    private String token;
    /**
     * url rewriter
     */
    private UrlRewriter urlRewriter;

    /**
     * init config
     *
     * @param config servlet config
     * @throws ServletException servlet exception
     */
    public void init(ServletConfig config) throws ServletException {
        this.token = config.getInitParameter("token");
        String confFile = config.getInitParameter("confFile");
        try {
            Conf conf = new Conf(config.getServletContext(), confFile);
            this.urlRewriter = new UrlRewriter(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信公共平台认证接口
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException servlet exception
     * @throws IOException      io exception
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String echostr = request.getParameter("echostr");
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (checkSignature(request.getQueryString(), token)) {
            out.print(echostr);
        } else {
            out.print("false");
        }
        out.flush();
    }

    /**
     * 微信消息接收接口
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @throws ServletException servlet exception
     * @throws IOException      io exception
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //validate signature
        boolean valid = checkSignature(request.getQueryString(), token);
        if (valid) {
            try {
                WeixinMessage wxMsg = WeixinMessage.parseXML(request.getInputStream());
                WeixinMessageContext.setWeixinMessage(wxMsg);
                WeixinMessageContext.setQueryString(request.getQueryString());
                response.setContentType("text/xml; charset=UTF-8");
                request.setAttribute("wxMsg", wxMsg);
                RewrittenUrl rewrittenUrl = urlRewriter.processRequest(wxMsg, request, response);
                if (rewrittenUrl != null) {
                    rewrittenUrl.doRewrite(request, response);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                WeixinMessageContext.clear();
            }
        }
    }

    /**
     * 验证签名
     *
     * @param queryString query string
     * @return valid mark
     */
    public boolean checkSignature(String queryString, String token) {
        Map<String, String> params = new HashMap<String, String>();
        for (String pair : queryString.split("&")) {
            String[] temp = pair.split("=", 2);
            params.put(temp[0], temp[1]);
        }
        List<String> elements = new ArrayList<String>();
        elements.add(params.get("timestamp"));
        elements.add(params.get("nonce"));
        elements.add(token);
        Collections.sort(elements);
        StringBuilder seed = new StringBuilder();
        for (String link : elements) {
            seed.append(link);
        }
        return DigestUtils.sha1Hex(seed.toString()).equals(params.get("signature"));
    }
}
