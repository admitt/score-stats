package cz.scores;

import java.util.*;

import static java.util.Arrays.asList;

final class ScoreStats {

    private ScoreStats() {
    }

    static String createScoreCountString(int... scores) {
        if (scores.length == 0) {
            return "";
        }
        int headScore = scores[0];
        Node head = new Node(new Payload(headScore, 0));
        for (int score : scores) {
            head.addChild(new Node(new Payload(score, 1)));
        }
        return createBreadthFirstStringRepresentation(head);
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

    private static class Node {
        private Payload payload;
        private Node left;
        private Node right;

        Node(Payload payload) {
            this.payload = payload;
        }

        void addChild(Node child) {
            int childValue = child.payload.value;
            int myValue = this.payload.value;
            if (childValue == myValue) {
                this.payload = this.payload.increment();
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
            return payload.value + ":" + payload.count;
        }
    }

    private static class Payload {
        private final int value;
        private final int count;

        Payload(int value, int count) {
            this.value = value;
            this.count = count;
        }

        Payload increment() {
            return new Payload(value, count + 1);
        }
    }
}
