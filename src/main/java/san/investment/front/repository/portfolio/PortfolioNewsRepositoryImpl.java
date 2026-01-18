package san.investment.front.repository.portfolio;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import san.investment.common.entity.portfolio.PortfolioNews;
import san.investment.front.enums.SearchType;

import java.util.List;
import java.util.Optional;

import static san.investment.common.entity.portfolio.QPortfolioNews.portfolioNews;

/**
 * packageName : san.investment.front.repository.portfolio
 * className : PortfolioNewsRepositoryImpl
 * user : jwlee
 * date : 2026. 1. 18.
 * description :
 */
@RequiredArgsConstructor
public class PortfolioNewsRepositoryImpl implements PortfolioNewsCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Optional<List<PortfolioNews>> findPortfolioNewsList(Integer portfolioNo, SearchType searchType, String keyword) {

        List<PortfolioNews> list = factory.select(portfolioNews)
                .from(portfolioNews)
                .innerJoin(portfolioNews.portfolio).fetchJoin()
                .where(
                        matchPortfolioNo(portfolioNo),
                        matchSearch(searchType, keyword)
                )
                .orderBy(portfolioNews.orderNum.asc())
                .fetch();

        return Optional.ofNullable(list);
    }

    private BooleanExpression matchPortfolioNo(Integer portfolioNo) {
        if(portfolioNo != null && portfolioNo > 0) {
            return portfolioNews.portfolio.portfolioNo.eq(portfolioNo);
        }
        return null;
    }

    private BooleanExpression matchSearch(SearchType searchType, String keyword) {
        if(StringUtils.hasText(keyword)) {
            if(searchType != null) {
                if(SearchType.NEWS_TITLE.equals(searchType)) {
                    return portfolioNews.newsTitle.like("%" + keyword + "%");
                }else if(SearchType.NEWS_AGENCY.equals(searchType)) {
                    return portfolioNews.newsAgency.like("%" + keyword + "%");
                }
            }else {
                return portfolioNews.newsTitle.like("%" + keyword + "%")
                        .or(portfolioNews.newsAgency.like("%" + keyword + "%"));
            }
        }
        return null;
    }
}
