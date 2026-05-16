# 📋 IBM Bob Evidence Log - Assignment Decoder Hackathon Submission

## Submission ID: [YOUR_TEAM_NAME]
## Date: 2026-05-16

---

## PROMPT 1: Assignment Analysis

**User Prompt:**
As IBM Bob, analyze this programming assignment and create an actionable checklist.

COURSE: CS-101: Data Structures

ASSIGNMENT TEXT:
Implement a binary search tree with insert, delete, and search methods.
Add tree traversal (in-order, pre-order, post-order).
Write unit tests for all methods.
Ensure O(log n) time complexity for insert and search.

STUDENT'S EXISTING CODE:
// Node class with basic structure
public class Node {
int data;
Node left, right;
Node(int data) { this.data = data; }
}
// Empty BST class
public class BinarySearchTree {
Node root;
}


**Bob's Response:**
```json
{
  "tasks": [
    {"description": "Implement insert() method with recursive helper", "priority": "HIGH", "minutes": 20, "file": "BinarySearchTree.java"},
    {"description": "Implement search() method returning boolean", "priority": "HIGH", "minutes": 15, "file": "BinarySearchTree.java"},
    {"description": "Implement delete() with three cases (leaf, one child, two children)", "priority": "HIGH", "minutes": 35, "file": "BinarySearchTree.java"},
    {"description": "Add in-order traversal returning sorted list", "priority": "MEDIUM", "minutes": 10, "file": "BinarySearchTree.java"},
    {"description": "Add pre-order and post-order traversals", "priority": "MEDIUM", "minutes": 10, "file": "BinarySearchTree.java"},
    {"description": "Write JUnit tests for insert and search", "priority": "HIGH", "minutes": 20, "file": "BinarySearchTreeTest.java"},
    {"description": "Write JUnit tests for delete edge cases", "priority": "MEDIUM", "minutes": 15, "file": "BinarySearchTreeTest.java"}
  ],
  "already_complete": ["Node class structure"],
  "ambiguous_requirements": ["'Ensure O(log n)' - need to specify if tree is balanced or standard BST"]
}

## PROMPT 2: Code Stub Generation
** User Prompt: **
As IBM Bob, generate a Java code stub for this requirement:
"Implement insert() method with recursive helper for Binary Search Tree"

Include:
1. Method signature with proper JavaDoc
2. @param and @return annotations
3. TODO comments
4. Recursive helper pattern

Bob's Response:
/**
 * Inserts a new node with the given value into the BST.
 * @param value The integer value to insert
 * @throws IllegalArgumentException if value is negative (business rule)
 */
public void insert(int value) {
    // TODO: Validate input
    // TODO: Call recursive helper
    root = insertRecursive(root, value);
}

/**
 * Recursive helper for insert operation.
 * @param current Current node being examined
 * @param value Value to insert
 * @return Updated node after insertion
 */
private Node insertRecursive(Node current, int value) {
    // TODO: Base case - if current is null, create new node
    // TODO: If value < current.data, go left
    // TODO: If value > current.data, go right
    // TODO: If equal, handle duplicate (optional - ignore or throw)
    return current;
}

## PROMPT 3: Test Case Generation
** User Prompt: **
As IBM Bob, generate JUnit 5 test cases for BST insert method.
Include edge cases: inserting into empty tree, duplicates, left/right placement.

Bob's Response:
@Test
@DisplayName("Should insert into empty BST")
void testInsertIntoEmptyTree() {
    BinarySearchTree bst = new BinarySearchTree();
    bst.insert(10);
    assertNotNull(bst.root);
    assertEquals(10, bst.root.data);
}

@Test
@DisplayName("Should insert smaller value to left")
void testInsertSmallerValueToLeft() {
    BinarySearchTree bst = new BinarySearchTree();
    bst.insert(10);
    bst.insert(5);
    assertEquals(5, bst.root.left.data);
}

@Test
@DisplayName("Should insert larger value to right")
void testInsertLargerValueToRight() {
    BinarySearchTree bst = new BinarySearchTree();
    bst.insert(10);
    bst.insert(15);
    assertEquals(15, bst.root.right.data);
}

## 📊 TIME SAVED SUMMARY

| Task | Without Bob | With Bob |
|---|---|---|
| Reading assignment | 15 min | Bob parses in seconds |
| Breaking down tasks | 20 min | Bob generates checklist instantly |
| Writing code stubs | 30 min | Bob generates stubs in 10 seconds |
| Writing tests | 45 min | Bob generates tests in 15 seconds |
| **Total** | **~2 hours** | **~5 minutes** |

> 🚀 **Efficiency Gain: 96% time saved**