package com.example.lessons_service.service;

import com.example.lessons_service.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final TwilioConfig twilioConfig;

    public void sendSms(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioConfig.getFromNumber()),
                message
        ).create();
    }
}
