package network;

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
}
