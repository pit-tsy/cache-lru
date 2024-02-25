public interface Cache {
    boolean inCache(Address addr);
    byte[] getData(Address addr, int bytes, Counter time);
    void writeData(Address addr, byte[] data, Counter time);
    double getLose();
}
