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
            "/Promotion/**",
            "/User/admins",
            "/User/{id}/roles",
            "/Customer/search/existsByPhoneNumber",
            "/User/**",
            "/checkout"


    };

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
            "/Customer/**"
    };






    // Private Customer GET endpoints
    public static final String[] PRIVATE_CUSTOMER_GET_ENDPOINTS = {
            "/api/orders/GetOrderByCustomerId/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/payment/create_payment"
    };

    public static final String[] PRIVATE_GET_ADMIN = {
            "/Customer/create",
            "/api/statistical/**",
    };

    // Private Admin POST endpoints
    public static final String[] PRIVATE_POST_ADMIN = {
            "/Customer/create",
            "/User/create",
            "/Promotion/create",
            "/User/admins/create",
            "/Product",
            "/Product/**",
            "/Product"
    };

    // Private Admin PATCH endpoints
    public static final String[] PRIVATE_PATCH_ADMIN = {
            "/Customer/update/{id}",
            "/Customer/updateAccountStatus/{id}",
            "/User/update/{id}",
            "/User/updateAccountStatus/{id}",
            "/Promotion/update/{id}",
            "/User/admins/update/{id}"
    };


    public static final String[] PRIVATE_PUT_ADMIN = {};

    // Private Admin DELETE endpoints
    public static final String[] PRIVATE_DELETE_ADMIN = {
            "/Customer/delete/{id}",
            "/User/delete/{id}",
            "/Promotion/delete/{id}",
            "/User/admins/delete/{id}",
            "/Product/**"
    };
}
