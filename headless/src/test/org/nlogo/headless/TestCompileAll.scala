// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.headless

import org.nlogo.api.Version
import org.nlogo.workspace.ModelsLibrary
import org.scalatest.FunSuite
import org.nlogo.util.SlowTest

class TestCompileAll extends FunSuite with SlowTest {

  for (path <- ModelsLibrary.getModelPaths)
    test("compile: " + path) {
      compile(path)
    }

  def compile(path: String) {
    import java.io.File.separatorChar
    def pathMatches(bad: String) =
      path.toUpperCase.containsSlice(separatorChar + bad + separatorChar)
    if (!Version.is3D && path.endsWith(".nlogo3d") ||
        // in 3D skip models that aren't in the 3D directory.
        Version.is3D && !pathMatches("3D"))
      return
    val workspace = HeadlessWorkspace.newInstance
    // compilerTestingMode keeps patches from being created, which we don't need (and which was
    // slowing things down), and has some other effects too - ST 1/13/05, 12/6/07
    workspace.compilerTestingMode = true
    try {
      workspace.open(path)
      val lab = HeadlessWorkspace.newLab
      lab.load(HeadlessModelOpener.protocolSection(path))
      lab.names.foreach(lab.newWorker(_).compile(workspace))
    }
    finally workspace.dispose()
  }

}
