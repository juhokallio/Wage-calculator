/** A class for representing money
  *
  *
  */
case class Money(dollars: Int, cents: Int) {
  def *(that: Int): Money = {
    Money(dollars * that, cents * that)
  }
}
