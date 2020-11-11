class Test : ArrayList<TestItem>()

data class TestItem(
    val author: String,
    val board_category: String,
    val comments: List<Any>,
    val comments_no: Int,
    val content: String,
    val created_at: String,
    val email: String,
    val header: String,
    val id: Int,
    val image: List<Any>,
    val is_delected: Any,
    val is_deleted: Boolean,
    val liked: Int,
    val likeds: List<Any>,
    val published_at: String,
    val reported: Int,
    val title: String,
    val updated_at: String,
    val users_permissions_user: Any,
    val views: Int
)