package com.mackenzie.waifuviewer.data.embed

import com.mackenzie.waifuviewer.domain.video.embed.EmbeddedVideoResolveResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class EmbeddedVideoHtmlParserTest {

    @Test
    fun `extrae mp4 desde video source`() {
        val html = """
            <html><head></head><body>
              <video controls>
                <source src=\"https://cdn.example.com/video/file.mp4\" type=\"video/mp4\"/>
              </video>
            </body></html>
        """.trimIndent()

        val parsed = EmbeddedVideoHtmlParser.parse(html, baseUrl = "https://example.com/embed")
        assertNotNull(parsed)
        assertEquals("https://cdn.example.com/video/file.mp4", parsed!!.mediaUrl)
        assertEquals(EmbeddedVideoResolveResult.MediaType.MP4, parsed.mediaType)
    }

    @Test
    fun `extrae hls desde og video meta`() {
        val html = """
            <html><head>
              <meta property=\"og:video\" content=\"https://stream.example.com/master.m3u8\" />
            </head><body></body></html>
        """.trimIndent()

        val parsed = EmbeddedVideoHtmlParser.parse(html, baseUrl = "https://example.com/embed")
        assertNotNull(parsed)
        assertEquals("https://stream.example.com/master.m3u8", parsed!!.mediaUrl)
        assertEquals(EmbeddedVideoResolveResult.MediaType.HLS, parsed.mediaType)
    }

    @Test
    fun `extrae url desde script inline`() {
        val html = """
            <html><head></head><body>
              <script>
                var player = { file: \"https://cdn.example.com/v/segment.mpd\" };
              </script>
            </body></html>
        """.trimIndent()

        val parsed = EmbeddedVideoHtmlParser.parse(html, baseUrl = "https://example.com/embed")
        assertNotNull(parsed)
        assertEquals("https://cdn.example.com/v/segment.mpd", parsed!!.mediaUrl)
        assertEquals(EmbeddedVideoResolveResult.MediaType.DASH, parsed.mediaType)
    }

    @Test
    fun `resuelve ruta relativa en script`() {
        val html = """
            <html><head></head><body>
              <script>
                window.config = { \"hls\": \"/hls/master.m3u8\" };
              </script>
            </body></html>
        """.trimIndent()

        val parsed = EmbeddedVideoHtmlParser.parse(html, baseUrl = "https://example.com/embed/page")
        assertNotNull(parsed)
        assertEquals("https://example.com/hls/master.m3u8", parsed!!.mediaUrl)
        assertEquals(EmbeddedVideoResolveResult.MediaType.HLS, parsed.mediaType)
    }
}
