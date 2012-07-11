// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.agent

// Importer is too big a class to convert to Scala all at once, so
// we'll convert it at method at a time, as needed, by relocating
// methods from ImporterJ to here. - ST 7/11/12

import org.nlogo.api.{ AgentVariables, ImporterUser }

class Importer(_errorHandler: ImporterJ.ErrorHandler,
               _world: World,
               _importerUser: ImporterUser,
               _stringReader: ImporterJ.StringReader)
extends ImporterJ(_errorHandler, _world, _importerUser, _stringReader) {

  override def getImplicitVariables(agentClass: Class[_ <: Agent]): Array[String] =
    if (agentClass == classOf[Observer])
      AgentVariables.getImplicitObserverVariables.toArray
    else if (agentClass == classOf[Turtle])
      AgentVariables.getImplicitTurtleVariables(world.program.is3D).toArray
    else if (agentClass == classOf[Patch])
      AgentVariables.getImplicitPatchVariables(world.program.is3D).toArray
    else if (agentClass == classOf[Link])
      AgentVariables.getImplicitLinkVariables.toArray
    else
      throw new IllegalArgumentException(agentClass.toString)

  def getSpecialObserverVariables: Array[String] = {
    import ImporterJ._
    Array(
      MIN_PXCOR_HEADER, MAX_PXCOR_HEADER, MIN_PYCOR_HEADER, MAX_PYCOR_HEADER,
      SCREEN_EDGE_X_HEADER, SCREEN_EDGE_Y_HEADER,
      PERSPECTIVE_HEADER, SUBJECT_HEADER,
      NEXT_INDEX_HEADER, DIRECTED_LINKS_HEADER, TICKS_HEADER)
  }

  def getSpecialTurtleVariables: Array[String] = {
    val vars = AgentVariables.getImplicitTurtleVariables(false)
    Array(vars(Turtle.VAR_WHO), vars(Turtle.VAR_BREED),
          vars(Turtle.VAR_LABEL), vars(Turtle.VAR_SHAPE))
  }

  def getSpecialPatchVariables: Array[String] = {
    val vars = AgentVariables.getImplicitPatchVariables(false)
    Array(vars(Patch.VAR_PXCOR), vars(Patch.VAR_PYCOR),
          vars(Patch.VAR_PLABEL))
  }

  def getSpecialLinkVariables: Array[String] = {
    val vars = AgentVariables.getImplicitLinkVariables
    Array(vars(Link.VAR_BREED), vars(Link.VAR_LABEL),
          vars(Link.VAR_END1), vars(Link.VAR_END2))
  }

}
