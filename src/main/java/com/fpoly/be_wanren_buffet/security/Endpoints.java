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
            "/User/search"
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/api/customer/register",
            "/api/customer/login",
            "/Customer/create",
            "/User/create",

    };

    public static final String[] PRIVATE_POST_ENDPOINS = {
            "/api/orders",
    };

    public static final String[] PUBLIC_PATCH_ENDPOINS = {
            "/Customer/update/{id}",
            "/Customer/updateAccountStatus/{id}",
            "/User/update/{id}",
            "User/updateAccountStatus/{id}"
    };

    public static final String[] PRIVATE_PUT_ENDPOINS = {
            "/Customer/**"
    };

    public static final String[] PUBLIC_DELETE_ENDPOINS = {
            "/Customer/delete/{id}",
            "/User/delete/{id}"
    };
}
