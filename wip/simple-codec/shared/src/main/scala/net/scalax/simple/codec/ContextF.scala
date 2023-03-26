package net.scalax.simple
package codec

trait ContextO[Model[_[_]]] {
  type AnyF       = Model[ContextI#AnyF]
  type NoneF      = Model[ContextI#NoneF]
  type IdF        = Model[ContextI#IdF]
  type StringF    = Model[ContextI#StringF]
  type SimpleVarF = Model[ContextI#SimpleVarF]
  type OptF       = Model[ContextI#OptF]
  type TagF       = Model[ContextI#Tag]
  type IntegerF   = Model[ContextI#Integer]
}

trait ContextI {
  type AnyF[_]       = Any
  type NoneF[_]      = None.type
  type IdF[T]        = T
  type StringF[T]    = String
  type SimpleVarF[T] = SimpleVar[T]
  type OptF[T]       = Option[T]
  type Tag[T]        = EmptyTag[T]
  type Integer[T]    = Int
}
