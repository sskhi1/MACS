import junit.framework.TestCase;

import java.security.NoSuchAlgorithmException;

public class CrackerTest extends TestCase {
    public void testHash() throws NoSuchAlgorithmException {
        String s = "molly";
        String hash = Cracker.computeHashValue(s);
        assertEquals("4181eecbd7a755d19fdf73887c54837cbecf63fd", hash);

        String a = "a";
        String hash2 = Cracker.computeHashValue(a);
        assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", hash2);
    }

    public void testHash2() throws NoSuchAlgorithmException {
        String s = "xyz";
        String hash = Cracker.computeHashValue(s);
        assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3", hash);

        String a = "a!";
        String hash2 = Cracker.computeHashValue(a);
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", hash2);
    }

    public void testCrackSimple() throws NoSuchAlgorithmException, InterruptedException {
        String hash = "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8";
        Cracker c = new Cracker(hash, 1, 8);  // a
        c.crackPassword();

        String hash1 = "34800e15707fae815d7c90d49de44aca97e2d759";  // a!
        Cracker c1 = new Cracker(hash1, 2, 8);
        c1.crackPassword();
    }

    public void testCrack() throws NoSuchAlgorithmException, InterruptedException {
        String hash = "4181eecbd7a755d19fdf73887c54837cbecf63fd";
        Cracker c = new Cracker(hash, 5, 8); // molly
        c.crackPassword();
    }

    public void testCrackWrong() throws NoSuchAlgorithmException, InterruptedException {
        String hash = "4181eecbd7a755d19fdf73887c54837cbecf63fd";
        Cracker c = new Cracker(hash, 1, 8); // molly
        c.crackPassword();
    }
}
