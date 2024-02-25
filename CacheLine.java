public class CacheLine {
    private final int tag;
    private byte[] data;
    private boolean dirtyFlag;

    public CacheLine(int tag, byte[] data) {
        this.tag = tag;
        dirtyFlag = false;
        init(data);
    }

    public CacheLine() {
        this(-1, new byte[Consts.CACHE_LINE_SIZE]);
    }

    private void init(byte[] data) {
        if (data.length != Consts.CACHE_LINE_SIZE) {
            throw new IllegalArgumentException("Invalid size for CacheLine");
        }
        this.data = data;
    }

    public byte[] getData() {
        return data.clone();
    }

    public int getTag() {
        return tag;
    }

    public void changeData(int offset, byte[] data) {
        for (int i = 0; i < data.length; i++) {
            this.data[offset + i] = data[i];
        }
        dirtyFlag = true;
    }

    public boolean isDirty() {
        return dirtyFlag;
    }
}
