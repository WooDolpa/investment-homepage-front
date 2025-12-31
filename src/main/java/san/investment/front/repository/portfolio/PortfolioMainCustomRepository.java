package san.investment.front.repository.portfolio;

import san.investment.common.entity.portfolio.PortfolioMain;

import java.util.List;
import java.util.Optional;

public interface PortfolioMainCustomRepository {
    Optional<List<PortfolioMain>> findPortfolioMainList();
}
