package network;

import persistence.dto.MenuDTO;
import persistence.dto.OrderDTO;
import persistence.dto.ReviewDTO;
import persistence.dto.StoreDTO;
import sharing.RootDTO;
import java.time.LocalDateTime;

public class Deserializer {
    public static int byteArrayToInt(byte[] arr) {
        return (int)(
                (0xff & arr[0]) << 8*3 |
                (0xff & arr[1]) << 8*2 |
                (0xff & arr[2]) << 8*1 |
                (0xff & arr[3]) << 8*0
        );
    }

    public static long byteArrayToLong(byte[] arr) {
        return (long)(
                (0xff & arr[0]) << 8*7 |
                (0xff & arr[1]) << 8*6 |
                (0xff & arr[2]) << 8*5 |
                (0xff & arr[3]) << 8*4 |
                (0xff & arr[4]) << 8*3 |
                (0xff & arr[5]) << 8*2 |
                (0xff & arr[6]) << 8*1 |
                (0xff & arr[7]) << 8*0
        );
    }

    public static LocalDateTime byteArrayToDate(byte[] arr) {
        final int LENGTH = 4;

        byte[] yearByteArray = new byte[LENGTH];
        byte[] monthByteArray = new byte[LENGTH];
        byte[] dayByteArray = new byte[LENGTH];
        byte[] hourByteArray = new byte[LENGTH];
        byte[] minuteByteArray = new byte[LENGTH];

        int pos = 0;
        System.arraycopy(yearByteArray, 0, arr, pos, LENGTH); pos += LENGTH;
        System.arraycopy(monthByteArray, 0, arr, pos, LENGTH); pos += LENGTH;
        System.arraycopy(dayByteArray, 0, arr, pos, LENGTH); pos += LENGTH;
        System.arraycopy(hourByteArray, 0, arr, pos, LENGTH); pos += LENGTH;
        System.arraycopy(minuteByteArray, 0, arr, pos, LENGTH); pos += LENGTH;

        int year = byteArrayToInt(yearByteArray);
        int month = byteArrayToInt(monthByteArray);
        int day = byteArrayToInt(dayByteArray);
        int hour = byteArrayToInt(hourByteArray);
        int minute = byteArrayToInt(minuteByteArray);

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public static RootDTO byteArrayToStoreDTO(byte[] arr) {
        final int INT_LENGTH = 4;
        final int LONG_LENGTH = 8;

        int pos = 0;

        byte[] idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long id = Deserializer.byteArrayToLong(idByteArray);

        byte[] nameLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(nameLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int nameLength = Deserializer.byteArrayToInt(nameLengthByteArray);

        byte[] nameByteArray = new byte[nameLength];
        System.arraycopy(nameByteArray, 0, arr, pos, nameLength); pos += nameLength;
        String name = new String(nameByteArray);

        byte[] tagLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(tagLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int tagLength = Deserializer.byteArrayToInt(tagLengthByteArray);

        byte[] tagByteArray = new byte[tagLength];
        System.arraycopy(tagByteArray, 0, arr, pos, tagLength); pos += tagLength;
        String tag = new String(tagByteArray);

        byte[] carry_priceByteArray = new byte[INT_LENGTH];
        System.arraycopy(carry_priceByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int carry_price = Deserializer.byteArrayToInt(carry_priceByteArray);

        byte[] phoneLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(phoneLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int phoneLength = Deserializer.byteArrayToInt(phoneLengthByteArray);

        byte[] phoneByteArray = new byte[phoneLength];
        System.arraycopy(phoneByteArray, 0, arr, pos, phoneLength); pos += phoneLength;
        String phone = new String(phoneByteArray);

        byte[] addressLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(addressLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int addressLength = Deserializer.byteArrayToInt(addressLengthByteArray);

        byte[] addressByteArray = new byte[addressLength];
        System.arraycopy(addressByteArray, 0, arr, pos, addressLength); pos += addressLength;
        String address = new String(addressByteArray);

        byte[] user_idLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(user_idLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int user_idLength = Deserializer.byteArrayToInt(user_idLengthByteArray);

        byte[] user_idByteArray = new byte[user_idLength];
        System.arraycopy(user_idByteArray, 0, arr, pos, user_idLength); pos += user_idLength;
        String user_id = new String(user_idByteArray);

        byte[] carry_saleByteArray = new byte[INT_LENGTH];
        System.arraycopy(carry_saleByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int carry_sale = Deserializer.byteArrayToInt(carry_saleByteArray);

        return new StoreDTO(id, name, tag, carry_price, phone, address, user_id, carry_sale);
    }

    public static RootDTO byteArrayToMenuDTO(byte[] arr) {
        final int INT_LENGTH = 4;
        final int LONG_LENGTH = 8;

        int pos = 0;

        byte[] idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long id = Deserializer.byteArrayToLong(idByteArray);

        byte[] nameLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(nameLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int nameLength = Deserializer.byteArrayToInt(nameLengthByteArray);

        byte[] nameByteArray = new byte[nameLength];
        System.arraycopy(nameByteArray, 0, arr, pos, nameLength); pos += nameLength;
        String name = new String(nameByteArray);

        byte[] priceByteArray = new byte[INT_LENGTH];
        System.arraycopy(priceByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int price = Deserializer.byteArrayToInt(priceByteArray);

        byte[] saleByteArray = new byte[INT_LENGTH];
        System.arraycopy(saleByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int sale = Deserializer.byteArrayToInt(saleByteArray);

        byte[] store_idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(store_idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long store_id = Deserializer.byteArrayToLong(store_idByteArray);

        byte[] ghost_review_idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(ghost_review_idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long ghost_review_id = Deserializer.byteArrayToLong(ghost_review_idByteArray);

        return new MenuDTO(id, name, price, sale, store_id, ghost_review_id);
    }

    public static RootDTO byteArrayToOrderDTO(byte[] arr) {
        final int INT_LENGTH = 4;
        final int LONG_LENGTH = 8;

        int pos = 0;

        byte[] idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long id = Deserializer.byteArrayToLong(idByteArray);

        byte[] regdateLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(regdateLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int regdateLength = Deserializer.byteArrayToInt(regdateLengthByteArray);

        byte[] regdateByteArray = new byte[regdateLength];
        System.arraycopy(regdateByteArray, 0, arr, pos, regdateLength); pos += regdateLength;
        LocalDateTime regdate = Deserializer.byteArrayToDate(regdateByteArray);

        byte[] optionLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(optionLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int optionLength = Deserializer.byteArrayToInt(optionLengthByteArray);

        byte[] optionByteArray = new byte[optionLength];
        System.arraycopy(optionByteArray, 0, arr, pos, optionLength); pos += optionLength;
        String option = new String(optionByteArray);

        byte[] priceByteArray = new byte[INT_LENGTH];
        System.arraycopy(priceByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int price = Deserializer.byteArrayToInt(priceByteArray);

        byte[] statusByteArray = new byte[INT_LENGTH];
        System.arraycopy(statusByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int status = Deserializer.byteArrayToInt(statusByteArray);

        byte[] commentLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(commentLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int commentLength = Deserializer.byteArrayToInt(commentLengthByteArray);

        byte[] commentByteArray = new byte[commentLength];
        System.arraycopy(commentByteArray, 0, arr, pos, commentLength); pos += commentLength;
        String comment = new String(commentByteArray);

        byte[] total_order_idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(total_order_idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long total_order_id = Deserializer.byteArrayToLong(total_order_idByteArray);

        byte[] store_idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(store_idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long store_id = Deserializer.byteArrayToLong(store_idByteArray);

        return new OrderDTO(id, regdate, option, price, status, comment, total_order_id, store_id);
    }

    public static RootDTO byteArrayToReviewDTO(byte[] arr) {
        final int INT_LENGTH = 4;
        final int LONG_LENGTH = 8;

        int pos = 0;

        byte[] idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long id = Deserializer.byteArrayToLong(idByteArray);

        byte[] regdateLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(regdateLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int regdateLength = Deserializer.byteArrayToInt(regdateLengthByteArray);

        byte[] regdateByteArray = new byte[regdateLength];
        System.arraycopy(regdateByteArray, 0, arr, pos, regdateLength); pos += regdateLength;
        LocalDateTime regdate = Deserializer.byteArrayToDate(regdateByteArray);

        byte[] contentsLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(contentsLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int contentsLength = Deserializer.byteArrayToInt(contentsLengthByteArray);

        byte[] contentsByteArray = new byte[contentsLength];
        System.arraycopy(contentsByteArray, 0, arr, pos, contentsLength); pos += contentsLength;
        String contents = new String(contentsByteArray);

        byte[] upper_review_idByteArray = new byte[LONG_LENGTH];
        System.arraycopy(upper_review_idByteArray, 0, arr, pos, LONG_LENGTH); pos += LONG_LENGTH;
        long upper_review_id = Deserializer.byteArrayToLong(upper_review_idByteArray);

        byte[] user_idLengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(user_idLengthByteArray, 0, arr, pos, INT_LENGTH); pos += INT_LENGTH;
        int user_idLength = Deserializer.byteArrayToInt(user_idLengthByteArray);

        byte[] user_idByteArray = new byte[user_idLength];
        System.arraycopy(user_idByteArray, 0, arr, pos, user_idLength); pos += user_idLength;
        String user_id = new String(user_idByteArray);

        return new ReviewDTO(id, regdate, contents, upper_review_id, user_id);
    }
}
