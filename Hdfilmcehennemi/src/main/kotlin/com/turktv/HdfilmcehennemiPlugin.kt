package com.turktv

import android.content.Context
import com.lagradost.cloudstream3.plugins.*

@CloudstreamPlugin
class HdfilmcehennemiPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Hdfilmcehennemi())
    }
}
