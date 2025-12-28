package san.investment.front.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * packageName : san.investment.front.controller.view
 * className : HomeController
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/main";
    }
}
