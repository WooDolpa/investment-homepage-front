package san.investment.front.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : san.investment.front.dto.portfolio
 * className : PortfolioNewsResDto
 * user : jwlee
 * date : 2026. 1. 18.
 * description :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioNewsResDto {

    private Integer portfolioNewsNo;
    private String newsTitle;
    private String newsAgency;
    private String newsLink;
    private String regDatetime;
}
