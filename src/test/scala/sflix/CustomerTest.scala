package sflix

import org.scalatest.{FunSuite, Matchers}

// TODO save the expected outputs into files [1pt]
class CustomerTest extends FunSuite with Matchers {

  // TODO refactor tests so there is no code duplicate [1pt] ======= DONE
  val movieService = new MovieService(getClass.getResource("/movies.xml").getFile)
  val customers = Customer.load(getClass.getResource("/customers.xml").getFile)
  ServiceLocator.movieService = movieService

  test("Customer 1 statement") {
    val statement = customers(0).statement(false)
    Formatter.getXml(statement,true) shouldBe
      """Streaming report for *Alice Wonderland* (1)
        |- The Nightmare Before Christmas (4K) 2.0 CZK
        |
        |Streamings: 1
        |Movies: 1
        |Total: *2.0* CZK
        |Points: 2
        |""".stripMargin
  }

  // TODO fix the test ========= DONE
  test("Customer 2 statement") {
    val statement = customers(1).statement(true)
    Formatter.getXml(statement, false) shouldBe
      """Streaming report for John Doe (2)
        |- Irishman 3.0 CZK
        |- Irishman (HD) 4.5 CZK
        |- Taxi Driver (4K) 3.0 CZK
        |
        |Streamings: 3
        |Movies: 2
        |Total: 10.5 CZK
        |Points: 12
        |""".stripMargin
  }

  // TODO create a test that asserts correct price computation [1pt]
  // TODO create a test that asserts correct loyalty points computation [1pt]
  test("loyaltyPointTest"){
    val points = customers(0).calc_loyaltyPoints(2, true)

    assert(points == 9) //!!!!!! NEVIM KOLIK TO MA VYJIT NEMEL JSEM CAS TO ZJISTIT
  }
  // TODO create a test that asserts an HTML report [3pt]
}
