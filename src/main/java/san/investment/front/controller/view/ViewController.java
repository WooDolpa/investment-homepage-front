package san.investment.front.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import san.investment.front.config.SeoConfig;
import san.investment.front.dto.company.CompanyResDto;
import san.investment.front.dto.menu.MenuResDto;
import san.investment.front.dto.portfolio.PortfolioMainResDto;
import san.investment.front.dto.portfolio.PortfolioNewsResDto;
import san.investment.front.dto.portfolio.PortfolioResDto;
import san.investment.front.service.CompanyService;
import san.investment.front.service.MenuService;
import san.investment.front.service.PortfolioService;

import java.util.List;

/**
 * packageName : san.investment.front.controller.view
 * className : ViewController
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final CompanyService companyService;
    private final MenuService menuService;
    private final PortfolioService portfolioService;
    private final SeoConfig seoConfig;

    /**
     * 메인 페이지
     *
     * @param model
     * @return
     */
    @GetMapping(path = "/main")
    public String main(Model model) {

        CompanyResDto company = companyService.findCompany();
        List<MenuResDto> menuList = menuService.findMenuList();
        List<PortfolioMainResDto> portfolioMainList = portfolioService.findPortfolioMainList();

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);
        model.addAttribute("portfolioMainList", portfolioMainList);

        // SEO
        model.addAttribute("pageTitle", seoConfig.getSiteName() + " - " + seoConfig.getSiteDescription());
        model.addAttribute("pageDescription", seoConfig.getSiteDescription());
        model.addAttribute("canonicalUrl", seoConfig.getBaseUrl() + "/main");

        return "main";
    }

    @GetMapping(path = "/portfolio")
    public String portfolio(Model model) {

        CompanyResDto company = companyService.findCompany();
        List<MenuResDto> menuList = menuService.findMenuList();
        List<PortfolioResDto> portfolioList = portfolioService.findProgressPortfolioList();

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);
        model.addAttribute("portfolioList", portfolioList);

        // SEO
        model.addAttribute("pageTitle", "진행중인 포트폴리오 | " + seoConfig.getSiteName());
        model.addAttribute("pageDescription", "San Investment 진행중인 포트폴리오 목록");
        model.addAttribute("canonicalUrl", seoConfig.getBaseUrl() + "/portfolio");

        return "portfolio";
    }

    @GetMapping(path = "/history")
    public String history(Model model) {

        CompanyResDto company = companyService.findCompany();
        List<MenuResDto> menuList = menuService.findMenuList();
        List<PortfolioResDto> portfolioList = portfolioService.findCompletedPortfolioList();

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);
        model.addAttribute("portfolioList", portfolioList);

        // SEO
        model.addAttribute("pageTitle", "완료된 포트폴리오 | " + seoConfig.getSiteName());
        model.addAttribute("pageDescription", "San Investment 완료된 포트폴리오 목록");
        model.addAttribute("canonicalUrl", seoConfig.getBaseUrl() + "/history");

        return "portfolio_history";
    }

    @GetMapping(path = "/portfolio/{portfolioNo}")
    public String portfolioDetail(@PathVariable(name = "portfolioNo") Integer portfolioNo, Model model) {

        CompanyResDto company = companyService.findCompany();
        List<MenuResDto> menuList = menuService.findMenuList();
        PortfolioResDto portfolio = portfolioService.findPortfolio(portfolioNo);
        List<PortfolioNewsResDto> portfolioNewsList = portfolioService.findPortfolioNewsList(portfolioNo, null, null);

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("portfolioNewsList", portfolioNewsList);

        // SEO
        model.addAttribute("pageTitle", portfolio.getPortfolioTitle() + " | " + seoConfig.getSiteName());
        model.addAttribute("pageDescription", portfolio.getPortfolioSummary());
        model.addAttribute("canonicalUrl", seoConfig.getBaseUrl() + "/portfolio/" + portfolioNo);
        if (portfolio.getPortfolioImgUrl() != null) {
            model.addAttribute("ogImage", seoConfig.getBaseUrl() + portfolio.getPortfolioImgUrl());
        }

        return "portfolio_detail";
    }

    /**
     * 명함 유효성 검증 후 리다이렉트
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/business/card/verify")
    public String verifyBusinessCard(@RequestParam(name = "id") String id) {

        boolean result = companyService.verifyBusinessCard(id);
        if (result) {
            return "redirect:/business/card?id=" + id;
        }
        return "redirect:/main";
    }

    /**
     * 명함 페이지
     *
     * @param id Base64 인코딩된 회사 ID
     * @param model
     * @return
     */
    @GetMapping(path = "/business/card")
    public String businessCard(@RequestParam(name = "id") String id, Model model) {

        CompanyResDto company = companyService.findCompanyById(id);

        model.addAttribute("company", company);

        return "business_card";
    }
}
