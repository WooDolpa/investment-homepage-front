package san.investment.front.repository.portfolio;

import san.investment.common.entity.portfolio.PortfolioNews;
import san.investment.front.enums.SearchType;

import java.util.List;
import java.util.Optional;

/**
 * packageName : san.investment.front.repository.portfolio
 * className : PortfolioNewsCustomRepository
 * user : jwlee
 * date : 2026. 1. 18.
 * description :
 */
public interface PortfolioNewsCustomRepository {
    Optional<List<PortfolioNews>> findPortfolioNewsList(Integer portfolioNo, SearchType searchType, String keyword);
}
