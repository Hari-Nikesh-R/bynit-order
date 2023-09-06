package com.dosmartie.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Urls {
    public static final String ORDER_ID_PARAM = "/{orderId}";
    public static final String BILL = "/bill";
    public static final String ORDER = "/order";
    public static final String ALL = "/all";
    public static final String ADMIN_ALL = "/admin/all";
    public static final String UNRATED = "/unrated";
}
