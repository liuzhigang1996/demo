/*
 * �������� 2004-7-29
 *
 */
package com.neturbo.set.utils;

/**
 * @author zhushaode
 *
 */
public class NumberUtil {
	/**
	 *  �������� d ת����С��λΪ i �ĸ�����
	 *  @param double	d			��ת��������
	 *  @param int		i			ת�����������С��λ��
	 *  @return 		double		ת����ĸ�����
	 *  @throws
	 **/
	public static double roundD2D(double d, int i) {

		return (
			Double.parseDouble(Utils.StringConvertor(Format.formatDecimal(d, i))));
	}

	/**
	 *  �������� d ת����С��λΪ i �ĸ������ַ���
	 *  @param double	d			��ת��������
	 *  @param int		i			ת�����������С��λ��
	 *  @return 		String		ת����ĸ�����
	 *  @throws
	 **/
	public static String roundD2S(double d, int i) {

		return (Utils.StringConvertor(Format.formatDecimal(d, i)));
	}

	/**
	 *  ���������ַ��� s ת����С��λΪ i �ĸ�����
	 *  @param String	s			��ת���������ַ���
	 *  @param int		i			ת�����������С��λ��
	 *  @return 		double		ת����ĸ�����
	 *  @throws
	 **/
	public static double roundS2D(String s, int i) {

		return (
			Double.parseDouble(Utils.StringConvertor(Format.formatDecimal(s, i))));
	}

	/**
	 *  ���������ַ��� s ת����С��λΪ i �ĸ������ַ���
	 *  @param double	d			��ת��������
	 *  @param int		i			ת�����������С��λ��
	 *  @return 		String		ת����ĸ�����
	 *  @throws
	 **/
	public static String roundS2S(String s, int i) {

		return (Utils.StringConvertor(Format.formatDecimal(s, i)));
	}

}
