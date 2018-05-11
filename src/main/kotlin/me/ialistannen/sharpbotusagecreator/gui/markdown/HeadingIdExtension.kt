package me.ialistannen.sharpbotusagecreator.gui.markdown

import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Node
import com.vladsch.flexmark.html.AttributeProvider
import com.vladsch.flexmark.html.AttributeProviderFactory
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory
import com.vladsch.flexmark.html.renderer.AttributablePart
import com.vladsch.flexmark.html.renderer.LinkResolverContext
import com.vladsch.flexmark.util.html.Attributes
import com.vladsch.flexmark.util.options.MutableDataHolder


class HeadingIdExtension : HtmlRenderer.HtmlRendererExtension {
    override fun rendererOptions(options: MutableDataHolder?) {

    }

    override fun extend(rendererBuilder: HtmlRenderer.Builder, rendererType: String) {
        rendererBuilder.attributeProviderFactory(HeadIdAttributeProvider.factory())
    }
}

class HeadIdAttributeProvider : AttributeProvider {

    companion object {

        /**
         * Creates an [AttributeProviderFactory] to create this provider.
         *
         * @return a factory to create this provider
         */
        @JvmStatic
        fun factory(): AttributeProviderFactory {
            return object : IndependentAttributeProviderFactory() {
                override fun create(context: LinkResolverContext?): AttributeProvider {
                    return HeadIdAttributeProvider()
                }
            }
        }
    }

    override fun setAttributes(node: Node, part: AttributablePart, attributes: Attributes) {
        if (node is Heading && node.level <= 2) {
            val id = node.text
                    .toLowerCase()
                    .replace(Regex("[^a-zA-Z0-9]"), "_")
            attributes.replaceValue("id", id)
        }
    }
}