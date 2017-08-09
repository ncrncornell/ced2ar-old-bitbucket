package edu.ncrn.cornell.site.view.editor

import scala.language.postfixOps
import scalacss.DevDefaults._

  /**

  Adapted from https://github.com/amirkarimi/neptune

  MIT license follows:

  Copyright (c) 2017 Amir Karimi

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  */

object NeptuneStyles extends StyleSheet.Inline {
  import dsl._

  val neptuneActionbarColor = grey(0xFF)
  val neptuneBorderColor = rgba(10, 10, 10, 0.1)
  val neptuneBorderRadius = 5 px
  val neptuneBorderStyle = solid
  val neptuneBorderWidth = 1 px
  val neptuneBoxShadow = s"0 2px 3px ${neptuneBorderColor.value}, 0 0 0 1px ${neptuneBorderColor.value}"
  val neptuneButtonHeight = 30 px
  val neptuneButtonWidth = 30 px
  val neptuneContentHeight = 300 px
  val neptuneContentPadding = 10 px

  val neptune = style(
    borderRadius(neptuneBorderRadius),
    boxShadow := neptuneBoxShadow,
    boxSizing.borderBox,
    width(100 %%)
  )

  val neptuneContent = style(
    boxSizing.borderBox,
    height(neptuneContentHeight),
    outline.none,
    overflowY.auto,
    padding(neptuneContentPadding),
    width(100 %%)
  )

  val neptuneActionbar = style(
    backgroundColor(neptuneActionbarColor),
    borderBottom(neptuneBorderWidth, neptuneBorderStyle, neptuneBorderColor),
    borderTopLeftRadius(neptuneBorderRadius),
    borderTopRightRadius(neptuneBorderRadius),
    width(100 %%)
  )

  val neptuneButton = style(
    backgroundColor.transparent,
    border.none,
    cursor.pointer,
    height(neptuneButtonHeight),
    outline.none,
    width(neptuneButtonWidth)
  )
}
