package rcominfo.com.ejejyxt.Bean.PostBean;

import java.util.List;

/**
 * Created by dell on 2017/1/4.
 */
public class Check_print_Bean {
    /// <summary>
    /// 订单ID
    /// </summary>
    public String orderID;

    public String express;
    /// <summary>
    /// 操做人
    /// </summary>
    public String operateMan;

    public String weight;
    /// <summary>
    /// 操作站点
    /// </summary>
    public String operateSite;
    /// <summary>
    /// 打包快递单号List
    /// </summary>
    public List<kdcom_billcode> PackaginBillcode;

    public String repair;
    public int is_Big;

    public class kdcom_billcode{
        public  String billcode;
        public  String kd_com;
    }


}

