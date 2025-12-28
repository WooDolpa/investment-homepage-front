package san.investment.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import san.investment.common.entity.menu.Menu;
import san.investment.common.enums.DataStatus;
import san.investment.front.dto.menu.MenuResDto;
import san.investment.front.repository.menu.MenuRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : san.investment.front.service
 * className : MenuService
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuResDto> findMenuList() {
        List<Menu> menuList = menuRepository.findByDataStatusOrderByOrderNumAsc(DataStatus.Yes).orElse(new ArrayList<>());
        return menuList.stream()
                .map(menu -> {
                    return MenuResDto.builder()
                            .menuId(menu.getMenuId())
                            .menuName(menu.getMenuName())
                            .menuUrl(menu.getMenuUrl())
                            .build();
                }).toList();
    }
}
