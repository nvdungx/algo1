// unionfind.cpp : Defines the entry point for the application.
//

#include "unionfind.h"

using namespace std;

int binary_search(std::vector<int>& arr, int low, int high, int element, bool reverse)
{
	while (low <= high)
	{
		int mid = low + (high - low) / 2;
		if (element < arr[mid]) {
			if (!reverse) high = mid - 1;
			else low = mid + 1;
		}
		else if (element > arr[mid]) {
			if (!reverse) low = mid + 1;
			else high = mid - 1;
		}
		else return mid;
	}
	return -1;
}
int bitonic_search(std::vector<int>& arr, int element)
{
	int lo = 0, mid = 0, hi = arr.size()-1;
	// smaller than 2 head then skip
	if ((element < arr[lo]) && (element < arr[hi])) return -1;
	// find max value of bitonic array first LogN
	while (lo < hi)
	{
		mid = lo + (hi - lo) / 2;
		// if mid < next item -> increasing -> max value in mid + 1 and hi
		if (arr[mid] < arr[mid + 1]) lo = mid + 1;
		// mid > next item -> decreasing -> max value in low and mid -1
		else hi = mid; 
	}
	lo = 0;
	hi = arr.size() - 1;
	// if element larger than max skip
	if (element <= arr[mid])
	{
		if (element >= arr[lo])
		{
			// LogN
			int index = binary_search(arr, lo, mid, element, false);
			if (index != -1) return index;
		}
		if (element >= arr[hi])
		{
			// LogN
			int index = binary_search(arr, mid, hi, element, true);
			if (index != -1) return index;
		}
	}
	else
	{
		return -1;
	}
	return -1;
}

int bitonic_search2(std::vector<int>& arr, int target)
{
	int lo = 0, mid = 0, hi = arr.size();

	while (lo <= hi)
	{
		mid = lo + (hi - lo) / 2;
		if (target < arr[mid])
		{
			int val = -1;
			if (arr[mid] < arr[mid + 1]) {
				val = binary_search(arr, lo, mid - 1, target, false);
				if (val != -1) return val;
				else lo = mid + 1;
			}
			else
			{
				val = binary_search(arr, mid + 1, hi, target, false);
				if (val != -1) return val;
				else hi = mid - 1;
			}
		}
		else if (target > arr[mid])
		{
			if (arr[mid] < arr[mid + 1]) lo = mid + 1;
			else hi = mid - 1;
		}
		else
		{
			return mid;
		}
	}
	return -1;
}
int main()
{
	WeightedQU qu(12);
	std::vector<int> arr{ -92, -23, -4, 2, 8, 33, 52, 98, 24, 11, 5, 3, 0 };
	std::cout << bitonic_search(arr, -92) << std::endl;
	std::cout << bitonic_search(arr, -4) << std::endl;
	std::cout << bitonic_search(arr, 5) << std::endl;
	std::cout << bitonic_search(arr, 0) << std::endl;
	std::cout << bitonic_search(arr, -3) << std::endl;
	std::cout << "---------------" << std::endl;
	std::cout << bitonic_search2(arr, -92) << std::endl;
	std::cout << bitonic_search2(arr, -4) << std::endl;
	std::cout << bitonic_search2(arr, 5) << std::endl;
	std::cout << bitonic_search2(arr, 0) << std::endl;
	std::cout << bitonic_search2(arr, -3) << std::endl;
	return 0;
}
