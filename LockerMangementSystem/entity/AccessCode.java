package entity;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccessCode {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new Random();
    public static AccessCode instance;
    private static Lock lck = new ReentrantLock();

    private AccessCode() {
    }

    public static AccessCode getInstance() {
        if (instance == null) {
            lck.lock();
            try {
                if (instance == null) {
                    instance = new AccessCode();
                }
            } finally {
                lck.unlock();
            }
        }
        return instance;
    }

    public String getAccessCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
