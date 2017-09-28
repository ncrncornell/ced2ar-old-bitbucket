package edu.ncrn.cornell.site.view.components

import edu.ncrn.cornell.site.view.component.Component
import mhtml.Rx

object About {
  def apply(): Component[Unit] = {
    val view = <h1>Just a test</h1>
    Component(view, Rx(()))
  }



}
