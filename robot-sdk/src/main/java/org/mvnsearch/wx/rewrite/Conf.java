package org.mvnsearch.wx.rewrite;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * weixin robot configuration
 *
 * @author linux_china
 */
public class Conf {
    /**
     * rules
     */
    private List<Rule> rules = new ArrayList<Rule>();

    /**
     * construct method
     *
     * @param path path
     * @throws Exception exception
     */
    @SuppressWarnings("unchecked")
    public Conf(ServletContext servletContext, String path) throws Exception {
        InputStream inputStream;
        if (path.startsWith("classpath:")) {
            inputStream = this.getClass().getResourceAsStream(path.replace("classpath:", "/"));
        } else {
            inputStream = servletContext.getResourceAsStream(path);
        }
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(inputStream);
        List<Element> ruleElements = document.getRootElement().getChildren("rule");
        for (Element ruleElement : ruleElements) {
            rules.add(new NormalRule(ruleElement.getChildTextTrim("type"), ruleElement.getChildTextTrim("content"), (ruleElement.getChildTextTrim("forward"))));
        }
    }

    /**
     * get forward rules
     *
     * @return rules
     */
    public List<Rule> getRules() {
        return rules;
    }
}
