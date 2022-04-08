package by.tms.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ShoppingCart implements Serializable {

    private List<BookDto> shoppingList;
}
