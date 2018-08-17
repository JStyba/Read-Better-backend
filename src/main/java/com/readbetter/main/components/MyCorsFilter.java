package com.readbetter.main.components;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyCorsFilter implements Filter {

    public MyCorsFilter () {
        super();
    }


    public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");

        // without this header jquery.ajax calls returns 401 even after successful login and SSESSIONID being succesfully stored.
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Origin, Content-Type, Version");
        response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");

        final HttpServletRequest request = (HttpServletRequest) req;
        if (!request.getMethod().equals("OPTIONS")) {
            chain.doFilter(req, res);
        } else {
            // do not continue with filter chain for options requests
        }
    }


    public void destroy() {

    }


    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }
}
