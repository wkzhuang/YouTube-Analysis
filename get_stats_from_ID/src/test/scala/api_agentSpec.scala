package api_access


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.net.MalformedURLException
import scala.util.Success

class api_agentSpec extends AnyFlatSpec with Matchers {
  val goodURL = "https://www.youtube.com/watch?v=ecekSCX3B4Q"
  val badURL = "x//recreations.northeastern.edu/"
  val malformedURL = "https://www.youtube.com/feed/subscriptions/"

  val validator = urlValidator()

  behavior of "validateURL"
  it should "work for valid url" in {
    validator.validateURL(goodURL) shouldBe Success("https://www.youtube.com/watch?v=ecekSCX3B4Q")
  }
  it should "not succeed for invalid url" in{
    a[MalformedURLException] should be thrownBy validator.validateURL(badURL)
  }

  behavior of "extract_vid_from_link"
  it should "extract video id from the given validated link" in {
    validator.extract_vid_from_link(goodURL) shouldBe Some("ecekSCX3B4Q")
  }
  it should "Report error for malformed links" in {
    validator.extract_vid_from_link(malformedURL) shouldBe None
  }
}
