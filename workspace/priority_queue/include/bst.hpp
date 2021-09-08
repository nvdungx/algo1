#ifndef __BST_H__
#define __BST_H__
#include <iostream>
#include <string>
#include <list>
#include <vector>
#include <memory>
#include <iterator>

template <typename KeyType = std::string, typename ValueType = int>
class Node
{
public:
    KeyType key;
    ValueType value;
    bool color;
    uint32_t count;
    std::shared_ptr<Node<KeyType, ValueType>> left;
    std::shared_ptr<Node<KeyType, ValueType>> right;

public:
    Node() : color(false), count(0), left(nullptr), right(nullptr) {}
    Node(const KeyType &_key, const ValueType &_val)
        : key(_key), value(_val), color(false), count(0),
          left(nullptr), right(nullptr) {}
    Node(const KeyType &_key, const ValueType &_val, bool _color, uint32_t _count,
         const std::shared_ptr<Node<KeyType, ValueType>> &_left, const std::shared_ptr<Node<KeyType, ValueType>> &_right)
        : key(_key), value(_val), color(_color), count(_count),
          left(_left), right(_right) {}
    ~Node() {}
};

template <typename KeyType = std::string, typename ValueType = int>
class BST
{
private:
    std::shared_ptr<Node<KeyType, ValueType>> root;

public:
    BST() {}
    ~BST() {}
    // put key-value pair into the table (remove key from table if value is null)
    void put(const KeyType &key, const ValueType &val)
    {
    }
    // value paired with key (null if key is absent)
    ValueType &get(const KeyType &key)
    {
    }
    // remove key (and its value) from table
    void remove(const KeyType &key)
    {
    }
    // is there a value paired with key?
    bool contains(const KeyType &key) { return (get(key) != nullptr); }
    // is the table empty?
    bool is_empty() { return (size() == 0); }
    // number of key-value pairs in the table
    uint32_t size() { return root.count; }
    // all the keys in the table
    // std::iterator keys()
    // smallest key
    KeyType min() {

    }
    // largest key
    KeyType max() {

    }
    // largest key less than or equal to key
    KeyType floor(const KeyType &key) {

    }
    // smallest key greater than or equal to key
    KeyType ceiling(const KeyType &key) {

    }
    // number of keys less than key
    int rank(const KeyType &key) {

    }
    // key of rank k
    KeyType select(int k) {

    }
    //  delete smallest key
    void removeMin() {

    }
    // delete largest key
    void removeMax() {

    }
    // number of keys in [lo..hi]
    int size(const KeyType &lo, const KeyType &hi) {

    }
    // keys in [lo..hi], in sorted order
    // Iterable<Key> keys(const KeyType &lo, const KeyType &hi)
    ValueType &operator[](const KeyType &key)
    {
    }
};

#endif // __BST_H__