package com.turktv

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class Hdfilmcehennemi : MainAPI() {
    override var mainUrl              = "https://www.hdfilmcehennemi.nl"
    override var name                 = "Hdfilmcehennemi"
    override val hasMainPage          = true
    override var lang                 = "tr"
    override val hasQuickSearch       = true
    override val supportedTypes       = setOf(TvType.Movie)

    // Ana sayfa kategorilerimiz
    override val mainPage = mainPageOf(
        "${mainUrl}/film-izle/" to "Tüm Filmler",
        "${mainUrl}/kategori/aksiyon/" to "Aksiyon",
        "${mainUrl}/kategori/bilim-kurgu/" to "Bilim Kurgu",
        "${mainUrl}/kategori/korku/" to "Korku",
        "${mainUrl}/kategori/komedi/" to "Komedi"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        // Bu kısmı bir sonraki adımda sitenin HTML yapısına göre dolduracağız.
        // Şimdilik eklentinin hata vermeden derlenmesi için boş liste döndürüyoruz.
        return newHomePageResponse(request.name, emptyList())
    }
}
