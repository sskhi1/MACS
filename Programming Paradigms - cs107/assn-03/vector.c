#include "vector.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <search.h>

void VectorNew(vector *v, int elemSize, VectorFreeFunction freeFn, int initialAllocation)
{
    assert(initialAllocation >= 0);
    assert(elemSize > 0);
    v->elem_size = elemSize;
    v->base = malloc(initialAllocation * v->elem_size);
    assert(v->base != NULL);
    v->logLen = 0;
    v->allocLen = initialAllocation;
    v->free_fn = freeFn;
}

void VectorDispose(vector *v)
{
    if(v->free_fn != NULL){
        for (int i = 0; i < v->logLen; i++) {
            v->free_fn((char*)v->base + i * v->elem_size);
        }
    }
    free(v->base);
}

int VectorLength(const vector *v)
{ 
    return v->logLen; 
}

void *VectorNth(const vector *v, int position)
{ 
    assert(position >= 0);
    assert(position < v->logLen);
    void *elem = (char*)v->base + position * v->elem_size;
    return elem; 
}

void VectorReplace(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0);
    assert(position < v->logLen);
    assert(elemAddr != NULL);
    void *insertionPoint = (char*)v->base + position * v->elem_size;
    if(v->free_fn != NULL) v->free_fn(insertionPoint);

    memcpy(insertionPoint, elemAddr, v->elem_size);
}

void VectorInsert(vector *v, const void *elemAddr, int position)
{
    assert(position >= 0);
    assert(position <= v->logLen);
    assert(elemAddr != NULL);
    if(v->logLen == v->allocLen){
        v->allocLen *= 2;
        v->base = realloc(v->base, v->allocLen * v->elem_size);
        assert(v->base != NULL);
    }
    void *insertionPoint = (char*)v->base + position * v->elem_size;
    void *toMove = (char*)insertionPoint + v->elem_size;
    memmove(toMove, insertionPoint, (v->logLen - position) * v->elem_size);

    void *insertionPointHelper = (char*)v->base + position * v->elem_size;
    memcpy(insertionPointHelper, elemAddr, v->elem_size);
    v->logLen++;
}

void VectorAppend(vector *v, const void *elemAddr)
{
    assert(elemAddr != NULL);
    if(v->logLen == v->allocLen){
        v->allocLen *= 2;
        v->base = realloc(v->base, v->allocLen * v->elem_size);
        assert(v->base != NULL);
    }
    void *elem = (char*)v->base + (v->logLen * v->elem_size);
    memcpy(elem, elemAddr, v->elem_size);
    v->logLen++;
}

void VectorDelete(vector *v, int position)
{
    assert(v->logLen > 0);
    assert(position >= 0);
    assert(position < v->logLen);
    void *deletionPoint = (char*)v->base + position * v->elem_size;
    void *toMove = (char*)deletionPoint + v->elem_size;
    if(v->free_fn != NULL) v->free_fn(deletionPoint);
    v->logLen--;
    memmove(deletionPoint, toMove, (v->logLen - position) * v->elem_size);
}

void VectorSort(vector *v, VectorCompareFunction compare)
{
    assert(compare != NULL);
    qsort(v->base, v->logLen, v->elem_size, compare);
}

void VectorMap(vector *v, VectorMapFunction mapFn, void *auxData)
{
    assert(mapFn != NULL);
    for(int i = 0; i < v->logLen; i++){
        mapFn((char*)v->base + i * v->elem_size, auxData);
    }
}

static const int kNotFound = -1;
int VectorSearch(const vector *v, const void *key, VectorCompareFunction searchFn, int startIndex, bool isSorted)
{ 
    assert(searchFn != NULL);
    assert(startIndex >= 0);
    assert(startIndex <= v->logLen);
    assert(key != NULL);
    void *elem;
    size_t len = v->logLen - startIndex;
    if(isSorted){
        elem = bsearch(key, (char*)v->base + startIndex * v->elem_size, len, v->elem_size, searchFn);
    }else{
        elem = lfind(key, v->base, &len, v->elem_size, searchFn);
    }

    if(elem == NULL) return kNotFound;
    else return ((char*)elem - (char*)v->base)/v->elem_size; 
} 
