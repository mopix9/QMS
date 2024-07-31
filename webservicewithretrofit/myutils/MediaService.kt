/*
package com.pankti.webservicewithretrofit.myutils

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LifecycleOwner
import com.pankti.webservicewithretrofit.domain.entities.NetworkDataViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val viewModel: NetworkDataViewModel
) {
    private var mediaPlayer: MediaPlayer? = null

    init {
        startMedia()
    }

    private fun startMedia() {
        viewModel.soundData.observe(context as LifecycleOwner) { sound ->
            sound?.sound?.let { url ->
                if (url.isNotBlank()) {
                    val partOfInterest = extractPartOfInterest(url)
                    if (partOfInterest != viewModel.previousPartOfInterest) {
                        viewModel.previousPartOfInterest = partOfInterest
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(url)
                            prepareAsync()
                            setOnPreparedListener { mp -> mp.start() }
                            setOnErrorListener { mp, _, _ ->
                                mp.reset()
                                true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun extractPartOfInterest(url: String): String {
        return url.split("/").getOrNull(4) ?: ""
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
*/
