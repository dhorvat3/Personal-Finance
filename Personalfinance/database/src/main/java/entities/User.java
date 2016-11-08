package entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Filip on 8.11.2016..
 */
@Table(database = MainDatabase.class)
public class User extends BaseModel{
    @PrimaryKey(autoincrement = true)
    @Column int id;
    @Column String ime;
    @Column String prezime;
    @Column String email;
    @Column String korime;
    @Column String lozinka;

    public User() {
    }

    public User(String lozinka, int id, String ime, String prezime, String email, String korime) {
        this.lozinka = lozinka;
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korime = korime;
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getEmail() {
        return email;
    }

    public String getKorime() {
        return korime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKorime(String korime) {
        this.korime = korime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
