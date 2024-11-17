package com.fpoly.be_wanren_buffet.security;

public class Endpoints {
    public static final String[] PUBLIC_GET_ENDPOINS = {
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
            "/User/{id}/roles"

    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/api/customer/register",
            "/api/customer/login",

    };

    public static final String[] PRIVATE_POST_ENDPOINS = {
            "/api/orders",
    };

    public static final String[] PUBLIC_PATCH_ENDPOINS = {


    };
    public static final String[] PRIVATE_PATCH_ENDPOINS = {
            "/Product",
            "/Product/{productId}"

    };
    public static final String[] PRIVATE_PUT_ENDPOINS = {
            "/Customer/**"
    };

    public static final String[] PUBLIC_DELETE_ENDPOINS = {

    };

//            "/Customer/**",
//            "/Orders/**",
//            "Order_detail/**",
//            "/Promotion/search/findByPromotionStatus",
//            "Promotion/**",
//            "/api/payment/RollBack_VNPAY",


    public static final String[]    PUBLIC_PORT_ENDPOINS = {
            "/api/customer/register",
            "/api/customer/login",
            "/api/review/Creact_review",
            "/api/forgot-password/request",
            "/api/otp/validate",
            "/api/reset-password/update",
            "/api/user/login"
    };


    public static final String[] PRIVATE_CUSTOMER_GET_ENDPOINS = {
            "/api/orders/GetOrderByCustomerId/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/payment/create_payment",

    };


    public static final  String[] PRIVATE_GET_ADMIN = {
            "/User",
            "/User/**"
    };
    public static final String[] PRIVATE_POST_ADMIN= {
            "/Customer/create",
            "/User/create",
            "/Promotion/create",
            "/User/admins/create",
    };
    public static final String[] PRIVATE_PATCH_ADMIN= {
            "/Customer/update/{id}",
            "/Customer/updateAccountStatus/{id}",
            "/User/update/{id}",
            "/User/updateAccountStatus/{id}",
            "/Promotion/update/{id}",
            "/User/admins/update/{id}",
    };
    public static final String[] PRIVATE_PUT_ADMIN= {
    };
    public static final String[] PRIVATE_DELETE_ADMIN= {
            "/Customer/delete/{id}",
            "/User/delete/{id}",
            "/Promotion/delete/{id}",
            "/User/admins/delete/{id}",
    };
}

