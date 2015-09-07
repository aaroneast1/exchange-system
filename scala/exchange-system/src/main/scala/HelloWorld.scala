import java.text.DateFormat._
import java.util.{Date, Locale}

object FrenchDate {
  def main(args: Array[String]) {
    val now = new Date
    val df = getDateInstance(LONG, Locale.FRANCE)
    println(df format now)
  }
}

object HelloWorld {
  def main(args: Array[String]) {
    println("Hello, world!")
  }
}
