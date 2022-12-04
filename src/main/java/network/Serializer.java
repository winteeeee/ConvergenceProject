package network;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Serializer {
    final static String UID_FIELD_NAME = "serialVersionUID";
    final static long DEFAULT_UID = 0l;
    final static int INT_LENGTH = 4;
    final static int LONG_LENGTH = 8;


    public static byte[] getBytes(Serializable obj) throws Exception {
        Class<?> c = obj.getClass();
        ArrayList<Byte> result = new ArrayList<>();
        byte[] head = makeHeader(obj);
        byte[] body;
        if (c.isEnum()) {
            body = stringToByteArray(((Enum) obj).name());
        }
        else {
            body = makeBody(obj);
        }

        addArrList(result, intToByteArray(head.length + body.length));
        addArrList(result, head);
        addArrList(result, body);
        return byteListToArray(result);
    }

    public static byte[] makeHeader(Serializable obj) throws Exception {
        Class<?> c = obj.getClass();

        String cName = c.getName();
        long uid = DEFAULT_UID;

        Field[] member = c.getDeclaredFields();
        ArrayList<Byte> result = new ArrayList<>();

        try {
            Field uidField = c.getDeclaredField(UID_FIELD_NAME);
            uidField.setAccessible(true);
            uid = (long) uidField.get(result);
        }
        catch (NoSuchFieldException e) {  }

        addArrList(result, stringToByteArray(cName));
        addArrList(result, longToByteArray(uid));
        return byteListToArray(result);
    }

    public static byte[] makeBody(Serializable obj) throws Exception {
        Class<?> c = obj.getClass();

        Field[] member = c.getDeclaredFields();
        ArrayList<Byte> result = new ArrayList<>();

        byte[] arr = new byte[0];
        Class<?> type;
        Object memberVal;
        String typeStr;

        for (int i = 0; i < member.length; i++) {
            if (!Modifier.isStatic(member[i].getModifiers())) {
                member[i].setAccessible(true);
                type = member[i].getType();
                memberVal = member[i].get(obj);

                if (memberVal == null) {    // 직렬화 첫 비트는 null 확인용 비트
                    addArrList(result, new byte[]{0});
                }
                else {
                    typeStr = type.toString();
                    if (typeStr.equals("int")) {
                        arr = intToByteArray((int) memberVal);
                    }
                    else if (typeStr.equals("long")) {
                        arr = longToByteArray((long) memberVal);
                    }
                    else if (typeStr.contains("Integer")) {
                        arr = intToByteArray((Integer) memberVal);
                    }
                    else if (typeStr.contains("Long")) {
                        arr = longToByteArray((Long) memberVal);
                    }
                    else if (typeStr.contains("String")) {
                        arr = stringToByteArray((String) memberVal);
                    }
                    else if (typeStr.contains("LocalDateTime")) {
                        arr = dateToByteArray((LocalDateTime)memberVal);
                    }
                    else {
                        for (Class<?> temp : type.getInterfaces()) {        // TODO Serializable 상속되지 않은 객체가 있을시 예외 던지도록 만들어야함
                            if (temp.getName().contains("Serializable")) {
                                arr = getBytes((Serializable) memberVal);
                            }
                        }
                    }
                    addArrList(result, new byte[]{1});
                    addArrList(result, arr);
                }
            }
        }

        return byteListToArray(result);
    }

    public static int checkVersion(Class<?> c, byte[] objInfo, int idx) throws Exception {
        long destUID = DEFAULT_UID;

        try {
            Field uidField = c.getDeclaredField(UID_FIELD_NAME);
            uidField.setAccessible(true);
            destUID = (long) uidField.get(c);
        }
        catch (NoSuchFieldException e) {  }

        byte[] longByteArray = new byte[LONG_LENGTH];
        System.arraycopy(objInfo, idx, longByteArray, 0, LONG_LENGTH); idx += LONG_LENGTH;
        long srcUID = byteArrayToLong(longByteArray);

        if (destUID != srcUID) {
            throw new Exception("not match version");
        }

        return idx;
    }

    public static Object makeObject(Class<?> c, byte[] objInfo, int idx) throws Exception {
        Object result = c.getConstructor().newInstance();
        Field[] member = c.getDeclaredFields();

        Class<?> type;
        String typeStr;
        byte[] arr;

        for (int i = 0; i < member.length; i++) {
            if (!member[i].getName().equals(UID_FIELD_NAME)) {
                member[i].setAccessible(true);
                type = member[i].getType();

                if (objInfo[idx++] == 0) {
                    member[i].set(result, null);
                }
                else {
                    typeStr = type.toString();
                    if (typeStr.equals("int") || typeStr.contains("Integer")) {
                        arr = new byte[INT_LENGTH];
                        System.arraycopy(objInfo, idx, arr, 0, INT_LENGTH);
                        idx += INT_LENGTH;
                        member[i].set(result, byteArrayToInt(arr));
                    }
                    else if (typeStr.equals("long") || typeStr.contains("Long")) {
                        arr = new byte[LONG_LENGTH];
                        System.arraycopy(objInfo, idx, arr, 0, LONG_LENGTH);
                        idx += LONG_LENGTH;
                        member[i].set(result, byteArrayToLong(arr));
                    }
                    else if (typeStr.contains("String")) {
                        arr = new byte[INT_LENGTH];
                        System.arraycopy(objInfo, idx, arr, 0, INT_LENGTH);
                        idx += INT_LENGTH;
                        int length = byteArrayToInt(arr);

                        arr = new byte[length];
                        System.arraycopy(objInfo, idx, arr, 0, length);
                        idx += length;
                        member[i].set(result, new String(arr));
                    }
                    else if (typeStr.contains("LocalDateTime")) {
                        byte[] yearByteArray = new byte[INT_LENGTH];
                        byte[] monthByteArray = new byte[INT_LENGTH];
                        byte[] dayByteArray = new byte[INT_LENGTH];
                        byte[] hourByteArray = new byte[INT_LENGTH];
                        byte[] minuteByteArray = new byte[INT_LENGTH];

                        System.arraycopy(objInfo, idx, yearByteArray, 0, INT_LENGTH); idx += INT_LENGTH;
                        System.arraycopy(objInfo, idx, monthByteArray, 0, INT_LENGTH); idx += INT_LENGTH;
                        System.arraycopy(objInfo, idx, dayByteArray, 0, INT_LENGTH); idx += INT_LENGTH;
                        System.arraycopy(objInfo, idx, hourByteArray, 0, INT_LENGTH); idx += INT_LENGTH;
                        System.arraycopy(objInfo, idx, minuteByteArray, 0, INT_LENGTH); idx += INT_LENGTH;

                        int year = byteArrayToInt(yearByteArray);
                        int month = byteArrayToInt(monthByteArray);
                        int day = byteArrayToInt(dayByteArray);
                        int hour = byteArrayToInt(hourByteArray);
                        int minute = byteArrayToInt(minuteByteArray);

                        member[i].set(result, LocalDateTime.of(year, month, day, hour, minute));
                    }
                    else {
                        for (Class<?> temp : type.getInterfaces()) {
                            if (temp.getName().contains("Serializable")) {
                                arr = new byte[INT_LENGTH];
                                System.arraycopy(objInfo, idx, arr, 0, INT_LENGTH);
                                int length = byteArrayToInt(arr);

                                arr = new byte[INT_LENGTH + length];
                                System.arraycopy(objInfo, idx, arr, 0, length + INT_LENGTH);
                                idx += length + INT_LENGTH;

                                member[i].set(result, getObject(arr));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public static Object getObject(byte[] objInfo) throws Exception {
        int idx = INT_LENGTH;

        /* find class */
        String name;
        byte[] lengthByteArray = new byte[INT_LENGTH];
        System.arraycopy(objInfo, idx, lengthByteArray, 0, INT_LENGTH); idx += INT_LENGTH;
        int length = byteArrayToInt(lengthByteArray);

        byte[] stringByteArray = new byte[length];
        System.arraycopy(objInfo, idx, stringByteArray,  0, length); idx += length;
        name = new String(stringByteArray);

        Class<?> c = Class.forName(name);
        idx = checkVersion(c, objInfo, idx);
        Object result = null;

        if (c.isEnum()) {
            byte[] arr = new byte[INT_LENGTH];
            System.arraycopy(objInfo, idx, arr, 0, INT_LENGTH);
            idx += INT_LENGTH;
            int enumNameLen = byteArrayToInt(arr);

            arr = new byte[enumNameLen];
            System.arraycopy(objInfo, idx, arr, 0, enumNameLen);

            result = Enum.valueOf((Class<? extends Enum>) c, new String(arr));
        }
        else {
            result = makeObject(c, objInfo, idx);
        }

        return result;
    }




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

    public static byte[] stringToByteArray(String str) {
        ArrayList<Byte> result = new ArrayList<>();
        byte[] arr = str.getBytes();

        int length = arr.length;
        byte[] lengthByteArray = Serializer.intToByteArray(length);

        addArrList(result, lengthByteArray);
        addArrList(result, arr);
        return byteListToArray(result);
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
        System.arraycopy(yearByteArray, 0, resultArray, pos, yearByteArray.length); pos += yearByteArray.length;
        System.arraycopy(monthByteArray, 0, resultArray, pos, monthByteArray.length); pos += monthByteArray.length;
        System.arraycopy(dayByteArray, 0, resultArray, pos, dayByteArray.length); pos += dayByteArray.length;
        System.arraycopy(hourByteArray, 0, resultArray, pos, hourByteArray.length); pos += hourByteArray.length;
        System.arraycopy(minuteByteArray, 0, resultArray, pos, minuteByteArray.length); pos += minuteByteArray.length;

        return resultArray;
    }

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


    /* util */
    public static void addArrList(ArrayList<Byte> result, byte[] arr) {
        for(int j = 0; j < arr.length; j++) {
            result.add(arr[j]);
        }
    }

    public static byte[] byteListToArray(ArrayList<Byte> byteList) {
        byte[] returnArray = new byte[byteList.size()];
        for(int i = 0; i < byteList.size(); i++) {
            returnArray[i] = byteList.get(i);
        }

        return returnArray;
    }
}
