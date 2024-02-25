import java.util.ArrayList;

public class LruCacheBlock extends AbstractCacheBlock {
    LruCacheBlock(int index, Mem mem) {
        super(index, mem);
    }

    @Override
    int push(Address addr, byte[] data) {
        int time = 0;
        if (cacheLines.get(Consts.CACHE_WAY - 1).isDirty()) {
            Address address = new Address(cacheLines.get(Consts.CACHE_WAY - 1).getTag(), index);
            time += mem.writeData(address, cacheLines.get(Consts.CACHE_WAY - 1).getData());
        }

        cacheLines.set(Consts.CACHE_WAY - 1, new CacheLine(addr.getTag(), data));
        used(Consts.CACHE_WAY - 1);
        return time;
    }

    @Override
    protected void used(int index) {
        for (int i = index; i > 0; i--) {
            CacheLine temp = cacheLines.get(i);
            cacheLines.set(i, cacheLines.get(i - 1));
            cacheLines.set(i - 1, temp);
        }
    }
}
