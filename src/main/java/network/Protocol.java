package network;

public class Protocol {
    private byte type;
    private byte code;
    private int dataLength;
    private Object data;

    public Protocol(byte t, byte c, int dL, Object d) {
        type = t;
        code = c;
        dataLength = dL;
        data = d;
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

    public void setData(Object d) {
        data = d;
    }

    public void setData(int dL, Object d) {
        dataLength = dL;
        data = d;
    }
}
