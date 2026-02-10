# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.7 web application serving as the front-end for an investment company homepage. It uses server-side rendering with Thymeleaf to display company information, portfolios, and navigation menus. The application runs on port 8090 and connects to a MariaDB database.

## Build and Run Commands

### Building the Application
```bash
./gradlew clean build
```

### Running with Different Profiles
```bash
# Local environment (default)
./gradlew bootRun

# Production environment
./gradlew bootRun -Pprofile=prod
```

### Building with Specific Profile
```bash
./gradlew clean build -Pprofile=prod
```

### Cleaning Generated Files
```bash
# Removes QueryDSL Q-classes from src/main/generated
./gradlew clean
```

## Required Environment Variables

The application requires the following environment variables to be set:

- `MARIADB_URL` - MariaDB connection URL (format: `host:port/database`)
- `MARIADB_USERNAME` - Database username
- `MARIADB_PASSWORD` - Database password
- `FILE_SAVE_URL` - Base directory path for uploaded files

## Architecture Overview

### Multi-Module Structure

This application depends on a shared common module:
- **Dependency**: `san.investment:investment-homepage-common:1.0` (installed in mavenLocal)
- **Provides**: Shared entities (Company, Menu, Portfolio, PortfolioMain, PortfolioNews), enums (DataStatus, PortfolioType), exceptions (CustomException, ExceptionCode), and ApiResponseDto
- **Component Scanning**: Both `san.investment.front` and `san.investment.common` packages
- **Entity Scanning**: Entities are in `san.investment.common.entity` package
- **Prerequisite**: `cd ../investment-homepage-common && ./gradlew clean build publishToMavenLocal`

### Layered Architecture

The application follows a clean layered MVC architecture:

**Controller Layer** (`controller/`)
- **View Controllers** (`controller/view/`): Return Thymeleaf template names for server-side rendering
- **API Controllers** (`controller/api/`): RESTful endpoints under `/v1/api` for AJAX requests
- Controllers populate Model objects with DTOs for template rendering
- Use `@Controller` for view controllers, `@RestController` for API endpoints

**Service Layer** (`service/`)
- Business logic and data transformation
- Uses `@Transactional(readOnly = true)` for read operations
- Converts entity objects to DTOs
- Handles file path conversion using `FileUtil`

**Repository Layer** (`repository/`)
- Extends `JpaRepository` from Spring Data JPA
- Uses method name derivation for simple queries (e.g., `findByDataStatusOrderByOrderNumAsc`)
- Custom repositories use QueryDSL with `JPAQueryFactory` for complex queries
- Custom repository pattern: interface `XxxCustomRepository` + implementation `XxxRepositoryImpl`
- Return `Optional` for single results, `Optional<List>` for collections

**DTO Layer** (`dto/`)
- Response DTOs for transferring data to templates
- All DTOs use Lombok (`@Builder`, `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Decouples entity structure from view requirements

### Configuration

**Profile-Based Configuration**:
- Profiles: `local` and `prod`
- Profile-specific configs in `src/main/resources/{profile}/`
- Each profile has its own `application.yml` and `logback-spring.xml`
- Profile selection via Gradle: `-Pprofile=prod` (defaults to `local`)

**Key Configuration Classes**:
- `BeanConfig`: Explicit HikariCP datasource configuration
- `JpaConfig`: Enables JPA auditing
- `QueryDSLConfig`: Provides `JPAQueryFactory` bean for type-safe queries
- `WebMvcConfig`: Maps `/uploads/**` URL pattern to file system for serving uploaded files

## View Routes

| URL | Controller Method | Template | `data-page` |
|-----|------------------|----------|-------------|
| `/` | `HomeController.home()` | redirect to `/main` | — |
| `/main` | `ViewController.main()` | `main.html` | (none) |
| `/portfolio` | `ViewController.portfolio()` | `portfolio.html` | `portfolio` |
| `/history` | `ViewController.history()` | `portfolio_history.html` | `portfolio-history` |
| `/portfolio/{portfolioNo}` | `ViewController.portfolioDetail()` | `portfolio_detail.html` | `portfolio-detail` |

## API Endpoints

### Portfolio Search API
- **Endpoint**: `GET /v1/api/portfolio/list`
- **Parameters**:
  - `portfolioType`: "P" (progress) or "C" (completed)
  - `searchType`: "portfolioTitle"
  - `keyword`: Search keyword (can be empty for all results)
- **Response**: JSON wrapped in `ApiResponseDto`

### Portfolio News Search API
- **Endpoint**: `GET /v1/api/portfolio/news/list`
- **Parameters**:
  - `portfolioNo`: Portfolio ID
  - `searchType`: "newsTitle" or "newsAgency" (empty searches both)
  - `keyword`: Search keyword
- **Response**: JSON wrapped in `ApiResponseDto`

**Response Structure Note**: API responses can be either array `[...]` or object with nested array `{data: [...]}` or `{list: [...]}`. Frontend JavaScript handles both formats.

## Template Structure

**Thymeleaf Fragment-Based Design**:
- Reusable fragments in `templates/fragments/`:
  - `common.html`: nav (mobile), logo (header), header (desktop navigation)
  - `header.html`: HTML head section
  - `footer.html`: Footer content
  - `scripts.html`: Common JavaScript includes
- Page templates: `main.html`, `portfolio.html`, `portfolio_history.html`, `portfolio_detail.html`
- Fragments are composed using `th:replace`

**Page Identification Pattern**:
- Each page template uses `data-page` attribute on `<body>` tag
- JavaScript uses `document.body.dataset.page` to identify current page and conditionally execute page-specific code
- Pattern: `if (currentPage === "portfolio") { ... }`

## Frontend Architecture

**Single-file approach**: All JS in `static/js/common.js`, all CSS in `static/css/common.css`.

**Key Frontend Features**:
1. **Portfolio Search** (portfolio.html, portfolio_history.html)
   - Triggered by Enter key or search button click
   - Dynamic card rendering via `createCardElement()` and `renderCards()`
   - Determines `portfolioType` ("P" or "C") based on current page

2. **News Search** (portfolio_detail.html)
   - Filter dropdown: all, newsTitle, newsAgency
   - Dynamic table rendering via `renderNewsTable()`

3. **Card Animation System**
   - Uses IntersectionObserver for scroll-based reveal animations
   - Cards have `is-reveal-init` class initially, `is-visible` class when in viewport
   - Staggered animation delays via `--reveal-delay` CSS custom property

4. **Navigation**
   - Mobile hamburger menu toggle
   - Desktop horizontal scrollable navigation with drag support
   - Active state management based on `data-page` attribute

**Card Styling** (CSS):
- `.card-date`: Small date text above title (portfolio pages)
- `.card-title`: Main title text (portfolio pages)
- `.card-title-date`: Date-only display (main page variation)
- All use absolute positioning over card images; hover color `#6a0dad` (purple)

## Enums

**SearchType** (`enums/SearchType.java`):
- `PORTFOLIO_TITLE("portfolioTitle")` — portfolio title search
- `NEWS_TITLE("newsTitle")` — news article title search
- `NEWS_AGENCY("newsAgency")` — news agency name search
- Static method: `findSearchType(String key)` returns matching enum or null

## Domain Structure

**Company**: Company info display (logo, main image, address)
- `CompanyRepository` → `CompanyService` → `CompanyResDto`
- Uses fixed ID: `ApiConstants.COMPANY_ID = 1`

**Menu**: Navigation menu items
- `MenuRepository` → `MenuService` → `MenuResDto`

**Portfolio**: Project portfolio display with search
- `PortfolioRepository` (+ `PortfolioCustomRepository`) → `PortfolioService` → `PortfolioResDto`
- `PortfolioMainRepository` (+ `PortfolioMainCustomRepository`) → featured portfolios on main page → `PortfolioMainResDto`
- `PortfolioNewsRepository` (+ `PortfolioNewsCustomRepository`) → news articles per portfolio → `PortfolioNewsResDto`

## Working with QueryDSL

QueryDSL is actively used in custom repository implementations:

1. Q-classes are generated in `src/main/generated/` during compilation
2. Annotation processors configured in `build.gradle` for Jakarta persistence
3. `JPAQueryFactory` bean available for injection
4. Q-classes are automatically cleaned on `./gradlew clean`
5. Existing implementations: `PortfolioRepositoryImpl`, `PortfolioMainRepositoryImpl`, `PortfolioNewsRepositoryImpl`
6. Pattern: null-safe `BooleanExpression` predicates for dynamic query building

## Database Configuration

- **ORM**: Spring Data JPA with Hibernate
- **Driver**: MariaDB JDBC Client
- **Connection Pool**: HikariCP (minimum 5 connections, 60s idle timeout)
- **Schema Management**: `ddl-auto: none` - no automatic schema generation
- **Open-in-view**: `false` - prevents lazy loading in views
- **SQL Logging**: DEBUG level for Hibernate SQL, TRACE for bind parameters and results

## File Upload Handling

- Max file size: 500MB
- Uploaded files served via `/uploads/**` endpoint
- Category directories: `company/`, `portfolio/`
- `FileUtil.convertToWebPath()` converts file system paths to web-accessible URLs
- Portfolio images use fallback: `/images/default-portfolio.svg` when `portfolioImgUrl` is null

## Important Notes

- This is a **hybrid application**: server-side rendering for initial page load, with AJAX API calls for dynamic content (search)
- All entities come from the shared `investment-homepage-common` module — they are not in this repository
- Logs are written to `logs/admin.log` (configured in `logback-spring.xml`)
- Constructor-based dependency injection via Lombok `@RequiredArgsConstructor`

# 자동 수정대신 제시 방향을 제공하고 내가 요청할때만 수정 진행
