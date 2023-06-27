package App

import scalafx.animation.Timeline.*
import scalafx.animation.{AnimationTimer, KeyFrame, Timeline}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.beans.property.{BufferProperty, IntegerProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Rectangle2D
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.*
import scalafx.scene.shape.{Circle, Rectangle}
import scalafx.stage.Screen
import scalafx.util.Duration

import scala.collection.immutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Random

object Particles extends JFXApp3 {

  override def start(): Unit = {
    val numberOfParticles: Int = 1500
    val particleRadius: Int = 3 // rayon de chaque particule
    val screenBounds: Rectangle2D = Screen.primary.visualBounds
    val (boardWidth, boardHeight): (Int, Int) = (screenBounds.width.intValue, screenBounds.height.intValue)
    val particules: List[Particule] = generateParticule(numberOfParticles, particleRadius, boardWidth, boardHeight)
    val state: ObjectProperty[List[Particule]] = ObjectProperty(particules)
    stage = new PrimaryStage {
      title = "Particules"
      height = boardHeight
      width = boardWidth
      scene = new Scene{
        fill = White
        content = drawParticles(state.value)
        state.onChange { (content = drawParticles(state.value))
        }
      }
    }
    infiniteTimeline(state, boardWidth, boardHeight).play()
  }


  def generateParticule(n: Int, radius: Int, width: Int, height: Int): List[Particule] =
    List
      .fill(n) {
        val (x, y) = (Random.nextInt(width), Random.nextInt(height)) // position aléatoire
        val direction = Direction.random // direction aléatoire
        val color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 1) // couleur aléatoire

        Particule(x, y, radius, direction, color)
      }

  def drawParticles(particules: List[Particule]): List[Circle] = particules.map(_.draw)

  def infiniteTimeline(particles: ObjectProperty[List[Particule]], boardWidth: Int, boardHeight: Int): Timeline =
    new Timeline {
      keyFrames = List(KeyFrame(time = Duration(25), onFinished = _ => updateState(particles, boardWidth, boardHeight)))
      cycleCount = Indefinite
    }

  def updateState(state: ObjectProperty[List[Particule]], boardWidth: Int, boardHeight: Int): Unit = {
    val board = state.value.groupBy(p => (p.x, p.y))
    state.update(state.value.map(_.move(board, boardWidth, boardHeight)))
  }

}
