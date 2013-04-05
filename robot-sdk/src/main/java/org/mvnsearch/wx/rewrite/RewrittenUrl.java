package org.mvnsearch.wx.rewrite;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * rewritten url
 *
 * @author linux_china
 */
public interface RewrittenUrl {
    /**
     * get target url
     *
     * @return target url
     */
    public String getTarget();

    /**
     * execute rewrite
     *
     * @param request  http servlet request
     * @param response http servlet response
     * @return rewrite mark
     * @throws ServletException servlet exception
     * @throws IOException      IO exception
     */
    public boolean doRewrite(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException;
}
