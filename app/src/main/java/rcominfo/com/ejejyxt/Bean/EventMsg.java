package rcominfo.com.ejejyxt.Bean;

/**
 * Created by wly on 2016/11/26.
 */
public class EventMsg {

        private int flag;
        private Object data;

        public EventMsg(int flag, Object data) {
            this.flag = flag;
            this.data = data;
        }

        public int getFlag() {
            return this.flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public Object getData() {
            return this.data;
        }

        public void setData(Object data) {
            this.data = data;
        }
}
