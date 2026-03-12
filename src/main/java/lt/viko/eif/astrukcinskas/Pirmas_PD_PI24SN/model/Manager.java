package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.model;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Table(name = "managers")
@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "account_number")
    private BigInteger accountNumber;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
