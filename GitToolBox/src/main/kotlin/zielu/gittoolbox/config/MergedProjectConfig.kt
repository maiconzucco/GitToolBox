package zielu.gittoolbox.config

internal class MergedProjectConfig(
  private val appConfig: GitToolBoxConfig2,
  private val projectConfig: GitToolBoxConfigPrj
) {

  fun autoFetchEnabled(): Boolean {
    return if (projectConfig.autoFetchEnabledOverride.enabled) {
      projectConfig.autoFetchEnabledOverride.value
    } else {
      appConfig.autoFetchEnabled
    }
  }

  fun autoFetchIntervalMinutes(): Int {
    return if (projectConfig.autoFetchIntervalMinutesOverride.enabled) {
      projectConfig.autoFetchIntervalMinutesOverride.value
    } else {
      return appConfig.autoFetchIntervalMinutes
    }
  }
}
