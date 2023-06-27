import scalafx.animation.{KeyFrame, Timeline}
import scalafx.animation.Timeline.Indefinite
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.Rectangle
import scalafx.util.Duration

object Particles extends JFXApp3 {

  override def start(): Unit = {
    stage = new PrimaryStage {
      width = 300
      height = 300
      scene = new Scene {
        fill = White
        content = new Rectangle {
          x = 100
          y = 100
          width = 50
          height = 50
          fill = Orange
        }
      }
    }
  }

  def generateParticles()

  def infiniteTimeline(particles: ObjectProperty[List[Particle]], boardWidth: Int, boardHeight: Int): Timeline =
    new Timeline {
      keyFrames = List(KeyFrame(time = Duration(25), onFinished = _ => updateState(particles, boardWidth, boardHeight)))
      cycleCount = Indefinite
    }
}
