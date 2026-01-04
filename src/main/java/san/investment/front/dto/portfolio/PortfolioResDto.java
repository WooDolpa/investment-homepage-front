package san.investment.front.dto.portfolio;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioResDto {

    private Integer portfolioNo;
    private String portfolioDate;
    private String portfolioTitle;
    private String portfolioSummary;
    private String portfolioImgUrl;
    private String portfolioContents;
    private String portfolioDetailUrl;
}
