package net.scalax.simple
package adt

import core.Core2

// Core2 实现自己有空改改，主要是 Function1 表达能力有限，最好这里的 def Core2 其实只是 Core2 的一个实例就可以表达
object CoreInstance {
  def Core2(func: (() => Core2) => Core2): Core2 = new Core2 {
    override def apply(v1: () => Core2): Core2 = func(v1)
  }

  // ===
  type AdtList = Core2
  val AdtListPositive: Core2 = Core2(tail => Core2(foldList => foldList.apply().apply(() => tail())))
  class AdtListZero extends Core2 {
    override def apply(otherFoldList: () => Core2): Core2 = otherFoldList()
  }
  lazy val AdtListException: Core2 = AdtListPositive(() => AdtListException)

  // ===
  type FoldList = Core2
  abstract class FoldListPositive extends Core2 {
    val tail: () => Core2
    override def apply(adtList: () => Core2): Core2 = adtList.apply().apply(() => tail())
  }

  private val FoldListZeroImpl: Core2 = Core2(tail => Core2(foldList => tail.apply().apply(() => foldList())))

  lazy val FoldListZero: Core2 = FoldListZeroImpl(() => FoldListZero)

}
