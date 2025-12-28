package san.investment.front.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : san.investment.front.dto.menu
 * className : MenuResDto
 * user : jwlee
 * date : 2025. 12. 29.
 * description :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResDto {

    private Integer menuId;
    private String menuName;
    private String menuUrl;
}
