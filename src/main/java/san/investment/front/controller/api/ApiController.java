package san.investment.front.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import san.investment.common.dto.ApiResponseDto;
import san.investment.front.service.PortfolioService;

/**
 * packageName : san.investment.front.controller.api
 * className : ApiController
 * user : jwlee
 * date : 2026. 1. 4.
 * description :
 */
@RestController
@RequestMapping(path = "/v1/api")
@RequiredArgsConstructor
public class ApiController {

    private final PortfolioService portfolioService;

    /**
     * 포트폴리오 검색 조회
     *
     * @param portfolioType
     * @param searchType
     * @param keyword
     * @return
     */
    @GetMapping(path = "/portfolio/list")
    public ResponseEntity<String> findPortfolioList(@RequestParam(name = "portfolioType") String portfolioType,
                                                    @RequestParam(name = "searchType") String searchType,
                                                    @RequestParam(name = "keyword") String keyword) {

        return new ResponseEntity<>(ApiResponseDto.makeResponse(portfolioService.findPortfolioList(portfolioType, searchType, keyword))
                , HttpStatus.OK);
    }

    @GetMapping(path = "/portfolio/news/list")
    public ResponseEntity<String> findPortfolioNewsList(@RequestParam(name = "portfolioNo") Integer portfolioNo,
                                                        @RequestParam(name = "searchType") String searchType,
                                                        @RequestParam(name = "keyword") String keyword) {

        return new ResponseEntity<>(ApiResponseDto.makeResponse(portfolioService.findPortfolioNewsList(portfolioNo, searchType, keyword)),
                HttpStatus.OK);
    }
}
