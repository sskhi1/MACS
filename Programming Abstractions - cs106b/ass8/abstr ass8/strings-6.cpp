#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'separateNumbers' function below.
 *
 * The function accepts STRING s as parameter.
 */

bool canSeparate(string result, string str){
    if(str.size() == 0) return true;
    long long prev = stoll(result);
    long long next = prev + 1;
    string nextStr = to_string(next);
    
    if(str.substr(0, nextStr.size()) == nextStr){
        if(canSeparate(nextStr, str.substr(nextStr.size()))){
            return true;
        }
    }
    return false;
}

void separateNumbers(string s) {
    if(s[0] == '0'){
        cout << "NO" << endl;
        return;
    }
    for(int i = 1; i < s.length()/2 + 1; i++){
        string result = s.substr(0, i);
        if(canSeparate(result, s.substr(i))){
            cout << "YES " << result << endl;
            return;
        }
    }
    cout << "NO" << endl;
}

int main()
{
    string q_temp;
    getline(cin, q_temp);

    int q = stoi(ltrim(rtrim(q_temp)));

    for (int q_itr = 0; q_itr < q; q_itr++) {
        string s;
        getline(cin, s);

        separateNumbers(s);
    }

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
