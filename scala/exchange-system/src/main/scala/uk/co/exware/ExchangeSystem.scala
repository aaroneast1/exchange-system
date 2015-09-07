package uk.co.exware

import uk.co.exware.ExchangeSystem.Direction.Direction

object ExchangeSystem {

  object Direction extends Enumeration {
    type Direction = Value
    val BUY, SELL = Value
  }

  case class User(userId:String){
    require(userId != null, "Must supply a valid userId")
  }

  case class Instrument(instrumentId:String){
    require(instrumentId != null, "Must supply a valid instrumentId")
  }

  case class Order(direction: Direction,
                   instrument: Instrument,
                   quantity: Long,
                   unitPrice:BigDecimal,
                   user: User){
    require(direction != null, "Must supply a valid direction")
    require(instrument != null, "Must supply a valid instrument")
    require(Option(quantity) != None, "Must supply a valid quantity")
    require(quantity >= 0, "Must supply a quantity greater than zero")
    require(unitPrice != null, "Must supply a valid unitPrice")
    require(unitPrice >= 0, "Must supply a unitPrice greater than zero")
    require(user != null, "Must supply a valid user")
  }

  def addOrder(order:Order) :Unit = {

  }

}
