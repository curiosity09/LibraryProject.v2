package by.tms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class BookDto implements Serializable {

    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @NotNull
    private AuthorDto author;
    @NotNull
    private GenreDto genre;
    @NotNull
    private SectionDto section;
    @Max(99)
    @Min(0)
    private int quantity;
    @Min(1800)
    private int publicationYear;
}
