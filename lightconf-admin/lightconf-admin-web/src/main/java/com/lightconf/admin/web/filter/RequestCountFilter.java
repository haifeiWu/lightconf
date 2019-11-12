package com.lightconf.admin.web.filter;

import io.netty.util.concurrent.FastThreadLocal;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author wuhaifei 2019-11-12
 */
@WebFilter
public class RequestCountFilter implements Filter {
    private static FastThreadLocal THREAD_LOCAL;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        THREAD_LOCAL.get();
    }
}
