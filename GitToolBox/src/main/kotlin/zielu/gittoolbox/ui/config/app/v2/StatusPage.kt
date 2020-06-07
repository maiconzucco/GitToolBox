package zielu.gittoolbox.ui.config.app.v2

import com.intellij.openapi.observable.properties.AtomicLazyProperty
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.layout.panel
import zielu.gittoolbox.ResBundle
import zielu.gittoolbox.config.GitToolBoxConfig2
import zielu.gittoolbox.config.ReferencePointForStatusType
import zielu.gittoolbox.ui.config.ReferencePointForStatusTypeRenderer
import zielu.intellij.ui.GtFormUiEx
import javax.swing.JComponent

internal class StatusPage : GtFormUiEx<GitToolBoxConfig2> {
  private val referencePointForStatus = AtomicLazyProperty {
    ReferencePointForStatusType.AUTOMATIC
  }
  private val referencePointName = AtomicLazyProperty {
    ""
  }
  private lateinit var panel: DialogPanel

  override fun init() {
    panel = panel {
      row {
        label(ResBundle.message("configurable.prj.parentBranch.label"))
        cell {
          comboBox(
            CollectionComboBoxModel(ReferencePointForStatusType.allValues()),
            referencePointForStatus::get,
            { referencePointForStatus.set(it!!) },
            ReferencePointForStatusTypeRenderer()
          )
          textField(
            referencePointName::get,
            { referencePointName.set(it) }
          )
        }
      }
    }
  }

  override fun fillFromState(state: GitToolBoxConfig2) {
    referencePointForStatus.set(state.referencePointForStatus.type)
    referencePointName.set(state.referencePointForStatus.name)
  }

  override fun isModified(): Boolean {
    return panel.isModified()
  }

  override fun getContent(): JComponent {
    return panel
  }

  override fun afterStateSet() {
    panel.reset()
  }

  override fun applyToState(state: GitToolBoxConfig2) {
    panel.apply()
    // TODO: apply to state
  }
}
