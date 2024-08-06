#include "UnionFind.h"

UnionFind::UnionFind(int n) {
    s = new int[n];
    for(int i = 0; i < n; i++){	// n disjoint sets
        s[i] = i;
    }
	counter = n;
}

UnionFind::~UnionFind() {
	delete[] s;
}

int UnionFind::size() {
    return counter;
}

bool UnionFind::isSingleComponent(){
	return counter == 1;
}

int UnionFind::find(int a) {
	if(s[a] == a) return a;

	return find(s[a]);
}

bool UnionFind::connected(int a, int b) {
    return find(a) == find(b);
}

void UnionFind::merge(int a, int b) {
	if(counter == 1) return;
    int x = find(a);
    int y = find(b);
    if(x == y) return; // same cluster
    
    s[x] = y;
    counter--;
}
