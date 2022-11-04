package sharing;
import persistence.enums.EnumInterface;

import java.util.Date;

public interface RootDTO {
    public static byte[] bitsToByteArray(byte val1, byte val2) {
        return new byte[] {
                (byte)((0xff & val1) << 8*1),
                (byte)((0xff & val2) << 8*0)
        };
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

    public static byte[] dateToByteArray(Date val) {
        return longToByteArray(val.getTime());
    }

    public static byte[] enumToByteArray(EnumInterface val) {
        String name = val.getName();
        int code = val.getCode();
        String title = val.getTitle();

        byte[] nameByteArray = name.getBytes();
        byte[] codeByteArray = intToByteArray(code);
        byte[] titleByteArray = title.getBytes();
        int resultArrayLength = nameByteArray.length + codeByteArray.length + titleByteArray.length;

        byte[] resultArray = new byte[resultArrayLength];
        for (int i = 0; i < resultArrayLength; i++) {
            if (0 <= i && i < nameByteArray.length) {
                resultArray[i] = nameByteArray[i];
            }
            else if (nameByteArray.length <= i && i < codeByteArray.length) {
                resultArray[i] = codeByteArray[i - nameByteArray.length];
            }
            else {
                resultArray[i] = titleByteArray[i - nameByteArray.length - codeByteArray.length];
            }
        }

        return resultArray;
    }

    public byte[] getBytes();
}
