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
            "/Customer/**",
            "/Orders/**",
            "Order_detail/**",
            "/api/orders/GetOrderDetailByOrderId/**",
            "/api/orders/GetOrderByCustomerId/**",
            "/api/table/tables",
            "/Table",
            "/api/product/by-category",
            "/api/orders_detail_staff/**",
            "/api/orders_detail_staff/add_or_update/**",
            "/api/product/**",
            "/api/order_staff/findOrderIdByTableId/**",
            "/api/order_staff/status/**",
            "/User",
            "/User/findByUsername"
    };

    public static final String[] PUBLIC_PORT_ENDPOINS = {
            "/api/customer/register",
            "/api/customer/login",
            "/api/order_staff/add",
            "/api/orders_detail_staff/add/**",
            "/api/orders_detail_staff/add_or_update/**"
    };

    public static final String[] PRIVATE_POST_ENDPOINS = {
            "/api/orders",
            "/api/order_staff/add",
            "/api/orders_detail_staff/add/**",
            "/api/orders_detail_staff/add_or_update/**",
    };

    public static final String[] PRIVATE_PUT_ENDPOINS = {
        "/Customer/**",
            "/api/order_staff/add",
            "/api/orders_detail_staff/add/**",
            "/api/table/**",
            "/api/order_staff/**"
    };


}
