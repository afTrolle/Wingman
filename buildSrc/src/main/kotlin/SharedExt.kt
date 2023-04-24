import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependency

fun PluginDependenciesSpec.aliasId(notation: Provider<PluginDependency>) {
    id(notation.get().pluginId)
}
