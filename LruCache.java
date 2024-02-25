import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LruCache implements Cache {
    private final Mem source;
    protected List<CacheBlock> blocks = new ArrayList<>();
    private int cacheMissCount = 0;
    private int totalCount = 0;

    public LruCache(Mem source, boolean pseudo) {
        this.source = source;
        if (pseudo) {
            for (int i = 0; i < Consts.CACHE_SETS_COUNT; i++) {
                blocks.add(new pLruCacheBlock(i, source));
            }
        } else {
            for (int i = 0; i < Consts.CACHE_SETS_COUNT; i++) {
                blocks.add(new LruCacheBlock(i, source));
            }
        }
    }

    @Override
    public boolean inCache(Address addr) {
        int index = addr.getIndex();
        int tag = addr.getTag();
        return blocks.get(index).find(tag) >= 0;
    }

    @Override
    public byte[] getData(Address addr, int bytes, Counter time) {
        int tag = addr.getTag();
        int index = addr.getIndex();
        if (blocks.get(index).find(tag) < 0) {
            time.add(Consts.CACHE_MISS_TIME);
            byte[] data = source.getData(addr.mainAddress(), Consts.CACHE_LINE_SIZE, time);
            int writeTime = blocks.get(index).writeData(addr, data);
            time.add(writeTime);
            cacheMissCount += 1;
        } else {
            time.add(Consts.CACHE_HIT_TIME);
        }

        totalCount += 1;
        byte[] data = blocks.get(index).getData(tag, time);
        return Arrays.copyOfRange(data, addr.getOffset(), addr.getOffset() + bytes);
    }

    public double getLose() {
        if (totalCount == 0) return 1;
        return (double) (totalCount - cacheMissCount) / totalCount;
    }

    @Override
    public void writeData(Address addr, byte[] newData, Counter time) {
        int index = addr.getIndex();
        int tag = addr.getTag();
        if (blocks.get(index).find(tag) < 0) {
            time.add(Consts.CACHE_MISS_TIME);
            byte[] data = source.getData(addr.mainAddress(), Consts.CACHE_LINE_SIZE, time);
            int writeTime = blocks.get(index).writeData(addr, data);
            time.add(writeTime);
            cacheMissCount += 1;
        } else {
            time.add(Consts.CACHE_HIT_TIME);
        }

        totalCount += 1;
        int writeTime = blocks.get(index).writeData(addr, newData);
        time.add(writeTime);
    }
}
