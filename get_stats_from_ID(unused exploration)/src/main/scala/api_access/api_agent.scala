package api_access

import com.google.api.client.http.{HttpRequest, HttpRequestInitializer}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.{YouTube, YouTubeRequestInitializer}
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.util.{Failure, Success, Try}
// import com.google.api.client.json.jackson2.JacksonFactory


object api_agent extends App{

  // Following the example here. One issue is JacksonFactory is deprecated but still usable
  // and it's included in the official YouTube API example
  // if using Gson, then the list part needs to be a java.util.List<String>
  // https://www.webkj.com/scala/scala-youtube-data-api-examples


  println("Enter YouTube video link: ")
  val input_url = scala.io.StdIn.readLine()
  // println(s"my link is : $input_url")
  // https://www.youtube.com/watch?v=is-rjsfoYtY

  val validator = urlValidator()
  def video_id:String = {
    validator.validateURL(input_url) match {
      case Success(u) =>
        validator.extract_vid_from_link(u) match {
          case Some(uid) => uid
        }
    }
  }
  // println(video_id)
  val transport = new NetHttpTransport()
  val factory = new GsonFactory()
  val httpRequestInit = new HttpRequestInitializer {
    override def initialize(re: HttpRequest): Unit = {}
  }
  val API_KEY = "YOUR_API_KEY"
  val service = new YouTube.Builder(transport, factory, httpRequestInit).setApplicationName("test").
    setYouTubeRequestInitializer(new YouTubeRequestInitializer(API_KEY)).build()

  val videoResponse = service.videos().list(List("snippet","statistics").asJava).setId(List(video_id).asJava).execute()
  val video = videoResponse.getItems().get(0)
  println(video.getSnippet.getTitle)
  // println(video.getSnippet.getDescription)
  val like_count = video.getStatistics.getLikeCount
  val view_count = video.getStatistics.getViewCount
  val comment_count = video.getStatistics.getCommentCount
  println(s"Current Like Count is: $like_count")
  println(s"Current View Count is: $view_count")
  println(s"Current Comment Count is: $comment_count")

}

case class urlValidator(){

  // Method to check if url supplied is a valid https link
  def validateURL(url: String): Try[String] = {
    val j_URL = new java.net.URL(url)
    Try(j_URL.getProtocol) match {
      case Success("https") => Success(url)
      case Failure(e) => Failure(throw new Exception(e.getMessage))
    }
  }

  def extract_vid_from_link(url: String): Option[String] = {
    // Regular expression taken from:
    // https://stackoverflow.com/questions/11431078/scala-youtube-video-id-from-url
    val url_Rgx = """https?://(?:[0-9a-zA-Z-]+\.)?(?:youtu\.be/|youtube\.com\S*[^\w\-\s])([\w \-]{11})(?=[^\w\-]|$)(?![?=&+%\w]*(?:[\'"][^<>]*>|</a>))[?=&+%\w-]*""".r
    url match{
      case url_Rgx(uid) => Some(uid)
      case _ => None
    }
  }

}