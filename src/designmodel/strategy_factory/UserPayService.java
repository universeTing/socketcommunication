package designmodel.strategy_factory;

import java.math.BigDecimal;

/**
 * 计算价格（活动的strategy接口）
 * @author wangyt
 * @date   2019-11-1
 */
public interface UserPayService<E> {

    /**
     * 计算应付价格
     * @param orderPay
     * @return
     */
    BigDecimal quote(BigDecimal orderPay);

}
