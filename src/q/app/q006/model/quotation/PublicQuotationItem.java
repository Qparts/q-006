package q.app.q006.model.quotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.q006.model.product.PublicProduct;

import java.io.Serializable;


public class PublicQuotationItem implements Serializable {

    private long id;
    private int quantity;
    private int newQuantity;
    private String name;
    private PublicProduct products;

    @JsonIgnore
    public int[] getQuantityArray() {
        int[] quantityArray = new int[20];
        for (int i = 0; i < quantityArray.length; i++) {
            quantityArray[i] = i + 1;
        }
        return quantityArray;
    }

    public void initNewQuantity(){
        newQuantity = quantity;
    }

    public PublicProduct getProducts() {
        return products;
    }

    public void setProducts(PublicProduct products) {
        this.products = products;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public void setName(String name) {
        this.name = name;
    }
}
