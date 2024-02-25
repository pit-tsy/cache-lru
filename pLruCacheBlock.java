import java.util.Arrays;

public class pLruCacheBlock extends AbstractCacheBlock {
    private boolean[] flags;

    pLruCacheBlock(int index, Mem mem) {
        super(index, mem);
        flags = new boolean[Consts.CACHE_WAY];
    }

    @Override
    int push(Address addr, byte[] data) {
        int time = 0;
        for (int i = 0; i < Consts.CACHE_WAY; i++) {
            if (flags[i] == false) {
                if (cacheLines.get(i).isDirty()) {
                    Address address = new Address(cacheLines.get(i).getTag(), index);
                    time += mem.writeData(address, cacheLines.get(i).getData());
                }
                cacheLines.set(i, new CacheLine(addr.getTag(), data));
                used(i);
                break;
            }
        }
        return time;
    }

    @Override
    protected void used(int index) {
        flags[index] = true;
        boolean allOne = true;
        for (int i = 0; i < Consts.CACHE_WAY; i++) {
            if (flags[i] == false) {
                allOne = false;
                break;
            }
        }
        if (allOne) {
            for (int i = 0; i < Consts.CACHE_WAY; i++) {
                if (i != index) {
                    flags[i] = false;
                }
            }
        }
    }
}
