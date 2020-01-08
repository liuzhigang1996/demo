package com.neturbo.set.utils;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * 模拟java.util.StringTokenizer，进行部分扩充，包括可以将整个字符串作为分隔符，<br>
 * 分隔符两边的空串被认为是token，例如ab;;;cd;被认为包括"ab","","","cd",""共<br>
 * 五个token。
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
	 * 判断是否还存在下一个token。
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
	 * 取下一个token。
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
	 * 取以给定分隔符为新分隔符的下一个token。
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
	 * 计算到从目前位置开始还剩几个token。
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
	 * 取下一个分隔符的位置。
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
	 * 设置最大分隔符字符，用于优化查询。
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
	 * 是否整个字符串作为分隔符，缺省为否，即该字符串每个字符均为分隔符。
	 */
	private boolean totalAsDelims = false;

	/**
	 * 是否将空串""算为token，缺省为否，即和StringTokenizer相同。
	 */
	private boolean counterZeroString = false;

	/**
	 * 是否使用到扩展功能。
	 */
	private boolean extendFlag = false;

	/**
	 * 是否以分隔符结尾。
	 */
	private boolean endWithDelim = false;

	/**
	 * 要分隔的字符串。
	 */
	private String source = null;

	/**
	 * 分隔符串。
	 */
	private String delims = null;

	/**
	 * 当前位置。
	 */
	private int currPos = -1;

	/**
	 * 最大位置.
	 */
	private int maxPos = -1;

	/**
	 * 最大分隔符，当每个字符都作为分隔符时有效。
	 */
	private char maxDelimiterChar = 0;
}
