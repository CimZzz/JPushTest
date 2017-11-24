package com.example.yumi.jpushtest.utils;

import com.example.yumi.jpushtest.base.BasePager;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static com.example.yumi.jpushtest.utils.LogUtilsKt.logV;

/**
 * Created by CimZzz(王彦雄) on 2017/11/24.<br>
 * Since : 项目名称_版本 <br>
 * Description : <br>
 * 描述
 */
public class BasePagerPool {
    private HashMap<Class<? extends BasePager>,SoftReference<BasePager>> map;

    public BasePagerPool() {
        map = new HashMap<>();
    }

    public <T extends BasePager> T getPager(Class<T> pageCls) {
        SoftReference<T> ref = (SoftReference<T>) map.get(pageCls);

        T t = null;
        if(ref == null || (t = ref.get()) == null) {
            try {
                logV("Create Pager : " + pageCls.getSimpleName());
                t = pageCls.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            map.put(pageCls,new SoftReference<BasePager>(t));
        }

        return t;
    }
}
