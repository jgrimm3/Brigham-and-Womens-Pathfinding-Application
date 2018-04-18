package com.manlyminotaurs.communications;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import static spark.Spark.*;

public class SendTxt {
    /// Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACb244c12f3935445fb009d1fe4f07d46f";
    public static final String AUTH_TOKEN = "e7493ead9e8f61f70f1a0974071328ee";

    public void send(String PhoneNumber, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message
                .creator(new PhoneNumber(PhoneNumber), new PhoneNumber("+17864810586"),
                        body).create();

        System.out.println(message.getSid());
    }
}