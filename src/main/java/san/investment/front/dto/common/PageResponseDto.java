package san.investment.front.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {

    private List<T> list;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private int startPage;
    private int endPage;
    private boolean hasPrev;
    private boolean hasNext;
    private boolean isFirst;
    private boolean isLast;

    public static <T> PageResponseDto<T> of(List<T> list, int page, int size, long totalElements) {
        int blockSize = 5;
        int totalPages = totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / size);
        int currentPage = Math.max(1, Math.min(page, totalPages));

        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        boolean hasPrev = currentPage > 1;
        boolean hasNext = currentPage < totalPages;
        boolean isFirst = currentPage == 1;
        boolean isLast = currentPage >= totalPages;

        return PageResponseDto.<T>builder()
                .list(list != null ? list : Collections.emptyList())
                .page(currentPage)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .startPage(startPage)
                .endPage(endPage)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .isFirst(isFirst)
                .isLast(isLast)
                .build();
    }
}
