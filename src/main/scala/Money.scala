/** A class for representing money
  *
  *
  */
case class Money private (units: Int, quarterDivisions: Int) {

  def *(x: Int): Money = {
    Money(units * x, quarterDivisions)
  }

  def +(that: Money): Money = {
    Money(units + that.units, quarterDivisions)
  }

  def divideBy4(): Money = {
    Money(units, quarterDivisions + 1)
  }

  override def toString: String = {
    val divider: Int = math.pow(4, quarterDivisions).toInt
    val dollars: Int = units / (100 * divider)
    val remainingUnits: Int = units % (100 * divider)

    val centsStr: String = {
      val succesfulRounding: Boolean = remainingUnits % divider * 2 < divider
      val cents: Int = if (succesfulRounding) remainingUnits / divider else 1 + remainingUnits / divider
      if (cents > 9) cents.toString else "0" + cents
    }

    "$" + dollars + "." + centsStr
  }
}

object Money {
  def fromCurrency(dollars: Int, cents: Int): Money = Money(dollars * 100 + cents, 0)
}
