package six.six.gateway.megafonsns;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.logging.Logger;
import org.json.JSONObject;
import six.six.gateway.SMSService;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MegafonSMSService implements SMSService {

    private static Logger logger = Logger.getLogger(MegafonSMSService.class);

    private static final int CUSTOMER_DATABASE_ID = 999;
    private static final String RECIPIENT_TYPE_ID = "customer";
    private static final String RECIPIENT_ID = "1014";
    private static final int MESSAGE_TYPE_ID = 100000;
    private static final String CHANNEL_CODE = "sms";
    private static final String OPERATOR_ADDRESS = "MegaFon";

    private static final String APPLICATION_JSON = "application/json";
    private static final int STATUS_CODE_200 = 200;

    private String baseUrl;

    public MegafonSMSService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean send(String phoneNumber, String message, String login, String pw) {
        boolean result = false;

        String formattedPhoneNumberForMegafon = verifyPhoneNumberForMegafon(phoneNumber);
        if (isNotExistRequiredAttributes(formattedPhoneNumberForMegafon, this.baseUrl, login, pw)) {
            logger.error("One of the required attributes is missing!");
            return false;
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(this.baseUrl + "/notifications");

            JSONObject object = createJsonObject(formattedPhoneNumberForMegafon, message);
            List<String> stringList = Collections.singletonList(object.toString());
            HttpEntity httpEntity = new StringEntity(stringList.toString(), Charset.defaultCharset());
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("LOGIN", login);
            httpPost.setHeader("APPL_CODE", pw);
            httpPost.setHeader("Accept", APPLICATION_JSON);
            httpPost.setHeader("Content-type", APPLICATION_JSON);

            CloseableHttpResponse httpResponse = client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == STATUS_CODE_200) {
                result = true;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }

        return result;
    }

    private boolean isNotExistRequiredAttributes(String formattedPhoneNumberForMegafon, String baseUrl,
                                                 String login, String pw) {

        return Objects.isNull(formattedPhoneNumberForMegafon) || Objects.isNull(baseUrl)
                || Objects.isNull(login) || Objects.isNull(pw);
    }

    private String verifyPhoneNumberForMegafon(String phoneNumber) {
        if (Objects.nonNull(phoneNumber) && !phoneNumber.isEmpty()) {
            phoneNumber = phoneNumber.replaceAll("\\D", "");
            if ((phoneNumber.startsWith("7") || phoneNumber.startsWith("8")) && phoneNumber.length() == 11) {
                phoneNumber = phoneNumber.substring(1);
            }
            if (phoneNumber.startsWith("9") && phoneNumber.length() == 10) {
                return phoneNumber;
            }
        }

        return null;
    }

    private JSONObject createJsonObject(String phoneNumber, String message) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerDatabaseId", CUSTOMER_DATABASE_ID);
        jsonObject.put("recipientTypeId", RECIPIENT_TYPE_ID);
        jsonObject.put("recipientId", RECIPIENT_ID);
        jsonObject.put("messageTypeId", MESSAGE_TYPE_ID);
        jsonObject.put("channelCode", CHANNEL_CODE);
        jsonObject.put("operatorAddress", OPERATOR_ADDRESS);
        if (Objects.nonNull(phoneNumber) && !phoneNumber.isEmpty()) {
            jsonObject.put("recipientAddress", phoneNumber);
        }
        if (Objects.nonNull(message) && !message.isEmpty()) {
            jsonObject.put("messageText", message);
        }

        return jsonObject;
    }
}