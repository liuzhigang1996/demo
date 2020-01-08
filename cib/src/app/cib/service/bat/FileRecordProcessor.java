package app.cib.service.bat;

import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface FileRecordProcessor {
    public static int FILE_RECORD_STOP = -1;
    public static int FILE_RECORD_BATCH = 1;
    public static int FILE_RECORD_NOPROCESS = 0;

    public static String RECORD_FILE_HEADER = "A";
    public static String RECORD_BATCH_HEADER = "H";
    public static String RECORD_BATCH_HEADER_BANK_STM = "1";
    public static String RECORD_BATCH_HEADER_BANK_MTS = "2";
    public static String RECORD_BATCH_DETAIL = "D";
    public static String RECORD_FILE_END = "Z";
    public static String RECORD_FILE_END2 = "T"; //20110623 by wen
    
    public boolean isBatchHeaderExists(); //20110623 by wen

	public boolean isFileHeaderExists(); //20110623 by wen

	public boolean isTrailerExists(); //20110623 by wen

    public int processRecord(Map recordData) throws Exception;
    public void processRecords(List recordList) throws Exception;
}
