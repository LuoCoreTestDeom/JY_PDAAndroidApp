package rcominfo.com.ejejyxt.Bean.GetBean;

import java.util.List;

/**
 * Created by dell on 2016/12/29.
 */
public class ErrorJHBean {
    public class AbnormalPickingList
    {
        public List<AbnormalPickingRequest_Return> AbnormalPickingListReturn;
    }
    public class AbnormalPickingRequest_Return
    {
        /// <summary>
        /// 订单运单号
        /// </summary>
        public String billcode;
        /// <summary>
        /// 异常提交员
        /// </summary>
        public String AbnormalEmp;
    }


}
