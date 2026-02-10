package san.investment.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import san.investment.common.entity.company.Company;
import san.investment.common.exception.CustomException;
import san.investment.common.exception.ExceptionCode;
import san.investment.front.constants.ApiConstants;
import san.investment.front.dto.company.CompanyResDto;
import san.investment.front.repository.company.CompanyRepository;
import san.investment.front.utils.FileUtil;

import java.util.Base64;

/**
 * packageName : san.investment.front.service
 * className : CompanyService
 * user : jwlee
 * date : 2025. 12. 28.
 * description :
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final FileUtil fileUtil;

    /**
     * 회사 조회
     *
     * @return
     */
    public CompanyResDto findCompany() {

        Company findCompany = companyRepository.findById(ApiConstants.COMPANY_ID)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMPANY));

        String postCode = findCompany.getPostCode();
        String address = findCompany.getAddress();
        String addressDetail = findCompany.getAddressDetail();
        String fullAddress = "";

        if(StringUtils.hasText(postCode)) {
            fullAddress = "(" + postCode + ")";
        }

        if(StringUtils.hasText(address)) {
            fullAddress += address;
        }

        if(StringUtils.hasText(addressDetail)) {
            fullAddress += " " + addressDetail;
        }

        return CompanyResDto.builder()
                .companyName(findCompany.getCompanyName())
                .companyInfo(findCompany.getCompanyInfo())
                .logoUrl(fileUtil.convertToWebPath(findCompany.getLogoUrl()))
                .mainImgUrl(fileUtil.convertToWebPath(findCompany.getMainImgUrl()))
                .fullAddress(fullAddress)
                .build();
    }

    /**
     * 회사 정보 유효성 검증
     *
     * @param encodedId Base64 인코딩된 회사 ID
     * @return
     */
    public boolean verifyBusinessCard(String encodedId) {
        Integer id = decodeId(encodedId);
        if (id == null) {
            return false;
        }
        return companyRepository.existsById(id);
    }

    /**
     * ID로 회사 조회
     *
     * @param encodedId Base64 인코딩된 회사 ID
     * @return
     */
    public CompanyResDto findCompanyById(String encodedId) {
        Integer id = decodeId(encodedId);
        if (id == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_COMPANY);
        }

        Company findCompany = companyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMPANY));

        String postCode = findCompany.getPostCode();
        String address = findCompany.getAddress();
        String addressDetail = findCompany.getAddressDetail();
        String fullAddress = "";

        if(StringUtils.hasText(postCode)) {
            fullAddress = "(" + postCode + ")";
        }

        if(StringUtils.hasText(address)) {
            fullAddress += address;
        }

        if(StringUtils.hasText(addressDetail)) {
            fullAddress += " " + addressDetail;
        }

        return CompanyResDto.builder()
                .companyName(findCompany.getCompanyName())
                .companyInfo(findCompany.getCompanyInfo())
                .logoUrl(fileUtil.convertToWebPath(findCompany.getLogoUrl()))
                .mainImgUrl(fileUtil.convertToWebPath(findCompany.getMainImgUrl()))
                .businessCard1(fileUtil.convertToWebPath(findCompany.getBusinessCard1()))
                .businessCard2(fileUtil.convertToWebPath(findCompany.getBusinessCard2()))
                .fullAddress(fullAddress)
                .build();
    }

    /**
     * Base64 디코딩하여 Integer ID 반환
     *
     * @param encodedId
     * @return 디코딩된 ID, 실패 시 null
     */
    private Integer decodeId(String encodedId) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encodedId));
            return Integer.parseInt(decoded);
        } catch (Exception e) {
            return null;
        }
    }
}
