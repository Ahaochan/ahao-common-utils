package moe.ahao.transaction.user.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "User")
@Table(name = "user")
public class User extends BaseDO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Integer sex;

    @Column(name = "is_locked")
    private Boolean locked;
    @Column(name = "is_disabled")
    private Boolean disabled;
    @Column(name = "is_deleted")
    private Boolean deleted;

    private Date expireTime;

    @Transient
    private String transientValue;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
