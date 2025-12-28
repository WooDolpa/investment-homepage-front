package san.investment.front.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import san.investment.common.entity.company.Company;

/**
 * packageName : san.investment.front.repository
 * className : CompanyRepository
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
