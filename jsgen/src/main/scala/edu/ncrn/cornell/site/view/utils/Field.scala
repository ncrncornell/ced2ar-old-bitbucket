package edu.ncrn.cornell.site.view.utils

import edu.ncrn.cornell.site.view.component.editor.Editor
import edu.ncrn.cornell.site.view.component.editor.Editor.Settings
import edu.ncrn.cornell.site.view.utils.Utils._
import mhtml.{Rx, Var}
import org.scalajs.dom

import scala.xml.{Group, Node, Text}


/**
  * Houses some intermediate function used by certain components, but also makes
  * use of other, more fundamental components, like Editor.
  */
object Field {

  def renderField(
    fieldName: String,
    fieldValues: List[String],
    collapsibleFields: Set[String]
  ): Node = {

    val editing: Var[Boolean] = Var(false)
    val editIconClass: Rx[String] = editing.map {
      case false => "glyphicon glyphicon-pencil"
      case true => "glyphicon glyphicon-minus"
    }

    val showClicked: Var[Boolean] = Var(false)
    val showClass: Rx[String] = showClicked.map {
      case false => "glyphicon glyphicon-menu-right"
      case true => "glyphicon glyphicon-menu-down"
    }
    val showStyle: Rx[String] = showClicked.map {
      case false => "display: none;"
      case true => "display: block;"
    }

    val viewField = {
      if (collapsibleFields.contains(fieldName))
        <div id={s"$fieldName-detail"} style={showStyle}>
          <p>
            {fieldValues.map(fv => Group(Seq(Text(fv), <br/>)))}
          </p>
        </div>
      else
        <div>
          <p>
            {fieldValues.mkString("\n")}
          </p>
        </div>
    }

    //FIXME: prevent field edits from being lost when "minimized" (switched to viewField)
    val editField = makeFieldEditor(fieldName, fieldValues)

    <div>
      <h3>
        {
          val showGlyph =
            if (collapsibleFields.contains(fieldName)) {
              Some(<a class={showClass}
                onclick={(ev: dom.Event) => {
                  showClicked.update(click => !click)
                }}>
              </a>)
            }
            else None
          <span>{ showGlyph} { fieldName }</span>
        }
        <a class={editIconClass}
           onclick={(ev: dom.Event) => {
             editing.update(click => !click)
           }}>
        </a>
      </h3>
      {
        editing.map{
          case false => viewField
          case true => editField
        }
      }
    </div>

  }

  def makeFieldEditor(
    fieldName: String,
    fieldValues: List[String]
  ): Node = {

    // TODO: only supports one field value currently
    // TODO: Ideally, we will solves the "component of components" problem before tackling this,
    // TODO: so that we can have multiple subfield components within one editor, with the edit bar
    // TODO: being shared among all subfield components

    val varEditor = Editor.editor(Settings(), fieldValues.head)
    <div>
      { varEditor.view() }
      <h3>XML Output</h3>
      { varEditor.model() }
    </div>
  }
}
