package san.investment.front.repository.portfolio;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import san.investment.common.entity.portfolio.Portfolio;
import san.investment.common.enums.DataStatus;
import san.investment.common.enums.PortfolioType;
import san.investment.front.enums.SearchType;

import java.util.List;
import java.util.Optional;

import static san.investment.common.entity.portfolio.QPortfolio.portfolio;

@RequiredArgsConstructor
public class PortfolioRepositoryImpl implements PortfolioCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Optional<List<Portfolio>> findPortfolioList(DataStatus dataStatus, PortfolioType portfolioType) {

        List<Portfolio> list = factory.select(portfolio)
                .from(portfolio)
                .where(
                        matchDataStatus(dataStatus),
                        matchPortfolioType(portfolioType)
                )
                .orderBy(portfolio.orderNum.asc())
                .fetch();

        return Optional.ofNullable(list);
    }

    @Override
    public Optional<List<Portfolio>> findPortfolioList(SearchType searchType, String keyword, DataStatus dataStatus, PortfolioType portfolioType) {

        List<Portfolio> list = factory.select(portfolio)
                .from(portfolio)
                .where(
                        matchSearch(searchType, keyword),
                        matchDataStatus(dataStatus),
                        matchPortfolioType(portfolioType)
                )
                .orderBy(portfolio.orderNum.asc())
                .fetch();

        return Optional.ofNullable(list);
    }

    /**
     * 상태 매칭
     *
     * @param dataStatus
     * @return
     */
    private BooleanExpression matchDataStatus(DataStatus dataStatus) {
        if(dataStatus != null) {
            return portfolio.dataStatus.eq(dataStatus);
        }
        return null;
    }

    /**
     * 포트폴리오 타입 매칭
     *
     * @param portfolioType
     * @return
     */
    private BooleanExpression matchPortfolioType(PortfolioType portfolioType) {
        if(portfolioType != null) {
            return portfolio.portfolioType.eq(portfolioType);
        }
        return null;
    }

    /**
     * 검색 조건 검색
     *
     * @param searchType
     * @param keyword
     * @return
     */
    private BooleanExpression matchSearch(SearchType searchType, String keyword) {
        if(StringUtils.hasText(keyword)) {
            if(searchType != null) {
                if(SearchType.PORTFOLIO_TITLE.equals(searchType)) {
                    return portfolio.portfolioTitle.like("%" + keyword + "%");
                }
            }
        }
        return null;
    }
}
