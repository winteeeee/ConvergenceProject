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
        if (data != null) {
            try {
                dataByteArray = Serializer.getBytes(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataLength = dataByteArray.length;
        byte[] typeAndCodeByteArray = Serializer.bitsToByteArray(type, code);
        byte[] dataLengthByteArray = Serializer.intToByteArray(dataLength);

        int resultArrayLength = typeAndCodeByteArray.length + dataLengthByteArray.length + dataByteArray.length;
        byte[] resultArray = new byte[resultArrayLength];

        int pos = 0;
        System.arraycopy(typeAndCodeByteArray, 0, resultArray, pos, typeAndCodeByteArray.length); pos += typeAndCodeByteArray.length;
        System.arraycopy(dataLengthByteArray, 0, resultArray, pos, dataLengthByteArray.length); pos += dataLengthByteArray.length;
        System.arraycopy(dataByteArray, 0, resultArray, pos, dataByteArray.length); pos += dataByteArray.length;

        return resultArray;
    }

    private DTO byteArrayToData(byte type, byte code, byte[] arr) throws Exception {
        if (type == ProtocolType.REGISTER) {
            return (DTO) Deserializer.getObject(arr);
            /*if (code == ProtocolCode.ORDER) {
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
            else if ()*/
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
            else if (code == (ProtocolCode.STORE | ProtocolCode.HISTORY)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.REVIEW | ProtocolCode.HISTORY)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.CLASSIFICATION) {
                return (DTO) Deserializer.getObject(arr);
            }
        }

        else if (type == ProtocolType.RESPONSE) {
            if (code == ProtocolCode.ACCEPT) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.REFUSAL) {
                return null;
            }

            else if (code == ProtocolCode.USER) {
                return (DTO) Deserializer.getObject(arr);
            }
            else if (code == (ProtocolCode.USER | ProtocolCode.ACCEPT)) {
                return (DTO) Deserializer.getObject(arr);
            }
            else if (code == (ProtocolCode.USER | ProtocolCode.REFUSAL)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.MENU | ProtocolCode.ACCEPT)) {
                return (DTO) Deserializer.getObject(arr);
            }
            else if (code == (ProtocolCode.MENU | ProtocolCode.REFUSAL)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.STORE | ProtocolCode.ACCEPT)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.STORE | ProtocolCode.REFUSAL)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.OPTION)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.ORDER) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.ORDER | ProtocolCode.ACCEPT)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == (ProtocolCode.ORDER | ProtocolCode.REFUSAL)) {
                return (DTO) Deserializer.getObject(arr);
            }

            else if (code == ProtocolCode.REVIEW) {
                return (DTO) Deserializer.getObject(arr);
            }
        }

        try {
            throw new Exception("타입과 코드가 맞지 않음");
        } catch (Exception e) {
            System.out.println(type + " " + code);
            e.printStackTrace();
        }

        return null;
    }

    public void byteArrayToProtocol(byte[] arr) {
        final int INT_LENGTH = 4;
        type = arr[0];
        code = arr[1];

        int pos = 0;
        pos += 2;
        byte[] dataLengthByteArray = new byte[4];
        System.arraycopy(arr, pos, dataLengthByteArray, 0, INT_LENGTH); pos += 4;
        dataLength = Deserializer.byteArrayToInt(dataLengthByteArray);

        byte[] dataArray = new byte[dataLength];
        System.arraycopy(arr, 2 + INT_LENGTH, dataArray, 0, dataLength); pos += dataLength;
        try {
            data = byteArrayToData(type, code, dataArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}