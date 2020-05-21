package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.customer.LoginObject;
import q.app.q006.reqs.NotLoggedRequester;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@SessionScoped
public class ResetPasswordBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;
	private String systemSMS;
	private String userSMS;
	private String newPassword;
	private String confirmNewPassword;

	@Inject
	private NotLoggedRequester reqs;

	@Inject
	private LoginBean loginBean;

	public void resetPassword() {
		if (newPassword.equals(confirmNewPassword)) {
			if (userSMS.equals(this.systemSMS)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", mobile);
				map.put("password", newPassword);
				Response r = reqs.putSecuredRequest(AppConstants.PUT_RESET_PASSWORD, map);
				if (r.getStatus() == 200) {
					loginBean.setLoginObject(r.readEntity(LoginObject.class));
					loginBean.doLogin();
					Helper.redirect("/");
				}
				else{
					Helper.addErrorMessage("errorOccured");
				}
			}else {
				Helper.addErrorMessage(Bundler.getValue("invalidSms"));
			}
		} else {
			Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
		}
	}

	public void requestSMS() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", this.mobile);
		Response r = reqs.postSecuredRequest(AppConstants.POST_RESET_PASSWORD_SMS, map);
		if (r.getStatus() == 200) {
			this.systemSMS = r.readEntity(String.class);
		} else if (r.getStatus() == 404) {
			Helper.addErrorMessage(Bundler.getValue("customerNotFound"));
		} else {
			Helper.addErrorMessage(Bundler.getValue("errorOccured"));
		}
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSystemSMS() {
		return systemSMS;
	}

	public void setSystemSMS(String systemSMS) {
		this.systemSMS = systemSMS;
	}

	public String getUserSMS() {
		return userSMS;
	}

	public void setUserSMS(String userSMS) {
		this.userSMS = userSMS;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

}
