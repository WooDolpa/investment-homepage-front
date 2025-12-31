package san.investment.front.repository.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.portfolio.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer>, PortfolioCustomRepository {
}
