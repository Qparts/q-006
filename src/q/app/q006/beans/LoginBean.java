package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.customer.LoginObject;
import q.app.q006.reqs.Requester;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private LoginObject loginObject;
    private String username;
    private String password;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init() {
    }

    public void checkCodeLogin() {
        try{
            String code = Helper.getParam("c");
            String email = Helper.getParam("e");
            String quotationId = Helper.getParam("q");
            Map<String,Object> map = new HashMap<>();
            map.put("code", code);
            map.put("email", email);
            Response r = reqs.postSecuredRequest(AppConstants.POST_CODE_LOGIN, map, null, 0);
            if (r.getStatus() == 200) {
                this.loginObject = r.readEntity(LoginObject.class);
                //saveCookie();
                doLogin();
                //check q.app.qetaa.cart status
                String anchor = "#c" +quotationId;
                Helper.redirect("/quotations" + anchor);
            } else {
                throw new Exception();
            }
        }catch (Exception ex){
            Helper.redirect("/index");
        }
    }

    public void login(){
        Map<String,String> map = new HashMap<>();
        map.put("email", username);
        map.put("password", password);
        Response r = reqs.postSecuredRequest(AppConstants.POST_EMAIL_LOGIN, map, null, 0);
        if(r.getStatus() == 200){
            this.loginObject = r.readEntity(LoginObject.class);
            doLogin();
            Helper.redirect("/");
        }
        else if(r.getStatus() == 404){
            Helper.addErrorMessage(Bundler.getValue("passwordNotMatched"));
        }
        else {
            Helper.addErrorMessage(Bundler.getValue("Server Error"));
        }
    }

    public void updateLogin(LoginObject loginObject) {
        this.loginObject = loginObject;
        doLogin();
        Helper.redirect("/");
    }

    public void doLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("customer.loginObject", loginObject);
    }

    public void doLogout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("customer.loginObject");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        Helper.redirect("/index");
    }

    public boolean isLogged() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customer.loginObject") != null;
    }

    public void setLoginObject(LoginObject loginObject) {
        this.loginObject = loginObject;
    }

    public LoginObject getLoginObject() {
        return loginObject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
