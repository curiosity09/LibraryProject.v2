package by.tms.model.entity.user;

import by.tms.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("admin")
@PrimaryKeyJoinColumn(name = "account_id")
@Table(schema = "library_hibernate")
public class Admin extends Account {

    @Column(name = "admin_level")
    @Enumerated(EnumType.STRING)
    private Level adminLevel;

    @Builder
    public Admin(String username, String password, String role, UserData userData, List<Order> orders, Level adminLevel) {
        super(username, password, role, userData, orders);
        this.adminLevel = adminLevel;
    }
}
