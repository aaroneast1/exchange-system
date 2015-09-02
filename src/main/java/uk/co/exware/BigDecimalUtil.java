package uk.co.exware;

import java.math.BigDecimal;

public class BigDecimalUtil {

    private BigDecimalUtil(){}

    public static BigDecimal newBigDecimal(String amount){
        return addScale(new BigDecimal(amount));
    }

    public static BigDecimal addScale(BigDecimal amount){
        return amount.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal newBigDecimal(long amount){
        return addScale(new BigDecimal(amount));
    }

}
