package q.app.q006.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.quotation.*;
import q.app.q006.model.vehicle.PublicMake;
import q.app.q006.model.vehicle.PublicModel;
import q.app.q006.model.vehicle.PublicModelYear;
import q.app.q006.reqs.Requester;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CreateQuotationBean implements Serializable {
    private int step;
    private PublicMake selectedMake;
    private PublicModel selectedModel;
    private PublicModelYear selectedYear;
    private int selectedRegionId;
    private int selectedCityId;
    private String vin;
    private boolean agree;
    private List<CreateQuotationItemRequest> quotationItems;
    private int lastOrderId;
    @JsonIgnore
    private int[] quantityArray;


    private void initQuantityArray() {
        quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
    }


    @Inject
    private LoginBean loginBean;

    @Inject
    private Requester reqs;

    @PostConstruct
    private void init(){
        initQuantityArray();
        step = 1;
        quotationItems = new ArrayList<CreateQuotationItemRequest>();
        addItem();
    }

    public void resetAll(){
        initQuantityArray();
        step = 1;
        selectedMake = null;
        selectedModel = null;
        selectedYear = null;
        selectedRegionId = 0;
        selectedCityId = 0;
        vin = "";
        agree = false;
        quotationItems = new ArrayList<>();
        addItem();
        lastOrderId = 0;
    }

    public void verifyItems() {
        if (getQuotationItems().size() == 0) {
            Helper.addErrorMessage(Bundler.getValue("addItemsReq"));
        } else {
            this.step = 6;
        }
    }

    public void submit() {
        CreateQuotationRequest cqr = new CreateQuotationRequest();
        cqr.setCustomerId(loginBean.getLoginObject().getCustomer().getId());
        cqr.setMobile(loginBean.getLoginObject().getCustomer().getMobile());
        cqr.setMakeId(this.selectedMake.getId());
        cqr.setVehicleYearId(this.selectedYear.getId());
        cqr.setCityId(this.selectedCityId);
        cqr.setVin(this.vin);
        int index = 0;
        for (CreateQuotationItemRequest cqir : quotationItems) {
            cqir.setTempId(index);
            cqir.setHasImage(false);
            cqir.setItemName(cqir.getItemName().trim());
            index++;
        }
        cqr.setQuotationItems(quotationItems);
        cqr.setPaymentMethod('F');
        Response r = reqs.postSecuredRequestAndLog(AppConstants.POST_CREATE_QUOTATION_FREE, cqr);
        System.out.println(r.getStatus());
        if (r.getStatus() == 200) {
            CreateQuotationResponse res = r.readEntity(CreateQuotationResponse.class);
            System.out.println(res.getQuotationId());
            lastOrderId = res.getQuotationId();
            System.out.println(lastOrderId);
            step = 7;
        }
        else{
            Helper.addErrorMessage(Bundler.getValue("error"));
        }
    }

    public void addItem() {
        CreateQuotationItemRequest item = new CreateQuotationItemRequest();
        getQuotationItems().add(item);
    }

    public void removeItem(CreateQuotationItemRequest item) {
        getQuotationItems().remove(item);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void chooseYear(PublicModelYear my){
        this.selectedYear = my;
        step  = 4;
    }

    public void chooseMake(PublicMake make){
        selectedMake = make;
        step = 2;
    }

    public void chooseModel(PublicModel model) {
        this.selectedModel = model;
        step = 3;
    }

    public void resetToStep(int step){
        this.step = step;
    }

    public void verifyVin() {
        if(this.vin.length() == 17){
            this.step = 5;
        }
        else{
            Helper.addErrorMessage(Bundler.getValue("invalidVin"), "form-1:vin");
        }
    }

    public PublicMake getSelectedMake() {
        return selectedMake;
    }

    public PublicModel getSelectedModel() {
        return selectedModel;
    }

    public PublicModelYear getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(PublicModelYear selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<CreateQuotationItemRequest> getQuotationItems() {
        return quotationItems;
    }

    public void setQuotationItems(List<CreateQuotationItemRequest> quotationItems) {
        this.quotationItems = quotationItems;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getSelectedRegionId() {
        return selectedRegionId;
    }

    public void setSelectedRegionId(int selectedRegionId) {
        this.selectedRegionId = selectedRegionId;
    }

    public int getSelectedCityId() {
        return selectedCityId;
    }

    public void setSelectedCityId(int selectedCityId) {
        this.selectedCityId = selectedCityId;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public int getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(int lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    public int[] getQuantityArray() {
        return quantityArray;
    }

}
