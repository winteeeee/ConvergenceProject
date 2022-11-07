package network;

public class ProtocolCode {
    static final int REGIST = 1;
    static final int MODIFICATION = 1 << 1;
    static final int ACCEPT = 1 << 2;
    static final int REFUSAL = 1 << 3;
    static final int MENU = 1 << 16;
    static final int OPTION = 1 << 17;
    static final int ORDER = 1 << 18;
    static final int REVIEW = 1 << 19;
    static final int STORE = 1 << 20;
    static final int TOTAL_ORDER = 1 << 21;
    static final int USER = 1 << 22;
}
