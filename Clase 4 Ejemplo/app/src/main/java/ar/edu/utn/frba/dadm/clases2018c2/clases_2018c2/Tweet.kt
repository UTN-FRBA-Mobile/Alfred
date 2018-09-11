package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2

data class Tweet(
    val profilePic: String,
    val name: String,
    val certified: String,
    val username: String,
    val content: String,
    val image: String?,
    val commentCount: Int,
    val retweetCount: Int,
    val likeCount: Int
) {

}