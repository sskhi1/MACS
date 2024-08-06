#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'pangrams' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

string pangrams(string s) {
    vector<int> freq(26);
    
    for(int i = 0; i < s.length(); i++){
        char ch = tolower(s[i]);
        int k = ch - 'a';
        freq[k]++;
    }
    for(int i = 0 ; i < freq.size(); i++){
        if(freq[i] == 0) return "not pangram";
    }
    return "pangram";
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = pangrams(s);

    fout << result << "\n";

    fout.close();

    return 0;
}
