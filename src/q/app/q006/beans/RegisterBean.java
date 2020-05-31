package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.customer.LoginObject;
import q.app.q006.model.customer.RegisterModel;
import q.app.q006.model.location.PublicCountry;
import q.app.q006.reqs.NotLoggedRequester;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SessionScoped
@Named
public class RegisterBean implements Serializable {
    private RegisterModel registerModel;
    private String smsCode;
    private String smsCodeUser;


    @Inject
    private NotLoggedRequester reqs;

    @Inject
    private CountryBean countryBean;

    @Inject
    private LoginBean loginBean;



    @PostConstruct
    private void init(){
        registerModel = new RegisterModel();
        registerModel.setType('M');
        registerModel.setCreatedBy(0);
        registerModel.setCountryId(1);

    }



    private Map<String,String> initSMSMap(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", registerModel.getMobile());
        map.put("email", registerModel.getEmail());
        if (registerModel.getCountryId() == 1) {
            map.put("countryCode", "966");
            registerModel.setCountryCode("966");
        } else {
            PublicCountry pc = countryBean.getCountryFromId(registerModel.getCountryId());
            map.put("countryCode", pc.getCountryCode());
        }
        return map;
    }



    public void signup() {
        if (this.smsCode.equals(this.smsCodeUser)) {
            Response r = reqs.postSecuredRequest(AppConstants.POST_SIGNUP, registerModel);
            if(r.getStatus() == 200) {
                loginBean.updateLogin(r.readEntity(LoginObject.class));
            } else {

            }
        } else {
            Helper.addErrorMessage(Bundler.getValue("invalidSms"));
        }
    }


    public void requestSMS() {
        if (registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
            Map<String,String> map = initSMSMap();
            Response r = reqs.postSecuredRequest(AppConstants.POST_SIGNUP_SMS, map);
            if (r.getStatus() == 200) {
                this.smsCode = r.readEntity(String.class);
            } else if (r.getStatus() == 409) {
                Helper.addErrorMessage(Bundler.getValue("customerRegistered"));
            } else {
                Helper.addErrorMessage(Bundler.getValue("errorOccured"));
            }
        } else {
            Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
        }
    }


    public RegisterModel getRegisterModel() {
        return registerModel;
    }

    public void setRegisterModel(RegisterModel registerModel) {
        this.registerModel = registerModel;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsCodeUser() {
        return smsCodeUser;
    }

    public void setSmsCodeUser(String smsCodeUser) {
        this.smsCodeUser = smsCodeUser;
    }
}
