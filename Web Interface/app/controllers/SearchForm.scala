package controllers

object SearchForm{

  import play.api.data.Form
  import play.api.data.Forms._

  case class Data(category: String)


  val form = Form(
    mapping(
      "category" -> text,
    )(Data.apply)(Data.unapply)
  )
}
