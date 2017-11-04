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
            int childValue = child.value.value;
            int myValue = this.value.value;
            if (childValue == myValue) {
                this.value = this.value.increment();
                return;
            }
            boolean isChildValueLess = childValue < myValue;
            Optional<Node> childOptional = Optional.ofNullable(isChildValueLess ? this.left : this.right);
            if (childOptional.isPresent()) {
                childOptional.get().addChild(child);
                return;
            }
            if (isChildValueLess) {
                this.left = child;
            } else {
                this.right = child;
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
