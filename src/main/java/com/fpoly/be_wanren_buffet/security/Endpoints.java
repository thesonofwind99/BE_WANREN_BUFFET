package com.fpoly.be_wanren_buffet.security;

public class Endpoints {

    // Public GET endpoints
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/product/search/findByTypeFood",
            "/api/product/ProductHot",
            "/api/product/search/findByProductNameContaining",
            "/api/product/**",
            "/api/promotion/search/findByPromotionStatus",
            "/api/customer/existsByUsername",
            "/api/customer/existsByEmail",
            "/api/payment/create_payment",
            "/api/payment/RollBack_VNPAY",
            "/api/customer",
            "/api/orders/**",
            "/api/order_detail/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/orders/GetOrderByCustomerId/**",
            "/api/customer/search",
            "/api/user",
            "/api/user/search",
            "/api/table",
            "/api/account",
            "/api/promotion/**",
            "/api/user/admins",
            "/api/user/{id}/roles",
            "/api/customer/search/existsByPhoneNumber",
            "/api/user/**",
            "/api/checkout",
            "/api/statistical/**",
            "/api/register",
            "/Customer/**",
            "/Category",
            "/Product",
            "/Product/**",
            "/Category/**",
            "/api/payment/callbck_qrcode/**",
            "/oauth2/authorization/google"

    };

    // Public POST endpoints
    public static final String[] PUBLIC_PORT_ENDPOINTS = {
            "/api/customer/register",
            "/api/customer/login",
            "/api/review/Creact_review",
            "/api/forgot-password/request",
            "/api/otp/validate",
            "/api/reset-password/update",
            "/api/user/login",
    };

    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/api/orders"
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/api/customer/**",

    };

    // Private Customer GET endpoints
    public static final String[] PRIVATE_CUSTOMER_GET_ENDPOINTS = {
            "/api/orders/GetOrderByCustomerId/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/payment/create_payment"
    };

    public static final String[] PRIVATE_GET_ADMIN = {
            "/api/customer/create",
            "/api/statistical/**",
    };

    // Private Admin POST endpoints
    public static final String[] PRIVATE_POST_ADMIN = {
            "/api/customer/create",
            "/api/user/create",
            "/api/promotion/create",
            "/api/user/admins/create",
            "/api/product",
            "/api/product/**",
            "/api/product"
    };

    // Private Admin PATCH endpoints
    public static final String[] PRIVATE_PATCH_ADMIN = {
            "/api/customer/update/{id}",
            "/api/customer/updateAccountStatus/{id}",
            "/api/user/update/{id}",
            "/api/user/updateAccountStatus/{id}",
            "/api/promotion/update/{id}",
            "/api/user/admins/update/{id}"
    };

    public static final String[] PRIVATE_PUT_ADMIN = {
            "/Product/**",
            "/api/product/UpdateCategory"
    };

    // Private Admin DELETE endpoints
    public static final String[] PRIVATE_DELETE_ADMIN = {
            "/api/customer/delete/{id}",
            "/api/user/delete/{id}",
            "/api/promotion/delete/{id}",
            "/api/user/admins/delete/{id}",
            "/api/product/**"
    };

    // Private STAFF GET endpoints
    public static final String[] PRIVATE_GET_STAFF = {
            //Staff
            "/Table",
            "/Table/**",
            "/api/order_staff/findOrderIdByTableId/**",
            "/api/product/by-category/**",
            "/api/order_staff/**",
            "/api/orders_detail_staff/**",
            "/api/order_staff/status/**",
            "/api/product/**",
            "/api/promotions/info/**",
            "/api/customer/loyalty-points",
            "/api/table/status/**",

    };

    // Private STAFF POST endpoints
    public static final String[] PRIVATE_POST_STAFF = {
            // STAFF
            "/api/order_staff/add",
            "/api/orders_detail_staff/add_or_update/**",
            "/api/payment/create_payment/normal",
            "/api/payment/submit_order_vnpay"
            // STAFF
    };

    public static final String[] PRIVATE_PUT_STAFF = {
            // STAFF
            "/api/order_staff/update-status/**",
            "/api/order_staff/**",
            "/api/table/**",
            "/api/orders_detail_staff/quantity-update",
            "/api/customer/loyal_point/**",
            "/api/customer/update-loyalty-points",
            // STAFF
    };
}
