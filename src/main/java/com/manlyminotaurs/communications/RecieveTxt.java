package com.manlyminotaurs.communications;

import com.twilio.twiml.messaging.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;

import static spark.Spark.get;
import static spark.Spark.post;

public class RecieveTxt {
    /**
     *  main function to receive a text message
     *
     * @param args n/a
     */
    public static void main(String[] args) {
        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            res.type("application/xml");
            Body body = new Body
                    .Builder("The Robots are coming! Head for the hills!")
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            System.out.println(twiml.toXml());
            return null;
        });
    }
}
