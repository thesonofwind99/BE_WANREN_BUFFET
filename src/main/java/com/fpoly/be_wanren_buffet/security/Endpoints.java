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
            "/orders/**",
            "Order_detail/**"

    };

    public static final String[] PUBLIC_PORT_ENDPOINS = {
            "/api/customer/register",
            "/api/customer/login",
            "/api/payment/RollBack_VNPAY"
    };

    public static final String[] PRIVATE_POST_ENDPOINS = {
            "/api/orders"
    };


}
