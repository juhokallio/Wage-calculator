package juho.wagecalculator

/** A class for representing money
  *
  * Everything is kept as integers to avoid numerical problems. Only three operations are allowed for calculations:
  * multiply, plus and divideBy4. Everything is immutable. Money itself is handled as units, which are kept small enough
  * to avoid ever needing fractions. Because of the limited operations, only thing that could cause fractions is
  * divideBy4. Here fractions are avoided by simply keeping count of the number of divisions, which is in fact the
  * quarterDivisions parameter.
  */
case class Money private (units: Int, quarterDivisions: Int) {

  /** Multiplies money with an integer
    *
    * @param x  The multiplier
    * @return   New Money object
    */
  def *(x: Int): Money = {
    Money(units * x, quarterDivisions)
  }

  /** Sums two Money objects together, forming a new one
    *
    * As units may be of different size depending on the amount of quarter divisions, this is in practice a sum of two
    * fractions. Sometimes it could be possible to find a smaller common divider, i.e. find a smaller quarterDivision
    * than either of the former objects had, however just defaulting to the higher quarterDivision value is simpler.
    *
    * @param that The second money to be multiplied
    * @return     New Money object
    */
  def +(that: Money): Money = {
    val newUnits: Int = {
      if (quarterDivisions > that.quarterDivisions)
        units + math.pow(4, quarterDivisions - that.quarterDivisions).toInt * that.units
      else
        math.pow(4, that.quarterDivisions - quarterDivisions).toInt * units + that.units
    }

    Money(newUnits, math.max(quarterDivisions, that.quarterDivisions))
  }

  /** Divides money by four
    *
    * This is needed for calculating salary by 15 minutes and finding out 25% and 50% extra wages. As this is the only
    * fraction causing calculation, floats where chosen to be avoided and quarterDivision variable to be had instead.
    * Division is nothing else but increasing this parameter.
    *
    * @return New Money object
    */
  def divideBy4(): Money = {
    Money(units, quarterDivisions + 1)
  }

  /** Forms the string representation of money. This should be the only needed form.
    *
    * Examples of the output:
    * $1.00
    * $0.01
    *
    * @return Money in string form
    */
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

  /** The method for creating Money objects from other places
    *
    * @param dollars  Number of dollars in integer
    * @param cents    Number of cents in integer
    * @return         New Money object
    */
  def fromCurrency(dollars: Int, cents: Int): Money = Money(dollars * 100 + cents, 0)
}
