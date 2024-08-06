#include <vector>
#include <list>
#include <set>
#include <string>
#include <iostream>
#include <iomanip>
#include "imdb.h"
#include "path.h"
using namespace std;

/**
 * Using the specified prompt, requests that the user supply
 * the name of an actor or actress.  The code returns
 * once the user has supplied a name for which some record within
 * the referenced imdb existsif (or if the user just hits return,
 * which is a signal that the empty string should just be returned.)
 *
 * @param prompt the text that should be used for the meaningful
 *               part of the user prompt.
 * @param db a reference to the imdb which can be used to confirm
 *           that a user's response is a legitimate one.
 * @return the name of the user-supplied actor or actress, or the
 *         empty string.
 */

static string promptForActor(const string& prompt, const imdb& db)
{
  string response;
  while (true) {
    cout << prompt << " [or <enter> to quit]: ";
    getline(cin, response);
    if (response == "") return "";
    vector<film> credits;
    if (db.getCredits(response, credits)) return response;
    cout << "We couldn't find \"" << response << "\" in the movie database. "
	 << "Please try again." << endl;
  }
}

/**
 * Serves as the main entry point for the six-degrees executable.
 * There are no parameters to speak of.
 *
 * @param argc the number of tokens passed to the command line to
 *             invoke this executable.  It's completely ignored
 *             here, because we don't expect any arguments.
 * @param argv the C strings making up the full command line.
 *             We expect argv[0] to be logically equivalent to
 *             "six-degrees" (or whatever absolute path was used to
 *             invoke the program), but otherwise these are ignored
 *             as well.
 * @return 0 if the program ends normally, and undefined otherwise.
 */

void generateShortestPath(const imdb& db, const string& source, const string& target){
  list<path> partialPaths; // functions as a queue
  set<string> previouslySeenActors;
  set<film> previouslySeenFilms;

  // create a partial path around the start actor;
  path partialPath(source);
  // add this partial path to the queue of partial paths;
  partialPaths.push_back(partialPath);
  while(!partialPaths.empty() && (partialPaths.front()).getLength() < 6){
    // pull off front path (involves both front and pop_front)
    path frontPath = partialPaths.front();
    partialPaths.pop_front();

    // look up last actor’s movies
    string lastActor = frontPath.getLastPlayer();
    vector<film> lastActorMovies;
    db.getCredits(lastActor, lastActorMovies);

    // for each movie in his/her list of movies you’ve not seen before (iteration)
    vector<film>::iterator it = lastActorMovies.begin();
    vector<film>::iterator end = lastActorMovies.end();
    for(; it != end; it++){
      if(previouslySeenFilms.find(*it) == previouslySeenFilms.end()){
            
        // add movie to the set of previously seen movies
        previouslySeenFilms.insert(*it);

        // add movie to the set of previously seen movies
        vector<string> cast;
        // look up movie’s cast
        db.getCast(*it, cast);

        // for each cast member you’ve not seen before (iteration)
        vector<string>::iterator iter = cast.begin();
        vector<string>::iterator iterEnd = cast.end();
        for(; iter != iterEnd; iter++){
          if(previouslySeenActors.find(*iter) == previouslySeenActors.end()){
            // add cast member to set of those previously seen
            previouslySeenActors.insert(*iter);

            // clone the partial path
            path clone = frontPath;

            // add the movie/costar connection to the clone
            clone.addConnection(*it, *iter);

            // if you notice the costar is your target, then print the path and return
            if(*iter == target){
              cout << clone;
              return;
            }
            // otherwise add this new partial path to the end of the queue
            partialPaths.push_back(clone);
          }
        }
      }
    }
  }
  // if the while loop ends, print that you didn’t find a path
  cout << endl << "No path between those two people could be found." << endl << endl;
}

int main(int argc, const char *argv[])
{
  imdb db(determinePathToData(argv[1])); // inlined in imdb-utils.h
  if (!db.good()) {
    cout << "Failed to properly initialize the imdb database." << endl;
    cout << "Please check to make sure the source files exist and that you have permission to read them." << endl;
    return 1;
  }
  
  while (true) {
    string source = promptForActor("Actor or actress", db);
    if (source == "") break;
    string target = promptForActor("Another actor or actress", db);
    if (target == "") break;
    if (source == target) {
      cout << "Good one.  This is only interesting if you specify two different people." << endl;
    } else {
      // replace the following line by a call to your generateShortestPath routine... 
      generateShortestPath(db, source, target);
    }
  }
  
  cout << "Thanks for playing!" << endl;
  return 0;
}

