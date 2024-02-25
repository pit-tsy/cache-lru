public class Mem {
    private byte[] data = new byte[Consts.MEM_SIZE];
    private int ptr = 0x40000;

    public Address initData(byte[] data) {
        if (ptr + data.length > Consts.MEM_SIZE) {
            throw new IllegalStateException("Overflow");
        }
        Address address = new Address(ptr);
        for (int i = 0; i < data.length; i++) {
            this.data[ptr++] = data[i];
        }
        return address;
    }

    public int writeData(Address addr, byte[] data) {
        int writePtr = addr.getAddr();
        if (addr.getAddr() + data.length > Consts.MEM_SIZE) {
            throw new IllegalStateException("Overflow");
        }
        for (int i = 0; i < data.length; i++) {
            this.data[writePtr++] = data[i];
        }
        return writeTime();
    }

    public byte[] getData(Address address, int len) {
        int addr = address.getAddr();
        if (addr + len > Consts.MEM_SIZE) {
            throw new IndexOutOfBoundsException("got out for memory. MEM_SIZE = " + Consts.MEM_SIZE);
        }

        byte[] answer = new byte[len];
        for (int i = 0; i < len; i++) {
            answer[i] = data[addr++];
        }
        return answer;
    }

    public byte[] getData(Address address, int len, Counter time) {
        time.add(getTime(len));
        return getData(address, len);
    }

    private int getTime(int len) {
        return Consts.MEM_ANSWER_TIME
                + Consts.CTR_TIME + (len * 8) / Consts.DATA2_BUS_LEN;
    }

    private int writeTime() {
        return Consts.MEM_ANSWER_TIME
                + 2 * Consts.CTR_TIME;
    }

    public void clear() {
        ptr = 0x40000;
    }
}
