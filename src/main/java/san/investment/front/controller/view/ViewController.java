package san.investment.front.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import san.investment.front.dto.company.CompanyResDto;
import san.investment.front.dto.menu.MenuResDto;
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

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);

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

        return "portfolio_history";
    }

    @GetMapping(path = "/portfolio/{portfolioNo}")
    public String portfolioDetail(@PathVariable(name = "portfolioNo") Integer portfolioNo, Model model) {

        CompanyResDto company = companyService.findCompany();
        List<MenuResDto> menuList = menuService.findMenuList();

        model.addAttribute("company", company);
        model.addAttribute("menuList", menuList);

        return "portfolio_detail";
    }
}
