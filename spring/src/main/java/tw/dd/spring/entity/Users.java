package tw.dd.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id")
    private long id;

    @Column(name = "sub")
    private String sub;
    
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;
    
    public Users() {}
   
    public Users(long id, String sub, String email, String name) {
        this.id = id;
        this.sub = sub;
        this.email = email;
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSub() {
        return sub;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }

}
