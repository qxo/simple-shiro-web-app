package sample;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * 安全认证最主要的实现类
 * 
 * @author Yockii Hsu
 * 
 */
public class ShiroSsoRealm extends AuthorizingRealm {

	

	public ShiroSsoRealm(){  
                // 设置无需凭证，因为从sso认证后才会有用户名  
    setCredentialsMatcher(new AllowAllCredentialsMatcher());  
                // 设置token为我们自定义的  
    setAuthenticationTokenClass(TrustedSsoAuthenticationToken.class);  
  }

	/**
	 * 认证回调函数，登陆时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		TrustedSsoAuthenticationToken token = (TrustedSsoAuthenticationToken) authcToken;
		// UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		Object username = token.getPrincipal();
		// String username = token.getUsername();
		// 不允许无username
		if (username == null) {
			// 自定义异常，于前端捕获
			
		}

		return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
	}

	/**
	 * 授权查询回调函数，进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = (String) principals.fromRealm(getName()).iterator().next();
		//User user = accountManager.findUserByLoginName(loginName);
		if ( true) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 将用户权限放入其中，代码略
			return info;
		}
		return null;
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载。
	 * 
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
}