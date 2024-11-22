package com.fpoly.be_wanren_buffet.security;

public class Endpoints {

    // Public GET endpoints
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/Product/search/findByTypeFood",
            "/api/product/ProductHot",
            "/Product/search/findByProductNameContaining",
            "/Product/**",
            "/Promotion/search/findByPromotionStatus",
            "/Customer/search/existsByUsername",
            "/Customer/search/existsByEmail",
            "/api/payment/create_payment",
            "/api/payment/RollBack_VNPAY",
            "/Customer",
            "/Orders/**",
            "/Order_detail/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/orders/GetOrderByCustomerId/**",
            "/Customer/search",
            "/User",
            "/User/search",
            "/Table",
            "/Account",
            "/Promotion",
            "/User/admins",
            "/User/{id}/roles",

            "/work-shift",
            "/work-schedules/{date}",

            //Staff
            "/Table/**",
            "/api/order_staff/findOrderIdByTableId/**",
            "/api/product/by-category/**",
            "/api/order_staff/**",
            "/api/orders_detail_staff/**",
            "/api/order_staff/status/**",
            "/api/product/**"

    };

    // Public POST endpoints
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/customer/register",
            "/api/customer/login",
            // STAFF
            "/api/order_staff/add",
            "/api/orders_detail_staff/add_or_update/**"
            // STAFF
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            // STAFF
            "/api/order_staff/**",
            "/api/table/**",
            // STAFF
    };

    // Private POST endpoints
    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/api/orders"
    };

    // Public PATCH endpoints (currently empty)
    public static final String[] PUBLIC_PATCH_ENDPOINTS = {};

    // Private PATCH endpoints
    public static final String[] PRIVATE_PATCH_ENDPOINTS = {
            "/Product",
            "/Product/{productId}"
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/Customer/**"
    };

    // Public PORT endpoints
    public static final String[] PUBLIC_PORT_ENDPOINTS = {
            "/api/customer/register",
            "/api/customer/login",

            "/api/review/Creact_review",
            "/api/forgot-password/request",
            "/api/otp/validate",
            "/api/reset-password/update",
            "/api/user/login"
    };

    // Private Customer GET endpoints
    public static final String[] PRIVATE_CUSTOMER_GET_ENDPOINTS = {
            "/api/orders/GetOrderByCustomerId/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/payment/create_payment"
    };

    // Private Admin GET endpoints
    public static final String[] PRIVATE_GET_ADMIN = {
            "/Customer/create"
    };

    // Private Admin POST endpoints
    public static final String[] PRIVATE_POST_ADMIN = {
            "/Customer/create",
            "/User/create",
            "/Promotion/create",
            "/User/admins/create",
            "/work-schedules/create"
    };

    // Private Admin PATCH endpoints
    public static final String[] PRIVATE_PATCH_ADMIN = {
            "/Customer/update/{id}",
            "/Customer/updateAccountStatus/{id}",
            "/User/update/{id}",
            "/User/updateAccountStatus/{id}",
            "/Promotion/update/{id}",
            "/User/admins/update/{id}",

    };

    // Private Admin PUT endpoints (currently empty)
    public static final String[] PRIVATE_PUT_ADMIN = {};

    // Private Admin DELETE endpoints
    public static final String[] PRIVATE_DELETE_ADMIN = {
            "/Customer/delete/{id}",
            "/User/delete/{id}",
            "/Promotion/delete/{id}",
            "/User/admins/delete/{id}"
    };
}
