/**
 * @author hjs
 * 2007-3-13
 */
package com.neturbo.set.utils;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-3-13
 */
public class Sorting {
	//升序
	public final static int SORT_TYPE_ASC = 0; 
	//降序
	public final static int SORT_TYPE_DESC = 1; 
	
	private Sorting(){
	}
	
	/**
	 * @param mapList：用于排序的map list
	 * @param keyWordArray：用于排序的key
	 * @param sortType：默认为ASC
	 * @return
	 * @throws NTBException
	 */
	public static void sortMapList(List mapList, String[] keyWordArray, int sortType) throws NTBException {
		if(keyWordArray!=null && keyWordArray.length>0){
			int[] sortTypeArray = new int[keyWordArray.length];
			Arrays.fill(sortTypeArray, sortType);
			sortMapList(mapList, keyWordArray, sortTypeArray);
		}
	}
	/**
	 * 
	 * @param mapList：用于排序的map list
	 * @param keyWordArray：用于排序的key
	 * @param sortTypeArray：默认为ASC
	 * @return
	 * @throws NTBException
	 */
	public static void sortMapList(List mapList, String[] keyWordArray, int[] sortTypeArray) throws NTBException {
		if(keyWordArray!=null && keyWordArray.length>0){
			if(sortTypeArray==null){//排序类型数组为空
				sortTypeArray = new int[keyWordArray.length];
				Arrays.fill(sortTypeArray, SORT_TYPE_ASC);
			} else if(sortTypeArray.length > keyWordArray.length){ //排序类型数组的个数超过field的个数
				//只截取需要的个数
				int[] tempArray = new int[keyWordArray.length];
				System.arraycopy(sortTypeArray, 0, tempArray, 0, tempArray.length);
				sortTypeArray = tempArray;
			} else if(sortTypeArray.length < keyWordArray.length){ //排序类型数组的个数小于field的个数
				//补充缺少的类型
				int[] tempArray = new int[keyWordArray.length];
				System.arraycopy(sortTypeArray, 0, tempArray, 0, sortTypeArray.length);
				Arrays.fill(tempArray, sortTypeArray.length, tempArray.length-1, SORT_TYPE_ASC);
			}
			Collections.sort(mapList, new Sorting().new MapComparator(keyWordArray, sortTypeArray));
		}
	}
	
	/**
	 * @param beanList：用于排序的bean list
	 * @param propertyArray：用于排序的property
	 * @param sortType：默认为ASC
	 * @return
	 * @throws NTBException
	 */
	public static void sortBeanList(List beanList, String[] propertyArray, int sortType) throws NTBException {
		if(propertyArray!=null && propertyArray.length>0){
			int[] sortTypeArray = new int[propertyArray.length];
			Arrays.fill(sortTypeArray, sortType);
			sortBeanList(beanList, propertyArray, sortTypeArray);
		}
	}
	/**
	 * 
	 * @param beanList：用于排序的bean list
	 * @param propertyArray：用于排序的property
	 * @param sortTypeArray：默认为ASC
	 * @return
	 * @throws NTBException
	 */
	public static void sortBeanList(List beanList, String[] propertyArray, int[] sortTypeArray) throws NTBException {
		if(propertyArray!=null && propertyArray.length>0){
			if(sortTypeArray==null){//排序类型数组为空
				sortTypeArray = new int[propertyArray.length];
				Arrays.fill(sortTypeArray, SORT_TYPE_ASC);
			} else if(sortTypeArray.length > propertyArray.length){ //排序类型数组的个数超过field的个数
				//只截取需要的个数
				int[] tempArray = new int[propertyArray.length];
				System.arraycopy(sortTypeArray, 0, tempArray, 0, tempArray.length);
				sortTypeArray = tempArray;
			} else if(sortTypeArray.length < propertyArray.length){ //排序类型数组的个数小于field的个数
				//补充缺少的类型
				int[] tempArray = new int[propertyArray.length];
				System.arraycopy(sortTypeArray, 0, tempArray, 0, sortTypeArray.length);
				Arrays.fill(tempArray, sortTypeArray.length, tempArray.length-1, SORT_TYPE_ASC);
			}
			Collections.sort(beanList, new Sorting().new BeanComparator(propertyArray, sortTypeArray));
		}
	}
	
	private class MapComparator implements Comparator {
		//用于排序的field数组
		private Object[] keyWordArray = null; 
		//对应每个field的排序类型
		private int[] sortTypeArray = null;
		
		public MapComparator(Object[] keyWordArray, int[] sortTypeArray){
			this.keyWordArray = keyWordArray;
			this.sortTypeArray = sortTypeArray;
		}
		
		public int compare(Object o1, Object o2) {
			Map row1= (Map) o1;
			Map row2= (Map) o2;
			Object comparedFiled1 = null;
			Object comparedFiled2 = null;
			
			//取用于排序的key value
			for(int i=0; i<keyWordArray.length; i++) {
				//default result
				int result = 0;
				
				comparedFiled1 = row1.get(keyWordArray[i]);
				comparedFiled2 = row2.get(keyWordArray[i]);
				if (SORT_TYPE_DESC == sortTypeArray[i]) { //降序
					comparedFiled1 = row2.get(keyWordArray[i]);
					comparedFiled2 = row1.get(keyWordArray[i]);
				}
				
				//if compared object is null
				if(comparedFiled1==null && comparedFiled2==null){
					result = 0;
				} else if(comparedFiled1==null) {
					result = -1;
				} else if (comparedFiled2==null) {
					result = 1;
				} else {
					result = compareField(comparedFiled1, comparedFiled2);
				}
				if(result == 0){ //相等, 进入下一个key的比较
					continue;
				}
				//最终结果
				return result;
			}
			//默认结果
			return 0;
		}
	}
	
	private class BeanComparator implements Comparator {
		//用于排序的field数组
		private Object[] keyWordArray = null; 
		//对应每个field的排序类型
		private int[] sortTypeArray = null;
		
		public BeanComparator(Object[] keyWordArray, int[] sortTypeArray){
			this.keyWordArray = keyWordArray;
			this.sortTypeArray = sortTypeArray;
		}
		public int compare(Object o1, Object o2) {
			Object comparedFiled1 = null;
			Object comparedFiled2 = null;
			
			//取用于排序的key value
			for(int i=0; i<keyWordArray.length; i++) {
				//default result
				int result = 0;
				
				try {
					comparedFiled1 = BeanUtils.getProperty(o1, (String) keyWordArray[i]);
					comparedFiled2 = BeanUtils.getProperty(o2, (String) keyWordArray[i]);
					if (SORT_TYPE_DESC == sortTypeArray[i]) { //降序
						comparedFiled2 = BeanUtils.getProperty(o1, (String) keyWordArray[i]);
						comparedFiled1 = BeanUtils.getProperty(o2, (String) keyWordArray[i]);
					}
				} catch (Exception e) {
					Log.error("BeanComparator.compare() error", e);
				}
				
				//if compared object is null
				if(comparedFiled1==null && comparedFiled2==null){
					result = 0;
				} else if(comparedFiled1==null) {
					result = -1;
				} else if (comparedFiled2==null) {
					result = 1;
				} else {
					result = compareField(comparedFiled1, comparedFiled2);
				}
				if(result == 0){ //相等, 进入下一个key的比较
					continue;
				}
				//最终结果
				return result;
			}
			//默认结果
			return 0;
		}
	}
	
	private int compareField(Object o1, Object o2) {
		Integer result = new Integer(0);
		if(o1 instanceof String) {
			Comparator chineseComparator = Collator.getInstance(Locale.CHINESE);
			result = new Integer(chineseComparator.compare(o1, o2));
		} else {
			try {
				Method comparedMethod = o1.getClass().getMethod("compareTo", new Class[]{o1.getClass()});
				result = (Integer) comparedMethod.invoke(o1, new Object[]{o2});
			} catch (Exception e) {
				Log.error("Not supported filed type[" + o1.getClass().getName() + "]", e);
			}
		}
		return result.intValue();
	}

	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {}

}
