import java.nio.charset.CodingErrorAction

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.rdd.RDD

import scala.io.{Codec, Source}

class MovieRec20m {

  /** Load up a Map of movie IDs to movie names. */
  def loadMovieNames() : Map[Int, String] = {

    // Handle character encoding issues:
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    var movieNames:Map[Int, String] = Map()

    val lines = Source.fromFile("ml-20m/movies.csv").getLines().drop(1)
    for (line <- lines) {
      var fields = line.split(',')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }

    return movieNames
  }

  /** Our main function where the action happens */
  val (movieNameDict, ratings, model) = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "MovieRecommendationsALS")

    println("Loading movie names...")
    val movieNameDict = loadMovieNames()

    val dataWithHeader = sc.textFile(path = "ml-20m/ratings.csv")
    val header = dataWithHeader.first()
    val data = dataWithHeader.filter(row => row != header)
    var testMap: Map[String, String] = Map()
    val ratings: RDD[Rating] = data.map( x => x.split(',') ).map(x => {
       Rating(user = x(0).toInt, product = x(1).toInt, rating = x(2).toDouble)
    }).cache()

    // Build the recommendation model using Alternating Least Squares
    println("\nTraining recommendation model...")

    val rank = 8
    val numIterations = 20

    val model = ALS.train(ratings, rank, numIterations)

    (movieNameDict, ratings, model)
  }

  def getRecommendations(userID: String): User = {
    println("\nRatings for user ID " + userID + ":")

    val userRatings = ratings.filter(x => x.user == userID.toInt)

    val myRatings = userRatings.collect()

    for (rating <- myRatings) {
      println(movieNameDict(rating.product.toInt) + ": " + rating.rating.toString)
    }

    println("\nTop 10 recommendations:")

    val recommendations = model.recommendProducts(userID.toInt, 500)
    for (recommendation <- recommendations) {
      println( movieNameDict(recommendation.product.toInt) + " score " + recommendation.rating )
    }

    val recs = recommendations.toList.map(rating => movieNameDict(rating.product) + " score: " + rating.rating)
    val userData = myRatings.toList.map(rating => movieNameDict(rating.product.toInt) + ": " + rating.rating.toString)


    User(userID = userID, userData = userData, recs = recs)
  }

  case class User(val userID: String, val userData: List[String], val recs: List[String])
}
