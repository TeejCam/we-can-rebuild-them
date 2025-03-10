package org.example;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

public class AVLTree
{
    class Node
    {
        int bf;
        int data;
        int height;
        Node left, right;

        public Node(int data)
        {
            this.data = data;
            this.height = 0;
            this.bf = 0;
            this.left = this.right = null;
        }
    }
    
    Node root;
    private int nodeCount = 0;

    public int height()
    {
        if(root == null) return 0;
        return root.height;
    }

    public int size()
    {
        return nodeCount;
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public boolean contains(int value)
    {
        return contains(root, value);
    }

    public boolean contains(Node node, int value)
    {
        if(node == null) return false;

        int cmp = Integer.compare(value, node.data);

        if(cmp < 0) return contains(node.left, value);
        if(cmp > 0) return contains(node.right, value);

        return true;
    }

    public boolean insert(int value)
    {
        if(!contains(root, value)){
            root = insert(root, value);
            nodeCount++;
            return true;
        }
        return false;
    }

    private Node insert(Node node, int value)
    {
        if(node == null) return new Node(value);
        int cmp = Integer.compare(value, node.data);

        if(cmp < 0){
            node.left = insert(node.left, value);
        } else{
            node.right = insert(node.right, value);
        }

        update(node);
        return balance(node);
    }                        

    private void update(Node node)
    {
        int leftNodeHeight = (node.left == null) ? -1: node.left.height;
        int rightNodeHeight = (node.right == null) ? -1: node.right.height;

        node.height = 1 + Math.max(leftNodeHeight, rightNodeHeight);

        node.bf = rightNodeHeight - leftNodeHeight;
    }             

    private Node balance(Node node)
    {
        if(node.bf == -2){
            if(node.left.bf <= 0){
                return leftLeftCase(node);
            } else{
                return leftRightCase(node);
            }
        } else if(node.bf == +2){
            if(node.right.bf >= 0){
                return rightRightCase(node);
            } else{
                return rightLeftCase(node);
            }
        }
        return node;
    }

    private Node leftLeftCase(Node node)
    {
        return rightRotation(node);
    }

    private Node leftRightCase(Node node)
    {
        node.left = leftRotation(node.left);
        return leftLeftCase(node);
    }

    private Node rightRightCase(Node node)
    {
        return leftRotation(node);
    }

    private Node rightLeftCase(Node node)
    {
        node.right = rightRotation(node.right);
        return rightRightCase(node);
    }

    private Node leftRotation(Node node)
    {
        Node newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;
        update(node);
        update(newParent);
        return newParent;
    }

    private Node rightRotation(Node node)
    {
        Node newParent = node.left;
        node.left = newParent.right;
        newParent.right = node;
        update(node);
        update(newParent);
        return newParent;
    }

    public boolean delete(int elem)
    {
        if(contains(root, elem)){
            root = delete(root, elem);
            nodeCount--;
            return true;
        }
        return false;
    }

    private Node delete(Node node, int elem)
    {
        if(node == null) return null;

        int cmp = Integer.compare(elem, node.data);

        if(cmp < 0){
            node.left = delete(node.left, elem);
        } else if(cmp > 0){
            node.right = delete(node.right, elem);
        } else{
            if(node.left == null){
                return node.right;
            } else if(node.right == null){
                return node.left;
            } else{
                if(node.left.height > node.right.height){
                    int successorValue = findMax(node.left);
                    node.data = successorValue;
                    node.left = delete(node.left, successorValue);
                } else{
                    int successorValue = findMin(node.right);
                    node.data = successorValue;
                    node.right = delete(node.right, successorValue);
                }
            }
        }
        update(node);
        return balance(node);
    }

    private int findMin(Node node)
    {
        while(node.left != null){
            node = node.left;
        }
        return node.data;
    }

    private int findMax(Node node)
    {
        while(node.right != null){
            node = node.right;
        }
        return node.data;
    }

    public String serialize(Node root)
    {
        if(root == null) return "X,";

        String leftSerialized = serialize(root.left);
        String rightSerialized = serialize(root.right);

        return root.data + "," + leftSerialized + rightSerialized;
    }

    public Node deserialize(String s)
    {
        Queue<String> nodesLeft = new LinkedList<>();
        nodesLeft.addAll(Arrays.asList(s.split(",")));
        return deserializeHelper(nodesLeft);
    }

    public Node deserializeHelper(Queue<String> nodesLeft)
    {
        String valueForNode = nodesLeft.poll();
        if(valueForNode.equals("X")) return null;

        Node newNode = new Node(Integer.valueOf(valueForNode));

        newNode.left = deserializeHelper(nodesLeft);
        newNode.right = deserializeHelper(nodesLeft);

        return newNode;
    }

    public void printTree(Node root)
    {
        if(root != null){
            printTree(root.left);
            System.out.print(root.data + " ");
            printTree(root.right);
        }
    }
}