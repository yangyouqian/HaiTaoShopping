package com.bishe.haitaoshopping;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

/**
 * Created by yhviews on 2019/3/4.
 * 所有常量统一定义的地方
 */

public class Constant {
    public static final String USER_NAME = "name";
    public static final String DB_SHOP_BRAND = "ShopBrand";
    public static final String DB_SHOP_WEBSITE = "ShopWebsite";
    public static final String DB_SHOP_TYPE = "ShopType";
    public static final String DB_SHOP_PRICE = "ShopPrice";
    public static final String DB_JOIN_SHOP = "Participation";

    /**
     * 参与拼单状态
     * 0 等待同意
     * 1 已同意
     * 2 发起者下单成功
     *    2001等待用户付款 2002用户付款成功
     * 3 拼单失败
     *    3001人数未达到 3002不想买了
     * 4 拼单成功
     * 5 拒绝
     */
    //获取分类的优惠信息列表
    public static final String URL_PRODUCT_LIST = "http://www.bacaoo.com/bacaoo/bacaoo_service_api/web/webservice/4.0.0/system_service.php?action=get_index_list";
    //获取首页最新的优惠信息列表
    public static final String URL_HOME_PAGE_LIST = "http://www.bacaoo.com/bacaoo/bacaoo_service_api/web/webservice/4.0.0/system_service.php?action=get_home_page";
    //优惠详情
    public static final String URL_PRODUCT_DETAIL = "http://www.bacaoo.com/bacaoo/bacaoo_service_api/web/webservice/4.0.0/system_service.php?action=get_product_detail";
    //海淘攻略
    public static final String URL_RAIDERS_LIST = "http://www.bacaoo.com/bacaoo/bacaoo_service_api/web/webservice/4.0.0/system_service.php?action=get_info_list";
    //海淘攻略详情
    public static final String URL_RAIDERS_DETAIL = "http://www.bacaoo.com/bacaoo/bacaoo_service_api/web/webservice/4.0.0/system_service.php?action=get_info_detail";
    public static final String URL_SEND_MESSAGE = "https://ky3ey67w.api.lncld.net/1.2/rtm/service-conversations/5cbd4e62d376160065596e59/messages";

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_RANGE = "page_range";
    public static final String PARAM_CAT_TYPE = "cat_type";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_INFO_ID = "info_id";
    public static final String PARAM_FROM_CLIENT = "from_client";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_TO_CLIENT = "to_clients";

    public static final String VALUE_RECOMMEND = "new";//首页最新
    public static final String VALUE_MAKE_UP = "15";//个护化妆
    public static final String VALUE_CLOCK = "21";//礼品钟表
    public static final String VALUE_BAG = "16";//服饰鞋包
    public static final String VALUE_PAGE_RANGE = "20";
    public static final String VALUE_FROM_CLIENT = "系统通知";


    public static final int REQUEST_CODE_LOGIN_BACK = 1;
    public static final int REQUEST_CODE_CREATE_SHOP = 2;
    public static final int REQUEST_CODE_CREATE_CHAT = 3;
    public static final int REQUEST_CODE_JOIN_SHOP = 4;
    public static final int REQUEST_CODE_UPDATE_SHOP = 5;

    public static final int DEFAULT_SELECT_TAB_INDEX = 0;

    public static final String HEADER_PARAM_ID = "X-LC-Id";
    public static final String HEADER_VALUE_ID = "KY3ey67wou6y9lq4qMja1BOg-gzGzoHsz";
    public static final String HEADER_PARAM_KEY= "X-LC-Key";
    public static final String HEADER_VALUE_KEY= "ncWRgQRlYdRLDrXg1VKBgFAL";
    public static final String HEADER_VALUE_KEY_MASTER= "eokKUz8VmVDvQJ5RMvVvkjLg,master";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
}
