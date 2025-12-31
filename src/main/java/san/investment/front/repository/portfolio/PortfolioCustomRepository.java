package san.investment.front.repository.portfolio;

import san.investment.common.entity.portfolio.Portfolio;
import san.investment.common.enums.DataStatus;
import san.investment.common.enums.PortfolioType;

import java.util.List;
import java.util.Optional;

public interface PortfolioCustomRepository {
    Optional<List<Portfolio>> findPortfolioList(DataStatus dataStatus, PortfolioType portfolioType);
}
