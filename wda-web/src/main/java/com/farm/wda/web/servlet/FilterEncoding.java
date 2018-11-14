package com.farm.wda.web.servlet;

import com.farm.wda.util.AppConfig;

import javax.servlet.*;
import java.io.IOException;


/**
 * SetEncodingϵͳ����
 *
 * @author WangDong
 * @date Mar 14, 2010
 */
public class FilterEncoding implements Filter {

    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
        String encode = AppConfig.getString("config.filter.encode");
        arg0.setCharacterEncoding(encode);
        arg2.doFilter(arg0, arg1);
    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }
}