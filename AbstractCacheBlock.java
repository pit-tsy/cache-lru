import java.util.ArrayList;

public abstract class AbstractCacheBlock implements CacheBlock {
    protected ArrayList<CacheLine> cacheLines;
    protected Mem mem;
    protected int index;

    AbstractCacheBlock(int index, Mem mem) {
        this.index = index;
        this.mem = mem;
        cacheLines = new ArrayList<>();
        for (int i = 0; i < Consts.CACHE_WAY; i++) {
            cacheLines.add(new CacheLine());
        }
    }

    @Override
    public int find(int tag) {
        for (int i = 0; i < Consts.CACHE_WAY; i++) {
            if (cacheLines.get(i).getTag() == tag) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public byte[] getCacheLineData(int cacheLineNumber, Counter time) {
        if (cacheLineNumber < 0 || cacheLineNumber >= Consts.CACHE_WAY) {
            throw new IllegalArgumentException("Invalid number of CACHE_LINE." +
                    " Excepted from 0 to " + cacheLineNumber);
        }
        used(cacheLineNumber);
        return cacheLines.get(0).getData();
    }

    @Override
    public byte[] getData(int tag, Counter time) {
        int index = find(tag);
        if (index == -1) {
            throw new IllegalArgumentException("Not found tag");
        }
        return getCacheLineData(index, time);
    }

    @Override
    public int writeData(Address addr, byte[] data) {
        int index = find(addr.getTag());
        if (index == -1) {
            return push(addr, data);
        } else {
            cacheLines.get(index).changeData(addr.getOffset(), data);
            used(index);
            return 0;
        }
    }

    abstract int push(Address addr, byte[] data);

    protected abstract void used(int index);
}
