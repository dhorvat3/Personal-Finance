package helper;

import entities.User;

/**
 * Created by Filip on 8.11.2016..
 */

public class MockData {
    public static  void writeAll(){
        User korisnik = new User();
        korisnik.setIme("Filip");
        korisnik.setPrezime("Strahija");
        korisnik.setEmail("fico@gmail.com");
        korisnik.setKorime("fico");
        korisnik.setLozinka("1234");
        korisnik.save();
    }
}
