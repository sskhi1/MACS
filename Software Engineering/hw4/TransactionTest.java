import junit.framework.TestCase;

public class TransactionTest extends TestCase {
    Transaction t;

    protected void setUp() {
        t = new Transaction(0, 1, 100);
    }

    public void testTransactionBasic(){
        assertEquals(t.getFrom(), 0);
        assertEquals(t.getTo(), 1);
        assertEquals(t.getAmount(), 100);
    }

    public void testToString(){
        assertEquals(t.toString(), "from:" + 0 + " to:" + 1 + " amt:" + 100);
    }
}
