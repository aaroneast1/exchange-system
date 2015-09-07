package uk.co.exware

import uk.co.exware.ExchangeSystem.{User, Instrument, Order, Direction}
import org.scalatest._

class ExchangeSystemSpec extends FlatSpec with Matchers {

  val exchangeSystem = ExchangeSystem


  "An Order" should "create successfully with valid arguments" in {

      Order(Direction.BUY, Instrument("RDSA"), 100L, BigDecimal("1606.86"),User("userId")) should not be Option.empty
  }


}
