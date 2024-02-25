public class Counter {
    private int count = 0;

    public void add(int x) {
        if (x < 0) return;
        count += x;
    }

    public int get() {
        return count;
    }
}
