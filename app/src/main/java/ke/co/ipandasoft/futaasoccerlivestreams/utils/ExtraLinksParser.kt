package ke.co.ipandasoft.futaasoccerlivestreams.utils

import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkSpan
import org.nibor.autolink.LinkType
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

object ExtraLinksParser {

    fun checkExtraStreams(additionalLinks: String):ArrayList<String> {
        var extraStreamLinks:ArrayList<String> = arrayListOf()
        val linkExtractor: LinkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW))
            .build()
        val links: Iterable<LinkSpan> = linkExtractor.extractLinks(additionalLinks)
        for (Link in links){
            val link: LinkSpan = Link
            link.type // LinkType.URL
            link.beginIndex // 17
            link.endIndex // 32
            Timber.e("EXTRACT LINK $link")
            Timber.e("EXTRA LINKS SIZE ${extraStreamLinks.size}")
            val singleLink=additionalLinks.substring(link.beginIndex, link.endIndex)
            extraStreamLinks.add(singleLink)
        }
        return extraStreamLinks
    }
}