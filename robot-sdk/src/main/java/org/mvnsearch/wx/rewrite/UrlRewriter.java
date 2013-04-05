package org.mvnsearch.wx.rewrite;

import org.mvnsearch.wx.WeixinMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * url rewriter
 *
 * @author linux_china
 */
public class UrlRewriter {
    /**
     * rewriter configuration
     */
    private Conf conf;

    /**
     * construct method
     *
     * @param conf rewriter configuration
     */
    public UrlRewriter(Conf conf) {
        this.conf = conf;
    }


    /**
     * Helpful for testing but otherwise, don't use.
     */
    public RewrittenUrl processRequest(WeixinMessage wxMsg,
                                       final HttpServletRequest request,
                                       final HttpServletResponse response)
            throws IOException, ServletException, InvocationTargetException {
        RuleChain chain = getNewChain(request);
        chain.process(wxMsg, request, response);
        return chain.getFinalRewrittenRequest();
    }

    /**
     * get new rule  chain
     *
     * @param request     http servlet request
     * @return rule chain
     */
    private RuleChain getNewChain(final HttpServletRequest request) {
        return new RuleChain(conf.getRules());
    }

}
