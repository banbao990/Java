public enum Signal {
    RED, GREEN, YELLOW;
    public void next() {
        System.out.println(Signal.values()[this.ordinal() + 1]);
    }
    public static void main(String...args) {
        Signal.RED.next();
    }
}