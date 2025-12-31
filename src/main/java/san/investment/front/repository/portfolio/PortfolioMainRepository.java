package san.investment.front.repository.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.portfolio.PortfolioMain;

public interface PortfolioMainRepository extends JpaRepository<PortfolioMain, Integer>, PortfolioMainCustomRepository {

}
