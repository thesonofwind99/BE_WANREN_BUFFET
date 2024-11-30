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
            "/Reservation/**",

            "/work-shift",
            "/work-schedules/{date}"
    };

    // Public POST endpoints
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/customer/register",
            "/api/customer/login",
            //CUSTOMER
            "/api/reservation/create"
    };

    public static final String[] PUBLIC_PUT_ENDPOINTS = {
            // STAFF
            "/api/customer/loyal_point/**",
            // STAFF
    };

    // Private POST endpoints
    public static final String[] PRIVATE_POST_ENDPOINTS = {
            "/api/orders"
    };

    // Public PATCH endpoints (currently empty)
    public static final String[] PUBLIC_PATCH_ENDPOINTS = {};

    // Private PATCH endpoints
    public static final String[] PRIVATE_PATCH_CASHIER = {
            "/Product",
            "/Product/{productId}",
            "/Orders/{orderId}",
            "/Orders/{orderId}/orderDetails",
    };

    public static final String[] PRIVATE_PUT_CASHIER = {
            "/Orders/{orderId}/updateOrder",
    };

    public static final String[] PRIVATE_PUT_ENDPOINTS = {
            "/Customer/**",
    };

    // Public PORT endpoints
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

    // Private Admin GET endpoints
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

    // Private Admin PUT endpoints (currently empty)
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
