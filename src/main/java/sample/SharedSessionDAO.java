package sample;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

public class SharedSessionDAO extends AbstractSessionDAO implements ServletContextAware {

	   private static final Logger log = LoggerFactory.getLogger(MemorySessionDAO.class);

	   private ConcurrentMap<Serializable, Session> sessions;

	    public SharedSessionDAO() {
	    	
	        this.sessions = new ConcurrentHashMap<Serializable, Session>();
	    }

	    private String sharedContextPath;
	    
	    public String getSharedContextPath() {
			return sharedContextPath;
		}

		public void setSharedContextPath(String sharedContextPath) {
			this.sharedContextPath = sharedContextPath;
		}
		
		protected  ConcurrentMap<Serializable, Session> getSsoSessionStorage(){
			if( sharedContextPath != null  && servletContext != null ) {
				ServletContext context = servletContext.getContext(sharedContextPath);
				if( context != null ) {
					ConcurrentMap<Serializable, Session>  sessions = ( ConcurrentMap<Serializable, Session>)context.getAttribute("shrioSessions");
					if( sessions != null ) {
						return sessions;
					}
				}
			}
			return sessions;
		}
		
		private ServletContext servletContext;

		@Override
		public void setServletContext(ServletContext servletContext) {
			if( StringUtils.isEmpty(sharedContextPath ) ){
				sharedContextPath = null;
			}else {
				String servletContextName = servletContext.getContextPath();
				if( sharedContextPath.equals(servletContextName)) {
					servletContext.setAttribute("shrioSessions",sessions );
					sharedContextPath = null;
				}
			}
			this.servletContext = servletContext;
		}

		protected Serializable doCreate(Session session) {
	        Serializable sessionId = generateSessionId(session);
	        assignSessionId(session, sessionId);
	        storeSession(sessionId, session);
	        return sessionId;
	    }

	    protected Session storeSession(Serializable id, Session session) {
	        if (id == null) {
	            throw new NullPointerException("id argument cannot be null.");
	        }
	        if( sessions.containsKey(id)) {
	        	
	        }
	        return sessions.putIfAbsent(id, session);
	    }

	    protected Session doReadSession(Serializable sessionId) {
	    	Session session = sessions.get(sessionId);
	    	if( session == null ) {
	    		Serializable s = (Serializable)getSsoSessionStorage().get(sessionId);
	    		if( s != null ) {
	    			SimpleSession simpleSession = new SimpleSession();
		    			
	    			//Serializer serializer = SerializerHelper.getSerializer();
	    			//session = serializer.deserialize(serializer.serialize(s),SimpleSession.class);
					
	    			BeanUtils.copyProperties(s, simpleSession);
						//BeanUtils.copyProperties(s, simpleSession);
					
		    		//session = simpleSession;
		    		sessions.put(sessionId, session);
	    		}
	    		//SerializationUtils.
	    		//org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO
	    	}
	    	
	        return session ;
	    }

	    public void update(Session session) throws UnknownSessionException {
	        storeSession(session.getId(), session);
	    }

	    public void delete(Session session) {
	        if (session == null) {
	            throw new NullPointerException("session argument cannot be null.");
	        }
	        Serializable id = session.getId();
	        if (id != null) {
	        	sessions.remove(id);
	            getSsoSessionStorage().remove(id);
	        }
	    }

	    public Collection<Session> getActiveSessions() {
	        Collection<Session> values = sessions.values();
	        if (CollectionUtils.isEmpty(values)) {
	            return Collections.emptySet();
	        } else {
	            return Collections.unmodifiableCollection(values);
	        }
	    }


}
