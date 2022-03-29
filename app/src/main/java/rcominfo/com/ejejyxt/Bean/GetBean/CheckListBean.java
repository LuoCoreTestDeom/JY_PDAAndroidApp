package rcominfo.com.ejejyxt.Bean.GetBean;

import java.util.List;

/**
 * Created by dell on 2017/1/3.
 */
public class CheckListBean {
    public List<checkOrderInfoReturn> coir;
    public class checkOrderInfoReturn
    {

        public String kd_billcode;
        /// <summary>
        /// 会员名称
        /// </summary>
        public String username;
        /// <summary>
        /// 快递公司
        /// </summary>
        public String kd_com;
        /// <summary>
        /// 商品名称
        /// </summary>
        public String goods;

        public String OrderWeight;

        public String OrderId;
        public String dd_size;
        public String isSensitive;
        public String cforhm;
        public int is_lock;
        public String stockArea;
        //航班时效
        public String timeBarName;
        //承运商
        public String Carrier;
        //收货地址
        public String DeliveryAddress;

    }

}
