package q.app.q006.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class CreateQuotationItemRequest implements Serializable {

    private boolean hasImage;
    private int quantity;
    private String itemName;
    private int tempId;

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
