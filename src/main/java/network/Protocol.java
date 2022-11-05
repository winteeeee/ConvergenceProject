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
        byte[] dataByteArray = data.getBytes();
        dataLength = dataByteArray.length;
        byte[] typeAndCodeByteArray = RootDTO.bitsToByteArray(type, code);
        byte[] dataLengthByteArray = RootDTO.intToByteArray(dataLength);

        int resultArrayLength = typeAndCodeByteArray.length + dataLengthByteArray.length + dataByteArray.length;
        byte[] resultArray = new byte[resultArrayLength];

        int pos = 0;
        System.arraycopy(resultArray, pos, typeAndCodeByteArray, 0, typeAndCodeByteArray.length); pos += typeAndCodeByteArray.length;
        System.arraycopy(resultArray, pos, dataLengthByteArray, 0, dataLengthByteArray.length); pos += dataLengthByteArray.length;
        System.arraycopy(resultArray, pos, dataByteArray, 0, dataByteArray.length); pos += dataByteArray.length;

        return resultArray;
    }
}