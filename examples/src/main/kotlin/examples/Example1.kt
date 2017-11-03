package examples
fun main(args: Array<String>) {

    //Retrieve user "John"
    val user = users.first { it.firstName == "John" }

    //see recommended friends for John
    user.recommendedFriends()
            .forEach { println(it) }
}

data class SocialUser(
        val userId: Int,
        val firstName: String,
        val lastName: String
) {
    val friends get() = friendships.asSequence()
            .filter { userId == it.first || userId == it.second }
            .flatMap { sequenceOf(it.first, it.second) }
            .filter { it != userId }
            .map { friendId ->
                users.first { it.userId == friendId }
            }.toList()

    fun mutualFriendsOf(otherUser: SocialUser) =
            friends.asSequence().filter { it in otherUser.friends }.toList()

    fun recommendedFriends() = users.asSequence()
            .filter { it.userId != userId } // only look for other users
            .filter { it !in friends } // filter to people not already friends with
            .map { otherUser -> // package up mutual friends
                MutualFriendships(this, otherUser, mutualFriendsOf(otherUser).toList())
            }.filter { it.mutualFriends.count() > 0 } // omit where no MutualFriendships exist
            .sortedByDescending { it.mutualFriends.count() } // sort greatest number of mutual friendships first
            .map { it.otherUser }
}

data class MutualFriendships(val user: SocialUser, val otherUser: SocialUser, val mutualFriends: List<SocialUser>)

val users = listOf(
        SocialUser(1, "John", "Scott"),
        SocialUser(2, "Hannah", "Earley"),
        SocialUser(3, "Timothy", "Tannen"),
        SocialUser(4, "Scott", "Marcum"),
        SocialUser(5, "Sid", "Maddow"),
        SocialUser(6, "Rachel", "Zimmerman"),
        SocialUser(7, "Heather", "Timmers"),
        SocialUser(8, "Casey", "Crane"),
        SocialUser(9, "Billy", "Awesome")
)

val friendships = listOf(
        1 to 6,
        1 to 5,
        1 to 8,
        2 to 3,
        2 to 9,
        2 to 6,
        3 to 8,
        3 to 2,
        3 to 7,
        4 to 1,
        4 to 2,
        4 to 9,
        5 to 7,
        9 to 2,
        8 to 4,
        6 to 9
)
