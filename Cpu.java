
import java.util.Arrays;

public class Cpu {
    private Cache cache;
    private Counter time = new Counter();

    public Cpu(Cache cache) {
        this.cache = cache;
    }

    public byte[] getData(Address address, int bytes) {
        if (bytes != 1 && bytes != 2 && bytes != 4) {
            throw new IllegalArgumentException("Supported only 1, 2 and 4 bytes");
        }
        time.add(Consts.CTR_TIME);
        byte[] data = cache.getData(address, bytes, time);
        time.add((bytes + Consts.DATA1_BUS_LEN - 1) / Consts.DATA1_BUS_LEN);
        return data;
    }

    public void writeData(Address address, byte[] data) {
        if (data.length != 1 && data.length != 2 && data.length != 4) {
            throw new IllegalArgumentException("Supported only 1, 2 and 4 bytes");
        }
        time.add(Consts.CTR_TIME);
        cache.writeData(address, data, time);
        time.add(Consts.CTR_TIME);
    }

    public static int getInt(byte[] data) {
        int result = data[0]; // byte is signed type
        for (int i = 1; i < data.length; i++) {
            result = result * (1 << 8) + data[i] + (data[i] < 0 ? (1 << 8) : 0);
        }
        return result;
    }

    public int getTime() {
        return time.get();
    }

    public double getCacheLose() {
        return cache.getLose() * 100;
    }

    public int multiply(Address addr1, int bytes1, Address addr2, int bytes2) {
        int a = getInt(getData(addr1, bytes1));
        int b = getInt(getData(addr2, bytes2));
        time.add(Consts.MULT_TIME);
        return a * b;
    }

    public void addFor(int N) {
        time.add(Consts.INIT_TIME + N * Consts.NEW_IT_TIME
                + N * Consts.SUM_TIME);
    }

    public void addInit() {
        time.add(Consts.INIT_TIME);
    }

    public void addSum() {
        time.add(Consts.SUM_TIME);
    }

    public void addAccess() {
        time.add(1);
    }

    public void exit() {
        time.add(1);
    }
}
