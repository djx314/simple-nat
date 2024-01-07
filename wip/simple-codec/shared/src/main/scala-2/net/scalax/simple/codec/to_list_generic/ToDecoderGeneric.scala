package net.scalax.simple.codec
package to_list_generic

import shapeless._

trait ToDecoderGeneric[F[_[_]]] {
  def toHList[M1[_], M2[_]](monad: MonadAdd[M1])(input: F[({ type U1[X] = M1[M2[X]] })#U1]): M1[F[M2]]
}

object ToDecoderGeneric {
  type IdImpl[T] = T

  trait HListFuncMapGeneric[Source1, Target1, Target2, M1[_], M2[_]] {
    def output(monad: MonadAdd[M1])(o1: Target1): M1[Target2]
  }
  object HListFuncMapGeneric {
    implicit def implicit1[T1, Source1 <: HList, HL1 <: HList, HL2 <: HList, M1[_], M2[_]](implicit
      tail: HListFuncMapGeneric[Source1, HL1, HL2, M1, M2]
    ): HListFuncMapGeneric[T1 :: Source1, M1[M2[T1]] :: HL1, M2[T1] :: HL2, M1, M2] =
      new HListFuncMapGeneric[T1 :: Source1, M1[M2[T1]] :: HL1, M2[T1] :: HL2, M1, M2] {
        override def output(o: MonadAdd[M1])(o1: M1[M2[T1]] :: HL1): M1[M2[T1] :: HL2] = {
          val m1 = o.zip(tail.output(o)(o1.tail), o1.head)
          o.map(m1)(tuplei => tuplei._2 :: tuplei._1)
        }
      }
    implicit def implicit2[M1[_], M2[_]]: HListFuncMapGeneric[HNil, HNil, HNil, M1, M2] =
      new HListFuncMapGeneric[HNil, HNil, HNil, M1, M2] {
        override def output(o: MonadAdd[M1])(o1: HNil): M1[HNil] = o.map(o.unit)(_ => HNil)
      }
  }

  trait HListFuncMapGenericGen[Source1, M1[_], M2[_]] {
    def generic[Target1, Target2](implicit
      i: HListFuncMapGeneric[Source1, Target1, Target2, M1, M2]
    ): HListFuncMapGeneric[Source1, Target1, Target2, M1, M2] = i
  }
  object HListFuncMapGenericGen {
    def apply[Source1, M1[_], M2[_]]: HListFuncMapGenericGen[Source1, M1, M2] = new HListFuncMapGenericGen[Source1, M1, M2] {
      //
    }
  }

  // ===
  class SimpleFuncion1Impl[F[_[_]], M1[_], M2[_]] {
    self =>
    def derived2[Source1, Target1, Target2](
      simpleTo1: SimpleTo[F[IdImpl], Source1],
      simpleFrom2: SimpleFrom[F[M2], Target2],
      simpleTo3: SimpleTo[F[({ type U1[X] = M1[M2[X]] })#U1], Target1]
    ): FuncInnerApply1[F, M1, M2, Source1, Target1, Target2] =
      new FuncInnerApply1[F, M1, M2, Source1, Target1, Target2](simpleTo1, simpleFrom2, simpleTo3)
  }

  class FuncInnerApply1[F[_[_]], M1[_], M2[_], Source1, Target1, Target2](
    simpleTo1: SimpleTo[F[IdImpl], Source1],
    simpleFrom2: SimpleFrom[F[M2], Target2],
    simpleTo3: SimpleTo[F[({ type U1[X] = M1[M2[X]] })#U1], Target1]
  ) {
    def apply(
      genericFunc: HListFuncMapGenericGen[Source1, M1, M2] => HListFuncMapGeneric[Source1, Target1, Target2, M1, M2]
    ): MonadAdd[M1] => F[({ type U1[X] = M1[M2[X]] })#U1] => M1[F[M2]] = { monad => model =>
      monad.map(genericFunc(HListFuncMapGenericGen[Source1, M1, M2]).output(monad)(simpleTo3.to(model)))(simpleFrom2.from)
    }
  }

  trait Impl[F[_[_]]] extends ToDecoderGeneric[F] {
    def impl[M1[_], M2[_]]: SimpleFuncion1Impl[F, M1, M2] => (MonadAdd[M1] => F[({ type U1[X] = M1[M2[X]] })#U1] => M1[F[M2]])
    override def toHList[M1[_], M2[_]](monad: MonadAdd[M1])(input: F[({ type U1[X] = M1[M2[X]] })#U1]): M1[F[M2]] =
      impl[M1, M2](new SimpleFuncion1Impl[F, M1, M2])(monad)(input)
  }
}