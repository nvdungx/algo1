#ifndef __BST_H__
#define __BST_H__
#include <iostream>
#include <string>
#include <list>
#include <vector>
#include <memory>
#include <iterator>

#define RED (true)
#define BLACK (false)

template <typename KeyType = std::string, typename ValueType = int>
class Node
{
public:
    KeyType key;
    ValueType value;
    bool color; // color of parent link
    uint32_t count;
    typedef std::shared_ptr<Node<KeyType, ValueType>> Node_ptr;
    Node_ptr left;
    Node_ptr right;

public:
    Node() : color(BLACK), count(0), left(nullptr), right(nullptr) {}
    Node(const KeyType &_key, const ValueType &_val, uint32_t _count, bool _color)
        : key(_key), value(_val), color(_color), count(_count),
          left(nullptr), right(nullptr) {}
    Node(const KeyType &_key, const ValueType &_val, bool _color, uint32_t _count,
         const Node_ptr &_left, const Node_ptr &_right)
        : key(_key), value(_val), color(_color), count(_count),
          left(_left), right(_right) {}
    ~Node() {}
    Node(const Node &cp)
        : key(cp.key), value(cp.value), color(cp.color), count(cp.count),
          left(cp.left), right(cp.right) {}
    Node(const Node &&mv)
        : key(std::move(mv.key)), value(std::move(mv.value)), color(std::move(mv.color)),
          left(std::move(mv.left)), right(std::move(mv.right)) {}
    Node &operator=(const Node &cp)
    {
        if (this != &cp)
        {
            key = cp.key;
            value = cp.value;
            count = cp.count;
            left = cp.left;
            right = cp.right;
        }
        return *this;
    }
    Node &operator=(const Node &&mv)
    {
        key = std::move(mv.key);
        value = std::move(mv.value);
        color = std::move(mv.color);
        left = std::move(mv.left);
        right = std::move(mv.right);
        return *this;
    }
};

template <typename KeyType = std::string, typename ValueType = int>
class BST
{
private:
    typedef std::shared_ptr<Node<KeyType, ValueType>> Node_ptr;
    typedef Node<KeyType, ValueType> _Node;
    Node_ptr root;

public:
    BST() {}
    ~BST() {}
    // put key-value pair into the table (remove key from table if value is null)
    void put(const KeyType &key, const ValueType &val)
    {
        root = _put(root, key, val);
        root->color = BLACK;
    }
    // value paired with key (null if key is absent)
    ValueType *get(const KeyType &key)
    {
        Node_ptr x = root;
        while (x != nullptr)
        {
            if (key < x->key)
                x = x->left;
            else if (key > x->key)
                x = x->right;
            else
                return &x->value;
        }
        return nullptr;
    }
    // remove key (and its value) from table
    void remove(const KeyType &key)
    {
        root = _remove(root, key);
    }
    // is there a value paired with key?
    bool contains(const KeyType &key) { return (get(key) != nullptr); }
    // is the table empty?
    bool isEmpty() { return (size() == 0); }
    // number of key-value pairs in the table
    uint32_t size() { return _node_size(root); }
    // all the keys in the table
    // std::iterator keys()
    // smallest key
    KeyType *min()
    {
        return &_min(root)->key;
    }
    // largest key
    KeyType *max()
    {
        return &_max(root)->key;
    }
    // largest key less than or equal to key
    KeyType *floor(const KeyType &key)
    {
        Node_ptr x = _floor(root, key);
        if (x == nullptr)
            return nullptr;
        return &x->key;
    }
    // smallest key greater than or equal to key
    KeyType *ceiling(const KeyType &key)
    {
        Node_ptr x = _ceiling(root, key);
        if (x == nullptr)
            return nullptr;
        return &x->key;
    }
    // number of keys less than key
    int rank(const KeyType &key)
    {
        return _rank(root, key);
    }
    // key of rank k
    KeyType *select(int k)
    {
        Node_ptr x = _select(root, k);
        if (x == nullptr) return nullptr;
        else return &x->key;
    }
    //  delete smallest key
    void removeMin()
    {
        root = _removeMin(root);
    }
    // delete largest key
    void removeMax()
    {
        root = _removeMax(root);
    }
    // number of keys in [lo..hi]
    int size(const KeyType &lo, const KeyType &hi)
    {
        if (contains(hi))
            return rank(hi) - rank(lo) + 1;
        else
            return rank(hi) - rank(lo);
    }
    // keys in [lo..hi], in sorted order
    // iterator keys(const KeyType &lo, const KeyType &hi)
    ValueType *operator[](const KeyType &key)
    {
    }

private:
    Node_ptr _put(Node_ptr &x, const KeyType &key, const ValueType &val)
    {
        // if node x not exist then return new one
        if (x == nullptr)
            return std::make_shared<_Node>(key, val, 1, RED);
        // if input key smaller than current key then put key to left link
        if (key < x->key)
            x->left = _put(x->left, key, val);
        // if input key larger than current key then put key to left right
        else if (key > x->key)
            x->right = _put(x->right, key, val);
        // if val == current x.val then update and return current x
        else
            x->value = val;
        if (_isRed(x->right) && (!_isRed(x->left)))
            x = _rotateLeft(x); // lean left
        if (_isRed(x->left) && (_isRed(x->left->left)))
            x = _rotateRight(x); // balance 4-node
        if (_isRed(x->left) && (_isRed(x->right)))
            _flipColor(x); // split 4-node
        x->count = 1 + _node_size(x->left) + _node_size(x->right);
        return x;
    }
    Node_ptr _floor(Node_ptr &x, const KeyType &key)
    {
        // if can not found node with key return null for no floor value
        // null could be smallest left node on the left and k still < root
        // null could be right node then largest item < key is previous root node
        if (x == nullptr)
            return nullptr;
        // return node with key
        if (key == x->key)
            return x;
        // if key smaller than current node key then move to left subnode
        if (key < x->key)
            return _floor(x->left, key);

        // key larger than current node, search the right subnode
        Node_ptr r = _floor(x->right, key);
        // case of not nullptr > found node with match value for key > return node
        if (r != nullptr)
            return r;
        // case nullptr > return current x node nearest
        else
            return x;
    }
    Node_ptr _ceiling(Node_ptr &x, const KeyType &key)
    {
        if (x == nullptr)
            return nullptr;
        if (key == x->key)
            return x;
        // if key larger than current node key then move to right subnode
        if (key > x->key)
            return _ceiling(x->right, key);

        // key smaller than current node, search the left subnode
        Node_ptr l = _ceiling(x->left, key);
        // case of not nullptr > found the node with value
        if (l != nullptr)
            return l;
        // nullptr then return the current x node(smallest root > key)
        else
            return x;
    }
    int _node_size(Node_ptr &x)
    {
        if (x == nullptr)
            return 0;
        else
            return x->count;
    }
    int _rank(Node_ptr &x, const KeyType &key)
    {
        // null node on the left rank 0
        if (x == nullptr)
            return 0;
        // key smaller than current node key > find next left node
        if (key < x->key)
            return _rank(x->left, key);
        // key larger than current node key > rank = 1(current node) + size of left branch + smaller node on right branch
        else if (key > x->key)
            return 1 + _node_size(x->left) + _rank(x->right, key);
        // key == current node key > number of key less than key is size of left
        else
            return _node_size(x->left);
    }
    Node_ptr _min(Node_ptr &x)
    {
        if (x->left == nullptr)
            return x;
        else
            return _min(x->left);
    }
    Node_ptr _max(Node_ptr &x)
    {
        if (x->right == nullptr)
            return x;
        else
            return _max(x->right);
    }
    Node_ptr _select(Node_ptr &x, int k)
    {
        if (x == nullptr)
            return nullptr;
        int t = _node_size(x->left);
        // if node size of left branch > than current rank k then move left
        if (t > k)
            return _select(x->left, k);
        // if node size of left branch smaller than finding rank k
        // move right and find k - t -1 rank element on right branch
        else if (t < k)
            return _select(x->right, k - t - 1);
        else
            return x;
    }
    // TODO: update for red-black BST
    Node_ptr _removeMin(Node_ptr &x)
    {
        // last left node then return right node of this
        if (x->left == nullptr)
            return x->right;
        // move left
        x->left = _removeMin(x->left);
        // update size
        x->count = 1 + _node_size(x->left) + _node_size(x->right);
        return x;
    }
    // TODO: update for red-black BST
    Node_ptr _removeMax(Node_ptr &x)
    {
        if (x->right == nullptr)
            return x->left;
        x->right = _removeMax(x->right);
        x->count = 1 + _node_size(x->left) + _node_size(x->right);
        return x;
    }
    // TODO: update for red-black BST
    Node_ptr _remove(Node_ptr &x, const KeyType &key)
    {
        if (x == nullptr)
            return nullptr;
        // search key
        if (key < x->key)
            x->left = _remove(x->left, key);
        if (key > x->key)
            x->right = _remove(x->right, key);
        // found key
        else
        {
            // no right node, take left node
            if (x->right == nullptr)
                return x->left;
            // no left node, take right node
            if (x->left == nullptr)
                return x->right;
            // 2 subnode case, find min node in right side
            // delete min node right side and swap min node right side with current node
            Node_ptr t = x;
            // replace x = min of right
            x = _min(t->right);
            // remove min of right
            x->right = _removeMin(t->right);
            x->left = t->left;
        }
        x->count = 1 + _node_size(x->left) + _node_size(x->right);
        return x;
    }
    bool _isRed(Node_ptr &x)
    {
        if (x == nullptr)
            return false;
        return (x->color == RED);
    }
    Node_ptr _rotateLeft(Node_ptr &h)
    {
        Node_ptr x = h->right;
        h->right = x->left;
        x->left = h;
        x->color = h->color;
        h->color = RED;
        x->count = h->count;
        h->count = 1 + _node_size(h->left) + _node_size(h->right);
        return x;
    }
    Node_ptr _rotateRight(Node_ptr &h)
    {
        Node_ptr x = h->left;
        h->left = x->right;
        x->right = h;
        x->color = h->color;
        h->color = RED;
        x->count = h->count;
        h->count = 1 + _node_size(h->left) + _node_size(h->right);
        return x;
    }
    void _flipColor(Node_ptr &h)
    {
        h->color = RED;
        h->left->color = BLACK;
        h->right->color = BLACK;
    }
};

#endif // __BST_H__