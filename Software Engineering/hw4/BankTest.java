import junit.framework.TestCase;

import java.util.ArrayList;

public class BankTest extends TestCase {
    String small;
    String File5k;
    String File100k;
    Bank b;

    protected void setUp() {
        small = "small.txt";
        File5k = "5k.txt";
        File100k = "100k.txt";
        b = new Bank(1);
    }

    public void testInitial(){
        ArrayList<Account> lst = b.getAccounts();
        for(Account a : lst){
            assertEquals(a.getBalance(), 1000);
        }
    }

    public void testSmall() throws InterruptedException {
        ArrayList<Account> lst = b.ProcessForTests(small, 4);
        int i = 0;
        for(Account a : lst){
            if(i % 2 == 0){
                assertEquals(a.getBalance(), 999);
            }else{
                assertEquals(a.getBalance(), 1001);
            }
            assertEquals(a.getTransactions(), 1);
            i++;
        }
    }

    public void testFiveK() throws InterruptedException {
        ArrayList<Account> lst = b.ProcessForTests(File5k, 4);
        for(Account a : lst){
            assertEquals(a.getBalance(), 1000);
        }
    }

    public void testFiveK2() throws InterruptedException {
        ArrayList<Account> lst = b.ProcessForTests(File5k, 1);
        for(Account a : lst){
            assertEquals(a.getBalance(), 1000);
        }
    }

    public void testHundredK() throws InterruptedException {
        ArrayList<Account> lst = b.ProcessForTests(File100k, 4);
        for(Account a : lst){
            assertEquals(a.getBalance(), 1000);
        }
    }

    public void testHundredK2() throws InterruptedException {
        ArrayList<Account> lst = b.ProcessForTests(File100k, 1);
        for(Account a : lst){
            assertEquals(a.getBalance(), 1000);
        }
    }
}
