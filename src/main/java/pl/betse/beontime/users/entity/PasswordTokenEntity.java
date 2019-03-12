package pl.betse.beontime.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "userEntity")
@Entity
@Table(name = "PASSWORD_TOKEN")
public class PasswordTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PASSWORD_TOKEN_ID", nullable = false, unique = true)
    Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    String token;

    @JoinColumn(name = "USER_ID")
    @OneToOne(fetch = FetchType.EAGER)
    UserEntity userEntity;
}
