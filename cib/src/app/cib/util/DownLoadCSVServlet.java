package app.cib.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class DownLoadCSVServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Locale locale;
	private NTBUser user;
	private int rowNum = 0;
	private int generalRow = 0;

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String listName = req.getParameter("listName");
		String columnNames = req.getParameter("columnNames");
		String columnTitles = req.getParameter("columnTitles");
		String fileName = req.getParameter("fileName");
		String general = specialDispose_general(Utils.null2Empty(req.getParameter("general")));
		String logoFlag = req.getParameter("logo");
		if (fileName == null) {
			fileName = "download.xls";
		}
		fileName = Utils.replaceStr(fileName, " ", "_");
		fileName = fileName.toLowerCase();
		if (!fileName.endsWith(".xls")) {
			fileName += ".xls";
		}

		try {
			if (listName != null) {
				HttpSession session = req.getSession(true);
				locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
				user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
				if (locale == null) {
					if (user != null) {
						locale = user.getLanguage();
					}
				}
				if (locale == null) {
					locale = Config.getDefaultLocale();
				}

				HashMap resultData = (HashMap) session
						.getAttribute("ResultData$Of$Neturbo");
				List dataList = (List) resultData.get(listName);
				if (dataList != null) {
					resp.setContentType("application/octet-stream");
					resp.setHeader("Content-Disposition", "attachment;"
							+ " filename=" + fileName);

					/**
					 * Export Excel
					 */
					jxl.write.WritableWorkbook workbook = Workbook
							.createWorkbook(resp.getOutputStream());
					jxl.write.WritableSheet sheet = workbook.createSheet(
							"book1", 0);
					rowNum = 0;// ��ʼ����
					//Title ��ʽ
					WritableFont wf = new WritableFont(WritableFont.TIMES,
							12, WritableFont.BOLD, false);
					WritableCellFormat title = new WritableCellFormat(wf);

					// ����LOGO
					WritableImage image = null;
					int pos = 5;
					if (logoFlag != null && logoFlag.equals("true")) {
						String logoPath = Utils.null2Empty(Config
								.getProperty("ExcelLogo"));
						File imageFile = new File(logoPath);
						if (imageFile.exists()) {
							image = new WritableImage(pos, 1, 2.5, 4.8,
									imageFile);
							sheet.addImage(image);
							//�ϲ���Ԫ��
							sheet.mergeCells(0,0,10,5);
							rowNum+=6;
						}

					}
					
					//add general 
					//�A�� general λ��
					if(general != null && !general.equals("")){
						generalRow = rowNum;
						rowNum += 2;
					}
					
					if (columnTitles != null) {
						// TODO ���Titles
						String[] titles = columnTitles.split(",");
						// �O�Æ�ԪAutoSize
						CellView autoSizecell = new CellView();
						autoSizecell.setAutosize(true);
						for (int i = 0; i < titles.length; i++) {
//							Label transDate = new Label(i, rowNum, titles[i], title);
							Label transDate = new Label(i, rowNum, titles[i].replace("<br/>", " "), title);//mod by linrui for replace<br>
							sheet.addCell(transDate);
							sheet.setColumnView(i, autoSizecell);
						}
						rowNum++;
					}
					if (dataList.size() == 0) {
						RBFactory rbRecord = RBFactory.getInstance(
								"app.cib.resource.common.operation", locale
										.toString());
						Label transDate = new Label(0, rowNum, "*** " + rbRecord.getString("no_record"));
//						Label transDate = new Label(0, rowNum, "*** No Record");
						sheet.addCell(transDate);
					}
					for (int i = 0; i < dataList.size(); i++) {
						HashMap item = (HashMap) dataList.get(i);
						formatContent(sheet, item, columnNames);
						rowNum++;
					}

					// Auto Size Columns
					sheet.autosizeColumns();

					/**
					 * reset Image Width. It's magical. add by Han 2007-09-29
					 */
					if (image != null) {
						try {
							// Magic Number image.getImageWidth() pixel 256
							// ? 0.143
							double imageWidth = image.getImageWidth() * 256 * 0.143;

							int column = 0;
							double temp = 0;
							while (imageWidth > 0) {
								temp = sheet.getColumnWidth(pos) > 0 ? sheet
										.getColumnWidth(pos) : 2350;
								if (imageWidth - temp < 0) {
									break;
								}
								imageWidth -= temp;
								column++;
								pos++;
							}
							image.setWidth(column + imageWidth / temp);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					//���A��λ�ò���general��Ϣ
					if(general != null && !general.equals("")){
						String[] generalMsg = general.split(",");
						for (int i = 0; i < generalMsg.length; i++) {
							Label generalDate = new Label(i, generalRow, generalMsg[i],
									title);
							sheet.addCell(generalDate);
						}
					}
					
					workbook.write();
					workbook.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void formatContent(WritableSheet sheet, HashMap record,
			String columnNames) throws Exception {
		StringTokenizer strToken = new StringTokenizer(columnNames, ",");
		int columnNum = 0;
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.NO_BOLD, false);
		WritableCellFormat data = new WritableCellFormat(font);
		//�����Զ�����
		data.setWrap(true);
		// ��Ӵ���formatting��Number����
		NumberFormat numFormat = new NumberFormat("#,##0.00");
		BigDecimal decimal = new BigDecimal("0");//mofify by wen_chy 20100322
		WritableCellFormat cellFormat = new WritableCellFormat(font, numFormat);

		while (strToken.hasMoreTokens()) {
			String columnName = strToken.nextToken();
			Object valueObj = null;
			String columnValue = null;
			String db = null;
			String rb = null;
			String format = null;
			String pattern = null;
			String specialChar = null;
			//add by wen_chy 20100310
			String isDecimal = null;
			//#######

			if (columnName.indexOf("||") > 0) {
				String[] temp1 = Utils.splitStr(columnName, "||");
				columnName = temp1[0];
				for (int i = 1; i < temp1.length; i++) {
					String[] temp2 = Utils.splitStr(temp1[i], "@");
					if (temp2.length == 2) {
						if (temp2[0].equals("rb")) {
							rb = temp2[1];
						} else if (temp2[0].equals("db")) {
							db = temp2[1];
						} else if (temp2[0].equals("format")) {
							format = temp2[1];
						} else if (temp2[0].equals("pattern")) {
							pattern = temp2[1];
						} else if (temp2[0].equals("specialChar")) {
							specialChar = temp2[1];
						}
                        //	add by wen_chy 20100310
						else if(temp2[0].equals("decimal")){
							isDecimal = temp2[1];
						}
					    //#######

					}
				}
			}
			valueObj = record.get(columnName);
			columnValue = "";
			if (valueObj == null) { // add by hjs 2006-09-20
				columnValue = "";
			} else {
				if (valueObj instanceof Date) {
					columnValue = DateTime.Millis2DateTime(((Date) valueObj)
							.getTime());
				} else if (valueObj.getClass().isArray()) {
					Object[] valueObjArray = (Object[]) valueObj;
					if (valueObjArray.length > 0) {
						columnValue = valueObjArray[0].toString().trim();
					}
				} else if (valueObj instanceof List) {
					/* just for approval status enquiry - progress list */
					RBFactory rbFactory = RBFactory.getInstance(
							"app.cib.resource.flow.dealer_type", locale
									.toString());
					List list = (List) valueObj;
					Map row = null;
					String finishFlag = "";
					for (int i = 0; i < list.size(); i++) {
						row = (Map) list.get(i);
						finishFlag = row.get("FinishFlag").toString();
						if (finishFlag.equals("1")) {
							columnValue += row.get("Dealer")
									+ (i == list.size() - 1 ? "" : "\n");
						} else if (finishFlag.equals("0")) {
							columnValue += rbFactory.getString(row
									.get("Dealer").toString())
									+ (i == list.size() - 1 ? "" : "\n");
						}
					}
				} else {
					columnValue = Utils.null2EmptyWithTrim(valueObj);// Decimal��ʽ�����c,������Ͷ������D�Q��String
				}
			}

			if (rb != null) {
				RBFactory rbList = RBFactory.getInstance(rb, locale.toString());
				columnValue = rbList.getString(columnValue);
			}
			if (db != null) {
				DBRCFactory rbList = DBRCFactory.getInstance(db);
				if (rbList != null) {
					rbList.setArgs(user);
					columnValue = rbList.getString(columnValue);
				}
			}

			if (specialChar != null) {
				columnValue = specialDispose(columnValue);
			}

			// ���format���ڣ����ʽ���ִ�
			// Hjs modified for format problem(amount less than 1000)
			if (format != null && format != "" && !format.trim().equals("")
					&& !format.equalsIgnoreCase("amount")) {
				columnValue = Format.formatData(columnValue, format, pattern);
			}
			// ������
			if (format != null && format.trim() != "" && valueObj != null
					&& format.equalsIgnoreCase("amount")) {
				
				// Jet modified for formatted amount
				/*columnValue = Utils.replaceStr(columnValue, ",", "");
				decimal = new BigDecimal(columnValue);
				
				jxl.write.Number number = new jxl.write.Number(columnNum,
						rowNum, decimal.doubleValue(), cellFormat);
				sheet.addCell(number);*/
				
				//#######modified by wen_chy 20100310
				boolean fla = true;
				columnValue = Utils.replaceStr(columnValue, ",", "");
				try{
					decimal = new BigDecimal(columnValue);
				}catch(Exception e){
					fla = false;
				}
				
				if(fla){
					if("false".equals(isDecimal)){
						NumberFormat numFormat2 = new NumberFormat("#,##");
						WritableCellFormat cellFormat2 = new WritableCellFormat(font, numFormat2);
						jxl.write.Number number = new jxl.write.Number(columnNum,
								rowNum, decimal.doubleValue(), cellFormat2);
						sheet.addCell(number);
					}else{
						jxl.write.Number number = new jxl.write.Number(columnNum,
								rowNum, decimal.doubleValue(), cellFormat);
						sheet.addCell(number);
					}
					
				}else{
					Label trans = new Label(columnNum, rowNum, columnValue,
							data);
					sheet.addCell(trans);
				}		
				//###################

			} else {
				Label transDate = new Label(columnNum, rowNum, columnValue,
						data);
				sheet.addCell(transDate);
			}
			columnNum++;
		}
	}

	private static String specialDispose(String value) {
		value = Utils.replaceStr(value, "<br>", "\n");
		value = Utils.replaceStr(value, "&nbsp;", " ");
		value = value.trim();
		String[] valArray = Utils.splitStr(value, " ");
		String retValue = "";
		for (int i = 0; i < valArray.length; i++) {
			retValue += valArray[i] + " ";
		}
		return retValue;
	}

	// Modify by Jet 
	private static String specialDispose_general(String value) {
		value = Utils.replaceStr(value, "<br>", " - ");
		value = Utils.replaceStr(value, "&nbsp;", " ");
		value = value.trim();
		
		// remove the - at the end of the string
		if (value.endsWith("-")){
        	value = value.substring(0, value.length()-1);
		}
		
		String[] valArray = Utils.splitStr(value, " ");
		String retValue = "";
		for (int i = 0; i < valArray.length; i++) {
			retValue += valArray[i] + " ";
		}
		return retValue;
	}

	public static void main(String[] args) {
		System.out.println(specialDispose("EBK TFR 9005218689<br>BANK Online Transfer "));
	}
}
