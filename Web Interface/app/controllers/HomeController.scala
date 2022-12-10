package controllers

import models.{DataReadFromCSV, Search, readToday}
import play.api._
import play.api.data.Forms._
import play.api.data._
import play.api.i18n._
import play.api.mvc._

import javax.inject.Inject
import scala.collection._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

class HomeController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  import SearchForm._

  private val results = mutable.ArrayBuffer(
      Search("Film & Animation")
  )


  private val postUrl = routes.HomeController.postForm

//  private val postUrl2 = routes.HomeController.todayTrending
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index = Action {
    Ok(views.html.index())
  }

  def index1 = Action { implicit request: MessagesRequest[AnyContent] =>
    val DataReader = new DataReadFromCSV("Final_Trending_dataset4.csv")
    val inArray = DataReader.modelTraining(results.toSeq.last.category)
    Ok(views.html.index1(DataReader, results.toSeq, form, postUrl, inArray))
  }


  def postForm = Action { implicit request: MessagesRequest[AnyContent] =>
    val DataReader = new DataReadFromCSV("Final_Trending_dataset4.csv")
    val inArray = DataReader.modelTraining(results.toSeq.head.category)
    val errorFunction = { formWithErrors: Form[Data] =>
        BadRequest(views.html.index1(DataReader,results.toSeq,formWithErrors, postUrl, inArray))
    }

    val successFunction = { data: Data =>
      val result = Search(category = data.category)
      results += result
      Redirect(routes.HomeController.index1).flashing("info" -> "Result showed!")
    }

    val formValidationResult = form.bindFromRequest()
    formValidationResult.fold(errorFunction,successFunction)


  }


  def index2 = Action { implicit request: MessagesRequest[AnyContent] =>
    val DataReader = new readToday("Dec1_trending_video.csv")
    val inArray = DataReader.getDF()
    Ok(views.html.index2(DataReader, inArray))
  }

//  def todayTrending: Action[AnyContent] = Action {implicit request: MessagesRequest[AnyContent] =>
//    val DataReader = new readToday("Dec1_trending_video.csv")
//
//  }
}
