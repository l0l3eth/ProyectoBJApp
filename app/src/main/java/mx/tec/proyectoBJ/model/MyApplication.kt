package mx.tec.proyectoBJ.model

import android.app.Application
import android.content.Context // <-- Importa android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import coil3.svg.SvgDecoder

class MyApplication: Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {

                add(SvgDecoder.Factory())
            }
            .crossfade(true) // Por ejemplo, un bonito efecto de fundido
            .build()
    }
}
