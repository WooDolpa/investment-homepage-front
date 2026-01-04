package san.investment.front.dto.portfolio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioMainResDto {

    private Integer portfolioMainNo;
    private Integer portfolioNo;
    private String portfolioDate;
    private String portfolioTitle;
    private String portfolioSummary;
    private String portfolioImgUrl;
    private String portfolioContents;
    private String portfolioDetailUrl;
}
