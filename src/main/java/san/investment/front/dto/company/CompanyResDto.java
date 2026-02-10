package san.investment.front.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : san.investment.front.dto.company
 * className : CompanyResDto
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResDto {

    private String companyName;
    private String logoUrl;
    private String mainImgUrl;
    private String companyInfo;
    private String fullAddress;
    private String businessCard1;
    private String businessCard2;
}
