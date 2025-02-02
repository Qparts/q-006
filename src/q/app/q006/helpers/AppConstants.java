package q.app.q006.helpers;
 
public class AppConstants {

	private final static String PUBLIC_QUOTATION_SERVICE = SysProps.getValue("quotationPublicService");
	private final static String PUBLIC_VEHICLE_SERVICE = SysProps.getValue("vehiclePublicService");
	private final static String PUBLIC_CUSTOMER_SERVICE = SysProps.getValue("customerPublicService");
	private final static String PUBLIC_CART_SERVICE = SysProps.getValue("cartPublicService");
	private final static String PUBLIC_LOCATION_SERVICE = SysProps.getValue("locationPublicService");
	private final static String INVOICE_SERVICE = SysProps.getValue("invoiceService");
	public final static String APP_SECRET = SysProps.getValue("appSecret");
	public final static double VAT_PERCENTAGE = 0.05;
	public final static double DELIVERY_FEES = 35;

	//===============HOST=================//
	
	//for server
	public final static String APP_HOST = "https://www.qetaa.com/";
	public final static String HOST_ESCAPED = "https://www.qetaa.com/";

	//===============FACEBOOK=================//
	public final static String PAGE_SUCCESSFUL = APP_HOST + "successful";

	//===============VENDORS====================//
	public final static String POST_JOIN_US = /*VENDOR_SERVICE + */"vendor-join-request";


	/////////// INVOICE SERVICE CALLS ///////////
	public final static String PUT_PAYMENT_REQUEST = INVOICE_SERVICE + "payment-order";


	//////////// CART SERVICE CALLS /////////////
	public final static String POST_CART_CREDIT_CARD = PUBLIC_CART_SERVICE + "cart/credit-card";
	public final static String POST_CART_WIRE_TRANSFER = PUBLIC_CART_SERVICE + "cart/wire-transfer";
	public final static String PUT_CART_PAYMENT = PUBLIC_CART_SERVICE + "cart-payment";
	public final static String GET_BANKS = PUBLIC_CART_SERVICE + "banks";
	public final static String getPromotionCodeFromCode(String code) {
		return PUBLIC_CART_SERVICE + "discount/promocode/" + code;
	}
	public static String getLiveWallet(long customerId){
		return PUBLIC_CART_SERVICE + "wallet-amount/" + customerId;
	}

	//////////// AWS CALLS /////////////////////////
	public static final String AWS_REGION = "eu-central-1";


	//////////// VEHICLE SERVICE CALLS /////////////
	public final static String GET_ACTIVE_MAKES = PUBLIC_VEHICLE_SERVICE + "makes";

	////////////// CUSTOMER SERVICE CALLS /////////
	public final static String POST_EMAIL_LOGIN = PUBLIC_CUSTOMER_SERVICE + "login";
	public final static String POST_SIGNUP_SMS = PUBLIC_CUSTOMER_SERVICE + "request-signup-code";
	public final static String POST_SIGNUP= PUBLIC_CUSTOMER_SERVICE + "signup/qetaa";
	public final static String POST_FACEBOOK_LOGIN = PUBLIC_CUSTOMER_SERVICE + "login/facebook";
	public final static String POST_ADDRESS = PUBLIC_CUSTOMER_SERVICE + "address";
	public final static String POST_RESET_PASSWORD_SMS = PUBLIC_CUSTOMER_SERVICE + "reset-password/sms";
	public final static String PUT_RESET_PASSWORD = PUBLIC_CUSTOMER_SERVICE + "reset-password/sms";
	public final static String POST_CODE_LOGIN = PUBLIC_CUSTOMER_SERVICE + "code-login";


	//////////// LOCATION SERVICE CALLS ///////////
	public final static String GET_ACTIVE_COUNTRIES = PUBLIC_LOCATION_SERVICE + "countries";



	//////////// QUOTATION SERVICE CALLS /////////////
	public final static String POST_CREATE_QUOTATION_WIRE = PUBLIC_QUOTATION_SERVICE + "quotation/wire-transfer";
	public final static String POST_CREATE_QUOTATION_FREE = PUBLIC_QUOTATION_SERVICE + "quotation/free";
	public final static String POST_CREATE_QUOTATION_CC = PUBLIC_QUOTATION_SERVICE + "quotation/credit-card";
	public final static String PUT_QUOTATION_PAYMENT = PUBLIC_QUOTATION_SERVICE + "quotation/payment";
	public final static String PUT_CLOSE_QUOTATION = PUBLIC_QUOTATION_SERVICE + "close-quotation";

	public final static String getQuotation(long quotationId) {
		return PUBLIC_QUOTATION_SERVICE + "quotation/" + quotationId;
	}
	public final static String getPendingQuotations(long customerId){
		return PUBLIC_QUOTATION_SERVICE + "quotations/customer/" + customerId + "/pending";
	}
	public final static String getClosedQuotations(long customerId){
		return PUBLIC_QUOTATION_SERVICE + "quotations/customer/" + customerId + "/closed";
	}
	public final static String getCompletedQuotations(long customerId){
		return PUBLIC_QUOTATION_SERVICE + "quotations/customer/" + customerId + "/completed";
	}

	
}
