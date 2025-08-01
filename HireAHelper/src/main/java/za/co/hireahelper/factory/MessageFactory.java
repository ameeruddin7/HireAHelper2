// Gabriel Kiewietz
// 230990703
// 18 May 2024

package za.co.hireahelper.factory;

import za.co.hireahelper.domain.Client;
import za.co.hireahelper.domain.Message;
import za.co.hireahelper.domain.ServiceProvider;
import za.co.hireahelper.util.Helper;

import java.time.LocalDateTime;

public class MessageFactory {

    public static Message createMessage(String messageId, LocalDateTime timeStamp,
                                        String content, Client client,
                                        ServiceProvider serviceProvider) {
        // Validate required fields
        if (Helper.isNullOrEmpty(messageId) ||
                Helper.isNullOrEmpty(content) ||
                timeStamp == null) {
            return null;
        }

        return new Message.Builder()
                .setMessageId(messageId)
                .setTimeStamp(timeStamp)
                .setContent(content)
                .setClient(client)
                .setServiceProvider(serviceProvider)
                .build();
    }
}