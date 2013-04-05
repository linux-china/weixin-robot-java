Weixin robot Java
========================================
微信公共平台自动回复机器人的Java SDK，你可以使用SDK简单快速构建微信机器人。
微信Robot Java借鉴了url rewrite的思想，url rewrite是根据url进行路由，而微信Robot则是根据消息类型和内容进行路由。
整理的流程如下：

* WexinRobotServlet负责认证和微信消息接收
* 接收后进行XML解析，构建出 WeixinMessage对象，并注入到request的attribute中，名字为wxMsg
* 根据配置的路由参数，对微信消息进行匹配
* 根据匹配的结果进行URL路由转发
* 最后由处理的组件完成xml格式反馈的输出


### 如何使用
首先我们要在pom.xml中添加：

     <dependency>
        <groupId>org.mvnsearch.wx</groupId>
        <artifactId>wx-robot-sdk</artifactId>
        <version>1.0.0</version>
     </dependency>

接下来就是修改web.xml添加Servlet负责接收微信公共平台发过来的消息：

        <servlet>
            <servlet-name>WexinRobotServlet</servlet-name>
            <servlet-class>org.mvnsearch.wx.servlet.WexinRobotServlet</servlet-class>
            <init-param>
                <param-name>token</param-name>
                <param-value>yourTokenHere</param-value>
            </init-param>
            <init-param>
                <param-name>confFile</param-name>
                <param-value>/WEB-INF/weixin-router.xml</param-value>
            </init-param>
        </servlet>

        <!-- weixing -->
        <servlet-mapping>
            <servlet-name>WexinRobotServlet</servlet-name>
            <url-pattern>/servlet/weixinrobot</url-pattern>
        </servlet-mapping>

其中token是指你在微信公共平台上的token，这里和其一致。接下来我们还需要设置servlet mapping，添加url-pattern，
然后将最终的url，如上述配置的 http://www.foobar.com/servlet/weixinrobot 作为微信公共平台接口配置中的URL。
接下来是在WEB-INF目录下创建一个weixin-router.xml的文件，完成信息的路由。

接收到的消息如何路由到指定的组件处理，如Struts的Controller等，这里我们需要创建一个router文件，这里还是根据
消息的类型和内容进行转发，内容如下：

    <?xml version="1.0" encoding="utf-8"?>

    <weixinrobot>

        <rule>
            <type>text</type>
            <content>[\d]{11}</content>
            <forward>/weixin2.wx</forward>
        </rule>

        <rule>
            <type>text</type>
            <forward>/weixin.wx</forward>
        </rule>

    </weixinrobot>

上述的例子就是如果内容是手机号，交与/weixin2.wx这个URL处理。当然我们设置了一个默认的选项，如果text类型的消息没有任何
匹配，则由/weixin.wx处理。

收到微信消息后，要给出消息反馈。微信公共平台的消息反馈是一个xml格式的数据，在Java的系统中，我们使用jspx输出接口，样例如下：

     <?xml version="1.0" encoding="UTF-8"?>
     <xml>
         <ToUserName>${wxMsg.sender}</ToUserName>
         <FromUserName>${wxMsg.receiver}</FromUserName>
         <CreateTime>${wxMsg.createdTime}</CreateTime>
         <MsgType>text</MsgType>
         <Content>${content}</Content>
         <FuncFlag>0</FuncFlag>
     </xml>

### 编码

* 在代码中获取解析好的微信消息  WeixinMessage wxMsg = (WeixinMessage)request.getAttribute("wxMsg");

### FAQ

* 如何调试： 由于微信服务器只能通知互联网IP和80端口，开发时你可以在你家中的ADSL拨号路由器上设置一下80转发，
然后微信服务器的消息通知就可以路由到你的笔记本上，方便你测试和开发。查看你的互联网Ip请访问 http://ip.mvnsearch.org
* 参考样例清访问robot-demo Maven module

