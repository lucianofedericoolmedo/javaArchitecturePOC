package ar.com.mobeats.consolidar.backend.service.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

public class SpringLDAPTest {
	
	@Test
	public void testLdap() throws Exception {
		// Setup the LDAP client (normally done via Spring context file).
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://localhost:10389");
		contextSource.setBase("dc=example,dc=com");
		contextSource.setUserDn("cn=Cosme Fulanito+sn=Fulanito+uid=cosme,ou=carem,o=cnea,dc=example,dc=com");
		contextSource.setPassword("soycosme");
		contextSource.afterPropertiesSet();
		 
		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
		ldapTemplate.afterPropertiesSet();
		 
		// Perform the authentication.
//		Filter filter = new EqualsFilter("sAMAccountName", "userName");
		Filter filter = new EqualsFilter("uid", "homer");
		 
		boolean authed = ldapTemplate.authenticate("", filter.encode(), "soyhomer");
		
		Assert.assertTrue(authed);
		 
	}

}
