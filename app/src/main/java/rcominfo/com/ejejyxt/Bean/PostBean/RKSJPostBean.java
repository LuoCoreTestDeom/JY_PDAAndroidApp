package rcominfo.com.ejejyxt.Bean.PostBean;

/**
 * Created by dell on 2016/12/13.
 */
public class RKSJPostBean {
    public String place_code;
    public String billcode;
    public String in_type;
    public String emp;
    public String wavehouse_name;

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public void setIn_type(String in_type) {
        this.in_type = in_type;
    }

    public void setPlace_code(String place_code) {
        this.place_code = place_code;
    }

    public void setWavehouse_name(String wavehouse_name) {
        this.wavehouse_name = wavehouse_name;
    }

    public String getBillcode() {
        return billcode;
    }

    public String getEmp() {
        return emp;
    }

    public String getIn_type() {
        return in_type;
    }

    public String getPlace_code() {
        return place_code;
    }

    public String getWavehouse_name() {
        return wavehouse_name;
    }
}
