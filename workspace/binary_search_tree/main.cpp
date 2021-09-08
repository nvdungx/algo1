#include "bst.hpp"
#include <map>

template <typename A, typename B>
std::ostream &operator<<(std::ostream &os, const std::pair<A, B> &p)
{
    return os << '(' << p.first << ", " << p.second << ')';
}
template <typename A>
std::ostream &operator<<(std::ostream &os, const std::list<A> &v)
{
    auto it = v.begin();
    os << '[' << *it++;
    while (it != v.end())
        os << ", " << *it++;
    os << ']';
    return os;
}
template <typename A>
std::ostream &operator<<(std::ostream &os, const std::vector<A> &v)
{
    auto it = v.begin();
    os << '[' << *it++;
    while (it != v.end())
        os << ", " << *it++;
    os << ']';
    return os;
}

int main(void)
{
    BST<int, std::string> test;
    test.put(10, "key1");
    test.put(2, "key2");
    test.put(30, "key3");
    test.put(4, "key4");
    test.put(6, "key5");
    test.put(15, "key6");
    std::cout << test.rank(30) << std::endl;
    return 0;
}