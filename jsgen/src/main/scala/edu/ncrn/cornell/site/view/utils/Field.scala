package edu.ncrn.cornell.site.view.utils

import mhtml.{Rx, Var}
import org.scalajs.dom

import scala.xml.{Group, Node, Text}

object Field {
  def renderField(
    fieldName: String,
    fieldValues: List[String],
    collapsibleFields: Set[String]
  ): Node = {
    val glyphClicked: Var[Boolean] = Var(false)
    val glyphClass: Rx[String] = glyphClicked.map {
      case false => "glyphicon glyphicon-menu-right"
      case true => "glyphicon glyphicon-menu-down"
    }
    val showStyle: Rx[String] = glyphClicked.map {
      case false => "display: none;"
      case true  => "display: block;"
    }

    if (collapsibleFields.contains(fieldName))
      <div>
        <h3>
          <a class={ glyphClass }
             onclick={ (ev: dom.Event) => {
               glyphClicked.update(click => !click) }
             } >
            {fieldName}
          </a>

          <div id={s"$fieldName-detail"} style={ showStyle }>
            <p>{ fieldValues.map(fv => Group(Seq(Text(fv), <br />))) }</p>
          </div>

        </h3>
      </div>
    else
      <div>
        <h3>
          {fieldName}
        </h3>
        <p>
          {fieldValues.mkString("\n")}
        </p>
      </div>
  }
}
