package zielu.gittoolbox.config

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Transient
import zielu.gittoolbox.config.override.BoolValueOverride
import zielu.gittoolbox.config.override.ConfigItemOverride
import zielu.gittoolbox.config.override.IntValueOverride
import zielu.gittoolbox.config.override.ListValueOverride
import zielu.gittoolbox.config.override.StringValueOverride
import zielu.gittoolbox.fetch.AutoFetchParams
import zielu.gittoolbox.formatter.Formatter

@State(name = "GitToolBoxProjectSettings", storages = [Storage("git_toolbox_prj.xml")])
internal data class GitToolBoxConfigPrj(
  @Deprecated("Since 201.4.0") var autoFetch: Boolean = true,
  @Deprecated("Since 201.4.0") var autoFetchIntervalMinutes: Int = AutoFetchParams.DEFAULT_INTERVAL_MINUTES,
  @Deprecated("Since 192.3.1") var autoFetchExclusions: List<String> = ArrayList(),
  @Deprecated("Since 201.4.0") var autoFetchExclusionConfigs: List<AutoFetchExclusionConfig> = ArrayList(),
  @Deprecated("Since 201.4.0") var autoFetchOnBranchSwitch: Boolean = true,
  @Deprecated("Since 201.4.0") var commitDialogCompletion: Boolean = true,
  @Deprecated("Since 201.4.0") var completionConfigs: List<CommitCompletionConfig> =
    arrayListOf(CommitCompletionConfig()),
  @Deprecated("Since 201.4.0") var referencePointForStatus: ReferencePointForStatusConfig =
    ReferencePointForStatusConfig(),
  @Deprecated("Since 201.4.0") var commitMessageValidation: Boolean = false,
  @Deprecated("Since 201.4.0") var commitMessageValidationRegex: String =
    "(?:fix|chore|docs|feat|refactor|style|test)(?:\\(.*\\))?: [A-Z].*\\s#\\d+",
  var autoFetchEnabledOverride: BoolValueOverride = BoolValueOverride(),
  var autoFetchIntervalMinutesOverride: IntValueOverride =
    IntValueOverride(false, AutoFetchParams.DEFAULT_INTERVAL_MINUTES),
  var autoFetchOnBranchSwitchOverride: BoolValueOverride = BoolValueOverride(),
  var autoFetchExclusionConfigsOverride: ListValueOverride<AutoFetchExclusionConfig> = ListValueOverride(),
  var commitDialogCompletionOverride: BoolValueOverride = BoolValueOverride(),
  var completionConfigsOverride: ListValueOverride<CommitCompletionConfig> = ListValueOverride(),
  var referencePointForStatusOverride: ConfigItemOverride<ReferencePointForStatusConfig> =
    ConfigItemOverride(false, ReferencePointForStatusConfig()),
  var commitMessageValidationOverride: BoolValueOverride = BoolValueOverride(),
  var commitMessageValidationRegexOverride: StringValueOverride = StringValueOverride()
) : PersistentStateComponent<GitToolBoxConfigPrj> {

  @Transient
  fun copy(): GitToolBoxConfigPrj {
    return GitToolBoxConfigPrj(
      autoFetch,
      autoFetchIntervalMinutes,
      autoFetchExclusions,
      autoFetchExclusionConfigs.map { it.copy() },
      autoFetchOnBranchSwitch,
      commitDialogCompletion,
      completionConfigs.map { it.copy() },
      referencePointForStatus.copy(),
      commitMessageValidation,
      commitMessageValidationRegex,
      autoFetchEnabledOverride.copy(),
      autoFetchIntervalMinutesOverride.copy(),
      autoFetchOnBranchSwitchOverride.copy(),
      autoFetchExclusionConfigsOverride.copy(),
      commitDialogCompletionOverride.copy(),
      completionConfigsOverride.copy(),
      referencePointForStatusOverride.copy(),
      commitMessageValidationOverride.copy(),
      commitMessageValidationRegexOverride.copy()
    )
  }

  @Transient
  fun getCompletionFormatters(): List<Formatter> {
    return completionConfigs.map { it.createFormatter() }
  }

  fun isReferencePointForStatusChanged(other: GitToolBoxConfigPrj): Boolean {
    return referencePointForStatus != other.referencePointForStatus
  }

  fun fireChanged(project: Project, previous: GitToolBoxConfigPrj) {
    project.messageBus.syncPublisher(ProjectConfigNotifier.CONFIG_TOPIC).configChanged(previous, this)
  }

  override fun getState(): GitToolBoxConfigPrj? {
    return this
  }

  override fun loadState(state: GitToolBoxConfigPrj) {
    XmlSerializerUtil.copyBean(state, this)
  }
}
