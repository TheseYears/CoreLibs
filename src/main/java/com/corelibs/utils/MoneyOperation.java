package com.corelibs.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 金额计算, 加减乘除使用此类可以避免java中double类型的bug
 */
public class MoneyOperation {
    private static final int DEF_DIV_SCALE = 10;
    private static final String DEF_PATTERN = "0.00";

    private MoneyOperation() {}

    /**
     * 加
     */
    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));

        return b1.add(b2).doubleValue();
    }

    /**
     * 减
     */
    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));

        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘
     */
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));

        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除
     */
    public static double div(double d1, double d2) {
        return div(d1, d2, DEF_DIV_SCALE);
    }

    /**
     * 除
     */
    public static double div(double d1, double d2, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));

        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化double, 默认保留两位小数
     */
    public static String format(double d) {
        return format(d, DEF_PATTERN);
    }

    /**
     * 格式化double
     * @param pattern 0.00保留两位小数
     */
    public static String format(double d, String pattern) {
        return new DecimalFormat(pattern).format(d);
    }
}
