package otymus.com.apiremedio.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Entity(name="Usuario")
@Table(name = "tb_usuarios")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario  {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String login;
    private String senha;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_usuarios_roles", // Adapte o nome da tabela de junção
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    //construtor padrão vazio
    public Usuario() {
    }

    public enum Values {
        ADMIN(1L),
        BASIC(2L);


        Long roleId;
        Long userId;
        Values(Long roleId) {
            this.roleId = roleId;
        }
        public Long getRoleId() {
            return roleId;
        }
    }



}
