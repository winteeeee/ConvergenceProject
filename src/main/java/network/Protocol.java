package network;
import lombok.Getter;
import lombok.Setter;
import persistence.dto.*;

@Getter
@Setter
public class Protocol {
    private byte type;
    private byte code;
    private int dataLength;
    private DTO data;

    public Protocol(byte t, byte c, int dL, DTO d) {
        type = t;
        code = c;
        dataLength = dL;
        data = d;
    }

    public Protocol(byte[] arr) {
        byteArrayToProtocol(arr);
    }

    public byte[] getBytes() {
        byte[] dataByteArray = new byte[0];
        try {
            dataByteArray = Serializer.getBytes(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataLength = dataByteArray.length;
        byte[] typeAndCodeByteArray = Serializer.bitsToByteArray(type, code);
        byte[] dataLengthByteArray = Serializer.intToByteArray(dataLength);

        int resultArrayLength = typeAndCodeByteArray.length + dataLengthByteArray.length + dataByteArray.length;
        byte[] resultArray = new byte[resultArrayLength];

        int pos = 0;
        System.arraycopy(resultArray, pos, typeAndCodeByteArray, 0, typeAndCodeByteArray.length); pos += typeAndCodeByteArray.length;
        System.arraycopy(resultArray, pos, dataLengthByteArray, 0, dataLengthByteArray.length); pos += dataLengthByteArray.length;
        System.arraycopy(resultArray, pos, dataByteArray, 0, dataByteArray.length); pos += dataByteArray.length;

        return resultArray;
    }

    private DTO byteArrayToData(byte type, byte code, byte[] arr) throws Exception {
        if (type == ProtocolType.REGISTER) {
            if (code == ProtocolCode.ORDER) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.REVIEW) {
                return (DTO) Deserializer.getObject(arr);
            }
        }

        else if (type == ProtocolType.MODIFICATION || type == ProtocolType.SEARCH) {
            if (code == ProtocolCode.MENU) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.OPTION) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.ORDER) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.REVIEW) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.STORE) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.USER) {
                return (DTO) Deserializer.getObject(arr);
            }
        }

        else if (type == ProtocolType.RESPONSE) {
            if (code == ProtocolCode.ACCEPT) {
                return null;
            }

            else if (code == ProtocolCode.REFUSAL) {
                return null;
            }
        }

        try {
            throw new Exception("타입과 코드가 맞지 않음");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Protocol byteArrayToProtocol(byte[] arr) {
        final int INT_LENGTH = 4;

        int pos = 0;
        byte type = arr[0];
        byte code = arr[1];
        pos += 2;
        byte[] dataLengthByteArray = new byte[4];
        System.arraycopy(dataLengthByteArray, 0, arr, pos, INT_LENGTH); pos += 4;
        int dataLength = Deserializer.byteArrayToInt(dataLengthByteArray);
        byte[] dataArray = new byte[dataLength];
        System.arraycopy(dataArray, 0, arr, 2 + INT_LENGTH, dataLength); pos += dataLength;
        DTO data = null;
        try {
            data = byteArrayToData(type, code, dataArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new Protocol(type, code, dataLength, data);
    }
}