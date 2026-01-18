package san.investment.front.repository.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.portfolio.PortfolioNews;

/**
 * packageName : san.investment.front.repository.portfolio
 * className : PortfolioNewsRepository
 * user : jwlee
 * date : 2026. 1. 18.
 * description :
 */
public interface PortfolioNewsRepository extends JpaRepository<PortfolioNews, Integer>, PortfolioNewsCustomRepository {
}
