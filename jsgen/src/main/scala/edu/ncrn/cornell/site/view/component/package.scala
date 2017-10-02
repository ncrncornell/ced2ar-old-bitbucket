package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.component.Codebook.CodebookDetails
import mhtml.Rx

import scala.xml.Node

package object component {

  sealed abstract class AbstractComponent[D](view: Node, model: Rx[D])
  case class Component[D](view: Node, model: Rx[D]) extends AbstractComponent[D](view, model)

  /**
    * TaggedComponent is useful for updating a collection of components where one would want to
    * sometimes alter Component[D] based on input data of type T
    */
  case class TaggedComponent[D,T](view: Node, model: Rx[D], tag: T) extends AbstractComponent[D](view, model)

  type Codebook = TaggedComponent[CodebookDetails, String]

}
