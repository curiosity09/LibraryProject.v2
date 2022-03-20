package by.tms.entity.user;

import by.tms.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@DiscriminatorValue("user")
@PrimaryKeyJoinColumn(name = "account_id")
@Table(name = "`user`", schema = "library_hibernate")
public class User extends Account {

    @Column(name = "is_banned")
    private boolean isBanned;

    @Builder
    public User(String username, String password, UserData userData, List<Order> orders, boolean isBanned) {
        super(username, password, userData, orders);
        this.isBanned = isBanned;
    }
}
