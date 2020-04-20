package designpattern.strategy_factory.vipdto;

import designpattern.strategy_factory.UserPayService;

import java.math.BigDecimal;

/**
 * 专属会员
 * @author wangyt
 * @date   2019-11-1
 */
public class SpecialVipDto implements UserPayService {

    @Override
    public BigDecimal quote(BigDecimal orderPay) {
        //打7折
        return orderPay.multiply(new BigDecimal("0.7"));
    }
}
