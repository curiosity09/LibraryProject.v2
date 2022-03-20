package by.tms.model.entity.user;

import by.tms.model.entity.BaseEntity;
import by.tms.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "Account")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Table(schema = "library_hibernate")
public abstract class Account extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    protected String username;
    @Column(nullable = false)
    protected String password;
    @Column(name = "user_type", insertable = false, updatable = false)
    protected String role;
    @Column(nullable = false, length = 10)
    @Embedded
    private UserData userData;
    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    protected List<Order> orders;
}
