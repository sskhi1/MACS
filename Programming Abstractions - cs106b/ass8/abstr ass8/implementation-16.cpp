#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'happyLadybugs' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING b as parameter.
 */

string happyLadybugs(string b) {
    vector<int> freqs(27, 0);
    for(int i = 0; i < b.size(); i++){
        if(b[i] == '_'){
            freqs[26]++;
        }else{
            freqs[b[i] - 'A']++;
        }
    }
    for(int i = 0; i < freqs.size(); i++){
        if(i != 26 && freqs[i] == 1){
            return "NO";
        }
        if( i == 26 && freqs[i] > 0){
            return "YES";
        }
    }
    if(freqs[26] == 0){
        int counter = 0;
        for(int i = 0; i < b.length() - 1; i++){
            char curr = b[i];
            char next = b[i+1];
            if(curr == next){
                counter++;
            }else{
                if(counter == 0){
                    return "NO";
                }
                counter = 0;
            }
        }
    }
    return "YES";
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string g_temp;
    getline(cin, g_temp);

    int g = stoi(ltrim(rtrim(g_temp)));

    for (int g_itr = 0; g_itr < g; g_itr++) {
        string n_temp;
        getline(cin, n_temp);

        int n = stoi(ltrim(rtrim(n_temp)));

        string b;
        getline(cin, b);

        string result = happyLadybugs(b);

        fout << result << "\n";
    }

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}
