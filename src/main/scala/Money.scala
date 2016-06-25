/** A class for representing money
  *
  *
  */
case class Money(dollars: Int, cents: Int) {
  def *(x: Int): Money = {
    Money(dollars * x, cents * x)
  }

  def +(that: Money): Money = {
    Money(dollars + that.dollars, cents * that.cents)
  }
}
