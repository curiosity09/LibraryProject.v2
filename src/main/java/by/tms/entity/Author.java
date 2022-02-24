package by.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(schema = "library_hibernate")
public class Author extends BaseEntity<Long> {

    @Column(name = "full_name", unique = true, nullable = false)
    private String fullName;
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    List<Book> books;
}
