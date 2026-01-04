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
- **Provides**: Shared entities (Company, Menu, Portfolio), enums (DataStatus, PortfolioType), and exceptions
- **Component Scanning**: Both `san.investment.front` and `san.investment.common` packages
- **Entity Scanning**: Entities are in `san.investment.common.entity` package

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
- Uses method name derivation for queries (e.g., `findByDataStatusOrderByOrderNumAsc`)
- QueryDSL is configured but currently uses standard JPA queries
- `JPAQueryFactory` bean is available in `QueryDSLConfig` if complex queries are needed

**DTO Layer** (`dto/`)
- Response DTOs for transferring data to templates
- All DTOs use Lombok (`@Builder`, `@Getter`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Decouples entity structure from view requirements

### Configuration Files

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

### Template Structure

**Thymeleaf Fragment-Based Design**:
- Templates located in `src/main/resources/templates/`
- Reusable fragments in `templates/fragments/`:
  - `common.html`: nav (mobile), logo (header), header (desktop navigation)
  - `header.html`: HTML head section
  - `footer.html`: Footer content
  - `scripts.html`: Common JavaScript includes
- Page templates: `main.html`, `portfolio.html`, `portfolio_history.html`, `portfolio_detail.html`
- Fragments are composed using `th:replace`

**Page Identification Pattern**:
- Each page template uses `data-page` attribute on `<body>` tag
- JavaScript uses `document.body.dataset.page` to identify current page
- Examples: `data-page="portfolio"`, `data-page="portfolio-history"`, `data-page="portfolio-detail"`

### Frontend JavaScript Architecture

**Common JavaScript** (`src/main/resources/static/js/common.js`):
- Single JavaScript file for all page interactions
- Uses page identification pattern to conditionally execute page-specific code
- Pattern: `if (currentPage === "portfolio") { ... }`

**Key Frontend Features**:
1. **Portfolio Search** (portfolio.html, portfolio_history.html)
   - Client-side search with API integration
   - Triggered by Enter key or search button click
   - Dynamic card rendering via `createCardElement()` and `renderCards()`
   - API endpoint: `/v1/api/portfolio/list?portfolioType={P|C}&searchType=portfolioTitle&keyword={keyword}`

2. **Card Animation System**
   - Uses IntersectionObserver for scroll-based reveal animations
   - Cards have `is-reveal-init` class initially, `is-visible` class when in viewport
   - Staggered animation delays via `--reveal-delay` CSS custom property

3. **Navigation**
   - Mobile hamburger menu toggle
   - Desktop horizontal scrollable navigation with drag support
   - Active state management based on `data-page` attribute

**CSS Architecture** (`src/main/resources/static/css/common.css`):
- Single stylesheet for all pages
- Card styling classes:
  - `.card-date`: Small date text above title (portfolio pages)
  - `.card-title`: Main title text (portfolio pages)
  - `.card-title-date`: Date-only display (main page)
  - All use absolute positioning over card images
  - Hover effects change text color to `#6a0dad` (purple)

### Portfolio Card Rendering Pattern

**Unified Card Structure** (used in main.html, portfolio.html, portfolio_history.html):
```html
<a class="investment-card" th:href="${portfolio.portfolioDetailUrl}" th:data-name="${portfolio.portfolioTitle}">
    <div class="card-date" th:text="${portfolio.portfolioDate}"></div>  <!-- Only on portfolio pages -->
    <h3 class="card-title" th:text="${portfolio.portfolioTitle}"></h3>
    <img th:src="${portfolio.portfolioImgUrl ?: '/images/default-portfolio.svg'}" alt="" />
</a>
```

**Main Page Variation**:
- Uses `.card-title-date` class instead of separate date/title elements
- Displays only date information in the title position

**Dynamic Rendering** (via JavaScript):
- `createCardElement(portfolio, index)`: Creates card DOM elements from API data
- `renderCards(portfolios)`: Clears and re-renders entire card area
- Preserves animation effects during re-render

### File Upload Handling

- Max file size: 500MB
- Uploaded files served via `/uploads/**` endpoint
- Category directories: `company/`, `portfolio/`
- `FileUtil.convertToWebPath()` converts file system paths to web-accessible URLs
- Base path configured via `FILE_SAVE_URL` environment variable

## Database Configuration

- **ORM**: Spring Data JPA with Hibernate
- **Driver**: MariaDB JDBC Client
- **Connection Pool**: HikariCP (minimum 5 connections, 60s idle timeout)
- **Schema Management**: `ddl-auto: none` - no automatic schema generation
- **Open-in-view**: `false` - prevents lazy loading in views
- **SQL Logging**: DEBUG level for Hibernate SQL, TRACE for bind parameters and results

## Working with QueryDSL

QueryDSL is configured and ready to use:

1. Q-classes are generated in `src/main/generated/` during compilation
2. Annotation processors configured in `build.gradle` for Jakarta persistence
3. `JPAQueryFactory` bean available for injection
4. Q-classes are automatically cleaned on `./gradlew clean`

Example usage pattern:
```java
@RequiredArgsConstructor
public class CustomRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    // Use QEntity classes for type-safe queries
}
```

## API Endpoints

### Portfolio Search API
- **Endpoint**: `GET /v1/api/portfolio/list`
- **Parameters**:
  - `portfolioType`: "P" (progress) or "C" (completed)
  - `searchType`: "portfolioTitle" (currently only title search supported)
  - `keyword`: Search keyword (can be empty for all results)
- **Response**: JSON array of portfolio objects wrapped in `ApiResponseDto`
- **Response Structure**: Can be either array `[...]` or object with nested array `{data: [...]}` or `{list: [...]}`

## Code Patterns and Conventions

### Service Layer Pattern
- Use `@Transactional(readOnly = true)` for read-only operations
- Services return DTOs, not entities
- Inject repositories via constructor (use Lombok `@RequiredArgsConstructor`)

### Repository Layer Pattern
- Prefer Spring Data JPA method name derivation for simple queries
- Use QueryDSL for complex queries requiring type safety
- Return `Optional` for single results, `Optional<List>` for collections

### Controller Pattern
- Controllers inject multiple services as needed
- Populate `Model` with DTOs using descriptive attribute names
- Return template names as strings (e.g., `"main"`, `"portfolio"`)
- API controllers return `ResponseEntity` with `ApiResponseDto` wrapper

### Constants Usage
- API constants defined in `ApiConstants.java`
- Use constants for fixed IDs (e.g., `ApiConstants.COMPANY_ID`)

### Frontend JavaScript Patterns
- Always check `currentPage` before executing page-specific code
- Use `fetch` API for AJAX requests to `/v1/api` endpoints
- Handle both array and object-wrapped API responses
- Maintain animation classes during dynamic rendering

## Domain Structure

The application is organized by business domains:

**Company Domain**: Company information display
- Repository: `CompanyRepository`
- Service: `CompanyService`
- DTO: `CompanyResDto`
- Usage: Header logo, main page company info

**Menu Domain**: Navigation menu management
- Repository: `MenuRepository`
- Service: `MenuService`
- DTO: `MenuResDto`
- Usage: Site navigation (desktop and mobile)

**Portfolio Domain**: Project portfolio display
- Repository: `PortfolioRepository`
- Service: `PortfolioService`
- DTOs: `PortfolioResDto`, `PortfolioMainResDto`
- Usage: Portfolio pages with progress/completed projects, search functionality

## Important Notes

- This is a **hybrid application**: server-side rendering for initial page load, with AJAX API calls for dynamic content
- All entities come from the shared `investment-homepage-common` module
- Before running, ensure the common module is installed: `cd ../investment-homepage-common && ./gradlew clean build publishToMavenLocal`
- Logs are written to `logs/admin.log` (configured in `logback-spring.xml`)
- The application uses constructor-based dependency injection via Lombok
- Static resources (CSS, JS) are in `src/main/resources/static/`
- Portfolio images use fallback: `/images/default-portfolio.svg` when `portfolioImgUrl` is null
