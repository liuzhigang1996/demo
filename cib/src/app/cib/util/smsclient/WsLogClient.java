package app.cib.util.smsclient;

import app.cib.util.smsclient.log.LogRequest;
import app.cib.util.smsclient.log.LogResponse;
import app.cib.util.smsclient.log.LogSmsRequest;

public interface WsLogClient {

	public LogResponse logInfo(LogRequest logRequest);

	public LogResponse logSms(LogSmsRequest logSmsRequest);
	
}
