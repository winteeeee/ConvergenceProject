package sharing;
import persistence.enums.EnumInterface;
import java.time.LocalDateTime;

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

    public static byte[] dateToByteArray(LocalDateTime val) {
        //년,월,일,시,분
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

    public static byte[] enumToByteArray(EnumInterface val) {
        String name = val.getName();
        int code = val.getCode();
        String title = val.getTitle();

        byte[] nameByteArray = name.getBytes();
        byte[] codeByteArray = intToByteArray(code);
        byte[] titleByteArray = title.getBytes();

        int resultArrayLength = nameByteArray.length + codeByteArray.length + titleByteArray.length;
        byte[] resultArray = new byte[resultArrayLength];

        int pos = 0;
        System.arraycopy(resultArray, pos, nameByteArray, 0, nameByteArray.length); pos += nameByteArray.length;
        System.arraycopy(resultArray, pos, codeByteArray, 0, codeByteArray.length); pos += codeByteArray.length;
        System.arraycopy(resultArray, pos, titleByteArray, 0, titleByteArray.length); pos += titleByteArray.length;

        return resultArray;
    }

    public byte[] getBytes();
}
