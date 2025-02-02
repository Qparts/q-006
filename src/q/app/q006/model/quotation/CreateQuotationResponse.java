package q.app.q006.model.quotation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CreateQuotationResponse implements Serializable {
    private int quotationId;
    private List<Map<String,Object>> items;
    private String vehicleImageName;
    private boolean uploadImage;
    private String transactionUrl;

    public boolean isUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(boolean uploadImage) {
        this.uploadImage = uploadImage;
    }

    public String getVehicleImageName() {
        return vehicleImageName;
    }

    public void setVehicleImageName(String vehicleImageName) {
        this.vehicleImageName = vehicleImageName;
    }

    public int getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(int quotationId) {
        this.quotationId = quotationId;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public String getTransactionUrl() {
        return transactionUrl;
    }

    public void setTransactionUrl(String transactionUrl) {
        this.transactionUrl = transactionUrl;
    }
}
