using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <string.h>

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

// struct to overlay the blocks of data
struct dataStruct{
    const void* file;
    const void* name;
};

// bsearch player compare function.
static int compPlayer(const void *m1, const void *m2){
  dataStruct helper = *(dataStruct*) m1;
  const char* str1 = *(char**)helper.name;
  const char* str2 = (char*)helper.file + *(int*)m2;
  return strcmp(str1, str2);
}

// find player in actorfile.
void *imdb::binarySearch(const void* actorFile, const void* player) const{
  dataStruct key;
  key.name = player;
  key.file = actorFile;
  void *res = bsearch(&key, (int*)actorFile + 1, *(int*)actorFile, sizeof(int), compPlayer);
  return res;
}

bool imdb::getCredits(const string& player, vector<film>& films) const { 
  void *res = binarySearch(actorFile, &player);

  if(res == NULL) return false; // couldn't find player.

  // res now shoes how many bytes we need to read player's info
  int *offset = (int*)res;
  // ptr - current position
  char *ptr = (char*)actorFile + *offset;
  // The number of movies in which the actor has appeared, expressed as a two-byte short.
  short nFilms;
  // number of total bytes (must be even)
  int bytesZeroOffset = 0;  // %4 must be 0

  // If the length of the actor’s name is even, then the string is padded with an extra '\0'
  if(strlen(ptr) % 2 == 0){
    bytesZeroOffset += strlen(ptr) + 2;
    ptr += strlen(ptr) + 2;
  }else{
    bytesZeroOffset += strlen(ptr) + 1;
    ptr += strlen(ptr) + 1;
  }

  // after actor's name number of films in which the actor is is expressed in short.
  nFilms = *(short*)ptr;
  bytesZeroOffset += 2;
  ptr += 2;

  if(bytesZeroOffset % 4 != 0) ptr += 2;  // it will be even anyway.

  films.clear();
  for(short i = 0; i < nFilms; i++){
    char *moviePtr = (char*)movieFile + *(int*)ptr;
    string title;
    int year;
    while(*moviePtr != '\0'){
      title += *moviePtr;
      moviePtr++;
    }
    moviePtr++;
    // year - 1900 is stored in movie year.
    year = 1900 + *moviePtr;
    film currMovie = {title, year};
    films.push_back(currMovie);
    ptr += sizeof(int);
  }
  return true; 
}

// bsearch movies compare function.
static int compMovie(const void *m1, const void *m2){
  dataStruct *helper = (dataStruct *) m1;
  film *currMovie = (film *)helper->name;
  char *secondMovie = (char *)helper->file + *(int*)m2;

  string title;
  int year;
  while(*secondMovie != '\0'){
    title += *secondMovie;
    secondMovie++;
  }
  secondMovie++;
  year = 1900 + *secondMovie;
  film toCompare = {title, year};
  if(*currMovie == toCompare) return 0;
  else if(*currMovie < toCompare) return -1;
  else return 1;
}

// find movie in moviefile.
void *imdb::binarySearchMovie(const void *movieFile, const void *movie)const{
  dataStruct key;
  key.name = movie;
  key.file = movieFile;
  void *res = bsearch(&key, (int*)movieFile + 1, *(int*)movieFile, sizeof(int), compMovie);
  return res;
}

// almost everything is similar to getCredist() 
bool imdb::getCast(const film& movie, vector<string>& players) const { 
  void *res = binarySearchMovie(movieFile, &movie);

  if(res == NULL) return false; // couldn't find player.

  int *offset = (int*)res;
  char *ptr = (char*)movieFile + *offset;
  short nPlayers;
  int bytesZeroOffset = 0;  // %4 must be 0

  // different from getCredits() (because of the 1 byte 'year').
  if(movie.title.size() % 2 == 0){
    bytesZeroOffset += movie.title.size() + 2;
    ptr += movie.title.size() + 2;
  }else{
    bytesZeroOffset += movie.title.size() + 3;
    ptr += movie.title.size() + 3;
  }

  nPlayers = *(short*)ptr;
  bytesZeroOffset += 2;
  ptr += 2;

  if(bytesZeroOffset % 4 != 0) ptr += 2;

  players.clear();
  for(short i = 0; i < nPlayers; i++){
    char *playerPtr = (char*)actorFile + *(int*)ptr;
    string player;
    while(*playerPtr != '\0'){
      player += *playerPtr;
      playerPtr++;
    }
    players.push_back(player);
    ptr += sizeof(int);
  }
  return true; 
}

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
