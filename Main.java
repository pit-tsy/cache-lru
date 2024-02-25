import java.util.Locale;

public class Main {
    private static final int M = 64;
    private static final int N = 60;
    private static final int K = 32;

    private static void mmul(Cpu system, Mem mem) {
        Address A8 = mem.initData(new byte[M  * K * 1]); // 8-bit = 1-byte cells
        Address B16 = mem.initData(new byte[K * N * 2]); // 16-bit = 2-byte cells
        Address C32 = mem.initData(new byte[M * N * 4]); // 32-bit = 4-byte cells

        system.addInit();
        Address pa = A8;

        system.addInit();
        Address pc = C32;

        system.addFor(M);
        for (int y = 0; y < M; y++) {
            system.addFor(N);
            for (int x = 0; x < N; x++) {
                system.addInit();
                Address pb = B16;
                system.addInit();
                int s = 0;

                system.addFor(K);
                for (int k = 0; k < K; k++) {
                    system.addSum();
                    s += system.multiply(pa.add(k * 1), 1, pb.add(x * 2), 2);

                    system.addSum();
                    pb = pb.add(N * 2);
                }
                system.addInit();
                system.writeData(pc.add(4 * x), new byte[4]);
            }


            system.addSum();
            pa = pa.add(K * 1);

            system.addSum();
            pc = pc.add(N * 4);
        }

        system.exit();
    }

    public static void main(String[] args) {
        Mem mem1 = new Mem();
        Cpu system1 = new Cpu(new LruCache(mem1, false));
        mmul(system1, mem1);

        Mem mem2 = new Mem();
        Cpu system2 = new Cpu(new LruCache(mem2, true));
        mmul(system2, mem2);

        System.out.printf(Locale.ROOT, "LRU:\thit perc. %3.4f%%\ttime: %d\npLRU:\thit perc. %3.4f%%\ttime: %d\n",
                system1.getCacheLose(), system1.getTime(), system2.getCacheLose(), system2.getTime());
    }

}
