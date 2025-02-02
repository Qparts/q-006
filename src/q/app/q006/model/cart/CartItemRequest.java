package q.app.q006.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.q006.model.product.PublicProduct;

import javax.json.bind.annotation.JsonbTransient;

public class CartItemRequest {
    private long productId;
    private int quantity;
    private double salesPrice;
    private Long discountId;


    @JsonIgnore
    @JsonbTransient
    private Discount discount;
    @JsonbTransient
    @JsonIgnore
    private PublicProduct publicProduct;
    @JsonbTransient
    @JsonIgnore
    private String itemName;


    @JsonbTransient
    @JsonIgnore
    public double getDiscountValue(){
        if(discount != null){
            return salesPrice * (discount.getPercentage());
        }
        return 0;
    }

    @JsonbTransient
    @JsonIgnore
    public double getSalesPriceAfterDiscount(){
        return salesPrice - getDiscountValue();
    }


    public PublicProduct getPublicProduct() {
        return publicProduct;
    }

    public void setPublicProduct(PublicProduct publicProduct) {
        this.publicProduct = publicProduct;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
