package org.mvnsearch.wx;

import junit.framework.TestCase;
import org.mvnsearch.wx.rewrite.Conf;
import org.mvnsearch.wx.rewrite.Rule;

/**
 * conf test
 *
 * @author linux_china
 */
public class ConfTest extends TestCase {

    /**
     * test to construct conf
     *
     * @throws Exception exception
     */
    public void testConstructConf() throws Exception {
        Conf conf = new Conf(null, "classpath:weixin-router.xml");
        for (Rule rule : conf.getRules()) {
            System.out.println(rule);
        }
    }
}
