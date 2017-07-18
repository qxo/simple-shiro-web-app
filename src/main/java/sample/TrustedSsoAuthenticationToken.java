package sample;

import org.apache.shiro.authc.AuthenticationToken;

public class TrustedSsoAuthenticationToken implements AuthenticationToken {

	private String username;

	public TrustedSsoAuthenticationToken(String username) {
		super();
		this.username = username;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		return "username=" + this.username;
	}

}
