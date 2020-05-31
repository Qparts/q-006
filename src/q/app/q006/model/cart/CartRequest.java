package q.app.q006.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import q.app.q006.helpers.AppConstants;
import q.app.q006.model.customer.PublicAddress;

import javax.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.List;

public class CartRequest implements Serializable {

    private long customerId;
    private long addressId;
    @JsonbTransient
    @JsonIgnore
    private PublicAddress address;
    private double deliveryCharges;
    private Long discountId;
    @JsonIgnore
    @JsonbTransient
    private Discount discount;
    private Integer preferredCuorier;
    private List<CartItemRequest> cartItems;
    private double walletAmount;
    private PaymentRequest paymentRequest;


    @JsonIgnore
    @JsonbTransient
    public double getTotalProducts(){
        double total = 0;
        for(CartItemRequest cartItem : cartItems){
            total += cartItem.getSalesPrice() * cartItem.getQuantity();
        }
        return total;
    }

    @JsonIgnore
    @JsonbTransient
    public double getTotalProductsAfterDiscount(){
        double total = 0;
        for(CartItemRequest cartItem : cartItems){
            total += cartItem.getSalesPriceAfterDiscount() * cartItem.getQuantity();
        }
        return total;
    }

    @JsonIgnore
    @JsonbTransient
    public double getTotalProductsDiscountValue(){
        double total = 0;
        for(CartItemRequest cartItem : cartItems){
            total += cartItem.getDiscountValue() * cartItem.getQuantity();
        }
        return total;
    }

    @JsonIgnore
    @JsonbTransient
    public double getTotalDiscount(){
        double total = 0;
        for(CartItemRequest cartItem : cartItems){
            total += cartItem.getDiscountValue() * cartItem.getQuantity();
        }
        if(discount != null){
            total += deliveryCharges;
        }
        return total;
    }

    /**
     *
     * @return a total before discount and vat
     */
    @JsonIgnore
    @JsonbTransient
    public double getSubTotal(){
        return getTotalProducts() + deliveryCharges;
    }

    /**
     *
     * @return a total before vat
     */
    @JsonIgnore
    @JsonbTransient
    public double getSubTotalAfterDiscount(){
        return getTotalProducts() + deliveryCharges - getTotalDiscount();
    }

    @JsonbTransient
    @JsonIgnore
    public double getTotalVat(){
        return  getSubTotal() * AppConstants.VAT_PERCENTAGE;
    }

    @JsonbTransient
    @JsonIgnore
    public double getTotalVatAfterDiscount(){
        return  getSubTotalAfterDiscount() * AppConstants.VAT_PERCENTAGE;
    }

    @JsonbTransient
    @JsonIgnore
    public double getGrandTotal(){
        return getSubTotal() + getTotalVat();
    }

    @JsonbTransient
    @JsonIgnore
    public double getGrandTotalAfterDiscount(){
        return getSubTotalAfterDiscount() + getTotalVatAfterDiscount() - walletAmount;
    }


    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public List<CartItemRequest> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemRequest> cartItems) {
        this.cartItems = cartItems;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public Integer getPreferredCuorier() {
        return preferredCuorier;
    }

    public void setPreferredCuorier(Integer preferredCuorier) {
        this.preferredCuorier = preferredCuorier;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public PublicAddress getAddress() {
        return address;
    }

    public void setAddress(PublicAddress address) {
        this.address = address;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }
}
