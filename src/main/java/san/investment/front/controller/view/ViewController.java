package san.investment.front.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import san.investment.front.dto.company.CompanyResDto;
import san.investment.front.dto.menu.MenuResDto;
import san.investment.front.service.CompanyService;
import san.investment.front.service.MenuService;

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
}
