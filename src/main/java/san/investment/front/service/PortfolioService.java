package san.investment.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import san.investment.common.entity.portfolio.Portfolio;
import san.investment.common.entity.portfolio.PortfolioMain;
import san.investment.common.enums.DataStatus;
import san.investment.common.enums.PortfolioType;
import san.investment.common.exception.CustomException;
import san.investment.common.exception.ExceptionCode;
import san.investment.front.dto.portfolio.PortfolioMainResDto;
import san.investment.front.dto.portfolio.PortfolioResDto;
import san.investment.front.enums.SearchType;
import san.investment.front.repository.portfolio.PortfolioMainRepository;
import san.investment.front.repository.portfolio.PortfolioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMainRepository portfolioMainRepository;

    /**
     * 포트폴리오 진행중 조회
     *
     * @return
     */
    public List<PortfolioResDto> findProgressPortfolioList() {

        List<Portfolio> portfolioList = portfolioRepository.findPortfolioList(DataStatus.Yes, PortfolioType.PROGRESS)
                .orElse(new ArrayList<>());

        return portfolioList.stream()
                .map(portfolio -> {
                    return PortfolioResDto.builder()
                            .portfolioNo(portfolio.getPortfolioNo())
                            .portfolioDate(portfolio.getPortfolioDate())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioSummary(portfolio.getPortfolioSummary())
                            .portfolioContents(portfolio.getPortfolioContents())
                            .portfolioImgUrl(portfolio.getPortfolioImgUrl())
                            .portfolioDetailUrl("/portfolio/" + portfolio.getPortfolioNo())
                            .build();
                }).toList();
    }

    /**
     * 포트폴리오 완료 조회
     *
     * @return
     */
    public List<PortfolioResDto> findCompletedPortfolioList() {

        List<Portfolio> portfolioList = portfolioRepository.findPortfolioList(DataStatus.Yes, PortfolioType.COMPLETE)
                .orElse(new ArrayList<>());

        return portfolioList.stream()
                .map(portfolio -> {
                    return PortfolioResDto.builder()
                            .portfolioNo(portfolio.getPortfolioNo())
                            .portfolioDate(portfolio.getPortfolioDate())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioSummary(portfolio.getPortfolioSummary())
                            .portfolioContents(portfolio.getPortfolioContents())
                            .portfolioImgUrl(portfolio.getPortfolioImgUrl())
                            .portfolioDetailUrl("/portfolio/" + portfolio.getPortfolioNo())
                            .build();
                }).toList();
    }

    /**
     * 포트폴리오 상세
     *
     * @param portfolioNo
     * @return
     */
    public PortfolioResDto findPortfolio(Integer portfolioNo) {

        Portfolio findPortfolio = portfolioRepository.findById(portfolioNo)
                .orElseThrow(() -> new CustomException(ExceptionCode.PORTFOLIO_NOT_FOUND));

        return PortfolioResDto.builder()
                .portfolioNo(findPortfolio.getPortfolioNo())
                .portfolioDate(findPortfolio.getPortfolioDate())
                .portfolioTitle(findPortfolio.getPortfolioTitle())
                .portfolioSummary(findPortfolio.getPortfolioSummary())
                .portfolioContents(findPortfolio.getPortfolioContents())
                .portfolioImgUrl(findPortfolio.getPortfolioImgUrl())
                .portfolioDetailUrl("/portfolio/" + findPortfolio.getPortfolioNo())
                .build();
    }

    /**
     * 메인페이지 포트폴리오 조회
     *
     * @return
     */
    public List<PortfolioMainResDto> findPortfolioMainList() {

        List<PortfolioMain> list = portfolioMainRepository.findPortfolioMainList()
                .orElse(new ArrayList<>());

        return list.stream().map(pm -> {

            Portfolio portfolio = pm.getPortfolio();

            return PortfolioMainResDto.builder()
                    .portfolioMainNo(pm.getPortfolioMainNo())
                    .portfolioNo(portfolio.getPortfolioNo())
                    .portfolioDate(portfolio.getPortfolioDate())
                    .portfolioTitle(portfolio.getPortfolioTitle())
                    .portfolioSummary(portfolio.getPortfolioSummary())
                    .portfolioContents(portfolio.getPortfolioContents())
                    .portfolioImgUrl(portfolio.getPortfolioImgUrl())
                    .portfolioDetailUrl("/portfolio/" + portfolio.getPortfolioNo())
                    .build();
        }).toList();
    }

    /**
     * 포트폴리오 조회
     *
     * @param portfolioTypeStr
     * @param searchTypeStr
     * @param keyword
     * @return
     */
    public List<PortfolioResDto> findPortfolioList(String portfolioTypeStr, String searchTypeStr, String keyword) {

        PortfolioType portfolioType = PortfolioType.findPortfolioType(portfolioTypeStr);
        SearchType searchType = SearchType.findSearchType(searchTypeStr);

        List<Portfolio> portfolioList = portfolioRepository.findPortfolioList(searchType, keyword, DataStatus.Yes, portfolioType)
                .orElse(new ArrayList<>());

        return portfolioList.stream()
                .map(portfolio -> {
                    return PortfolioResDto.builder()
                            .portfolioNo(portfolio.getPortfolioNo())
                            .portfolioDate(portfolio.getPortfolioDate())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioSummary(portfolio.getPortfolioSummary())
                            .portfolioContents(portfolio.getPortfolioContents())
                            .portfolioImgUrl(portfolio.getPortfolioImgUrl())
                            .portfolioDetailUrl("/portfolio/" + portfolio.getPortfolioNo())
                            .build();
                }).toList();
    }
}
