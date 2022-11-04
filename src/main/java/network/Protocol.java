package network;
import sharing.RootDTO;
import java.util.*;

public class Protocol {
    private byte type;
    private byte code;
    private int dataLength;
    private RootDTO data;

    public Protocol(byte t, byte c, RootDTO d) {
        type = t;
        code = c;
        data = d;
        Date a = new Date();
    }

    public byte getType() {
        return type;
    }

    public byte getCode() {
        return code;
    }

    public int getDataLength() {
        return dataLength;
    }

    public Object getData() {
        return data;
    }

    public void setType(byte t) {
        type = t;
    }

    public void setCode(byte c) {
        code = c;
    }

    public void setDataLength(int dL) {
        dataLength = dL;
    }

    public void setData(RootDTO d) {
        data = d;
    }

    public void setData(int dL, RootDTO d) {
        dataLength = dL;
        data = d;
    }

    public byte[] getBytes() {
        int resultArrayLength = 0;
        byte[] dataByteArray = data.getBytes();
        dataLength = dataByteArray.length;
        byte[] typeAndCodeByteArray = RootDTO.bitsToByteArray(type, code);
        byte[] dataLengthByteArray = RootDTO.intToByteArray(dataLength);
        resultArrayLength = (typeAndCodeByteArray.length + dataLengthByteArray.length + dataByteArray.length);

        byte[] resultArray = new byte[resultArrayLength];
        for (int i = 0; i < resultArrayLength; i++) {
            if (0 <= i && i < typeAndCodeByteArray.length) {
                resultArray[i] = typeAndCodeByteArray[i];
            }
            else if (typeAndCodeByteArray.length <= i && i < dataLengthByteArray.length) {
                resultArray[i] = dataLengthByteArray[i - typeAndCodeByteArray.length];
            }
            else {
                resultArray[i] = dataByteArray[i - typeAndCodeByteArray.length - dataLengthByteArray.length];
            }
        }

        return resultArray;
    }
}