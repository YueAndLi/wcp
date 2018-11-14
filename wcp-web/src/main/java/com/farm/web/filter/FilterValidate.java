package com.farm.web.filter;

import com.farm.authority.FarmAuthorityService;
import com.farm.core.AuthorityService;
import com.farm.core.ParameterService;
import com.farm.core.auth.domain.AuthKey;
import com.farm.core.auth.domain.LoginUser;
import com.farm.core.auth.util.Urls;
import com.farm.parameter.FarmParameterService;
import com.farm.web.constant.FarmConstant;
import com.farm.web.online.OnlineUserOpImpl;
import com.farm.web.online.OnlineUserOpInter;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author WangDong
 * @date Mar 14, 2010
 */
public class FilterValidate implements Filter {
    private static final Logger log = Logger.getLogger(FilterValidate.class);

    private boolean isURL(String urlStr) {
        return Urls.isActionByUrl(urlStr, "do") || Urls.isActionByUrl(urlStr, "html");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        ParameterService parameterService = FarmParameterService.getInstance();
        String path = ((HttpServletRequest) arg0).getContextPath();
        String basePath = arg0.getScheme() + "://" + arg0.getServerName() + ":" + arg0.getServerPort() + path + "/";
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        HttpSession session = request.getSession();
        String requestUrl = request.getRequestURL().toString();
        //如果端口为80端口则，将该端口去掉，认为是不许要端口的
        String formatUrl = Urls.formatUrl(requestUrl, requestUrl.indexOf(":") < 8 ? basePath.replace(":80/", "/") : basePath);
        String key = null;
        LoginUser currentUser = null;
        AuthKey authkey = null;
        {// 不是后台请求直接运行访问()
            if (!isURL(formatUrl)) {
                arg2.doFilter(arg0, arg1);
                return;
            }
        }
        {// 组织URL参数
            key = Urls.getActionKey(formatUrl);
            currentUser = (LoginUser) session.getAttribute(FarmConstant.SESSION_USEROBJ);
            AuthorityService authority = FarmAuthorityService.getInstance();
            authkey = authority.getAuthKey(key);
        }
        {// 如果是不检查则任何连接都可执行
            if (parameterService.getParameter("config.auth.check.url").equals("false")) {
                // 放开就是对权限不做验证
                arg2.doFilter(arg0, arg1);
                return;
            }
        }
        {// 权限是否被定义为禁用
            if (authkey != null && !authkey.isUseAble()) {
                response.sendError(405, "当前用户没有权限请求该资源!");
                return;
            }
        }
        String subKey = Urls.getActionSubKey(key);
        {// 是否可以直接访问
            // 未被定义和不需要登录的均可以直接访问
            if (authkey != null && !authkey.isLogin()) {
                arg2.doFilter(arg0, arg1);
                return;
            }
            // 判断是否需要用户权限认证
            String prefix = parameterService.getParameter("config.url.free.path.prefix");
            if (prefix != null && subKey.indexOf("/" + prefix) == 0) {
                arg2.doFilter(arg0, arg1);
                return;
            }
        }
        {// 判断是否是登录即可访问
            String prefix = parameterService.getParameter("config.url.login.path.prefix");
            if (prefix != null && subKey.indexOf("/" + prefix) == 0) {
                if (isURL(formatUrl)) {
                    if (currentUser == null) {
                        response.sendRedirect(parameterService.getParameter("config.index.defaultpage"));
                        return;
                    } else {
                        arg2.doFilter(arg0, arg1);
                        return;
                    }
                }
            }
            if (authkey != null && authkey.isLogin() && !authkey.isCheck()) {
                arg2.doFilter(arg0, arg1);
                return;
            }
        }
        {// 处理登录操作
            String[] urls = parameterService.getParameter("config.url.login.indexs").split(",");
            if (isURL(formatUrl)) {
                // 如果是用户执行登录操作登录就放过
                if (Arrays.asList(urls).contains(key)) {
                    arg2.doFilter(arg0, arg1);
                    return;
                }
            }
        }
        {// 处理未登录用户---让其登录
            if (isURL(formatUrl)) {
                if (currentUser == null) {
                    if (request.getMethod().equals("GET")) {
                        session.setAttribute(parameterService.getParameter("farm.constant.session.key.go.url"),
                                requestUrl + "?" + Urls.getUrlParameters(request));
                    }
                    response.sendRedirect(basePath + parameterService.getParameter("config.url.login"));
                    return;
                }
            }
        }
        {// 处理--online--用户在线
            OnlineUserOpInter ouop = OnlineUserOpImpl.getInstance(request.getRemoteAddr(), currentUser.getLoginname(),
                    session);
            if (parameterService.getParameter("config.auth.multi.user").equals("false")) {
                ouop.userVisitHandle();
            }
        }
        // 验证登录用户权限(用户是否有该权限)
        {
            Set<String> usraction = ((Set<String>) request.getSession().getAttribute(FarmConstant.SESSION_USERACTION));
            if (authkey != null && !usraction.contains(key)) {
                log.info("用户未经授权访问'" + authkey.getTitle() + "(" + key + ")'被拦截");
                response.sendError(405, "当前用户没有权限请求该资源!");
                return;
            }
        }
        // 非受管的权限可以直接登录
        arg2.doFilter(arg0, arg1);
        return;
    }

    // -----------------------------------------------------------
    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}