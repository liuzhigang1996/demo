package app.cib.util.smsclient;

import java.util.Map;

import app.cib.util.smsclient.sms.SmsRequest;
import app.cib.util.smsclient.sms.SmsResponse;

public interface WsClient {

	public SmsResponse sendSms(SmsRequest req) throws Exception;
}
