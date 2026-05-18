package com.turktv

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class HdfilmcehennemiPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(Hdfilmcehennemi())
    }
}
