package com.neturbo.set.utils;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * ģ��java.util.StringTokenizer�����в������䣬�������Խ������ַ�����Ϊ�ָ�����<br>
 * �ָ������ߵĿմ�����Ϊ��token������ab;;;cd;����Ϊ����"ab","","","cd",""��<br>
 * ���token��
 */
public class StrTokenizer extends StringTokenizer {
	/**
	 * Constructor for StringTokenizer.
	 */
	public StrTokenizer(String source) {
		super(source);
	}

	/**
	 * Constructor for StringTokenizer.
	 */
	public StrTokenizer(String source, String delims) {
		super(source, delims);
	}

	/**
	 * Constructor for StringTokenizer.
	 */
	public StrTokenizer(String source, String delims, boolean retDelims) {
		super(source, delims, retDelims);
	}

	/**
	 * Constructor for StringTokenizer.
	 */
	public StrTokenizer(String source, String delims, boolean totalFlag,
			boolean zeroCounterFlag) {
		super(source, delims);

		this.source = source;
		this.delims = delims;
		this.totalAsDelims = totalFlag;
		this.counterZeroString = zeroCounterFlag;
		this.currPos = 0;
		this.maxPos = source.length();

		if (!totalFlag) {
			setMaxDelimiterChar();
		}

		extendFlag = totalFlag || zeroCounterFlag;
	}

	/**
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	/**
	 * @see java.util.Enumeration#nextElement()
	 */
	public Object nextElement() {
		return nextToken();
	}

	/**
	 * �ж��Ƿ񻹴�����һ��token��
	 */
	public boolean hasMoreTokens() {

		boolean retBoolean = false;

		if (extendFlag) {

			if (totalAsDelims) {
				if (counterZeroString) {
					if (currPos < maxPos) {
						retBoolean = true;
					} else if (currPos == maxPos && endWithDelim) {
						retBoolean = true;
					}
				} else if (currPos < maxPos) {
					int delimLength = delims.length();

					int tmpIndex = source.indexOf(delims, currPos);
					while (tmpIndex == currPos && currPos < maxPos) {
						currPos += delimLength;
						tmpIndex = source.indexOf(delims, currPos);
					}

					retBoolean = (currPos < maxPos);

				}
			} else {
				if (currPos < maxPos) {
					retBoolean = true;
				} else if (currPos == maxPos && endWithDelim) {
					retBoolean = true;
				}
			}

		} else {
			retBoolean = super.hasMoreTokens();
		}

		return retBoolean;

	}

	/**
	 * ȡ��һ��token��
	 */
	public String nextToken() {

		if (currPos > maxPos
				|| (currPos != -1 && currPos == maxPos && !endWithDelim)) {
			throw new NoSuchElementException();
		}

		String retString = null;

		if (extendFlag) {

			int nextPos = getNextPosition(currPos);
			retString = source.substring(currPos, nextPos);
			if (totalAsDelims) {
				currPos = nextPos + delims.length();
			} else {
				currPos = nextPos + 1;
			}

			if (counterZeroString && currPos == maxPos) {
				if (totalAsDelims) {
					if (counterZeroString
							&& delims.equals(source.substring(source.length()
									- delims.length()))) {
						endWithDelim = true;
					}
				} else {
					if (delims.indexOf(source.charAt(currPos - 1)) != -1) {
						endWithDelim = true;
					}
				}
			}

		} else {
			retString = super.nextToken();
		}

		return retString;
	}

	/**
	 * ȡ�Ը����ָ���Ϊ�·ָ�������һ��token��
	 */
	public String nextToken(String delim) {
		String retString = null;

		if (extendFlag) {

			delims = delim;
			if (!totalAsDelims) {
				setMaxDelimiterChar();
			}
			retString = nextToken();

		} else {
			retString = super.nextToken(delim);
		}
		return retString;
	}

	/**
	 * ���㵽��Ŀǰλ�ÿ�ʼ��ʣ����token��
	 */
	public int countTokens() {
		int count = 0;

		if (extendFlag) {

			int currPosTmp = currPos;
			int nextPosTmp = -1;
			if (totalAsDelims) {
				int delimsLength = delims.length();
				if (counterZeroString) {
					while (currPosTmp <= maxPos) {
						nextPosTmp = source.indexOf(delims, currPosTmp);
						if (nextPosTmp != -1) {
							currPosTmp = nextPosTmp + delimsLength;
						} else {
							currPosTmp = maxPos + 1;
						}

						count++;
					}
				} else {
					while (currPosTmp < maxPos) {
						nextPosTmp = source.indexOf(delims, currPosTmp);
						while (nextPosTmp == currPosTmp) {
							currPosTmp += delimsLength;
							nextPosTmp = source.indexOf(delims, currPosTmp);
						}

						if (nextPosTmp != -1) {
							currPosTmp = nextPosTmp + delimsLength;
						} else {
							currPosTmp = maxPos;
						}

						count++;
					}
				}
			} else {
				char currChar = 0;
				while (currPosTmp < maxPos) {
					currChar = source.charAt(currPosTmp);

					currPosTmp++;

					if (currChar > maxDelimiterChar
							|| delims.indexOf(currChar) < 0) {

						continue;
					}
					count++;
				}

				count++;
			}

		} else {
			count = super.countTokens();
		}

		return count;

	}

	public static void main(String[] args) {
		try {
			if (args.length != 0 && args.length != 4) {
				System.out
						.println("Must provide source String,delimeters String,delimsTotalFlag");
				System.out.println("and zeroStringCounterFlag as parameters");
				System.exit(0);
			}

			String testStr = null;
			String testDelims = null;
			boolean tFlag = false;
			boolean zFlag = true;

			if (args.length == 2) {
				testStr = args[0];
				testDelims = args[1];
				tFlag = Boolean.valueOf(args[2]).booleanValue();
				zFlag = Boolean.valueOf(args[3]).booleanValue();
			} else {
				testStr = "a;bb;;ccc;dddd;;;;eeeee;";
				testDelims = ";";
			}

			StrTokenizer st = new StrTokenizer(testStr, testDelims, tFlag,
					zFlag);

			System.out.println("Source is \"" + testStr + "\"");
			System.out.println("Tokens count :" + st.countTokens());

			int index = 1;
			String token = null;
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				System.out.println(index + "," + token.length() + ",\"" + token
						+ "\"");
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȡ��һ���ָ�����λ�á�
	 */
	private int getNextPosition(int currPos) {
		int nextPos = currPos;
		if (totalAsDelims) {
			nextPos = source.indexOf(delims, currPos);
			if (nextPos == -1) {
				nextPos = source.length();
			}
		} else {
			char tmpChar = 0;
			while (nextPos < maxPos) {
				tmpChar = source.charAt(nextPos);
				if (tmpChar > maxDelimiterChar || delims.indexOf(tmpChar) == -1) {
					nextPos++;
				} else {
					break;
				}
			}

		}

		return nextPos;
	}

	/**
	 * �������ָ����ַ��������Ż���ѯ��
	 */
	private void setMaxDelimiterChar() {

		if (delims == null) {
			maxDelimiterChar = 0;
			return;
		}

		char m = 0;
		for (int i = 0; i < delims.length(); i++) {
			char c = delims.charAt(i);
			if (m < c)
				m = c;
		}

		maxDelimiterChar = m;
	}

	/**
	 * �Ƿ������ַ�����Ϊ�ָ�����ȱʡΪ�񣬼����ַ���ÿ���ַ���Ϊ�ָ�����
	 */
	private boolean totalAsDelims = false;

	/**
	 * �Ƿ񽫿մ�""��Ϊtoken��ȱʡΪ�񣬼���StringTokenizer��ͬ��
	 */
	private boolean counterZeroString = false;

	/**
	 * �Ƿ�ʹ�õ���չ���ܡ�
	 */
	private boolean extendFlag = false;

	/**
	 * �Ƿ��Էָ�����β��
	 */
	private boolean endWithDelim = false;

	/**
	 * Ҫ�ָ����ַ�����
	 */
	private String source = null;

	/**
	 * �ָ�������
	 */
	private String delims = null;

	/**
	 * ��ǰλ�á�
	 */
	private int currPos = -1;

	/**
	 * ���λ��.
	 */
	private int maxPos = -1;

	/**
	 * ���ָ�������ÿ���ַ�����Ϊ�ָ���ʱ��Ч��
	 */
	private char maxDelimiterChar = 0;
}
