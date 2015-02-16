package papasoft.octopus.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import papasoft.octopus.webapp.mbeans.LoginBean;

public class AuthenticationFilter implements Filter {
	@SuppressWarnings("unused")
	private FilterConfig config;

	  public void doFilter(ServletRequest req, ServletResponse resp,
	      FilterChain chain) throws IOException, ServletException {
	    if (((HttpServletRequest) req).getSession().getAttribute(
	        LoginBean.AUTH_KEY) == null) {
	      ((HttpServletResponse) resp).sendRedirect("./../login.xhtml");
	    } else {
	      chain.doFilter(req, resp);
	    }
	  }

	  public void init(FilterConfig config) throws ServletException {
	    this.config = config;
	  }

	  public void destroy() {
	    config = null;
	  }

}
