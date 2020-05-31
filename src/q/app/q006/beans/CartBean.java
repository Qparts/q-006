package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.helpers.Bundler;
import q.app.q006.helpers.Helper;
import q.app.q006.model.cart.CartItemRequest;
import q.app.q006.model.cart.CartRequest;
import q.app.q006.model.cart.CartRequestResponse;
import q.app.q006.model.cart.Discount;
import q.app.q006.model.location.PublicCountry;
import q.app.q006.model.cart.PaymentRequest;
import q.app.q006.model.quotation.PublicQuotation;
import q.app.q006.model.quotation.PublicQuotationItem;
import q.app.q006.reqs.Requester;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Named
@SessionScoped
public class CartBean implements Serializable {

    private CartRequest cartRequest;
    private char paymentMethod;
    private CartRequestResponse cartRequestResponse;
    private boolean paymentFailed;
    private int stage;
    private String promoCodeQuery;
    private boolean promoVerified;
    private boolean havePromo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private Requester reqs;

    @Inject
    private CountryBean countryBean;

    @PostConstruct
    public void init() {
        stage = 1;
        cartRequest = new CartRequest();
        cartRequest.setCartItems(new ArrayList<>());
        cartRequest.setDeliveryCharges(35);
        cartRequest.setCustomerId(loginBean.getLoginObject().getCustomer().getId());
        paymentMethod = 'V';
        havePromo = false;
        promoVerified = false;
        promoCodeQuery = "";
    }

    public void checkStage() {
        if (stage == 1) {
            Helper.redirect("quotations");
        } else if (stage == 2) {
            Helper.redirect("select-address");
        }
    }

    //
    public void verifyPromoCode() {
        Response r = reqs.getSecuredRequest(AppConstants.getPromotionCodeFromCode(this.promoCodeQuery));
        if (r.getStatus() == 200) {
            promoVerified = true;
            Discount discount = r.readEntity(Discount.class);
            if (discount.getDiscountType() == 'D') {
                this.cartRequest.setDiscountId(discount.getId());
                this.cartRequest.setDiscount(discount);
            } else {
                for (var cartItem : cartRequest.getCartItems()) {
                    if (cartItem.getDiscountId() == null) {
                        cartItem.setDiscount(discount);
                        cartItem.setDiscountId(discount.getId());
                    }
                }
            }
        } else if (r.getStatus() == 498) {
            Helper.addErrorMessage(Bundler.getValue("promCodeQuestion"), "form-3:ms");
        } else if (r.getStatus() == 410) {
            Helper.addErrorMessage(Bundler.getValue("promCodeUsed"), "form-3:ms");
        } else if (r.getStatus() == 404) {
            Helper.addErrorMessage(Bundler.getValue("promCodeNotFound"), "form-3:ms");
        } else {
            Helper.addErrorMessage(Bundler.getValue("errorOccured"));
        }
    }

    public void addQuotation(PublicQuotation quotation) {
        for (PublicQuotationItem quotationItem : quotation.getQuotationItems()) {
            CartItemRequest cartItem = new CartItemRequest();
            cartItem.setItemName(quotationItem.getName());
            cartItem.setQuantity(quotationItem.getQuantity());
            cartItem.setPublicProduct(quotationItem.getProducts());
            cartItem.setProductId(quotationItem.getProducts().getId());
            cartItem.setSalesPrice(quotationItem.getProducts().getSalesPrice());
            cartRequest.getCartItems().add(cartItem);
        }
    }

    public void checkout() {
        makeCreditCardRequest();
    }

    private void preparePaymentRequest(PaymentRequest paymentRequest) {
        var customer = loginBean.getLoginObject().getCustomer();
        var country = countryBean.getCountryFromId(customer.getCountryId());
        paymentRequest.setSalesType('C');
        paymentRequest.setPaymentMethod('C');
        paymentRequest.setCustomerId(customer.getId());
        paymentRequest.setBaseAmount(cartRequest.getSubTotal());
        paymentRequest.setPromoDiscount(cartRequest.getTotalDiscount());
        paymentRequest.setVatPercentage(AppConstants.VAT_PERCENTAGE);
        paymentRequest.setPromoId(cartRequest.getDiscountId() == null ? 0 : cartRequest.getDiscountId().intValue());
        paymentRequest.setCountryId(country.getId());
        paymentRequest.setCurrency("SAR");
        paymentRequest.setThreeDSecure(true);
        paymentRequest.setDescription("Payment Order");
        paymentRequest.setCountry(country.getName());
        paymentRequest.setFirstName(customer.getFirstName());
        paymentRequest.setLastName(customer.getLastName());
        paymentRequest.setEmail(customer.getEmail());
        paymentRequest.setCountryCode(country.getCountryCode());
        paymentRequest.setMobile(customer.getMobile());
        paymentRequest.setClientIp(getClientIp());
    }

    private String getClientIp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }


    private void makeCreditCardRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        preparePaymentRequest(paymentRequest);
        cartRequest.setPaymentRequest(paymentRequest);
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_CREDIT_CARD, cartRequest);
        System.out.println(r.getStatus());
        if (r.getStatus() == 202) {
            cartRequestResponse = r.readEntity(CartRequestResponse.class);
            Helper.redirect(cartRequestResponse.getTransactionUrl());
        } else if (r.getStatus() == 200) {
            //successful transaction
            cartRequestResponse = r.readEntity(CartRequestResponse.class);
            paymentFailed = false;
            stage = 5;
            Helper.redirect("checkout");
        } else {
            Helper.addErrorMessage("لم تتم عملية الدفع بنجاح, الرجاء المحاولة مرة اخرى");
        }
    }


    private void makeWireTransferRequest() {
        Response r = reqs.postSecuredRequest(AppConstants.POST_CART_WIRE_TRANSFER, cartRequest);
        if (r.getStatus() == 200) {
            cartRequestResponse = r.readEntity(CartRequestResponse.class);
            stage = 4;
            Helper.redirect("checkout");
        } else {
            Helper.addErrorMessage("Something went wrong please try again later");
        }
    }


    public void processPaymentResponse() {
        try {
            String tapChargeId = Helper.getParam("tap_id");
            Map<String, String> map = new HashMap<>();
            map.put("chargeId", tapChargeId);
            if(tapChargeId == null ){
                paymentFailed = true;
                //throw  an error
            }
            Response r = reqs.putSecuredRequest(AppConstants.PUT_PAYMENT_REQUEST, map);
            if (r.getStatus() == 200) {
                //update cart status and fund wallet
                paymentFailed = false;
                stage = 5;
                updateCartStatus("paid");
            } else if (r.getStatus() == 409) {
                //this is duplicate
                paymentFailed = false;
            } else {
                paymentFailed = true;
                Helper.addErrorMessage("لم تتم عملية الدفع بنجاح, الرجاء المحاولة مرة اخرى");
                updateCartStatus("failed");
            }

        } catch (Exception ex) {

        }
        finally {
            Helper.redirect("checkout");
        }
    }

    private void updateCartStatus(String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("cartId", this.cartRequestResponse.getCartId());
        map.put("paymentStatus", status);
        Response r = reqs.putSecuredRequest(AppConstants.PUT_CART_PAYMENT, map);
    }


    public CartRequest getCartRequest() {
        return cartRequest;
    }

    public void setCartRequest(CartRequest cartRequest) {
        this.cartRequest = cartRequest;
    }

    public char getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(char paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isPaymentFailed() {
        return paymentFailed;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public CartRequestResponse getCartRequestResponse() {
        return cartRequestResponse;
    }

    public String getPromoCodeQuery() {
        return promoCodeQuery;
    }

    public void setPromoCodeQuery(String promoCodeQuery) {
        this.promoCodeQuery = promoCodeQuery;
    }

    public boolean isPromoVerified() {
        return promoVerified;
    }

    public void setPromoVerified(boolean promoVerified) {
        this.promoVerified = promoVerified;
    }

    public boolean isHavePromo() {
        return havePromo;
    }

    public void setHavePromo(boolean havePromo) {
        this.havePromo = havePromo;
    }
}
