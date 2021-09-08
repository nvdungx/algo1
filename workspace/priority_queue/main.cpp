#include "bst.hpp"

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

    return 0;
}