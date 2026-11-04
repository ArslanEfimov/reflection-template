package org.example.generator;

import org.example.classes.BinaryTreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RecursionGenerationTest {

    private final Generator generator = new Generator();

    @Test
    void generatesThreeLevelTreeTest() throws ReflectiveOperationException {
        BinaryTreeNode root = generator.generateValueOfType(BinaryTreeNode.class);

        assertTree(root, 3);
    }

    private void assertTree(BinaryTreeNode node, int remainingLevels) {
        if (remainingLevels == 0) {
            assertNull(node);
            return;
        }

        assertNotNull(node);

        assertTree(node.getLeft(), remainingLevels - 1);
        assertTree(node.getRight(), remainingLevels - 1);
    }
}
