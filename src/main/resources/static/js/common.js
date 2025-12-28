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

    if (currentPage === "investment") {
      investmentCards.forEach((card) => {
        card.addEventListener("click", () => {
          window.location.href = "portfolio.html";
        });
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

  // --- Portfolio Search Filtering --- //
  if (currentPage === "portfolio") {
    const searchInput = document.getElementById("portfolio-search-input");
    const cards = Array.from(
      document.querySelectorAll(".investment-card[data-name]")
    );
    const emptyState = document.querySelector(".portfolio-empty");

    const normalize = (value) =>
      value
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .trim();

    const filterCards = () => {
      const keyword = normalize(searchInput.value);
      let visibleCount = 0;

      cards.forEach((card, index) => {
        const name = normalize(card.dataset.name || card.textContent || "");
        const isMatch = !keyword || name.includes(keyword);

        card.style.display = isMatch ? "" : "none";
        card.classList.remove("is-visible", "is-reveal-init");

        if (isMatch) {
          card.style.setProperty("--reveal-delay", `${visibleCount * 0.12}s`);
          requestAnimationFrame(() => {
            card.classList.add("is-visible");
          });
          visibleCount += 1;
        }
      });

      if (emptyState) {
        emptyState.hidden = visibleCount !== 0;
      }
    };

    if (searchInput) {
      searchInput.addEventListener("input", filterCards);
      filterCards();
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
