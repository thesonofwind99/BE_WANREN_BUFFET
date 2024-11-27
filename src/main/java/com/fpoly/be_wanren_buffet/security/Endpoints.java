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
            "/Customer/**"
    };

    // Public POST endpoints
    public static final String[] PUBLIC_PORT_ENDPOINTS = {
            "/api/customer/register",
            "/api/customer/login",
            "/api/review/Creact_review",
            "/api/forgot-password/request",
            "/api/otp/validate",
            "/api/reset-password/update",
            "/api/user/login"
    };

    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/api/orders"
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/api/customer/**"
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

    public static final String[] PRIVATE_PUT_ADMIN = {};

    // Private Admin DELETE endpoints
    public static final String[] PRIVATE_DELETE_ADMIN = {
            "/api/customer/delete/{id}",
            "/api/user/delete/{id}",
            "/api/promotion/delete/{id}",
            "/api/user/admins/delete/{id}",
            "/api/product/**"
    };
}
