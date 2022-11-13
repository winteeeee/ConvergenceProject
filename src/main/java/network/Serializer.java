package network;

import persistence.enums.Enum;

import java.time.LocalDateTime;

public class Serializer {
    public static byte[] bitsToByteArray(byte val1, byte val2) {
        return new byte[] { val1, val2 };
    }
    public static byte[] intToByteArray(int val) {
        return new byte[] {
                (byte)((val >> 8*3) & 0xff),
                (byte)((val >> 8*2) & 0xff),
                (byte)((val >> 8*1) & 0xff),
                (byte)((val >> 8*0) & 0xff)
        };
    }

    public static byte[] longToByteArray(long val) {
        return new byte[] {
                (byte)((val >> 8*7) & 0xff),
                (byte)((val >> 8*6) & 0xff),
                (byte)((val >> 8*5) & 0xff),
                (byte)((val >> 8*4) & 0xff),
                (byte)((val >> 8*3) & 0xff),
                (byte)((val >> 8*2) & 0xff),
                (byte)((val >> 8*1) & 0xff),
                (byte)((val >> 8*0) & 0xff)
        };
    }

    public static byte[] dateToByteArray(LocalDateTime val) {
        byte[] yearByteArray = intToByteArray(val.getYear());
        byte[] monthByteArray = intToByteArray(val.getMonth().getValue());
        byte[] dayByteArray = intToByteArray(val.getDayOfMonth());
        byte[] hourByteArray = intToByteArray(val.getHour());
        byte[] minuteByteArray = intToByteArray(val.getMinute());

        int resultArrayLength = yearByteArray.length + monthByteArray.length + dayByteArray.length + hourByteArray.length + minuteByteArray.length;
        byte[] resultArray = new byte[resultArrayLength];

        int pos = 0;
        System.arraycopy(resultArray, pos, yearByteArray, 0, yearByteArray.length); pos += yearByteArray.length;
        System.arraycopy(resultArray, pos, monthByteArray, 0, monthByteArray.length); pos += monthByteArray.length;
        System.arraycopy(resultArray, pos, dayByteArray, 0, dayByteArray.length); pos += dayByteArray.length;
        System.arraycopy(resultArray, pos, hourByteArray, 0, hourByteArray.length); pos += hourByteArray.length;
        System.arraycopy(resultArray, pos, minuteByteArray, 0, minuteByteArray.length); pos += minuteByteArray.length;

        return resultArray;
    }

    public static byte[] enumToByteArray(Enum val) {
        return intToByteArray(val.getCode());
    }
}
