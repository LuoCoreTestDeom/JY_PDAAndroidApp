package rcominfo.com.ejejyxt.Bean.PostBean;

/**
 * Created by dell on 2016/12/13.
 */
public class KDJDBean {

    public  String KD_com;
    public  String billcode;
    public  String scan_site;
    public  String scan_emp;

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public void setKD_com(String KD_com) {
        this.KD_com = KD_com;
    }

    public void setScan_emp(String scan_emp) {
        this.scan_emp = scan_emp;
    }

    public void setScan_site(String scan_site) {
        this.scan_site = scan_site;
    }

    public String getBillcode() {
        return billcode;
    }

    public String getKD_com() {
        return KD_com;
    }

    public String getScan_emp() {
        return scan_emp;
    }

    public String getScan_site() {
        return scan_site;
    }
}
