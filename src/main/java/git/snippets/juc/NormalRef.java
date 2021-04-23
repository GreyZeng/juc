package git.snippets.juc;

import java.io.IOException;

public class NormalRef {
    public static void main(String[] args) throws IOException {
        M m = new M();
        m = null;
        System.gc();
        System.in.read();
    }
    static class M {
    	M() {}
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalized");
        }
    }
}


