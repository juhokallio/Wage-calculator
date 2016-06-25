/**
  * Created by juho on 6/24/16.
  */
case class Money(dollars: Int, cents: Int) {
  def *(that: Int): Money = {
    Money(dollars * that, cents * that)
  }
}
