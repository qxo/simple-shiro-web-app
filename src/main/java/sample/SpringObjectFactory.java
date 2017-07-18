package sample;

import org.apache.shiro.util.AbstractFactory;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

public class SpringObjectFactory<T> extends AbstractFactory<T> implements Destroyable,ApplicationContextAware,DisposableBean {

	private String configLocation;

	private String beanName;

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	private static ApplicationContext ctx;

	private static boolean outsideCtx = false;
	

	public static <T> SpringObjectFactory<T> createFactory() {
		return new SpringObjectFactory<T>();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if( ctx == null ) {
			SpringObjectFactory.ctx = applicationContext;
			outsideCtx = true;
		}else {
			throw new IllegalAccessError();
		}
	}

	protected static ClassPathXmlApplicationContext create(String configLocation) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				StringUtils.isEmpty(configLocation) ? "/applicationContext.xml" : configLocation);
		return ctx;
	}

	@Override
	protected T createInstance() {
		if (ctx == null) {
			ctx = create(configLocation);
		}
		if (StringUtils.isEmpty(beanName)) {
			throw new IllegalArgumentException("require beanName!");
		}
		return (T) ctx.getBean(beanName);
	}

	@Override
	public void destroy() throws Exception {
		if( ctx != null ) {
			synchronized (ctx) {
				ApplicationContext tmp = ctx;
				if( ctx != null) {
					ctx = null;
					if( !outsideCtx && ( tmp instanceof DisposableBean) ) {
						((DisposableBean)tmp).destroy();
					}
				}
			}		
		}
		
	}

	
}
