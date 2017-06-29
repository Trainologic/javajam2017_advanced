package com.tl.spark
import scala.collection.immutable

object Live extends App{
  class Foo(val bla: Int) {
    def bar(num: Int = 0) = {
      println(num)
    }
  }
  val f  = new  Foo(123)
  val f2 = Foo2(123)
  f.bla
  f2.bla
  f.bar()
  f.bar(3)
  val s: Seq[Int] = Seq(1,2,3,4)
  val x: immutable.Seq[Int] = 1 to 10
//  s.map()

}

case class Foo2(bla: Int)