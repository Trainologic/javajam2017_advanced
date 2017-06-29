package com.tl.spark

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.io.{Directory, File}

object Helpers {

  def createFiles(folder: Directory)(implicit ec: ExecutionContext) = Future {
      Thread.sleep(2000)
      (1 to 200).sliding(10, 10).foreach { nums =>
        Thread.sleep(250)
        val first = nums.headOption.getOrElse(1)
        File(folder / s"$first.txt").writeAll(nums.mkString("\n"))
      }
    }

  implicit class RichBool(val bool: Boolean) extends AnyVal {
    def toInt: Int = if (bool) 1 else 0
  }


}
