package rcominfo.com.ejejyxt.Utils;

import com.google.gson.Gson;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.YCJHBean;
import rcominfo.com.ejejyxt.Bean.PostBean.Check_print_Bean;
import rcominfo.com.ejejyxt.Bean.PostBean.FJCCBean;
import rcominfo.com.ejejyxt.Bean.PostBean.FinishTaskBean;
import rcominfo.com.ejejyxt.Bean.PostBean.GetTaskBean;
import rcominfo.com.ejejyxt.Bean.PostBean.JHXJ_FindBean;
import rcominfo.com.ejejyxt.Bean.PostBean.JHXJ_postBean;
import rcominfo.com.ejejyxt.Bean.PostBean.KDJDBean;
import rcominfo.com.ejejyxt.Bean.PostBean.LoginPostBean;
import rcominfo.com.ejejyxt.Bean.PostBean.PushBean;
import rcominfo.com.ejejyxt.Bean.PostBean.RKSJPostBean;
import rcominfo.com.ejejyxt.Bean.StopPicking;
import rcominfo.com.ejejyxt.Bean.TaskbackBean;


/**
 * Created by dell on 2016/12/13.
 */
public class JsonCreate {

    static Gson gson = new Gson();
    public static String Json_login(String uid,String pwd,String code){
        LoginPostBean loginPostBean = new LoginPostBean();
        loginPostBean.setUid(uid);
        loginPostBean.setPwd(pwd);
        loginPostBean.code = code;
        code = code;
        return gson.toJson(loginPostBean);
    }

    public static String RKSJ_json(String place_code,String billcode,String in_type,String emp,String wavehouse_name){
        RKSJPostBean rksjPostBean = new RKSJPostBean();
        rksjPostBean.setPlace_code(place_code);
        rksjPostBean.setBillcode(billcode);
        rksjPostBean.setIn_type(in_type);
        rksjPostBean.setEmp(emp);
        rksjPostBean.setWavehouse_name(wavehouse_name);
        return  gson.toJson(rksjPostBean);
    }

    public static  String JHXJ_Parent (String out_barcode){
        JHXJ_postBean jhxj_postBean = new JHXJ_postBean();
        jhxj_postBean.setOut_barcode(out_barcode);
        return gson.toJson(jhxj_postBean);
    }

    public static String JHXJfind (String out_barcode,String kd_billcode,String scan_emp){
        JHXJ_FindBean findBean = new JHXJ_FindBean();
        findBean.setOut_barcode(out_barcode);
        findBean.setKd_billcode(kd_billcode);
        findBean.setSc(scan_emp);

        return  gson.toJson(findBean);
    }

    public static String FJCC(String out_barcode,String scan_site,String scan_emp,String next_site,String packno){
            FJCCBean fjcc = new FJCCBean();
        fjcc.out_barcode = out_barcode;
        fjcc.scan_site = scan_site;
        fjcc.scan_emp = scan_emp;
        fjcc.next_site = next_site;
        fjcc.packno = packno;
        return gson.toJson(fjcc);
    }

    public static String KDJD(String KD_com,String billcode,String scan_site,String scan_emp){
        KDJDBean kdjdBean = new KDJDBean();
        kdjdBean.setKD_com(KD_com);
        kdjdBean.setBillcode(billcode);
        kdjdBean.setScan_site(scan_site);
        kdjdBean.setScan_emp(scan_emp);
        return gson.toJson(kdjdBean);
    }

    public static String StopPicking(String out_billcode, String oper){
        StopPicking stopPicking = new StopPicking();
        stopPicking.out_billcode = out_billcode;
        stopPicking.oper = oper;
        return gson.toJson(stopPicking);
    }

    public static String requestPicking(String areaCode, String Operator){
        GetTaskBean getTaskBean = new GetTaskBean();
        getTaskBean.areaCode = areaCode;
        getTaskBean.Operator = Operator;
        return gson.toJson(getTaskBean);
    }
    public static String requestPick(String areaCode, String Operator,String OrderID,String site,int shopId){
        GetTaskBean getTaskBean = new GetTaskBean();
        getTaskBean.areaCode = areaCode;
        getTaskBean.Operator = Operator;
        getTaskBean.OrderID = OrderID;
        getTaskBean.site = site;
        getTaskBean.shopId = shopId;
        return gson.toJson(getTaskBean);
    }

    public static String Print(String express,String operateMan, String operateSite, String orderID, ArrayList<Check_print_Bean.kdcom_billcode> PackaginBillcode,String weight,int is_Big){
        Check_print_Bean checkPrintBean = new Check_print_Bean();
        checkPrintBean.operateMan = operateMan;
        checkPrintBean.operateSite =operateSite;
        checkPrintBean.orderID = orderID;
        checkPrintBean.PackaginBillcode=PackaginBillcode;
        checkPrintBean.express = express;
        checkPrintBean.weight= weight;
        checkPrintBean.is_Big = is_Big;
        return gson.toJson(checkPrintBean);
    }

    public static String FinishTask(String out_barcode,String scan_emp,String scan_site){
        FinishTaskBean finishTaskBean = new FinishTaskBean();
        finishTaskBean.out_barcode = out_barcode;
        finishTaskBean.scan_emp = scan_emp;
        finishTaskBean.scan_site = scan_site;

        return gson.toJson(finishTaskBean);
    }

    public static String PrintAgain(String operateMan, String operateSite,String repair){
        Check_print_Bean checkPrintBean = new Check_print_Bean();
        checkPrintBean.operateMan = operateMan;
        checkPrintBean.operateSite =operateSite;
        checkPrintBean.repair = repair;

        return gson.toJson(checkPrintBean);
    }

    public static String PushMsg(String billcode , String ErrorType,String Operation_emp){
        PushBean pushBean = new PushBean();
        pushBean.billcode = billcode;
        pushBean.ErrorType = ErrorType;
        pushBean.Operation_emp = Operation_emp;

        return gson.toJson(pushBean);
    }

    public static String TaskBack(String order_code){
        TaskbackBean bean = new TaskbackBean();
        bean.order_code = order_code;

        return gson.toJson(bean);
    }

    public static String YCJHList(String operator,String site){
        YCJHBean ycjhBean = new YCJHBean();
        ycjhBean.Operator = operator;
        ycjhBean.site = site;
        return gson.toJson(ycjhBean);
    }
}
