package com.turktv

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class HdfilmcehennemiPlugin: Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Hdfilmcehennemi())
    }
}
