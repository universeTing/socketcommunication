package designpattern.strategy_factory.vipdto;

import designpattern.strategy_factory.UserPayService;

import java.math.BigDecimal;

/**
 * 过期一周内的会员
 * 如果用到spring，可以借用Spring种提供的InitializingBean接口，
 * 这个接口为Bean提供了属性初始化后的处理方法，
 * 它只包括afterPropertiesSet方法，凡是继承该接口的类，在bean的属性初始化后都会执行该方法。
 * @author wangyt
 * @date   2019-11-1
 */
public class OverdueOneWeekVipDto implements UserPayService {


    @Override
    public BigDecimal quote(BigDecimal orderPay) {
        // 打8折
        return orderPay.multiply(new BigDecimal("0.8"));
    }
}
