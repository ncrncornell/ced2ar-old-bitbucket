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

    val glyphClicked: Var[Boolean] = Var(false)
    val glyphClass: Rx[String] = glyphClicked.map {
      case false => "glyphicon glyphicon-menu-right"
      case true => "glyphicon glyphicon-menu-down"
    }
    val showStyle: Rx[String] = glyphClicked.map {
      case false => "display: none;"
      case true => "display: block;"
    }

    val viewfield = {
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

    <div>
      <h3> {
        if (collapsibleFields.contains(fieldName))
          <a class={glyphClass}
             onclick={(ev: dom.Event) => {
               glyphClicked.update(click => !click)
             }}>
          </a>
          fieldName
        }
        <a class={editIconClass}
           onclick={(ev: dom.Event) => {
             editing.update(click => !click)
           }}>
        </a>
      </h3>
      {
        editing.map{
          case false => viewfield
          case true => editField(fieldName, fieldValues)
        }
      }
    </div>

  }

  def editField(
    fieldName: String,
    fieldValues: List[String]
  ): Node = {

    // TODO: only supports one field value currently
    val varEditor = Editor.editor(Settings(), fieldValues.head)
    <div>
      <h2>
        {fieldName}
      </h2>{varEditor.view()}<h3>XML Output</h3>{varEditor.model()}
    </div>
  }
}
