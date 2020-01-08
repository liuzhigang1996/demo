package com.neturbo.set.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 和金额处理有关的静态方法。
 */
public final class MoneyUtil {

	/**
	 * Constructor for MoneyUtil.
	 */
	private MoneyUtil() {
		super();
	}

	/**
	 * 精确相加
	 */
	public static double add(double d1, double d2) {

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.add(b2).doubleValue();

	}

	/**
	 * 精确相减
	 */
	public static double subtract(double d1, double d2) {

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.subtract(b2).doubleValue();

	}

	/**
	 * 精确相乘
	 */
	public static double multiply(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 按给定精度精确相除
	 */
	public static double divide(double d1, double d2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
				"cant be negative,scale:" + scale);

		}

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 保留小数点后两位精确相除
	 */
	public static double divide(double d1, double d2) {

		return divide(d1, d2, DEFAULT_SCALE);

	}

	/**
	 * 精确四舍五入
	 */
	public static double round(double d1, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(
				"cant be negative,scale:" + scale);

		}

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal("1");

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 金额小写转化成大写
	 */
	public static String amountD2C(String amountStr) {

		if (amountStr == null || amountStr.equals("")) {
			return chineseNumber[0] + "元整";
		}

		StringBuffer amountBuffer = new StringBuffer("");
		String integerPart = null;
		String floatPart = null;

		int dotIndex = amountStr.indexOf(".");

		if (dotIndex == -1) {
			integerPart = amountStr;
			floatPart = "";
		} else {
			integerPart = amountStr.substring(0, dotIndex);
			floatPart = amountStr.substring(dotIndex + 1);
		}

		int digits = integerPart.length();

		if (digits > 13) {
			throw new IllegalArgumentException("Too large amount:" + amountStr);
		}

		for (int i = 13 - digits; i < digits; i++) {

		}

		return amountBuffer.toString();

	}

	/**
	 * 缺省精度
	 */
	public static final int DEFAULT_SCALE = 2;

	/**
	 * 大写数字
	 */
	public static final String chineseNumber[] =
		{ "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	/**
	 * 大写金额单位
	 */
	public static final String amountUnit[] =
		{
			"万",
			"仟",
			"佰",
			"拾",
			"亿",
			"仟",
			"佰",
			"拾",
			"万",
			"仟",
			"佰",
			"拾",
			"元",
			"角",
			"分",
			"整" };

}
