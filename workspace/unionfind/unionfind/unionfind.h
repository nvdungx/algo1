// unionfind.h : Include file for standard system include files,
// or project specific include files.

#pragma once
#include <vector>
#include <iostream>

// TODO: Reference additional headers your program requires here.

class QuickFindUF
{
public:
	QuickFindUF(int N)
	{
		count = N;
		id.reserve(N);
		for (size_t i = 0; i < N; i++)
		{
			id.push_back(i);
		}
	}
	~QuickFindUF()
	{
	}
	bool connected(int p, int q)
	{
		(id[p] == id[q]) ? std::cout << p << "," << q << " connected" << std::endl : std::cout << p << "," << q << " not connected" << std::endl;
		return id[p] == id[q];
	}
	// this take n^2 to perform n union operation
	void munion(int p, int q)
	{
		// change all element with id = p to q;
		int pid = id[p];
		int qid = id[q];
		if (pid == qid) return;
		for (size_t i = 0; i < id.size(); i++)
		{
			if (id[i] == pid) id[i] = qid;
		}
		count--;
		std::cout << "QF "<< count <<"- connect: " << p << "," << q << std::endl;
	}

private:
	std::vector<int> id;
	int count;
};


class QuickUnion
{
public:
	QuickUnion(int N)
	{
		id.reserve(N);
		count = N;
		for (size_t i = 0; i < N; i++)
		{
			id.push_back(i);
		}
	}

	~QuickUnion()
	{
	}
	bool connected(int p, int q)
	{
		(find(p) == find(q)) ? std::cout << p << "," << q << " connected" << std::endl : std::cout << p << "," << q << " not connected" << std::endl;
		return find(p) == find(q);
	}
	void munion(int p, int q)
	{
		if (find(p) == find(q)) return;
		id[find(p)] = find(q);
		count--;
		std::cout << "QU "<< count <<"- connect: " << p << "," << q << " - " << "root p:"<< find(p) <<" to root q:" << find(q) << std::endl;
	}
private:
	int find(int element)
	{
		while (element != id[element])
		{
			element = id[element];
		}
		return element;
	}
	std::vector<int> id;
	int count;
};


class WeightedQU
{
public:
	WeightedQU(int N)
	{
		count = N;
		size.resize(N, 1);
		id.reserve(N);
		for (size_t i = 0; i < N; i++)
		{
			id.push_back(i);
		}
	}

	~WeightedQU()
	{
	}

	void muinion(int p, int q)
	{
		int i = find(p);
		int j = find(q);
		if (i == j) return;
		if (size[i] > size[j])
		{
			// root j connect to i
			size[i] += size[j];
			id[j] = i;
		}
		else
		{
			// root i connect to j
			size[j] += size[i];
			id[i] = j;
		}
	}

	int root_size(int element)
	{
		return size[find(element)];
	}

	bool connected(int p, int q)
	{
		(find(p) == find(q)) ? std::cout << p << "," << q << " connected" << std::endl : std::cout << p << "," << q << " not connected" << std::endl;
		return find(p) == find(q);
	}

private:
	int find(int element)
	{
		while (element != id[element])
		{
			// make all node in path point to its grandparent
			id[element] = id[id[element]];
			element = id[element];
		}
		return element;
	}
	std::vector<int> id;
	// size of rooted tree(component) for current item
	std::vector<int> size;
	// count number of component
	int count;
};
