package san.investment.front.repository.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.portfolio.Portfolio;
import san.investment.common.enums.DataStatus;
import san.investment.common.enums.PortfolioType;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
    Optional<List<Portfolio>> findByDataStatusAndPortfolioType(DataStatus dataStatus, PortfolioType portfolioType);
}
