package designmodel.strategy_factory;

import com.sun.deploy.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户支付工厂
 * @author wangyt
 * @date   2019-11-1
 */
public class UserPayServiceStratrgyFactory{

    // 策略类的实例的缓存
    private static Map<String,UserPayService> userPayServiceMap = new ConcurrentHashMap<>();


    /**
     * 获取服务
     * @param type
     * @return
     */
    public static UserPayService getByUserType(String type){
        return userPayServiceMap.get(type);
    }


    /**
     * 策略服务注册
     * @param userType
     * @param userPayService
     * @throws Exception
     */
    public static void register(String userType, UserPayService userPayService) throws Exception {
        if(userType == null || "".equals(userType)){
            throw new Exception("userType is not null");
        }

        userPayServiceMap.put(userType,userPayService);
    }
}
