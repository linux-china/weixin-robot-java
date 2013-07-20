package org.mvnsearch.wx.servlet;

import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Nullable;
import org.mvnsearch.wx.WeixinMessage;
import org.mvnsearch.wx.WeixinUtils;
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
     * pass code，用于调试，不执行签名检查
     */
    private String passCode;
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
        this.passCode = config.getInitParameter("passCode");
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
        if (checkSignature(request.getQueryString(), getToken())) {
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
        boolean valid = checkSignature(request.getQueryString(), getToken());
        if (valid) {
            try {
                WeixinMessage wxMsg = WeixinUtils.parseXML(request.getInputStream());
                doMessage(wxMsg, request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                WeixinMessageContext.clear();
            }
        } else {
            send400(response);
        }
    }

    /**
     * 处理 weixin message
     *
     * @param wxMsg    weixin message
     * @param request  http servlet request
     * @param response http servlet response
     * @throws Exception exception
     */
    public void doMessage(WeixinMessage wxMsg, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WeixinMessageContext.setWeixinMessage(wxMsg);
        WeixinMessageContext.setQueryString(request.getQueryString());
        response.setContentType("text/xml; charset=UTF-8");
        request.setAttribute("wxMsg", wxMsg);
        RewrittenUrl rewrittenUrl = urlRewriter.processRequest(wxMsg, request, response);
        if (rewrittenUrl != null) {
            rewrittenUrl.doRewrite(request, response);
        }
    }

    /**
     * 验证签名
     *
     * @param queryString query string
     * @return valid mark
     */
    public boolean checkSignature(String queryString, @Nullable String token) {
        if (token == null || token.isEmpty() || queryString == null) return false;
        //pass code check
        if (passCode != null || queryString.contains(passCode)) return true;
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

    /**
     * get weixin call back token
     *
     * @return token
     */
    @Nullable
    protected String getToken() {
        return this.token;
    }

    /**
     * get query   from query string
     *
     * @param request http servlet request
     * @return query object
     */
    protected Map<String, String> getQuery(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString == null || !queryString.contains("=")) {
            return Collections.emptyMap();
        }
        Map<String, String> query = new HashMap<String, String>();
        for (String pair : queryString.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2 && !parts[1].isEmpty()) {
                query.put(parts[0], parts[1]);
            }
        }
        return query;
    }

    /**
     * send 400(bad request) response
     *
     * @param response response
     */
    public void send400(HttpServletResponse response) {
        try {
            response.sendError(400);
        } catch (Exception ignore) {

        }
    }
}
