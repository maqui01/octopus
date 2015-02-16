/**
 * 
 */
package papasoft.octopus.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import papasoft.octopus.webapp.mbeans.LoginBean;

/**
 * @author maqui
 *
 */
public class CompanyFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String company = req.getParameter("c");
		if (company != null) {
			String actualCompany = (String) ((HttpServletRequest) req).getSession().getAttribute("company");
			if (!company.equals(actualCompany)) {
				if (((HttpServletRequest) req).getSession().getAttribute(
				        LoginBean.AUTH_KEY) != null) {
					((HttpServletRequest) req).getSession().invalidate();
				}
				((HttpServletRequest) req).getSession().setAttribute("company", company);
			}
		}
	    chain.doFilter(req, resp);
	}

	  public void init(FilterConfig config) throws ServletException {
	    this.config = config;
	  }

	  public void destroy() {
	    config = null;
	  }

}
