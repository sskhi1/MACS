#ifndef UnionFind_Included
#define UnionFind_Included

class UnionFind {
public:
    UnionFind(int n);
    ~UnionFind();
	int size();
	bool isSingleComponent();
	int find(int a);
	bool connected(int a, int b);
	void merge(int a, int b);
private:
    int *s;
    int counter;
};
#endif