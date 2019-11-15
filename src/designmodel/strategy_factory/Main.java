package designmodel.strategy_factory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
        //int total = 494 + 260 + 296 + 179 + 359;

        // （白色）连衣裙 + （长袖）小众衬衣 + （西装）连衣裙 + 网纱两件套 + （绿色）棉服
       /* String type = "supervip";//6折
        int total = 251 + 260 + 341 + 179 + 494;*/

        // （白色）连衣裙 + （长袖）小众衬衣 //8折
        String type = "overdueoneweekvip";
        int total = 251 + 260;

        // （白色）连衣裙 + （长袖）小众衬衣 + （绿色）棉服
        /*String type = "spercialvip";//7折
        int total = 251 + 260 + 494;*/


        BigDecimal orderPay = new BigDecimal(String.valueOf(total));
        Iterator<Map.Entry<Object,Object>> iterator = set.iterator();
        while(iterator.hasNext()){
            // 注意这里有个坑，迭代器next一次就会往下一个指，所以把当前的指处理结束了再掉一次next
            Map.Entry<Object,Object> me = iterator.next();
            // 如果是该类型，则根据key取出对应的value 创建类来进行计算
            if(me.getKey().equals(type)){
                try {
                    // 动态的创建一个类实例
                    // E klass = (E) Class.forName(value.trim());
                    // 拿到类全名
                    String name = me.getValue().toString();

                    // 利用反射动态创建类
                    Class<?> aClass = Class.forName(name.trim());
                    // 指定一个类型，创建一个类实例
                    UserPayService userPayService = (UserPayService) aClass.newInstance();
                    // 动态的调用方法的两种方法, 打折
                    BigDecimal quote = userPayService.quote(orderPay);
                    quote = quote.add(new BigDecimal(208 + 387));

                    // 拿到aClass的quote方法，然后去invoke执行，
                    // 这种方法不推荐使用，因为invoke出来的是object对象，并不知道是需要的那个对象，使编译器错过了检测代码的机会，如果有问题就只有在测试阶段才暴露出来
                    Method method = aClass.getMethod("quote", BigDecimal.class);
                    // invoke方法的两个参数，第一个是实例对象，第二个是参数列表
                    Object obj3 = method.invoke(userPayService, orderPay);

                    System.out.println("you are the: " + type + "   order pay : "+ quote+ "￥");
                    System.out.println("you are the: " + type + "   order pay : "+ quote + "￥");
                    System.out.println("you are the: " + type + "   final order pay : "+ quote.subtract(new BigDecimal(100+20)) + "￥");
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
