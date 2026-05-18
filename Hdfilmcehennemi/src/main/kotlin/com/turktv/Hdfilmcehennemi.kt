package com.turktv

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class Hdfilmcehennemi : MainAPI() {
    override var mainUrl              = "https://www.hdfilmcehennemi.nl"
    override var name                 = "Hdfilmcehennemi"
    override val hasMainPage          = true
    override var lang                 = "tr"
    override val hasQuickSearch       = true
    override val supportedTypes       = setOf(TvType.Movie)

    override val mainPage = mainPageOf(
        "${mainUrl}/film-izle/" to "Tüm Filmler",
        "${mainUrl}/kategori/aksiyon/" to "Aksiyon",
        "${mainUrl}/kategori/bilim-kurgu/" to "Bilim Kurgu",
        "${mainUrl}/kategori/korku/" to "Korku",
        "${mainUrl}/kategori/komedi/" to "Komedi"
    )

    // 1. ANA SAYFA VE KATEGORİLERİ ÇEKME
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        // Sayfa geçişleri için (Örn: hdfilmcehennemi.nl/film-izle/page/2/)
        val url = if (page == 1) request.data else "${request.data}page/$page/"
        val document = app.get(url).document
        
        // Sitedeki film kartlarını yakalıyoruz (Genellikle card, poster veya article sınıfında olurlar)
        val home = document.select("div.card, div.poster, article").mapNotNull {
            it.toSearchResult()
        }

        return newHomePageResponse(request.name, home)
    }

    // 2. ARAMA YAPMA
    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/arama/$query/").document
        return document.select("div.card, div.poster, article").mapNotNull {
            it.toSearchResult()
        }
    }

    // Film kartından verileri ayıklayan yardımcı fonksiyonumuz
    private fun Element.toSearchResult(): SearchResponse? {
        val a = this.selectFirst("a") ?: return null
        val href = fixUrl(a.attr("href"))
        val title = a.attr("title").ifEmpty { this.selectFirst("h2, h3, .title")?.text() } ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src") ?: this.selectFirst("img")?.attr("data-src"))

        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }

    // 3. FİLMİN DETAY SAYFASI (Afiş, Konu, Oyuncular)
    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title = document.selectFirst("h1")?.text() ?: return null
        val poster = fixUrlNull(document.selectFirst("div.poster img, div.card-body img")?.attr("src"))
        val plot = document.selectFirst("article p, div.summary, .ozet")?.text()
        val year = document.selectFirst("div.year, span.year")?.text()?.toIntOrNull()

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.plot = plot
            this.year = year
        }
    }

    // 4. VİDEO OYNATICILARI (LİNKLERİ) ÇEKME
    override suspend fun loadLinks(data: String, isCasting: Boolean, callback: (ExtractorLink) -> Unit, subtitleCallback: (SubtitleFile) -> Unit): Boolean {
        val document = app.get(data).document

        // Sitedeki iframe (video oynatıcı) linklerini buluyoruz
        document.select("iframe").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) {
                // Şimdilik linkleri doğrudan yükleyiciye gönderiyoruz.
                // Eğer iframe içinde m3u8 veya mp4 varsa Cloudstream otomatik algılayacaktır.
                loadExtractor(fixUrl(src), data, subtitleCallback, callback)
            }
        }
        return true
    }
}
