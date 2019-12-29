package ru.itmo

package object fpiikt {

  case class Receipt(id: Int, payer: String, paid: Double, separatePaids: Seq[(String, Double)])
  case class Cost(total: Double = 0.0, mutual: Double = 0.0, separate: Double = 0.0)

  val Consumers = Map(
    "Azimboy" -> Cost(),
    "Ildar" -> Cost(),
    "Umid" -> Cost(),
    "Temur" -> Cost()
  )

}
