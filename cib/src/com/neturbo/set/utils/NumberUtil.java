/*
 * 创建日期 2004-7-29
 *
 */
package com.neturbo.set.utils;

/**
 * @author zhushaode
 *
 */
public class NumberUtil {
	/**
	 *  将浮点数 d 转换成小数位为 i 的浮点数
	 *  @param double	d			被转换浮点数
	 *  @param int		i			转换结果浮点数小数位数
	 *  @return 		double		转换后的浮点数
	 *  @throws
	 **/
	public static double roundD2D(double d, int i) {

		return (
			Double.parseDouble(Utils.StringConvertor(Format.formatDecimal(d, i))));
	}

	/**
	 *  将浮点数 d 转换成小数位为 i 的浮点数字符串
	 *  @param double	d			被转换浮点数
	 *  @param int		i			转换结果浮点数小数位数
	 *  @return 		String		转换后的浮点数
	 *  @throws
	 **/
	public static String roundD2S(double d, int i) {

		return (Utils.StringConvertor(Format.formatDecimal(d, i)));
	}

	/**
	 *  将浮点数字符串 s 转换成小数位为 i 的浮点数
	 *  @param String	s			被转换浮点数字符串
	 *  @param int		i			转换结果浮点数小数位数
	 *  @return 		double		转换后的浮点数
	 *  @throws
	 **/
	public static double roundS2D(String s, int i) {

		return (
			Double.parseDouble(Utils.StringConvertor(Format.formatDecimal(s, i))));
	}

	/**
	 *  将浮点数字符串 s 转换成小数位为 i 的浮点数字符串
	 *  @param double	d			被转换浮点数
	 *  @param int		i			转换结果浮点数小数位数
	 *  @return 		String		转换后的浮点数
	 *  @throws
	 **/
	public static String roundS2S(String s, int i) {

		return (Utils.StringConvertor(Format.formatDecimal(s, i)));
	}

}
