package san.investment.front.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import san.investment.front.config.SeoConfig;
import san.investment.front.dto.portfolio.PortfolioResDto;
import san.investment.front.service.PortfolioService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SitemapController {

    private final SeoConfig seoConfig;
    private final PortfolioService portfolioService;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        String baseUrl = seoConfig.getBaseUrl();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // 고정 페이지
        appendUrl(sb, baseUrl + "/main", "daily", "1.0");
        appendUrl(sb, baseUrl + "/portfolio", "weekly", "0.8");
        appendUrl(sb, baseUrl + "/history", "weekly", "0.8");

        // 동적 포트폴리오 상세 페이지
        List<PortfolioResDto> allPortfolios = new ArrayList<>();
        allPortfolios.addAll(portfolioService.findProgressPortfolioList());
        allPortfolios.addAll(portfolioService.findCompletedPortfolioList());

        for (PortfolioResDto portfolio : allPortfolios) {
            appendUrl(sb, baseUrl + "/portfolio/" + portfolio.getPortfolioNo(), "weekly", "0.6");
        }

        sb.append("</urlset>");

        return sb.toString();
    }

    private void appendUrl(StringBuilder sb, String loc, String changefreq, String priority) {
        sb.append("  <url>\n");
        sb.append("    <loc>").append(loc).append("</loc>\n");
        sb.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        sb.append("    <priority>").append(priority).append("</priority>\n");
        sb.append("  </url>\n");
    }
}
