import java.util.*;
class Main {

    public static void main(String[] args) {

        //NODE TESTS
        Node<Character> characterNode = new Node<>('A');
        characterNode.set('B');
        characterNode.set('C');

        Node<String> stringNode = new Node<>("hello");
        stringNode.set(stringNode.get() + " world");

        //ARRAY OF NODES
        Node[] array = new Node[5];
        array[0] = characterNode;
        array[1] = stringNode;
        array[2] = new Node<Boolean>(true);
        array[3] = new Node<Integer>(100);
        array[4] = new Node<Double>(3.1415);

        System.out.println("\nA regular array can only store objects of one "
                           + "type...\nThis one can store anything:\n");
        for (Node node : array) {
            System.out.println(node);
        }
    }
}
