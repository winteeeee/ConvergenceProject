package sharing;
import network.Serializer;
import persistence.dto.StoreDTO;
import persistence.enums.EnumInterface;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface RootDTO {
    default byte[] getBytes() {
        ArrayList<Byte> result = new ArrayList<>();
        Field[] classMembers = this.getClass().getDeclaredFields();
        for(int i = 0; i < classMembers.length; i++) {
            classMembers[i].setAccessible(true);
            String type = classMembers[i].getType().toString();

            byte[] arr = new byte[0];
            try {
                Object memberVal = classMembers[i].get(this);
                if (type.equals("int")) {
                    arr = Serializer.intToByteArray((int)memberVal);
                }

                else if (type.equals("long")) {
                    arr = Serializer.longToByteArray((long)memberVal);
                }

                else {
                    if (type.contains("String")) {
                        arr = ((String)memberVal).getBytes();

                        int stringLength = arr.length;
                        byte[] stringLengthByteArray = Serializer.intToByteArray(stringLength);
                        for(int j = 0; j < stringLengthByteArray.length; j++) {
                            result.add(stringLengthByteArray[j]);
                        }
                    }

                    else if (type.contains("LocalDateTime")) {
                        arr = Serializer.dateToByteArray((LocalDateTime)memberVal);

                        int dateLength = arr.length;
                        byte[] dateLengthByteArray = Serializer.intToByteArray(dateLength);
                        for(int j = 0; j < dateLengthByteArray.length; j++) {
                            result.add(dateLengthByteArray[j]);
                        }
                    }

                    else if (type.contains("enums")) {
                        arr = Serializer.enumToByteArray((EnumInterface)memberVal);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            for(int j = 0; j < arr.length; j++) {
                result.add(arr[j]);
            }
        }

        byte[] returnArray = new byte[result.size()];
        for(int i = 0; i < result.size(); i++) {
            returnArray[i] = result.get(i);
        }

        return returnArray;
    }
}
