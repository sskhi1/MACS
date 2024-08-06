#include "hashset.h"
#include <assert.h>
#include <stdlib.h>
#include <string.h>

void HashSetNew(hashset *h, int elemSize, int numBuckets,
		HashSetHashFunction hashfn, HashSetCompareFunction comparefn, HashSetFreeFunction freefn)
{
	assert(elemSize > 0);
	assert(numBuckets > 0);
	assert(hashfn != NULL);
	assert(comparefn != NULL);
	h->elem_size = elemSize;
	h->free_fn = freefn;
	h->hash_fn = hashfn;
	h->comp_fn = comparefn;
	h->num_buckets = numBuckets;
	h->hashTable = malloc(h->num_buckets * sizeof(vector));
	assert(h->hashTable != NULL);
	h->len = 0;
	for(int i = 0; i < h->num_buckets; i++){
		VectorNew(&h->hashTable[i], h->elem_size, h->free_fn, 4);
	}
}

void HashSetDispose(hashset *h)
{
	if(h->free_fn != NULL){
		for(int i = 0; i < h->num_buckets; i++){
			VectorDispose(&h->hashTable[i]);
		}
	}
	free(h->hashTable);
}

int HashSetCount(const hashset *h)
{ 
	return h->len; 
}

void HashSetMap(hashset *h, HashSetMapFunction mapfn, void *auxData)
{
	assert(mapfn != NULL);
	for(int i = 0; i < h->num_buckets; i++){
		VectorMap(&h->hashTable[i], mapfn, auxData);
	}
}

void HashSetEnter(hashset *h, const void *elemAddr)
{
	assert(elemAddr != NULL);

	int position = h->hash_fn(elemAddr, h->num_buckets);
	assert(position >= 0);
	assert(position < h->num_buckets);
	int result = VectorSearch(&h->hashTable[position], elemAddr, h->comp_fn, 0, false);

	if(result == -1){
		VectorAppend(&h->hashTable[position], elemAddr);
		h->len++;
	}else{
		VectorReplace(&h->hashTable[position], elemAddr, result);
	}
}

void *HashSetLookup(const hashset *h, const void *elemAddr)
{ 
	assert(elemAddr != NULL);

	int position = h->hash_fn(elemAddr, h->num_buckets);
	assert(position >= 0);
	assert(position < h->num_buckets);
	int result = VectorSearch(&h->hashTable[position], elemAddr, h->comp_fn, 0, false);

	if(result == -1){
		return NULL;
	}else{
		return VectorNth(&h->hashTable[position], result);
	}
}
