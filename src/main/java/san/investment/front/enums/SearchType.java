package san.investment.front.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * packageName : san.investment.front.enums
 * className : SearchType
 * user : jwlee
 * date : 2026. 1. 4.
 * description :
 */
@Getter
@RequiredArgsConstructor
public enum SearchType {

    PORTFOLIO_TITLE("portfolioTitle", "포트폴리오 제목")
    ;

    private final String key;
    private final String desc;

    public static SearchType findSearchType(String key) {
        return Arrays.stream(SearchType.values())
                .filter(i -> i.key.equals(key))
                .findFirst()
                .orElse(null);
    }
}
