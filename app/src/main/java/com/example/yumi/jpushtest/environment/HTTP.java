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
        String STATE = "gs_token";
        String URL = ConstantKt.HOST + "/api/home/index/";

        String TOKEN = "taoken";
    }

    interface Login {
        String STATE = "gs_login";
        String URL = ConstantKt.HOST + "/api/home/index/board";

        String USERNAME = "userName";
        String USERPWD = "userPwd";
    }
}
