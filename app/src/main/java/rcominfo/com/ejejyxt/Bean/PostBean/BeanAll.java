package rcominfo.com.ejejyxt.Bean.PostBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/12/13.
 */
public class BeanAll
{
    public class LoginReturn
    {
        /// <summary>
        /// 用户名
        /// </summary>
        public String username;
        /// <summary>
        /// 昵称
        /// </summary>
        public String nickname;
        /// <summary>
        /// 库位id
        /// </summary>
        public String houseid;
        /// <summary>
        /// 库位名称
        /// </summary>
        public String house_name;
        /// <summary>
        /// 库位类型
        /// </summary>
        public String house_type;

        public String Uid;
    }

    public class Inplace
    {
        /// <summary>
        /// 库位号
        /// </summary>
        public String place_code;
        /// <summary>
        /// 快递单号
        /// </summary>
        public String billcode;
        /// <summary>
        /// 上架人
        /// </summary>
        public String emp;
        /// <summary>
        /// 上架或盘点
        /// </summary>
        public int in_type;
        /// <summary>
        /// 仓库名称
        /// </summary>
        public String wavehouse_name;
    }


    /// <summary>
    ///
    /// </summary>
    public class pickingShelvesList
    {
        public ArrayList<OffShelfRuturn> OffShelfRuturn;
    }
    public class OffShelfRuturn
    {


        /// <summary>
        /// 发货单号
        /// </summary>
        public String out_barcode;
        public String OrderId;
        /// <summary>
        /// 快递单号
        /// </summary>
        public String kd_billcode;
        /// <summary>
        /// 货架
        /// </summary>
        public String stock_area;
        /// <summary>
        /// 包数
        /// </summary>
        public String number;
        /// <summary>
        /// 客户名称
        /// </summary>
        public String username;
        /// <summary>
        /// 重量
        /// </summary>
        public String dd_weight2;
        /// <summary>
        /// 规格
        /// </summary>
        public String guige;
        /// <summary>
        /// 是否到达
        /// </summary>
        public String is_inplace;
        //货物类型
        public String goodsType;
        //货物名称
        public String OrderGoods;
        //承运商
        public String carrier;
        //备注
        public String rem;

        public  String BillGoods;

        public double BillWeight;

    }


    /// <summary>
    /// 快递单号拣货
    /// </summary>
    public class expressJH
    {
        /// <summary>
        /// 发货单号
        /// </summary>
        public String out_barcode;
        /// <summary>
        /// 快递单号
        /// </summary>
        public String kd_billcode;
        /// <summary>
        /// 扫描人
        /// </summary>
        public String scan_emp;
    }

    /// <summary>
    /// 拣货完成
    /// </summary>
    public class pickingAchieve
    {
        /// <summary>
        /// 发货单号
        /// </summary>
        public String out_barcode;
        /// <summary>
        /// 扫描站点
        /// </summary>
        public String scan_site;
        /// <summary>
        /// 扫描人
        /// </summary>
        public String scan_emp;
    }

    /// <summary>
    /// 发件出库
    /// </summary>
    public class sendGoods
    {
        /// <summary>
        /// 发货单号
        /// </summary>
        public String out_barcode;
        /// <summary>
        /// 扫描站点
        /// </summary>
        public String scan_site;
        /// <summary>
        /// 扫面人
        /// </summary>
        public String scan_emp;
        /// <summary>
        /// 下一站
        /// </summary>
        public String next_site;
        /// <summary>
        /// 包号
        /// </summary>
        public String packno;
    }

    /// <summary>
    /// 自提点上架
    /// </summary>
    public class Putaway
    {
        /// <summary>
        /// 位置
        /// </summary>
        public String place_code;
        /// <summary>
        /// 快递单号
        /// </summary>
        public String billcode;
        /// <summary>
        /// 扫描人
        /// </summary>
        public String emp;
        /// <summary>
        /// 仓库名称
        /// </summary>
        public String wavehouse_name;
    }



    /// <summary>
    ///
    /// </summary>
    public class houseInfoList
    {
        public List<houseInfo> houseInfo;
    }
    /// <summary>
    /// 库位信息
    /// </summary>
    public class houseInfo
    {
        public String ID;
        /// <summary>
        /// 库位名称
        /// </summary>
        public String house_name;
        /// <summary>
        /// 库位类型
        /// </summary>
        public String house_type;

    }



    /// <summary>
    ///
    /// </summary>
    public class ztPriceList
    {
        public List<ztPrice> ztPrice;
    }
    /// <summary>
    /// 自提点名称
    /// </summary>
    public class ztPrice
    {
        /// <summary>
        /// 自提点名称
        /// </summary>
        public String type_name;
    }




    /// <summary>
    ///
    /// </summary>
    public class Kd_comInfoList
    {
        public List<Kd_comInfo> Kd_comInfo;
    }
    /// <summary>
    /// 快递公司信息
    /// </summary>
    public class Kd_comInfo
    {
        /// <summary>
        /// 快递公司ID
        /// </summary>
        public int id;
        /// <summary>
        /// 快递公司名称
        /// </summary>
        public String Kd_com;
        /// <summary>
        /// 国家ID
        /// </summary>
        public String country_id;
        /// <summary>
        /// 简码
        /// </summary>
        public String jym;
    }



    public class kd_jd
    {
        /// <summary>
        /// 快递公司
        /// </summary>
        public String KD_com;
        /// <summary>
        /// 快递单号
        /// </summary>
        public String billcode;
        /// <summary>
        /// 扫描站点
        /// </summary>
        public String scan_site;
        /// <summary>
        /// 扫描人
        /// </summary>
        public String scan_emp;
    }

}
