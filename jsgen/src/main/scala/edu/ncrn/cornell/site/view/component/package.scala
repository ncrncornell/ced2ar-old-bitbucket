package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.component.CodebookList._
import mhtml.Rx

import scala.xml.Node

package object component {

  sealed abstract class AbstractComponent[D](view: Node, model: Rx[D])
  final case class Component[D](view: Node, model: Rx[D]) extends AbstractComponent[D](view, model)

  /**
    * TaggedComponent is useful for updating a collection of components where one would want to
    * sometimes alter Component[D] based on input data of type T
    */
  final case class TaggedComponent[D,T](view: Node, model: Rx[D], tag: T) extends AbstractComponent[D](view, model)

  type Codebook = TaggedComponent[CodebookDetails, String]
  type CodebookList = Component[CodebookNameMap]
  //
  type Variable = TaggedComponent[VarDetails, String]

}
