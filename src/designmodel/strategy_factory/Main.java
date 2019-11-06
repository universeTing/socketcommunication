package designmodel.strategy_factory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.in;

/**
 * 策略-工厂模式的入口
 * @author wangyt
 * @date   2019-11-1
 */
public class Main {

    /*需要加载类 两种思路
        1、利用spring的创建bean的方法 继承InitializingBean，并实现afterPropertiesSet(),再里面去掉register方法去注册、针对web项目
        2、使用类加载器，去读配置文件
        3、forName静态方法搭配newInstance()方法动态的创建一个对象
     */

    /**
     * 如果需要把所有的类都加载的话，可以创建一个缓存器来进行缓存，然后再根据配置文件循环的去加载类
     */
//    private static Map<String, Class<UserPayService<?>>> payClassCache = new ConcurrentHashMap<>();

    /**
     * 主函数这里应该是策略模式的context，主要是用于选择使用哪个策略来执行。
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 加载文件里面所有类型参数
        InputStream in = Main.class.getClass().getResourceAsStream("/payservice.properties");
        Properties properties = new Properties();
        properties.load(in);

        // 获取到键值对
        Set<Map.Entry<Object,Object>> set = properties.entrySet();
        // 测试数据
        String type = "supervip";
        BigDecimal orderPay = new BigDecimal(123);

        Iterator<Map.Entry<Object,Object>> iterator = set.iterator();
        while(iterator.hasNext()){
            // 如果是该类型，则根据key取出对应的value 创建类来进行计算
            if(iterator.next().getKey().equals(type)){
                try {
                    // 动态的创建一个类实例
                    // E klass = (E) Class.forName(value.trim());
                    // 拿到类全名
                    String name = iterator.next().getValue().toString();

                    // 利用反射动态创建类
                    Class<?> aClass = Class.forName(name.trim());
                    // 指定一个类型，创建一个类实例
                    UserPayService userPayService = (UserPayService) aClass.newInstance();
                    // 动态的调用方法的两种方法
                    BigDecimal quote = userPayService.quote(orderPay);

                    Method method = aClass.getMethod("quote", BigDecimal.class);
                    Object obj3 = method.invoke(userPayService, orderPay);  // invoke方法的两个参数，第一个是实例对象，第二个是参数列表

                    System.out.println("you are the: " + type + "   order pay : "+ quote + "￥");
                    System.out.println("you are the: " + type + "   order pay : "+ obj3 + "￥");
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }








}
