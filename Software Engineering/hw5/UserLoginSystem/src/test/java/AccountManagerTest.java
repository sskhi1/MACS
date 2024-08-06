import com.example.userloginsystem.AccountManager;
import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    AccountManager accountManager;

    protected void setUp(){
        accountManager = new AccountManager();
    }

    public void testInit(){
        assertTrue(accountManager.accountExists("Patrick"));
        assertTrue(accountManager.accountExists("Molly"));
        assertFalse(accountManager.accountExists("kk"));
        assertTrue(accountManager.isCorrectPassword("Patrick", "1234"));
        assertFalse(accountManager.isCorrectPassword("Patrick", "1"));
    }

    public void testAdd(){
        accountManager.createNewAccount("Saba", "12345");
        assertTrue(accountManager.accountExists("Saba"));
        accountManager.createNewAccount("Lizi", "11111");
        assertTrue(accountManager.accountExists("Saba"));
        assertTrue(accountManager.accountExists("Lizi"));
        assertTrue(accountManager.isCorrectPassword("Saba", "12345"));
    }
}
