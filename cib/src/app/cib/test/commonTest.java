package app.cib.test;

import com.neturbo.set.utils.*;

public class commonTest {
    public commonTest() {
    }
    public static void main(String[] args){
        System.out.println(Encryption.digest("21022"+"111111", "MD5"));
    }
}
