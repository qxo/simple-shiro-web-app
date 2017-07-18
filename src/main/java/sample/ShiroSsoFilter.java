package sample;

import java.io.IOException;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.util.StringUtils;

public class ShiroSsoFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession(true);
		String key4uid = "org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
		Object key = session.getAttribute(key4uid);
		String sessionId = session.getId();
		System.out.println(request.getContextPath() + " sessionid:" + sessionId);

		if (true && key == null) {

			String uid = null;
			// Cookie[] cookies = request.getCookies();
			// for (Cookie cookie : cookies) {
			// if( cookie.getName().equals("JSESSIONID") ) {
			// sessionId = cookie.getValue();
			// break;
			// }
			// }
			//
			// HttpSession session2 = session.getSessionContext().getSession(sessionId);
			// if( session2 != null ) {
			// uid = (String)session2.getAttribute(key4uid);
			// }
			if (StringUtils.isEmpty(uid)) { 
				//通过jmx从app1获取认证用户UID
				try {
					MBeanServer beanServer = MBeanServerFactory.findMBeanServer(null).get(0);
					String opSig[] = { String.class.getName(), String.class.getName() };

					uid = (String) beanServer.invoke(
							new ObjectName("Catalina:type=Manager,context=/app1,host=localhost"), "getSessionAttribute",
							new Object[] { sessionId, key4uid }, opSig);

					System.out.println(sessionId + " uid:" + uid);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (StringUtils.isEmpty(uid)) {
				uid = request.getParameter("mockUser");
			}
			if (!StringUtils.isEmpty(uid)) {
				TrustedSsoAuthenticationToken token = new TrustedSsoAuthenticationToken(uid);
				SecurityUtils.getSubject().login(token);
			}
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
