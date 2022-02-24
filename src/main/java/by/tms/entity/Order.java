package by.tms.entity;

import by.tms.entity.user.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(schema = "library_hibernate")
public class Order extends BaseEntity<Long> {

    @ManyToMany
    @JoinTable(
            name = "order_book",
            schema = "library_hibernate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @ToString.Exclude
    private List<Book> book;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Account account;
    @Column(name = "rental_time", nullable = false)
    private LocalDateTime rentalTime;
    @Column(name = "rental_period", nullable = false)
    private LocalDateTime rentalPeriod;
}
