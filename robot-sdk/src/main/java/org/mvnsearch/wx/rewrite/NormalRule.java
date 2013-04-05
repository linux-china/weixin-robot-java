package org.mvnsearch.wx.rewrite;

import org.jetbrains.annotations.Nullable;
import org.mvnsearch.wx.WeixinMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * normal rule
 *
 * @author linux_china
 */
public class NormalRule implements Rule {
    /**
     * type
     */
    private String type;
    /**
     * content
     */
    private String content;
    /**
     * regex pattern
     */
    private Pattern regexPattern;
    /**
     * forward
     */
    private String forward;

    /**
     * construct method
     *
     * @param type    type
     * @param content content
     * @param forward forward
     */
    public NormalRule(String type, String content, String forward) {
        this.type = type;
        this.content = content;
        if (content != null && !content.isEmpty()) {
            this.regexPattern = Pattern.compile(content);
        }
        this.forward = forward;
    }

    /**
     * match weixin message?
     *
     * @param wxMsg    weixin message
     * @param request  http servlet request
     * @param response http servlet response
     * @return rewritten url
     * @throws IOException      io exception
     * @throws ServletException servlet exception
     */
    @Nullable
    public RewrittenUrl matches(WeixinMessage wxMsg, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        boolean matched = false;
        if (type.equals(wxMsg.getType())) {
            matched = regexPattern == null || regexPattern.matcher(wxMsg.getContent()).find();
        }
        return matched ? new NormalRewrittenUrl(forward) : null;
    }
}
