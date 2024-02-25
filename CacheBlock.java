public interface CacheBlock {
    int find(int tag);
    byte[] getData(int tag, Counter time);
    byte[] getCacheLineData(int CacheLineNumber, Counter time);
    int writeData(Address addr, byte[] data);
}
