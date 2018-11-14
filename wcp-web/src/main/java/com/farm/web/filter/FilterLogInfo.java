package com.farm.web.filter;

import com.farm.core.auth.domain.LoginUser;
import com.farm.web.constant.FarmConstant;
import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FilterLogInfo implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpSession session = req.getSession();
        MDC.put("IP", arg0.getRemoteAddr());
        if (session == null) {
            MDC.put("USERID", "NONE");
        } else {
            LoginUser user = (LoginUser) session
                    .getAttribute(FarmConstant.SESSION_USEROBJ);
            if (user == null) {
                MDC.put("USERID", "NONE");
            } else {
                MDC.put("USERID", user.getId());
            }
        }
        arg2.doFilter(arg0, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
