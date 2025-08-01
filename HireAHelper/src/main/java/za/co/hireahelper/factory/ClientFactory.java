/* ClientFactory.java
   Author: S Hendricks(221095136)
   Date: 18 May 2025
*/

package za.co.hireahelper.factory;

import za.co.hireahelper.domain.*;
import za.co.hireahelper.util.Helper;
import java.util.List;

public class ClientFactory {

    public static Client createClient(String userId, String name, String email, String password, String mobileNumber,
                                      Area area,
                                      List<Booking> bookings, List<Message> messages, List<Review> reviews) {

        if (Helper.isNullOrEmpty(userId) || Helper.isNullOrEmpty(name) || Helper.isNullOrEmpty(email)
                || Helper.isNullOrEmpty(password) || Helper.isNullOrEmpty(mobileNumber)
                || area == null || bookings == null || messages == null || reviews == null) {
            return null;
        }

        if (!Helper.isValidEmail(email)) {
            return null;
        }

        if (!Helper.isValidMobileNumber(mobileNumber)) {
            return null;
        }

        return new Client.Builder()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .setPassword(password)
                .setMobileNumber(mobileNumber)
                .setArea(area)
                .setBookings(bookings)
                .setMessages(messages)
                .setReviews(reviews)
                .build();
    }
}
