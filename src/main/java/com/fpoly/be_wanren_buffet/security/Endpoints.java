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
            "/Customer/**",
            "/Orders/**",
            "Order_detail/**",
            "/Promotion/search/findByPromotionStatus",
            "Promotion/**",
            "/api/payment/RollBack_VNPAY",
    };

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

    public static final String[] PRIVATE_POST_ENDPOINS = {
            "/api/orders"
    };

    public static final String[] PRIVATE_PUT_ENDPOINS = {
            "/Customer/**"
    };

    public static final  String[] PRIVATE_GET_ADMIN = {

    };
}
