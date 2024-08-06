import junit.framework.TestCase;

public class AccountTest extends TestCase {
    Account a;
    Bank b;
    protected void setUp() {
        b = new Bank(1);
        a = new Account(b, 0, 1000);
    }

    public void testBasic(){
        assertEquals(a.getBalance(), 1000);
        assertEquals(a.getId(), 0);
        assertEquals(a.getTransactions(), 0);
    }

    public void testToString(){
        assertEquals("acct:" + 0 + " bal:" + 1000 + " trans:" + 0,
                a.toString());
    }

    public void testTransaction(){
        a.makeTransaction(200);
        assertEquals(a.getBalance(), 1200);
    }
}
