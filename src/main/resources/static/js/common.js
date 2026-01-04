document.addEventListener("DOMContentLoaded", () => {
  // --- Mobile Menu Toggle --- //
  const hamburger = document.querySelector(".hamburger-menu");
  const mobileNav = document.querySelector(".mobile-nav");
  const closeBtn = document.querySelector(".btn-close");

  const openMenu = () => {
    if (!hamburger || !mobileNav) return;
    hamburger.classList.add("is-active");
    mobileNav.classList.add("is-open");
  };

  const closeMenu = () => {
    if (!hamburger || !mobileNav) return;
    hamburger.classList.remove("is-active");
    mobileNav.classList.remove("is-open");
  };

  if (hamburger && mobileNav) {
    hamburger.addEventListener("click", openMenu);
  }

  if (closeBtn && mobileNav) {
    closeBtn.addEventListener("click", closeMenu);
  }

  // --- Navigation Active State --- //
  const currentPage = document.body.dataset.page;
  const navLinks = document.querySelectorAll(".nav-link");

  navLinks.forEach((link) => {
    const linkPage = link.dataset.page;
    if (currentPage && linkPage === currentPage) {
      link.classList.add("is-active");
      link.scrollIntoView({
        behavior: "smooth",
        inline: "center",
        block: "nearest",
      });
    }

    link.addEventListener("click", () => {
      if (mobileNav && mobileNav.classList.contains("is-open")) {
        closeMenu();
      }
    });
  });

  // --- Draggable Horizontal Scroll --- //
  const scrollArea = document.querySelector(".content-area-5");

  if (scrollArea) {
    let isDown = false;
    let startX;
    let scrollLeft;

    const stopDragging = () => {
      isDown = false;
      scrollArea.style.cursor = "grab";
    };

    scrollArea.addEventListener("mousedown", (e) => {
      isDown = true;
      scrollArea.style.cursor = "grabbing";
      startX = e.pageX - scrollArea.offsetLeft;
      scrollLeft = scrollArea.scrollLeft;
    });

    scrollArea.addEventListener("mouseleave", stopDragging);
    scrollArea.addEventListener("mouseup", stopDragging);

    scrollArea.addEventListener("mousemove", (e) => {
      if (!isDown) return;
      e.preventDefault();
      const x = e.pageX - scrollArea.offsetLeft;
      const walk = (x - startX) * 2; // Scroll speed multiplier
      scrollArea.scrollLeft = scrollLeft - walk;
    });

    // --- Touch Events for Mobile --- //
    scrollArea.addEventListener("touchstart", (e) => {
      isDown = true;
      startX = e.touches[0].pageX - scrollArea.offsetLeft;
      scrollLeft = scrollArea.scrollLeft;
    });

    scrollArea.addEventListener("touchend", () => {
      isDown = false;
      scrollArea.style.cursor = "grab";
    });

    scrollArea.addEventListener("touchmove", (e) => {
      if (!isDown) return;
      const x = e.touches[0].pageX - scrollArea.offsetLeft;
      const walk = (x - startX) * 2; // Scroll speed multiplier
      scrollArea.scrollLeft = scrollLeft - walk;
    });
  }

  // --- Desktop Scroll Buttons --- //
  const contentArea5 = document.querySelector(".content-area-5");
  const leftArrow = document.querySelector(".left-arrow");
  const rightArrow = document.querySelector(".right-arrow");

  if (contentArea5 && leftArrow && rightArrow) {
    const scrollAmount = 200;

    const updateButtonStates = () => {
      const isScrollable = contentArea5.scrollWidth > contentArea5.clientWidth;

      if (!isScrollable) {
        leftArrow.style.display = "none";
        rightArrow.style.display = "none";
      } else {
        leftArrow.style.display = "";
        rightArrow.style.display = "";

        leftArrow.disabled = contentArea5.scrollLeft <= 5;
        rightArrow.disabled =
          contentArea5.scrollLeft + contentArea5.clientWidth >=
          contentArea5.scrollWidth - 5;
      }
    };

    updateButtonStates();
    contentArea5.addEventListener("scroll", updateButtonStates);

    leftArrow.addEventListener("click", () => {
      contentArea5.scrollBy({
        left: -scrollAmount,
        behavior: "smooth",
      });
    });

    rightArrow.addEventListener("click", () => {
      contentArea5.scrollBy({
        left: scrollAmount,
        behavior: "smooth",
      });
    });

    window.addEventListener("resize", updateButtonStates);
  }

  // --- Reveal cards on scroll --- //
  const investmentCards = document.querySelectorAll(".investment-card");

  if (investmentCards.length) {
    investmentCards.forEach((card, index) => {
      card.classList.add("is-reveal-init");
      card.style.setProperty("--reveal-delay", `${index * 0.12}s`);
    });

    if ("IntersectionObserver" in window) {
      const revealObserver = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            const card = entry.target;
            if (entry.isIntersecting) {
              card.classList.add("is-visible");
              card.classList.remove("is-reveal-init");
            } else {
              card.classList.remove("is-visible");
              card.classList.add("is-reveal-init");
            }
          });
        },
        { threshold: 0.2 }
      );

      investmentCards.forEach((card) => revealObserver.observe(card));
    } else {
      investmentCards.forEach((card) => {
        card.classList.add("is-visible");
        card.classList.remove("is-reveal-init");
      });
    }
  }

  // --- Reveal footer on scroll --- //
  const footer = document.querySelector(".site-footer");

  if (footer) {
    footer.classList.add("is-reveal-init");

    if ("IntersectionObserver" in window) {
      const footerObserver = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              footer.classList.add("is-visible");
              footer.classList.remove("is-reveal-init");
            } else {
              footer.classList.remove("is-visible");
              footer.classList.add("is-reveal-init");
            }
          });
        },
        { threshold: 0.15 }
      );

      footerObserver.observe(footer);
    } else {
      footer.classList.add("is-visible");
      footer.classList.remove("is-reveal-init");
    }
  }

  // --- Portfolio Search with API --- //
  if (currentPage === "portfolio" || currentPage === "portfolio-history") {
    const searchInput = document.getElementById("portfolio-search-input");
    const cardArea = document.querySelector(".investment-card-area");
    const emptyState = document.querySelector(".portfolio-empty");

    // portfolioType 결정: portfolio 페이지는 "P", portfolio-history 페이지는 "C"
    const portfolioType = currentPage === "portfolio" ? "P" : "C";

    const createCardElement = (portfolio, index) => {
      const card = document.createElement("a");
      card.className = "investment-card is-reveal-init";
      card.href = portfolio.portfolioDetailUrl || "#";
      card.dataset.name = portfolio.portfolioTitle || "";
      card.style.setProperty("--reveal-delay", `${index * 0.12}s`);

      const dateDiv = document.createElement("div");
      dateDiv.className = "card-date";
      dateDiv.textContent = portfolio.portfolioDate || "";

      const title = document.createElement("h3");
      title.className = "card-title";
      title.textContent = portfolio.portfolioTitle || "";

      const img = document.createElement("img");
      img.src = portfolio.portfolioImgUrl || "/images/default-portfolio.svg";
      img.alt = "";

      card.appendChild(dateDiv);
      card.appendChild(title);
      card.appendChild(img);

      return card;
    };

    const renderCards = (portfolios) => {
      if (!cardArea) return;

      // 기존 카드 모두 제거
      cardArea.innerHTML = "";

      if (!portfolios || portfolios.length === 0) {
        if (emptyState) {
          emptyState.hidden = false;
        }
        return;
      }

      if (emptyState) {
        emptyState.hidden = true;
      }

      // 새 카드 생성 및 추가
      portfolios.forEach((portfolio, index) => {
        const card = createCardElement(portfolio, index);
        cardArea.appendChild(card);

        // 애니메이션 효과 적용
        requestAnimationFrame(() => {
          card.classList.add("is-visible");
          card.classList.remove("is-reveal-init");
        });
      });
    };

    const searchPortfolios = async (keyword) => {
      try {
        const params = new URLSearchParams({
          portfolioType: portfolioType,
          searchType: "portfolioTitle",
          keyword: keyword || "",
        });

        const response = await fetch(`/v1/api/portfolio/list?${params}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // API 응답이 배열인지 확인하고, 배열이 아니면 적절히 추출
        let portfolios = data;
        if (!Array.isArray(data)) {
          // 응답이 객체인 경우 (예: {data: [...], total: 10})
          portfolios = data.data || data.list || data.portfolios || [];
        }

        renderCards(portfolios);
      } catch (error) {
        console.error("Portfolio search error:", error);
        if (emptyState) {
          emptyState.hidden = false;
        }
      }
    };

    const searchButton = document.querySelector(".portfolio-search__button");

    const handleSearch = () => {
      if (searchInput) {
        searchPortfolios(searchInput.value.trim());
      }
    };

    // 엔터키 이벤트
    if (searchInput) {
      searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
          e.preventDefault();
          handleSearch();
        }
      });
    }

    // 검색 버튼 클릭 이벤트
    if (searchButton) {
      searchButton.addEventListener("click", (e) => {
        e.preventDefault();
        handleSearch();
      });
    }
  }

  // --- Portfolio Detail HTML Editor & Reveal --- //
  if (currentPage === "portfolio-detail") {
    const htmlEditorIntro = document.querySelector(
      ".portfolio-detail__htmleditor-intro"
    );
    const detailContent = document.querySelector(".portfolio-detail__content");
    const htmlEditorTarget = document.querySelector(
      "[data-htmleditor-target]"
    );

    if (htmlEditorIntro) {
      htmlEditorIntro.innerHTML = `
        <p>
          이 프로필은 <strong>HTML Editor</strong>로 구성됩니다. 편집기에서 입력한
          스토리텔링, 시장 전망, 투자자 커뮤니케이션 등을 이 영역에 노출해 보세요.
        </p>
        <p>
          전략적 인사이트, 성과 요약, 미디어 자료 등 맞춤형 콘텐츠를 통해 이해관계자에게
          더 풍부한 정보를 제공할 수 있습니다.
        </p>
      `;
    }

    if (htmlEditorTarget) {
      const sampleHtmlEditorData = `
        <section class="htmleditor-block">
          <h2>Fund Narrative</h2>
          <p>
            Growth Fund는 혁신 기술과 지속 가능한 에너지 전환에 집중 투자하여
            장기적인 자본 성장을 추구합니다. 펀드 매니저는 분기마다 포트폴리오를
            재조정해 변동성을 제어하고, 핵심 섹터의 트렌드를 반영합니다.
          </p>
        </section>
        <section class="htmleditor-block">
          <h3>Strategy Highlights</h3>
          <ul>
            <li>글로벌 메가트렌드(클라우드, AI, 에너지 전환) 중심 섹터 배분</li>
            <li>ESG 프레임워크를 통한 리스크 관리와 기업 참여 확대</li>
            <li>주요 지역별 하이콘빅션 종목 25~35개 유지</li>
          </ul>
        </section>
        <section class="htmleditor-block">
          <h3>Latest Commentary</h3>
          <p>
            최근 분기에는 북미 AI 인프라 기업과 유럽 재생에너지 프로젝트에 대한
            익스포저를 확대했습니다. 향후 6개월 간 인플레이션 완화와 기술 수요 증가가
            실적에 긍정적으로 작용할 것으로 전망합니다.
          </p>
        </section>
      `;

      let editorMarkup = "";

      if (typeof window !== "undefined") {
        if (typeof window.htmlEditorData === "string") {
          editorMarkup = window.htmlEditorData.trim();
        } else if (Array.isArray(window.htmlEditorData)) {
          editorMarkup = window.htmlEditorData.join("").trim();
        }
      }

      htmlEditorTarget.innerHTML = editorMarkup || sampleHtmlEditorData;
    }

    if (detailContent) {
      const detailSections = Array.from(detailContent.children);

      detailSections.forEach((section, index) => {
        section.classList.add("is-reveal-init");
        section.style.setProperty("--reveal-delay", `${index * 0.12}s`);
      });

      if ("IntersectionObserver" in window) {
        const sectionObserver = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              const section = entry.target;
              if (entry.isIntersecting) {
                section.classList.add("is-visible");
                section.classList.remove("is-reveal-init");
              } else {
                section.classList.remove("is-visible");
                section.classList.add("is-reveal-init");
              }
            });
          },
          { threshold: 0.15 }
        );

        detailSections.forEach((section) => sectionObserver.observe(section));
      } else {
        detailSections.forEach((section) => {
          section.classList.add("is-visible");
          section.classList.remove("is-reveal-init");
        });
      }
    }
  }
});
