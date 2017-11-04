package cz.scores;

import java.util.*;

import static java.util.Arrays.asList;

public class ScoreRecord {

    private static class Value {
        private final int value;
        private final int count;

        Value(int value, int count) {
            this.value = value;
            this.count = count;
        }

        Value increment() {
            return new Value(value, count + 1);
        }
    }

    private static class Node {
        private Value value;
        private Node left;
        private Node right;

        Node(Value value) {
            this.value = value;
        }

        void addChild(Node child) {
            boolean less = child.value.value < value.value;
            boolean more = child.value.value > value.value;
            if (less) {
                Optional<Node> leftChild = Optional.ofNullable(this.left);
                if (!leftChild.isPresent()) {
                    this.left = child;
                }
                leftChild.ifPresent(l -> l.addChild(child));
            } else if (more) {
                Optional<Node> rightChild = Optional.ofNullable(this.right);
                if (!rightChild.isPresent()) {
                    this.right = child;
                }
                rightChild.ifPresent(l -> l.addChild(child));
            } else {
                value = value.increment();
            }
        }

        boolean isNonLeaf() {
            return left != null || right != null;
        }

        @Override
        public String toString() {
            return value.value + ":" + value.count;
        }
    }

    private static String createBreadthFirstStringRepresentation(Node head) {
        List<String> nodeStringRepresentation = new ArrayList<>();
        Queue<Node> traversalQueue = new LinkedList<>(Collections.singleton(head));
        while (!traversalQueue.isEmpty()) {
            Optional<Node> node = Optional.ofNullable(traversalQueue.poll());
            nodeStringRepresentation.add(node.isPresent() ? node.get().toString() : "");
            node.filter(Node::isNonLeaf).ifPresent(n -> traversalQueue.addAll(asList(n.left, n.right)));
        }

        return nodeStringRepresentation.stream().reduce((l, r) -> l + ", " + r).orElse("");
    }

    static String createScoreCountString(int... scores) {
        if (scores.length == 0) {
            return "";
        }
        int headScore = scores[0];
        Node head = new Node(new Value(headScore, 0));
        for (int score : scores) {
            head.addChild(new Node(new Value(score, 1)));
        }
        return createBreadthFirstStringRepresentation(head);
    }
}
