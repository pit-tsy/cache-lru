public class Address {
    private final int addr;

    public Address(int addr) {
        if (addr < 0 || addr >= (1 << (Consts.ADDR_LEN))) {
            throw new IllegalArgumentException("Invalid addr len");
        }
        this.addr = addr;
    }

    public Address(int tag, int index) {
        this(((tag << Consts.CACHE_IDX_LEN) + index) << Consts.CACHE_OFFSET_LEN);
    }

    public int getAddr() {
        return addr;
    }

    public int getOffset() {
        return addr & ((1 << Consts.CACHE_OFFSET_LEN) - 1);
    }

    public int getIndex() {
        return (addr >> Consts.CACHE_OFFSET_LEN)
                & ((1 << Consts.CACHE_IDX_LEN) - 1);
    }

    public int getTag() {
        return addr >>> (Consts.CACHE_IDX_LEN + Consts.CACHE_OFFSET_LEN);
    }

    public Address mainAddress() {
        Address main = new Address((addr >> Consts.CACHE_OFFSET_LEN) << Consts.CACHE_OFFSET_LEN);
        return main;
    }

    public Address add(int shift) {
        return new Address(addr + shift);
    }
}
