package org.mvnsearch.wx.rewrite;

import org.mvnsearch.wx.WeixinMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * rule chain
 *
 * @author linux_china
 */
public class RuleChain {
    /**
     * rules
     */
    private List<Rule> rules;
    /**
     * target
     */
    private RewrittenUrl rewrittenUrl;

    /**
     * rule chain construct
     *
     * @param rules rules
     */
    public RuleChain(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * process weixin message
     *
     * @param wxMsg    weixin message
     * @param request  http servlet request
     * @param response response
     * @throws IOException               IO Exception
     * @throws ServletException          servlet exception
     * @throws InvocationTargetException invocation target exception
     */
    public void process(WeixinMessage wxMsg, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, InvocationTargetException {
        doRules(wxMsg, request, response);
    }

    /**
     * iterated match in rules
     *
     * @param wxMsg    weixin message
     * @param request  http servlet request
     * @param response http servlet response
     * @throws IOException      IO Exception
     * @throws ServletException servlet exception
     */
    public void doRules(WeixinMessage wxMsg, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        for (Rule rule : rules) {
            RewrittenUrl rewrittenUrl = rule.matches(wxMsg, request, response);
            if (rewrittenUrl != null) {
                this.rewrittenUrl = rewrittenUrl;
                return;
            }
        }
    }

    /**
     * get final rewritten request
     *
     * @return rewritten url
     */
    public RewrittenUrl getFinalRewrittenRequest() {
        return this.rewrittenUrl;
    }

}
