package designpattern.strategy_factory.vipdto;

import designpattern.strategy_factory.UserPayService;

import java.math.BigDecimal;

/**
 * 超级VIP
 * @author wangyt
 * @date   2019-11-1
 */
public class SuperVipDto implements UserPayService {


    @Override
    public BigDecimal quote(BigDecimal orderPay) {
        // 打9折
        return orderPay.multiply(new BigDecimal("0.6"));
    }
}
