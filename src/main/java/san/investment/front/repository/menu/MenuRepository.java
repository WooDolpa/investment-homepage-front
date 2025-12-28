package san.investment.front.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.menu.Menu;
import san.investment.common.enums.DataStatus;

import java.util.List;
import java.util.Optional;

/**
 * packageName : san.investment.front.repository.menu
 * className : MenuRepository
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<List<Menu>> findByDataStatusOrderByOrderNumAsc(DataStatus dataStatus);
}
