package com.neturbo.set.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * �ͽ����йصľ�̬������
 */
public final class MoneyUtil {

	/**
	 * Constructor for MoneyUtil.
	 */
	private MoneyUtil() {
		super();
	}

	/**
	 * ��ȷ���
	 */
	public static double add(double d1, double d2) {

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.add(b2).doubleValue();

	}

	/**
	 * ��ȷ���
	 */
	public static double subtract(double d1, double d2) {

		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.subtract(b2).doubleValue();

	}

	/**
	 * ��ȷ���
	 */
	public static double multiply(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * ���������Ⱦ�ȷ���
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
	 * ����С�������λ��ȷ���
	 */
	public static double divide(double d1, double d2) {

		return divide(d1, d2, DEFAULT_SCALE);

	}

	/**
	 * ��ȷ��������
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
	 * ���Сдת���ɴ�д
	 */
	public static String amountD2C(String amountStr) {

		if (amountStr == null || amountStr.equals("")) {
			return chineseNumber[0] + "Ԫ��";
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
	 * ȱʡ����
	 */
	public static final int DEFAULT_SCALE = 2;

	/**
	 * ��д����
	 */
	public static final String chineseNumber[] =
		{ "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };

	/**
	 * ��д��λ
	 */
	public static final String amountUnit[] =
		{
			"��",
			"Ǫ",
			"��",
			"ʰ",
			"��",
			"Ǫ",
			"��",
			"ʰ",
			"��",
			"Ǫ",
			"��",
			"ʰ",
			"Ԫ",
			"��",
			"��",
			"��" };

}
