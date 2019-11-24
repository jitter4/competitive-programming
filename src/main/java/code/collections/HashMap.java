package code.collections;

import java.util.TreeMap;

public class HashMap<K, V> {

    int size;
    int bucketCount = 16;
    float bucketCountThreshold = 0.75f;
    int treefyThresholdForBucketItems = 8;
    Node<K, V>[] arr = new Node[bucketCount];

    HashMap() {}
    HashMap(float bucketCountThreshold) {
        this.bucketCountThreshold = bucketCountThreshold;
    }
    HashMap(int initialBucketCount, float bucketCountThreshold) {
        this(bucketCountThreshold);
        this.bucketCount = initialBucketCount;
    }
    HashMap(int initialBucketCount, float bucketCountThreshold, int treefyThresholdForBucketItems) {
        this(initialBucketCount, bucketCountThreshold);
        this.treefyThresholdForBucketItems = treefyThresholdForBucketItems;
    }
    HashMap(int treefyThresholdForBucketItems) {
        this.treefyThresholdForBucketItems = treefyThresholdForBucketItems;
    }

    void put(K k, V v) {
        if (size >= 0.75 * bucketCount) {
            increaseBuckets();
        }
        int hashCode = k.hashCode();
        int bucketNo = hashCode % bucketCount;
        Node<K, V> bucketItem = arr[bucketNo];
        if (bucketItem == null) {
            arr[bucketNo] = new MapNode<>(k, v, hashCode);
            size++;
        } else {
            if (bucketItem instanceof RBTreeMap) {
                ((RBTreeMap) bucketItem).put(k, v, hashCode);
                size++;
            } else {
                int bucketItemCount = 0;
                MapNode bucketListItem = (MapNode) bucketItem;
                while (bucketListItem != null) {
                    if (++bucketItemCount == treefyThresholdForBucketItems) {
                        arr[bucketNo] = treefyAndInsert(bucketNo, k, v, hashCode);
                        size++;
                        break;
                    }
                    if (bucketListItem.k.equals(k)) {
                        bucketListItem.v = v;
                        break;
                    }
                    if (bucketListItem.next == null) {
                        bucketListItem.next = new MapNode<>(k, v, hashCode);
                        size++;
                        break;
                    }
                    bucketListItem = bucketListItem.next;
                }
            }
        }
    }

    Node<K, V> treefyAndInsert(int bucketNo, K k, V v, int hashCode) {
        RBTreeMap<K, V> tree = treefy(bucketNo, hashCode);
        tree.put(k, v, hashCode);
        return tree;
    }

    RBTreeMap<K, V> treefy(int bucketNo, int hashCode) {
        Node<K, V> bucketItem = arr[bucketNo];
        RBTreeMap<K, V> rbTreeMap = new RBTreeMap<>();
        MapNode<K, V> bucketListItem = (MapNode) bucketItem;
        while (bucketListItem != null) {
            rbTreeMap.put(bucketListItem.k, bucketListItem.v, hashCode);
            bucketListItem = bucketListItem.next;
        }
        return rbTreeMap;
    }

    /** ToDo */
    void increaseBuckets() {
        Node[] arrCopy = new Node[bucketCount * 2];
        for (int currentBucketLocation = 0; currentBucketLocation < bucketCount; currentBucketLocation++) {
            Node<K, V> currentBucket = arr[currentBucketLocation];
            if (currentBucket instanceof RBTreeMap) {
                /** ToDo */
//                performSplitOfTree((RBTreeMap<K, V>) currentBucket, currentBucketLocation, arrCopy);
            } else {
                MapNode<K, V> currentNode = (MapNode<K, V>) currentBucket;
                if (currentNode.next == null) {
                    arrCopy[currentNode.hash & bucketCount - 1] = currentNode;
                } else {
                    MapNode<K, V> currentBucketLocation__LocationBucketList__Tail = null;
                    MapNode<K, V> currentBucketLocation__LocationBucketList__Head = null;
                    MapNode<K, V> currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Tail = null;
                    MapNode<K, V> currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head = null;
                    while (currentNode != null) {
                        if ((currentNode.hash & bucketCount) == 0) {
                            if (currentBucketLocation__LocationBucketList__Head == null) {
                                currentBucketLocation__LocationBucketList__Head = currentBucketLocation__LocationBucketList__Tail = currentNode;
                                arrCopy[currentBucketLocation] = currentBucketLocation__LocationBucketList__Head;
                            } else {
                                currentBucketLocation__LocationBucketList__Tail.next = currentNode;
                            }
                        } else {
                            if (currentBucketLocation__LocationBucketList__Head == null) {
                                currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head = currentBucketLocation__LocationBucketList__Tail = currentNode;
                                arrCopy[currentBucketLocation + bucketCount] = currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head;
                            } else {
                                currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Tail.next = currentNode;
                            }
                        }
                        currentNode = currentNode.next;
                    }
                }
            }
        }
        arr = arrCopy;
        bucketCount *= 2;
    }

    /** ToDo */
//    void performSplitOfTree(RBTreeMap<K,V> currentBucketTree, int currentBucketLocation, MapNode<K, V>[] arrCopy) {
//        MapNode<K, V> currentBucketLocation__LocationBucketList__Tail = null;
//        MapNode<K, V> currentBucketLocation__LocationBucketList__Head = null;
//        MapNode<K, V> currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Tail = null;
//        MapNode<K, V> currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head = null;
//        currentBucketTree.get().entrySet().forEach(e -> e.hash());
//        while (iterator.hasNext()) {
//            if ((currentNode.hash & bucketCount) == 0) {
//                if (currentBucketLocation__LocationBucketList__Head == null) {
//                    currentBucketLocation__LocationBucketList__Head = currentBucketLocation__LocationBucketList__Tail = currentNode;
//                    arrCopy[currentBucketLocation] = currentBucketLocation__LocationBucketList__Head;
//                } else {
//                    currentBucketLocation__LocationBucketList__Tail.next = currentNode;
//                }
//            } else {
//                if (currentBucketLocation__LocationBucketList__Head == null) {
//                    currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head = currentBucketLocation__LocationBucketList__Tail = currentNode;
//                    arrCopy[currentBucketLocation + bucketCount] = currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Head;
//                } else {
//                    currentBucketLocation_plus_CurrentBucketCount__LocationBucketList__Tail.next = currentNode;
//                }
//            }
//            currentNode = currentNode.next;
//        }
//
//    }


    V get(K k) {
        int hashCode = k.hashCode();
        int bucketNo = hashCode % bucketCount;
        Node<K, V> bucketItem = arr[bucketNo];
        if (bucketItem instanceof RBTreeMap) {
            return (V) ((RBTreeMap) bucketItem).get(k);
        } else {
            MapNode<K, V> buckListItem = (MapNode<K, V>) bucketItem;
            if (buckListItem != null) {
                while (buckListItem != null) {
                    if (buckListItem.k.equals(k))
                        return buckListItem.v;
                    buckListItem = buckListItem.next;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> m = new HashMap<>(1, 1, 0);
        m.put(1, 1);
        m.put(2, 1);
        m.put(3, 1);
        m.put(4, 1);
        m.put(5, 2);
        System.out.println(m.get(5));
    }

}


interface Node<K, V> {}

class MapNode<K, V> implements Node<K, V> {
    K k;
    V v;
    int hash;
    MapNode<K, V> next;
    MapNode(K k, V v, int hash) {
        this.k = k;
        this.v = v;
        this.hash = hash;
    }

    public K getKey() {
        return this.k;
    }


    public V getValue() {
        return this.v;
    }


    public V setValue(V value) {
        V oldValue = this.v;
        this.v = value;
        return oldValue;
    }
}

/** ToDo */
class RBTreeMap<K, V> implements Node<K, V> {

    TreeMapNode<K, V> root;

    /**
     * Replace java.util.TreeMap with implementation of RBTree
     */
    java.util.TreeMap<K, V> tree = new java.util.TreeMap<>();

    /**
     * ToDo
     *
     * @param k
     * @param v
     */
    void put(K k, V v, int hashCode) {
        TreeMapNode<K, V> n = new TreeMapNode<>(k, v, hashCode);

        tree.put(k, v);

    }

    V get(K k) {
        return tree.get(k);
    }

    int size() {
        return tree.size();
    }


    class TreeMapNode<K, V> extends MapNode<K, V> implements java.util.Map.Entry<K, V> {
        TreeMapNode<K, V> left;
        TreeMapNode<K, V> right;
        TreeMapNode(K k, V v, int hash) {
            super(k, v, hash);
        }
    }

    TreeMap get() {
        return tree;
    }

}
