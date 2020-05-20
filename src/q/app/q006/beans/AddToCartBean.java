package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.quotation.PublicQuotation;
import q.app.q006.reqs.Requester;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;

@Named
@ViewScoped
public class AddToCartBean implements Serializable {

    private PublicQuotation quotation;

    @Inject
    private Requester reqs;


    @Inject
   // private CartBean cartBean;


    @PostConstruct
    private void init(){
        try {
            String s = Helper.getParam("quotation");
            initQuotation(Long.parseLong(s));
        }
        catch (Exception ex){
            Helper.redirect("quotations");
        }
    }

    private void initQuotation(long quotationId){
        Response r = reqs.getSecuredRequest(AppConstants.getQuotation(quotationId));
        if(r.getStatus() == 200){
            quotation = r.readEntity(PublicQuotation.class);
            quotation.initNewQuantities();
        }
    }

    public void checkout() {
        if (quotation.getTotalProducts() > 0) {
       //     cartBean.init();
         //   cartBean.addQuotation(quotation);
           // cartBean.setStage(2);
            Helper.redirect("select-address");
        } else {
            Helper.addErrorMessage(Bundler.getValue("partsNotSelected"));
        }
    }


    public PublicQuotation getQuotation() {
        return quotation;
    }

    public void setQuotation(PublicQuotation quotation) {
        this.quotation = quotation;
    }
}
