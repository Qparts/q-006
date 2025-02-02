package q.app.q006.reqs;


import q.app.q006.beans.LoginBean;
import q.app.q006.filter.LoggingFilter;
import q.app.q006.helpers.AppConstants;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named(value="requester")
@RequestScoped
public class Requester implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private LoginBean loginBean;
	
	public String getSecurityHeader() {
		return "Bearer " + loginBean.getLoginObject().getToken() + " && " + loginBean.getLoginObject().getCustomer().getId() + " && "
				+ AppConstants.APP_SECRET + " && C";//from q.app.qetaa.customer
	}
	
	public String getSecurityHeader(String token, long customerId){
		return "Bearer " + token + " && " + customerId + " && "
				+ AppConstants.APP_SECRET + " && " + "C";//from q.app.qetaa.customer
	}

	public Response getSecuredRequest(String link) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.get();// not secured
		return r;
	}

	public <T> Response postSecuredRequest(String link, T t) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.post(Entity.entity(t, "application/json"));
		return r;
	}

	public <T> Response postSecuredRequestAndLog(String link, T t) {
		Invocation.Builder b = ClientBuilder.newClient().target(link).register(LoggingFilter.class).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.post(Entity.entity(t, "application/json"));
		return r;
	}
	
	public <T> Response putSecuredRequest(String link, T t) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.put(Entity.entity(t, "application/json"));
		return r;
	}

	public <T> Response putSecuredRequest(String link, T t, String token, long customerId) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader(token, customerId));
		Response r = b.put(Entity.entity(t, "application/json"));
		return r;
	}
	

	public <T> Response postSecuredRequest(String link, T t, String token, long customerId) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader(token, customerId));
		Response r = b.post(Entity.entity(t, "application/json"));
		return r;
	}
}
