package san.investment.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import san.investment.common.entity.portfolio.Portfolio;
import san.investment.common.enums.DataStatus;
import san.investment.common.enums.PortfolioType;
import san.investment.front.dto.portfolio.PortfolioResDto;
import san.investment.front.repository.portfolio.PortfolioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    /**
     * 포트폴리오 진행중 조회
     *
     * @return
     */
    public List<PortfolioResDto> findProgressPortfolioList() {

        List<Portfolio> portfolioList = portfolioRepository.findByDataStatusAndPortfolioType(DataStatus.Yes, PortfolioType.PROGRESS)
                .orElse(new ArrayList<>());

        return portfolioList.stream()
                .map(portfolio -> {
                    return PortfolioResDto.builder()
                            .portfolioNo(portfolio.getPortfolioNo())
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

        List<Portfolio> portfolioList = portfolioRepository.findByDataStatusAndPortfolioType(DataStatus.Yes, PortfolioType.COMPLETE)
                .orElse(new ArrayList<>());

        return portfolioList.stream()
                .map(portfolio -> {
                    return PortfolioResDto.builder()
                            .portfolioNo(portfolio.getPortfolioNo())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioSummary(portfolio.getPortfolioSummary())
                            .portfolioContents(portfolio.getPortfolioContents())
                            .portfolioImgUrl(portfolio.getPortfolioImgUrl())
                            .portfolioDetailUrl("/portfolio/" + portfolio.getPortfolioNo())
                            .build();
                }).toList();
    }
}
