package designmodel.strategy_factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户支付工厂
 * @author wangyt
 * @date   2019-11-1
 */
public class UserPayServiceStratrgyFactory{

    // 策略类的实例的缓存，第一个参数是type, 第二个参数是一个接口类，可以存放实现这个接口类的对象，即策略类
    private static Map<String,UserPayService> userPayServiceMap = new ConcurrentHashMap<>();


    /**
     * 获取服务，即获取策略对象
     * @param type
     * @return
     */
    public static UserPayService getByUserType(String type){
        return userPayServiceMap.get(type);
    }


    /**
     * 策略服务注册, 这里还是需要if else来判断user type类型，所以解决不了策略模式需要解决的问题，
     * 所以在main函数中，利用反射机制去动态的创建，这个类暂时不用。
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
