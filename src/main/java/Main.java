import structures.Node;

public class Main {
    public static void main(String[] args) {
        new Node("127.0.0.1:55555", "55556", true);
        new Node("127.0.0.1:55556", "55557", false);
        new Node("127.0.0.1:55557", "55558", false);
        new Node("127.0.0.1:55558", "55555", false);
    }
}
