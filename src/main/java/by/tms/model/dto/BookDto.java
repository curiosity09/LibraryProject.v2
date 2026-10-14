package by.tms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
