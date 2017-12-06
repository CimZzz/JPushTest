package com.example.yumi.jpushtest.environment;

/**
 * Created by CimZzz(王彦雄) on 2017/11/29.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 *
 */
public interface HTTP {
    /*
    * 0 = 访问成功
    * 3 = token失效
    * */

    String CODE = "code";
    String MSG = "msg";
    String DATA = "data";


    interface Token {
        String URL = ConstantKt.HOST + "/api/home/index/";
        String TOKEN = "token";
    }

    interface Login {
        String STATE = "gs_login";
        String URL = ConstantKt.HOST + "/api/home/index/board";

        String USERNAME = "userName";
        String USERPWD = "userPwd";
    }

    interface Register {
        String STATE = "gs_register";
        String URL = ConstantKt.HOST + "/api/home/index/reg";

        String USERNAME = "userName";
        String USERPWD = "userPwd";
        String VALIDATION = "validationCode";
    }

    interface UpdatePwd {
        String STATE = "gs_updatePwd";
        String URL = ConstantKt.HOST + "/api/home/index/password";

        String PHONE_NUM = "phoneNum";
        String EMAIL = "email";
        String NEW_PWD = "newPwd";
        String VALIDATION = "validation";
    }

    interface EmailCode {
        String STATE = "gs_emailCode";
        String URL = ConstantKt.HOST + "/api/home/index/reg";

        String EMAIL = "email";
    }

    interface PhoneCode {
        String STATE = "gs_phoneCode";
        String URL = ConstantKt.HOST + "/api/home/index/phone";

        String PHONE_NUM = "phoneNum";
        String CODE = "data";
    }
}
